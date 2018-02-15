#include <OneWire.h>
#include <DallasTemperature.h>
#include <Wire.h>
#include "SSD1306Ascii.h"
#include "SSD1306AsciiWire.h"
#include <RGBdriver.h>
//brew step is defined by "step;name;setTemperature;duration;stirSpeed"

// Port of the ds18b20 temperature sensor on the Arduino.
#define ONE_WIRE_BUS 2

// Baud rate for the serial interface.
#define SERIAL_BAUD_RATE 115200

// Resolution of the temperature seons 8,9,10,11 or 12 bit.
#define THERMOMETER_RESOLUTION 10

// display i2c address 0X3C+SA0 - 0x3C or 0x3D
//SDA: A4, SCL: A5
#define I2C_ADDRESS 0x3C

// Pins used for the pwm signal to control the motor.
// PWM signal right direction.
#define H_BRIDGE_RPWM 5
// PWM signal left direction.
#define H_BRIDGE_LPWM 6
// enable motor left direction.
#define H_BRIDGE_L_EN 7
// enable motor right direction.
#define H_BRIDGE_R_EN 8

// Pins for the relais to control the heating.
#define RELAIS_CHANNEL1 12
#define RELAIS_CHANNEL2 13

// Pins for the led strip driver board
#define LED_CLK 11
#define LED_DI 10

// If the current temperature more than TEMP_THRESHOLD below the temperature the heating is enabled.
#define TEMP_THRESHOLD 1.0

// Buffer size used for the name length.
#define MAX_NAME_LENGTH 16

// Array size for the brew steps.
#define MAX_BREW_STEPS 10

// Coldest and hottest temperature considered by the LED color.
#define MIN_TEMP 0
#define MAX_TEMP 100

// struct for a brewStep.
typedef struct brewStep {
  char name[MAX_NAME_LENGTH];
  float setTemperature;
  // duration of 0 means wait for user input.
  // duration of -1 means wait until setTemperature is reached.
  float duration;
  float stirSpeed;
  bool used;
  //initialize to 0.
  brewStep() : setTemperature(0), duration(0), stirSpeed(0), used(false) {}
} BrewStep;

// enum for the state of the BrewController.
enum BrewState {
  WAITING,
  BREWING,
  FINISHED
};

// struct to store the current state and related variables.
typedef struct currentState {
  enum BrewState brewState;
  int brewStepInd;
  float temperature;
  float stirSpeed;
  bool heatingEnabled;
  bool reachedTemperature;
  //variables to calculate the milliseconds in the current step
  uint64_t millisInStep;
  uint64_t millisStartOfStep;
  //millis to calculate refresh rate
  uint64_t m;
  //String received from serial port
  String rcvdSerialData;
  //indices used to parse the string
  int stepDelimiterIndex;
  int nameDelimiterIndex;
  int tempDelimiterIndex;
  int durationDelimiterIndex;
  //Direction for the led color range
  bool up;
  currentState(): brewState(WAITING), brewStepInd(-1), temperature(0), stirSpeed(0), heatingEnabled(false), reachedTemperature(false), millisInStep(0), millisStartOfStep(0), up(true) {}
} CurrentState;

// Currrent brew recipe.
BrewStep brewRecipe[MAX_BREW_STEPS];

// Current brew state,
CurrentState currentBrewState;

// Setup a oneWire instance to communicate with any OneWire devices (not just Maxim/Dallas temperature ICs).
OneWire oneWire(ONE_WIRE_BUS);

// Pass our oneWire reference to Dallas Temperature. 
DallasTemperature sensors(&oneWire);

// Arrays to hold device address.
DeviceAddress thermometerAddr;

// LED strip driver
RGBdriver rgbDriver(LED_CLK,LED_DI);

// oled display
SSD1306AsciiWire display;

// Initialize ds18b20 temperature on OneWire bus.
void initializeThemormeter(void) {
  // Get address of the thermometer.
  if (!sensors.getAddress(thermometerAddr, 0)) {
    Serial.println("Error: Unable to find thermometer on OneWire bus"); 
  }
  // Set the resolution in bits.
  sensors.setResolution(thermometerAddr, THERMOMETER_RESOLUTION);
  // Disable waiting until conversion has finished.
  sensors.setWaitForConversion(false);
  // Send the command to get temperatures.
  sensors.requestTemperatures();
}

// convert hsv to rgb color space.
// Fixed version from https://gist.github.com/hdznrrd/656996
void hsv2rgb(float h, float s, float v, byte *r, byte *g, byte *b)
{
  int i;
  float f,p,q,t;
  
  h = max(0.0, min(360.0, h));
  s = max(0.0, min(1.0, s));
  v = max(0.0, min(1.0, v));
    
  if(s == 0) {
    // achromatic (grey)
    *r = *g = *b = round(v*255);
    return;
  }

  h /= 60; // sector 0 to 5
  i = floor(h);
  f = h - i; // factorial part of h
  p = v * (1 - s);
  q = v * (1 - s * f);
  t = v * (1 - s * (1 - f));
  switch(i) {
    case 0:
      *r = round(255*v);
      *g = round(255*t);
      *b = round(255*p);
      break;
    case 1:
      *r = round(255*q);
      *g = round(255*v);
      *b = round(255*p);
      break;
    case 2:
      *r = round(255*p);
      *g = round(255*v);
      *b = round(255*t);
      break;
    case 3:
      *r = round(255*p);
      *g = round(255*q);
      *b = round(255*v);
      break;
    case 4:
      *r = round(255*t);
      *g = round(255*p);
      *b = round(255*v);
      break;
    default: // case 5:
      *r = round(255*v);
      *g = round(255*p);
      *b = round(255*q);
    }
}

void setLEDColorToTemp(float temp) {
  byte r;
  byte g;
  byte b;
  temp = max(MIN_TEMP, min(MAX_TEMP, temp));
  int temp_rounded = round(temp);
  int h = map(temp_rounded, MIN_TEMP, MAX_TEMP, 240, 0);
  hsv2rgb(h, 1.0, 1.0, &r, &g, &b);
  rgbDriver.begin();
  rgbDriver.SetColor(r, g, b);
  rgbDriver.end();
}

// Function to get the current temperature reading, temperature is only updated if sensors.requestTemperatures() is called.
float getCurrentTemperatureReading(DeviceAddress thermometerAddr) {
  return sensors.getTempC(thermometerAddr);
}

// Start with the next brew step.
void nextBrewStep() {
  currentBrewState.brewStepInd++;
  currentBrewState.millisStartOfStep = millis();
  currentBrewState.reachedTemperature = false;
}

// Print temp to the oled display.
void printTempToDisplay(float temp) {
  display.setCursor(0,0);
  display.println("Temperatur:");
  display.println();
  display.set2X();
  display.setCursor(0,2);
  //display.clearToEOL();
  display.print(temp);
  display.print((char)247);
  display.println("C");
  display.println();
  uint64_t old_m = currentBrewState.m;
  currentBrewState.m=millis();
  display.print((uint32_t)(currentBrewState.m-old_m));
  display.println("ms        ");
  display.set1X();
}

// Print waiting for brew recipe message.
void printWaitingMsgToDisplay() {
  display.setCursor(0,0);
  display.println("Initialisierung");
  display.set2X();
  display.setCursor(0,2);
  //display.clearToEOL();
  display.println("Warten auf");
  display.println("Rezept!");
  display.set1X();
}

// Print message for end of brew process
void printFinishedMsgToDisplay() {
  display.set2X();
  display.setCursor(0,2);
  //display.clearToEOL();
  display.println("Fertig!");
  display.set1X();
}

// Print current brew state to the display.
void printBrewStateToDisplay() {
  display.setCursor(0,0);
  display.set2X();
  display.print(brewRecipe[currentBrewState.brewStepInd].name);
  display.println("                ");
  display.set1X();
  display.print("Temp: ");
  display.print(currentBrewState.temperature);
  display.print(" / ");
  display.print(brewRecipe[currentBrewState.brewStepInd].setTemperature);
  display.print((char)247);
  display.println("C  ");
  display.print("Dauer: ");
  display.print((float)currentBrewState.millisInStep / 60000);
  display.print(" / ");
  display.print(brewRecipe[currentBrewState.brewStepInd].duration);
  display.println("min  ");
  display.print("Ruehrwerk: ");
  display.print(currentBrewState.stirSpeed);
  display.println("%    ");
  display.print("Heizen: ");
  if(currentBrewState.heatingEnabled) {
    display.println("Ein");
  } else {
    display.println("Aus");
  }
  display.print("Temp. erreicht: ");
  if(currentBrewState.reachedTemperature) {
    display.println("Ja  ");
  } else {
    display.println("Nein");
  }
  uint64_t old_m = currentBrewState.m;
  currentBrewState.m=millis();
  display.print("Update Rate: ");
  display.print(1000/(float)(currentBrewState.m-old_m));
  display.println("Hz        ");
}

// Prints the brew recipe to Serial.
void printBrewRecipe() {
  Serial.println("Brew recipe: ");
  for(int i = 0; i < MAX_BREW_STEPS; i++) {
    Serial.print("step: ");
    Serial.println(i);
    Serial.print("name: ");
    Serial.println(brewRecipe[i].name);
    Serial.print("temperature: ");
    Serial.println(brewRecipe[i].setTemperature);
    Serial.print("duration: ");
    Serial.println(brewRecipe[i].duration);
    Serial.print("stirSpeed: ");
    Serial.println(brewRecipe[i].stirSpeed);
    Serial.print("used: ");
    Serial.println(brewRecipe[i].used);
  }
}

// Control the brew process.
void brewBeer() {
  sensors.requestTemperatures();
  if(currentBrewState.brewStepInd == -1) {
   nextBrewStep();
  } else if(currentBrewState.brewStepInd > (MAX_BREW_STEPS - 1)) {
    currentBrewState.brewStepInd = -1;
    currentBrewState.brewState = FINISHED;
    display.clear();
    return;
  } if(!brewRecipe[currentBrewState.brewStepInd].used) {
    currentBrewState.brewStepInd = -1;
    currentBrewState.brewState = FINISHED;
    display.clear();
    return;
  }

  // Calculate duration in current step.
  currentBrewState.millisInStep = millis() - currentBrewState.millisStartOfStep;
  currentBrewState.temperature = getCurrentTemperatureReading(thermometerAddr);

  // Set LED color to temperature
  setLEDColorToTemp(currentBrewState.temperature);

  // Set heating.
  if(currentBrewState.temperature < brewRecipe[currentBrewState.brewStepInd].setTemperature - TEMP_THRESHOLD) {
    currentBrewState.heatingEnabled = true;
  } else {
    currentBrewState.heatingEnabled = false;
    if(!currentBrewState.reachedTemperature) {
      currentBrewState.reachedTemperature = true;
      currentBrewState.millisStartOfStep = millis();
      currentBrewState.millisInStep = 0;
    }
  }

  //Control the relais for heating
  if(currentBrewState.heatingEnabled) {
    digitalWrite(RELAIS_CHANNEL1, LOW);
    digitalWrite(RELAIS_CHANNEL2, LOW);
  } else {
    digitalWrite(RELAIS_CHANNEL1, HIGH);
    digitalWrite(RELAIS_CHANNEL2, HIGH);
  }

  // Set stir speed.
  if(currentBrewState.stirSpeed < brewRecipe[currentBrewState.brewStepInd].stirSpeed) {
    currentBrewState.stirSpeed += 0.5;
  }

  if(currentBrewState.stirSpeed > brewRecipe[currentBrewState.brewStepInd].stirSpeed) {
    currentBrewState.stirSpeed -= 0.5;
  }
  analogWrite(H_BRIDGE_LPWM, (uint8_t)(currentBrewState.stirSpeed * 255 / 100));
  
  printBrewStateToDisplay();

  // Check if this step is finished.
  if(brewRecipe[currentBrewState.brewStepInd].duration == -1 && currentBrewState.reachedTemperature) {
    nextBrewStep();
  } else if((brewRecipe[currentBrewState.brewStepInd].duration > 0) && (currentBrewState.millisInStep > (brewRecipe[currentBrewState.brewStepInd].duration * 60000)) && currentBrewState.reachedTemperature) {
    nextBrewStep();
  }
}

// Parses a string command read from serial port.
void parseCommand(String &msg) {
  if(msg.startsWith("<") && msg.endsWith(">")) {
    if(msg == "<START>" || msg == "<start>") {
      currentBrewState.brewState = BREWING;
      display.clear();
    } else if((msg == "<NEXT>" || msg == "<next>") && currentBrewState.brewState  == BREWING) {
      nextBrewStep();
    } else {
      Serial.println("Error: Unknown command!"); 
    }
  } else {
    //parse brew step "step;name;setTemperature;duration;stirSpeed"
    currentBrewState.stepDelimiterIndex = msg.indexOf(';');
    currentBrewState.nameDelimiterIndex = msg.indexOf(';', currentBrewState.stepDelimiterIndex + 1);
    currentBrewState.tempDelimiterIndex = msg.indexOf(';', currentBrewState.nameDelimiterIndex + 1);
    currentBrewState.durationDelimiterIndex = msg.indexOf(';', currentBrewState.tempDelimiterIndex + 1);
    
    int step = msg.substring(0, currentBrewState.stepDelimiterIndex).toInt();
    if(step >= MAX_BREW_STEPS) {
      Serial.println("Error: Brew step index is too large!"); 
      return;
    }
    msg.substring(currentBrewState.stepDelimiterIndex+1, currentBrewState.nameDelimiterIndex).toCharArray(brewRecipe[step].name, MAX_NAME_LENGTH);
    brewRecipe[step].setTemperature = msg.substring(currentBrewState.nameDelimiterIndex+1, currentBrewState.tempDelimiterIndex).toFloat();
    brewRecipe[step].duration = msg.substring(currentBrewState.tempDelimiterIndex+1, currentBrewState.durationDelimiterIndex).toFloat();
    brewRecipe[step].stirSpeed = msg.substring(currentBrewState.durationDelimiterIndex+1).toFloat();
    brewRecipe[step].used = true;
    Serial.println("Received " + msg);
    //printBrewRecipe();
  }
}

void setup() {
  // Start serial port.
  Serial.begin(SERIAL_BAUD_RATE);
  initializeThemormeter();

  // Set output pins for the relais.
  pinMode(RELAIS_CHANNEL1, OUTPUT);
  pinMode(RELAIS_CHANNEL2, OUTPUT);

  // Disable heating.
  digitalWrite(RELAIS_CHANNEL1, HIGH);
  digitalWrite(RELAIS_CHANNEL2, HIGH);
  
  // Set pins to control the motor to outputs.
  pinMode(H_BRIDGE_RPWM, OUTPUT);
  pinMode(H_BRIDGE_LPWM, OUTPUT);
  pinMode(H_BRIDGE_R_EN, OUTPUT);
  pinMode(H_BRIDGE_L_EN, OUTPUT);

  // Set motor speed to 0 and activate left direction
  analogWrite(H_BRIDGE_RPWM, 0);
  analogWrite(H_BRIDGE_LPWM, 0);
  digitalWrite(H_BRIDGE_R_EN, HIGH);
  digitalWrite(H_BRIDGE_L_EN, HIGH);

  // Turn LED strip off
  rgbDriver.begin();
  rgbDriver.SetColor(0, 0, 0);
  rgbDriver.end();
  
  //Initialize i2c.
  Wire.begin();

  //Initialize display at I2C_ADDRESS.
  display.begin(&Adafruit128x64, I2C_ADDRESS);
  display.set400kHz();
  //Set display font.
  display.setFont(Adafruit5x7);  
  display.clear();
  currentBrewState.m=millis();
}

void loop() {
  if(Serial.available()) {
    currentBrewState.rcvdSerialData = Serial.readStringUntil('\n');
    parseCommand(currentBrewState.rcvdSerialData);
  }
  
  if(currentBrewState.brewState == WAITING) {
    printWaitingMsgToDisplay();
    setLEDColorToTemp(currentBrewState.temperature);
    if(currentBrewState.up) {
      currentBrewState.temperature += 1;
    } else {
      currentBrewState.temperature -= 1;
    }
    if(currentBrewState.temperature >=100) {
      currentBrewState.up = false;
    }
    if(currentBrewState.temperature < 1) {
      currentBrewState.up = true;
    }
  } else if(currentBrewState.brewState == BREWING) {
    brewBeer();
  } else if (currentBrewState.brewState == FINISHED) {
    // Turn stiring off
    analogWrite(H_BRIDGE_LPWM, 0);
    // Disable heating.
    digitalWrite(RELAIS_CHANNEL1, HIGH);
    digitalWrite(RELAIS_CHANNEL2, HIGH);
    //Set LED color to green
    rgbDriver.begin();
    rgbDriver.SetColor(0, 255, 0);
    rgbDriver.end();
    printFinishedMsgToDisplay();
  }
}

