package de.apps.brewmaster.model.temperature;

public final class Temperature {

	private static final int TIME_CONVERSION_CONSTANT = 1000;

	private final float temperatureValue;

	private final long timeStamp;

	public Temperature(final float temperatureValue) {
		this.timeStamp = System.currentTimeMillis() / TIME_CONVERSION_CONSTANT;
		this.temperatureValue = temperatureValue;
	}

	public float getTemperatureValue() {
		return temperatureValue;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
}
