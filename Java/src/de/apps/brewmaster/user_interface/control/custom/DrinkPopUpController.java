package de.apps.brewmaster.user_interface.control.custom;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.apps.brewmaster.BrewMasterApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class DrinkPopUpController extends Alert implements Initializable {

	private static final String fxmlName = BrewMasterApplication.userInterfacePath + "custom/DrinkPopUp.fxml";

	private static final String title = "DRINK !!!";

	private static final int BOUNDS_SPACING = 40;

	@FXML
	private ImageView imageView;

	private final String customSound = "tornado-signal.mp3";

	/**
	 * Creates a new instance of this controller class.
	 */
	public DrinkPopUpController() {
		super(AlertType.WARNING);
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));
		fxmlLoader.setController(this);

		try {
			setDialogPane((DialogPane) fxmlLoader.load());
		} catch (final IOException e) {
			e.printStackTrace();
		}

		playSound();
		final DialogPane dialogPane = this.getDialogPane();
		imageView.setFitHeight(dialogPane.getHeight() - BOUNDS_SPACING);
		imageView.setFitWidth(dialogPane.getWidth());
		// final Stage stage = (Stage) getDialogPane().getScene().getWindow();
		// imageView.setImage(new Image(BrewMaster.gifPath + "drink.gif"));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.fxml.Initializable#initialize(java.net.URL,
	 * java.util.ResourceBundle)
	 */
	@Override
	public void initialize(final URL url, final ResourceBundle resources) {
		setTitle(title);
	}

	private void playSound() {
		try {
			final File audioFile = new File(
					"/media/alex/DATA/Common/BrewMaster Git/BrewMaster/Java/res/sound/tornado-signal.mp3");
			final Media mediaClip = new Media(audioFile.toURI().toString());
			final MediaPlayer mediaPlayer = new MediaPlayer(mediaClip);
			mediaPlayer.setAutoPlay(false);
			mediaPlayer.play();
		} catch (final Exception ex) {
			System.err.println(ex);
		}
	}
}