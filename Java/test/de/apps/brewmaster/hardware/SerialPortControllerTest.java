package de.apps.brewmaster.hardware;

import org.junit.Test;

public class SerialPortControllerTest {

	@Test
	public void connectTest() {
		final String portName = "/dev/ttyS80";
		final int baudRate = 115200;
		SerialPortController.getInstance().connect(portName, baudRate);
		while (true) {
		}
		// SerialPortController.getInstance().disconnect();
	}
}
