package de.apps.brewmaster;

import java.io.IOException;

import javafx.application.Preloader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BrewMasterPreloader extends Preloader {

	private static final String fxmlName = "Preloader.fxml";

	private static final String cssID = "stack-pane";

	@FXML
	private ProgressBar progressBar;

	private Stage stage;
	private boolean noLoadingProgress = true;

	@Override
	public void handleApplicationNotification(final PreloaderNotification pn) {
		if (pn instanceof ProgressNotification) {
			// expect application to send us progress notifications
			// with progress ranging from 0 to 1.0
			double v = ((ProgressNotification) pn).getProgress();
			if (!noLoadingProgress) {
				// if we were receiving loading progress notifications
				// then progress is already at 50%.
				// Rescale application progress to start from 50%
				v = 0.5 + v / 2;
			}
			progressBar.setProgress(v);
		} else if (pn instanceof StateChangeNotification) {
			// hide after get any state update from application
			stage.hide();
		}
	}

	@Override
	public void handleProgressNotification(final ProgressNotification pn) {
		// application loading progress is rescaled to be first 50%
		// Even if there is nothing to load 0% and 100% events can be
		// delivered
		if (pn.getProgress() != 1.0 || !noLoadingProgress) {
			progressBar.setProgress(pn.getProgress() / 2);
			if (pn.getProgress() > 0) {
				noLoadingProgress = false;
			}
		}
	}

	@Override
	public void handleStateChangeNotification(final StateChangeNotification evt) {
		// ignore, hide after application signals it is ready
	}

	@Override
	public void start(final Stage stage) throws Exception {
		this.stage = stage;

		// Load FXML file for Main GUI
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
		fxmlLoader.setController(this);
		StackPane rootPane = null;
		try {
			rootPane = fxmlLoader.load();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		rootPane.setId(cssID);
		final Scene scene = new Scene(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());
		stage.setScene(scene);
		stage.show();
	}
}
