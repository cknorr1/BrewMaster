package de.apps.brewmaster.user_interface.control.custom;

import de.apps.brewmaster.model.temperature.Temperature;
import de.apps.brewmaster.model.temperature.TemperatureDataModel;
import de.apps.brewmaster.model.temperature.TemperatureDataModelListener;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

public class TemperatureLiveChart extends LineChart<Number, Number> {

	private static final int TIME_RANGE = 60;

	private static final int MAX_TEMP = 120;

	private static final int TICK_STEP = 5;

	private final XYChart.Series<Number, Number> actualDataSeries;

	private final ObservableList<javafx.scene.chart.XYChart.Series<Number, Number>> lineChartData;

	private Timeline animation;

	private final NumberAxis timeAxis;

	public TemperatureLiveChart() {
		super(new NumberAxis(0, TIME_RANGE, TICK_STEP), new NumberAxis(0, MAX_TEMP, TICK_STEP));

		// setup chart
		this.setId("temperatureLiveChart");
		this.setCreateSymbols(false);
		this.setAnimated(false);
		this.setLegendVisible(false);
		this.setTitle("Live Temperatur");

		this.timeAxis = (NumberAxis) this.getXAxis();
		timeAxis.setLabel("Zeit [s]");
		timeAxis.setForceZeroInRange(false);
		this.getYAxis().setLabel("Temperatur [°C]");
		actualDataSeries = new XYChart.Series<Number, Number>();
		actualDataSeries.setName("Minute Data");

		TemperatureDataModel.getInstance().registerListener(new TemperatureDataModelListener() {

			@Override
			public void notify(final Temperature newValue) {
				actualDataSeries.getData()
						.add(new XYChart.Data<Number, Number>(
								newValue.getTimeStamp() - TemperatureDataModel.getInstance().getStartTimeStamp(),
								newValue.getTemperatureValue()));
				if (actualDataSeries.getData().size() > TIME_RANGE) {
					System.out.println("oversized series");
					actualDataSeries.getData().remove(0);
					timeAxis.setLowerBound((Long) actualDataSeries.getData().get(0).getXValue());
					timeAxis.setUpperBound(
							(Long) actualDataSeries.getData().get(actualDataSeries.getData().size() - 1).getXValue());
				}
			}
		});

		this.lineChartData = this.getData();
		init();
	}

	private void init() {

		// create timeline to add new data every 60th of second
		animation = new Timeline();
		final EventHandler<ActionEvent> updateListener = new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent actionEvent) {
				lineChartData.clear();
				lineChartData.add(actualDataSeries);
			}
		};

		animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 60), updateListener));
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
	}

}
