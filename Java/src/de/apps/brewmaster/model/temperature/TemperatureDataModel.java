package de.apps.brewmaster.model.temperature;

import java.util.ArrayList;
import java.util.List;

public final class TemperatureDataModel {

	/**
	 * The unique instance of this class.
	 */
	private static TemperatureDataModel instance;

	/**
	 * Returns the unique instance of this class.
	 *
	 * @return the unique instance.
	 */
	public static TemperatureDataModel getInstance() {
		if (null == instance) {
			instance = new TemperatureDataModel();
		}
		return instance;
	}

	private TemperatureDataModelListener listener;

	private final List<Temperature> temperatureValues;

	private final long startTimeStamp;

	/**
	 * Private constructor to restrict access to this class.
	 */
	private TemperatureDataModel() {
		this.temperatureValues = new ArrayList<>();
		this.startTimeStamp = System.currentTimeMillis() / 1000;
	}

	public void addTemperature(final Temperature newValue) {
		temperatureValues.add(newValue);
		listener.notify(newValue);
	}

	public long getStartTimeStamp() {
		return startTimeStamp;
	}

	public void registerListener(final TemperatureDataModelListener listener) {
		this.listener = listener;
	}
}
