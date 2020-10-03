package application.views;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.model.Clue;
import application.model.QuinzicalModel;
import javafx.application.Platform;
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

	private QuinzicalModel _model;

	private Clue _clue;

	Alert _alert = new Alert(AlertType.INFORMATION);

	@Override
	public void initialize (URL arg0, ResourceBundle arg1) {
		try {
			_model = QuinzicalModel.createInstance();
			_clue = _model.getCurrentClue();
			_clue.ttsQuestion();
			tagLabel.setText(_clue.getTag());;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Event Listener on Button[#submitBtn].onAction
	@FXML
	public void checkAnswer(ActionEvent event) throws IOException {
		_model.stopTts();
		if (_clue.checkAnswer(answerField.getText())) {
			_model.addWinnings(_clue.getValue());
			_alert.setTitle("Answer");
			_alert.setHeaderText(null);
			_alert.setContentText("Correct! You have won "+ _clue.getValue());
			_model.tts("Correct! You have won "+ _clue.getValue());
			_alert.showAndWait();
			returnToGames(event);
		} else {
			_model.decreaseWinnings(_clue.getValue());
			_alert.setTitle("Answer");
			_alert.setHeaderText(null);
			_alert.setContentText("Incorrect! You have lost "+ _clue.getValue());
			_model.tts("Incorrect! You have lost "+ _clue.getValue());
			_alert.showAndWait();
			returnToGames(event);
		}
	}
	@FXML
	void repeatBtnClick(ActionEvent event) {
		_clue.ttsQuestion();

	}

	// Event Listener on Button[#noIdeaBtn].onAction
	@FXML
	public void noIdeaBtnClick(ActionEvent event) throws IOException {
		_model.stopTts();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to give up?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Parent menuView = FXMLLoader.load(getClass().getResource("GamesModuleView.fxml"));
			Scene menuScene = new Scene(menuView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(menuScene);
			window.show();
		}
	}

	/**
	 * returns to Games model screen
	 */
	void returnToGames(ActionEvent event) throws IOException {
		Parent menuView = FXMLLoader.load(getClass().getResource("GamesModuleView.fxml"));
		Scene menuScene = new Scene(menuView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(menuScene);
		window.show();
	}


}
