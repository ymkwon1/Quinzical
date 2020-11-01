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

/**
 * Controller for the international question view.
 */
public class InternationalQuestionViewController implements Initializable{

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
    
    private Alert _alert = new Alert(AlertType.INFORMATION);


    /**
     * Initialize all the components for the internatinal questionn view.
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
	
    @FXML
    void returnBtnClick(ActionEvent event) throws IOException {
    	_clue.resetAttempts();
    	_model.stopTts();
    	Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/InternationalModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }
	
    /**
     * fires when the submit button is clicked
     * if its empty lets user know to type something
     * if user input is correct then correct alert opens
     * if user input is incorrect then incorrect alert opens with correct answer
     */
    @FXML
    void checkAnswer(ActionEvent event) throws IOException {
    	_model.stopTts();
    	if (_clue.checkAnswer(answerField.getText())) {
    		_alert.setTitle("Answer");
    		_alert.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());
        	_alert.setHeaderText(null);
        	_alert.setContentText("Correct!");
        	_alert.setGraphic(null);
        	_model.tts("Correct");
        	_alert.showAndWait();
        	returnToPractice(event);
    	}
    	else {
    		_alert.setTitle("Answer");
        	_alert.setHeaderText(null);
        	_alert.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());
        	_alert.setContentText(String.format("Incorrect you have %d attempts remaining", _clue.attemptsLeft()));
        	_model.tts("Incorrect");
        	_alert.setGraphic(null);
        	_alert.showAndWait();	
        	attemptsLabel.setText(_clue.getAttempts());
        	hintsLabel.setText(_clue.getHint());
        	answerLabel.setText(_clue.getAnswer());
        	if (answerLabel.getText() != null) {
        		_clue.resetAttempts();
        		_clue.ttsAnswer();
        		submitBtn.setDisable(true);
        	}
        	
    	}
    }
    
    /**
     * returns to Practice _model screen
     */
    void returnToPractice(ActionEvent event) throws IOException {
    	_model.stopTts();
    	Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/InternationalModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }
    

    @FXML
    void repeatBtnClick(ActionEvent event) {
    	_model.stopTts();
    	_clue.ttsQuestion();
    }

    @FXML
    void ttsSettingsBtnClick(ActionEvent event) throws IOException {
    	_model.setPreviousScene(4);
		Parent settingsView = FXMLLoader.load(getClass().getResource("/quinzical/views/TTSSettingsView.fxml"));
		Scene settingsScene = new Scene(settingsView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(settingsScene);
		window.show();
    }

}
