package de.apps.brewmaster.hardware;

import de.apps.brewmaster.model.temperature.Temperature;
import de.apps.brewmaster.model.temperature.TemperatureDataModel;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public final class SerialPortController {

	private final class TemperatureReader implements SerialPortEventListener {

		// private static final int BUFFER_SIZE = 1024;

		// private final byte[] buffer;

		public TemperatureReader() {
			// this.buffer = new byte[BUFFER_SIZE];
		}

		@Override
		public void serialEvent(final SerialPortEvent event) {

			try {
				String inputString = serialPort.readString();
				inputString.trim();
				final String[] tempString = inputString.split("\n");

				if (tempString.length > 0) {
					inputString = tempString[0];

					// System.out.println("input string: '" + inputString +
					// "'");

					if (!inputString.isEmpty()) {
						final float inputFloat = Float.parseFloat(inputString);
						TemperatureDataModel.getInstance().addTemperature(new Temperature(inputFloat));
					}
				}
			} catch (final SerialPortException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * The unique instance of this class.
	 */
	private static SerialPortController instance;

	private static final int TIME_OUT = 1000;

	/**
	 * Returns the unique instance of this class.
	 *
	 * @return the unique instance.
	 */
	public static SerialPortController getInstance() {
		if (null == instance) {
			instance = new SerialPortController();
		}
		return instance;
	}

	private SerialPort serialPort;

	/**
	 * Private constructor to restrict access to this class.
	 */
	private SerialPortController() {
	}

	public final void connect(final String portName, final int baudRate) {

		// Enumerate system ports and try connecting to Arduino over each
		// Try to connect to the Arduino on this port
		serialPort = new SerialPort(portName);

		// set port parameters
		try {
			serialPort.openPort();
			serialPort.setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.addEventListener(new TemperatureReader());
		} catch (final SerialPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("success");
	}

	public final void disconnect() {
		try {
			serialPort.closePort();
		} catch (final SerialPortException e) {
			e.printStackTrace();
		}
	}

	public final void sendRecipe() {
		// TODO
	}

}
