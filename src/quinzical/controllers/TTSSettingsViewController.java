package quinzical.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import quinzical.model.QuinzicalModel;
import quinzical.model.TextToSpeech;
import quinzical.util.CustomTimer;

public class TTSSettingsViewController implements Initializable{

	@FXML
	private Button testBtn;

	@FXML
	private Button decreaseBtn;

	@FXML
	private Button increaseBtn;

	@FXML
	private Label speedLabel;

	@FXML
	private Button returnBtn;

	@FXML
	private Label timerLabel;
	private Timeline animation;
	private CustomTimer _customTimer;

	private QuinzicalModel model;
	private Alert _alert;
	private TextToSpeech _tts = new TextToSpeech();


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			model = QuinzicalModel.createInstance();
			speedLabel.setText(String.format("%d wpm",_tts.getTTSSpeed()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (model.getPreviousScene() == 2) {
			_customTimer = CustomTimer.getInstance();
			timerLabel.setText(String.valueOf(_customTimer.getSecondsLeft()));
			animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
			animation.setCycleCount(Timeline.INDEFINITE);
			animation.play();
		}

	}

	private void updateTimer() {
		if (_customTimer.getSecondsLeft() > 0) {
			_customTimer.countDown();
		}
		timerLabel.setText(String.valueOf(_customTimer.getSecondsLeft()));
		if(_customTimer.getSecondsLeft() == 0) {
			animation.stop();
			if(_customTimer.getSecondsLeft() == 0) {
				animation.stop();					
				_alert = new Alert(AlertType.INFORMATION);
				_alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				_alert.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());
				_alert.setTitle("Time out!");
				_alert.setHeaderText("Time Out!");
				_alert.setContentText("You've run out of time! But take your time changing settings!");
				_alert.show();	
			}
		}
	}


	@FXML
	void decreaseBtnClick(ActionEvent event) {
		if (_tts.getTTSSpeed() > 10) {
			_tts.decreaseTTSSpeed();
			speedLabel.setText(String.format("%d wpm",_tts.getTTSSpeed()));
		}
	}

	@FXML
	void increaseBtnClick(ActionEvent event) {
		_tts.increaseTTSSpeed();
		speedLabel.setText(String.format("%d wpm",_tts.getTTSSpeed()));
	}

	@FXML
	void testBtnClick(ActionEvent event) {
		_tts.stopTts();
		_tts.tts("Testing the speed at which text is read");
	}

	@FXML
	void returnBtnClick(ActionEvent event) throws IOException {
		_tts.stopTts();
		if (model.getPreviousScene() == 1) {
			Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/MenuView.fxml"));
			Scene menuScene = new Scene(menuView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(menuScene);
			window.show();
		}
		else if (model.getPreviousScene() == 2) {
			animation.stop();
			model.setPreviousScene(1);
			Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/GamesQuestionView.fxml"));
			Scene menuScene = new Scene(menuView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(menuScene);
			window.show();
		}
		else if (model.getPreviousScene() == 3){
			model.setPreviousScene(1);
			Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/PracticeQuestionView.fxml"));
			Scene menuScene = new Scene(menuView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(menuScene);
			window.show();
		}
		else if (model.getPreviousScene() == 4){
			model.setPreviousScene(1);
			Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/InternationalQuestionView.fxml"));
			Scene menuScene = new Scene(menuView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(menuScene);
			window.show();
		}
	}


}
