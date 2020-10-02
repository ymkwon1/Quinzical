package application.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Clue;
import application.model.QuinzicalModel;
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
    
    Alert alert = new Alert(AlertType.INFORMATION);


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			_model = QuinzicalModel.createInstance();
			_clue = _model.getCurrentClue();
			_clue.ttsQuestion();
			_clue.setQuestionLabel(questionLabel);
			_clue.setTagLabel(tagLabel);
			_clue.setHintLabel(hintsLabel);
			_clue.setAttemptsLabel(attemptsLabel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    void returnBtnClick(ActionEvent event) throws IOException {
    	_clue.resetAttempts();
    	Parent menuView = FXMLLoader.load(getClass().getResource("PracticeModuleView.fxml"));
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
    	if (_clue.checkAnswer(answerField.getText())) {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText("Correct!");
        	_model.tts("Correct");
        	alert.showAndWait();
        	returnToPractice(event);
    	}
    	else {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText(String.format("Incorrect you have %d attempts remaining", _clue.attemptsLeft()));
        	_model.tts("Incorrect");
        	alert.showAndWait();	
        	_clue.setAttemptsLabel(attemptsLabel);
        	_clue.setHintLabel(hintsLabel);
        	_clue.setAnswerLabel(answerLabel);
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
    	Parent menuView = FXMLLoader.load(getClass().getResource("PracticeModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }

}
