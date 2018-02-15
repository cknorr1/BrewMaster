package de.apps.brewmaster.user_interface.control;

import java.awt.SplashScreen;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import de.apps.brewmaster.BrewMasterApplication;
import de.apps.brewmaster.hardware.SerialPortController;
import de.apps.brewmaster.user_interface.control.custom.DrinkPopUpController;
import de.apps.brewmaster.user_interface.control.custom.TemperatureLiveChart;
import de.apps.brewmaster.user_interface.control.step.BrewRecipeTreeView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controls the main GUI of BrewMaster.
 *
 * @author Alexander Rohlfing(Original author)
 *
 */
public final class MainUIController implements Initializable {

	private class DrinkPopUpPresenter extends TimerTask {

		@Override
		public void run() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					new DrinkPopUpController().showAndWait();
					timer.schedule(new DrinkPopUpPresenter(), getNextRandomTime());
				}
			});
		}

	}

	/**
	 * FXML file name containing the static visible structure for the controlled
	 * GUI.
	 */
	private static final String fxmlName = "MainUI.fxml";

	/**
	 * The unique singleton instance of this class.
	 */
	private static final MainUIController instance = new MainUIController();

	private static final int SECONDS_CONST = 1000;

	private static final int MINUTE_CONST = 60;

	private static final int MIN_TIME = 10 * SECONDS_CONST * MINUTE_CONST;

	private static final int MAX_TIME = 30 * SECONDS_CONST * MINUTE_CONST;

	/**
	 * Returns the unique instance of this class.
	 *
	 * @return instance the unique {@code MainUIController}
	 */
	public static MainUIController getInstance() {
		return instance;
	}

	// private final long startTime;

	/**
	 * Root pane containing all {@code Controls}.
	 */
	@FXML
	private AnchorPane rootPane;

	@FXML
	private TabPane tabPane;

	@FXML
	private Tab liveDataTab;

	@FXML
	private Tab brewStepTab;

	/**
	 * The current {@code Stage}.
	 */
	private Stage stage;

	private final Timer timer;

	/**
	 * Private constructor to restrict access.
	 */
	private MainUIController() {

		// Load FXML file for Main GUI
		final FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource(BrewMasterApplication.userInterfacePath + fxmlName));
		fxmlLoader.setController(this);

		try {
			rootPane = fxmlLoader.load();
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// rootPane.getStyleClass().add("pane");
		// rootPane.getStylesheets().add(getClass().getResource("css/brew-step-tree.css").toExternalForm());
		final String portName = "/dev/ttyS80";
		final int baudRate = 115200;
		SerialPortController.getInstance().connect(portName, baudRate);

		timer = new Timer(true);
		initRandomDrinkPopup();

	}

	private long getNextRandomTime() {
		final Random random = new Random();
		final long randomTime = MIN_TIME + random.nextInt(MAX_TIME - MIN_TIME);
		System.out.println("waiting time: " + randomTime / 1000 / 60 + " min");
		return randomTime;
	}

	public final Pane getRootPane() {
		return rootPane;
	}

	private void initBrewStepTree() {

		final BrewRecipeTreeView tree = new BrewRecipeTreeView();
		brewStepTab.setContent(tree);

	}

	@Override
	public void initialize(final URL location, final ResourceBundle resources) {
		// Get the splashscreen
		final SplashScreen splash = SplashScreen.getSplashScreen();

		initTemperatureLiveChart();
		initBrewStepTree();

		// Close splashscreen
		if (splash != null) {
			splash.close();
		}

	}

	private void initRandomDrinkPopup() {
		timer.schedule(new DrinkPopUpPresenter(), getNextRandomTime());
	}

	private void initTemperatureLiveChart() {
		liveDataTab.setContent(new TemperatureLiveChart());
	}

	public final void setStage(final Stage stage) {
		this.stage = stage;
	}
}
