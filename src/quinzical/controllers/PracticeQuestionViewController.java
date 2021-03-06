package quinzical.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import quinzical.model.Clue;
import quinzical.model.QuinzicalModel;
import quinzical.model.TextToSpeech;

/**
 * Controller for the practice question view
 */
public class PracticeQuestionViewController implements Initializable{

	private QuinzicalModel _model;
	
	private Clue _clue;
	
    @FXML
    private Label questionLabel;

    @FXML
    private TextField answerField;

    @FXML
    private Button submitBtn;

    @FXML
    private Label attemptsLabel;

    @FXML
    private Label hintsLabel;

    @FXML
    private Button returnBtn;
    
    @FXML
    private Label tagLabel;
    
    @FXML
    private Label answerLabel;
    
    @FXML
    private Button repeatBtn;

    @FXML
    private Button ttsSettingsBtn;
    
    private TextToSpeech _tts = new TextToSpeech();
    
    private Alert alert = new Alert(AlertType.INFORMATION);


    /**
     * Initialize all the components for the practice question view.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			_model = QuinzicalModel.createInstance();
			_clue = _model.getCurrentClue();
			_clue.ttsQuestion();
			questionLabel.setText(_clue.getQuestion());;
			tagLabel.setText(_clue.getTag());;
			hintsLabel.setText(_clue.getHint());
			attemptsLabel.setText(_clue.getAttempts());;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Return to the practice module view
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void returnBtnClick(ActionEvent event) throws IOException {
    	_clue.resetAttempts();
    	_tts.stopTts();
    	Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/PracticeModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }
	
    /**
     * Check whether answer is correct or not.
     * @param event
     * @throws IOException
     */
    @FXML
    void checkAnswer(ActionEvent event) throws IOException {
    	_tts.stopTts();
    	if (_clue.checkAnswer(answerField.getText())) {
    		alert.setTitle("Answer");
    		alert.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());
        	alert.setHeaderText(null);
        	alert.setContentText("Correct!");
        	alert.setGraphic(null);
        	_tts.tts("Correct");
        	alert.showAndWait();
        	returnToPractice(event);
    	}
    	else {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());
        	alert.setContentText(String.format("Incorrect you have %d attempts remaining", _clue.attemptsLeft()));
        	_tts.tts("Incorrect");
        	alert.setGraphic(null);
        	alert.showAndWait();	
        	attemptsLabel.setText(_clue.getAttempts());
        	hintsLabel.setText(_clue.getHint());
        	answerLabel.setText(_clue.showAnswer());
        	if (answerLabel.getText() != null) {
        		_clue.resetAttempts();
        		_clue.ttsAnswer();
        		submitBtn.setDisable(true);
        	}
        	
    	}
    }
    
    /**
     * Return to practice module
     */
    void returnToPractice(ActionEvent event) throws IOException {
    	_tts.stopTts();
    	Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/PracticeModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }
    

    /**
     * Repeat the question
     * @param event
     */
    @FXML
    void repeatBtnClick(ActionEvent event) {
    	_tts.stopTts();
    	_clue.ttsQuestion();
    }

    /**
     * Go to the text to speech setting
     * @param event
     * @throws IOException
     */
    @FXML
    void ttsSettingsBtnClick(ActionEvent event) throws IOException {
    	_model.setPreviousScene(3);
		Parent settingsView = FXMLLoader.load(getClass().getResource("/quinzical/views/TTSSettingsView.fxml"));
		Scene settingsScene = new Scene(settingsView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(settingsScene);
		window.show();
    }

}
