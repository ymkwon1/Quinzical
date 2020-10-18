package application.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.model.Clue;
import application.model.QuinzicalModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class GamesQuestionViewController implements Initializable {
	@FXML
	private Label questionLabel;
	@FXML
	private TextField answerField;
	@FXML
	private Button submitBtn;
	@FXML
	private Button noIdeaBtn;
	@FXML
	private Label tagLabel;
	@FXML
	private Label answerLabel;
	@FXML
	private Button repeatBtn;
    @FXML
    private Button ttsSettingsBtn;
    @FXML
    private Label timerLabel;
    @FXML
    private AnchorPane anchorPane;

	private QuinzicalModel _model;

	private Clue _clue;
	
	private int _secondsLeft;
	private Timeline animation;

	private Alert _alert = new Alert(AlertType.INFORMATION);

	@Override
	public void initialize (URL arg0, ResourceBundle arg1) {
		try {
			_model = QuinzicalModel.createInstance();
			_clue = _model.getCurrentClue();
			_clue.ttsQuestion();
			tagLabel.setText(_clue.getTag());
		} catch (Exception e) {
			e.printStackTrace();
		}
		_secondsLeft = 60;
		timerLabel.setText(String.valueOf(_secondsLeft));
		animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> countDown()));
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
	}
	
	private void countDown() {
		if (_secondsLeft > 0) {
			_secondsLeft--;
		}
		timerLabel.setText(String.valueOf(_secondsLeft));
		if(_secondsLeft == 0) {
			_model.stopTts();
			animation.stop();					_alert = new Alert(AlertType.INFORMATION);
			_alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			_alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/views/theme.css").toExternalForm());
			_alert.setTitle("Time out!");
			_alert.setHeaderText("Time Out!");
			_alert.setContentText("You've run out of time! No point deducted?");
			_alert.setOnHidden(e -> {
				try {
					returnToGames();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				});
			_alert.show();

		}
	}
	
	// Event Listener on Button[#submitBtn].onAction
	@FXML
	public void checkAnswer(ActionEvent event) throws IOException {
		_model.stopTts();
		animation.stop();
		if (_clue.checkAnswer(answerField.getText())) {
			_model.addWinnings(_clue.getValue());
			_alert.setTitle("Answer");
			_alert.setHeaderText(null);
			_alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/views/theme.css").toExternalForm());
			_alert.setContentText("Correct! You have won "+ _clue.getValue());
			_alert.setGraphic(null);
			_model.tts("Correct! You have won "+ _clue.getValue());
			Optional<ButtonType> result = _alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				_model.stopTts();
			}
			returnToGames();
		} else {
			_model.decreaseWinnings(_clue.getValue());
			_alert.setTitle("Answer");
			_alert.setHeaderText(null);
			_alert.setGraphic(null);
			_alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/views/theme.css").toExternalForm());
			_alert.setContentText(String.format("Incorrect, the answer was \"%s\"! You have lost %d", _clue.getAnswers(), _clue.getValue()));
			_model.tts(String.format("Incorrect, the answer was %s!", _clue.getAnswers(), _clue.getValue()));
			Optional<ButtonType> result = _alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				_model.stopTts();
			}
			returnToGames();
		}
	}
	@FXML
	void repeatBtnClick(ActionEvent event) {
		_model.stopTts();
		_clue.ttsQuestion();

	}

	// Event Listener on Button[#noIdeaBtn].onAction
	@FXML
	public void noIdeaBtnClick(ActionEvent event) throws IOException {
		_model.stopTts();
		animation.stop();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/views/theme.css").toExternalForm());
		alert.setContentText("Are you sure you want to give up?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			returnToGames();
		}
	}

    @FXML
    void ttsSettingsBtnClick(ActionEvent event) throws IOException {
    	animation.stop();
    	_model.setPreviousScene(2);
		Parent settingsView = FXMLLoader.load(getClass().getResource("/application/views/TTSSettingsView.fxml"));
		Scene settingsScene = new Scene(settingsView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(settingsScene);
		window.show();
    }
	
	/**
	 * returns to Games model screen
	 */
	void returnToGames() throws IOException {
		animation.stop();
		Parent menuView = FXMLLoader.load(getClass().getResource("/application/views/GamesModuleView.fxml"));
		Scene menuScene = new Scene(menuView);

		Stage window = (Stage)anchorPane.getScene().getWindow();

		window.setScene(menuScene);
		window.show();
		if (_model.gameCompleted()) {
			_alert = new Alert(AlertType.INFORMATION);
			_alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			_alert.getDialogPane().getStylesheets().add(getClass().getResource("/application/views/theme.css").toExternalForm());
			_alert.setTitle("Congratulations");
			_alert.setHeaderText(String.format("You had %s points!",_model.getWinnings()));
			_alert.setContentText("You have answered every question, return to menu to reset the game!");
			_alert.show();
			_model.addPlayerRanking();
		}
	}


}
