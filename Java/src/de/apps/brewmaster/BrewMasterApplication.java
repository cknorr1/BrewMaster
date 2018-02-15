package de.apps.brewmaster;

import com.sun.javafx.application.LauncherImpl;

import de.apps.brewmaster.user_interface.control.MainUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Application launcher of {@code GesturEd}.
 *
 * @author Alexander Rohlfing (Original author)
 *
 */
@SuppressWarnings("restriction")
public final class BrewMasterApplication extends Application {

	private static final String applicationName = "BrewMaster";

	// /**
	// * Application logo name.
	// */
	// private static final String logoName = "gesturEd_logo.png";

	public static final String imagePath = "/res/image/";

	public static final String stylePath = "/res/style/";

	public static final String userInterfacePath = "/de/apps/brewmaster/user_interface/model/";

	public static final String gifPath = "/res/gif/";

	public static final String soundPath = "/res/sound/";

	/**
	 * Initiates application logic sequence.
	 *
	 * @param args
	 *            application arguments.
	 */
	public static void main(final String[] args) {
		LauncherImpl.launchApplication(BrewMasterApplication.class, BrewMasterPreloader.class, args);
	}

	private final BooleanProperty ready = new SimpleBooleanProperty(false);

	/**
	 * Creates a new {@code GesturEd} application instance.
	 */
	public BrewMasterApplication() {
		super();
	}

	private void longStart() {
		// simulate long init in background
		final Runnable task = new Runnable() {

			@Override
			public void run() {
				final int max = 10;
				for (int i = 1; i <= max; i++) {
					try {
						Thread.sleep(200);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					// Send progress to preloader
					notifyPreloader(new ProgressNotification(((double) i) / max));
				}
				// After init is ready, the app is ready to be shown
				// Do this before hiding the preloader stage to prevent the
				// app from exiting prematurely
				ready.setValue(Boolean.TRUE);

				notifyPreloader(new StateChangeNotification(StateChangeNotification.Type.BEFORE_START));

			}
		};
		new Thread(task).start();
	}

	/**
	 * Launches the application.
	 */
	@Override
	public void start(final Stage stage) throws Exception {
		MainUIController.getInstance().setStage(stage);
		final Pane rootPane = MainUIController.getInstance().getRootPane();

		final Scene scene = new Scene(rootPane);

		// init Stage
		stage.setTitle(applicationName);
		try {
			// stage.getIcons().add(new
			// Image(getClass().getResourceAsStream(imagePath + logoName)));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		stage.initStyle(StageStyle.DECORATED);
		stage.setScene(scene);

		// After the app is ready, show the stage
		ready.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(final ObservableValue<? extends Boolean> ov, final Boolean t, final Boolean t1) {
				if (Boolean.TRUE.equals(t1)) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							stage.show();
						}
					});
				}
			}
		});

		longStart();
	}

}
