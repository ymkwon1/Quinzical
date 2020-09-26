package application.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.model.PracticeModule;
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

public class QuestionViewController implements Initializable{

	private PracticeModule model;
	
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
    
    Alert alert = new Alert(AlertType.INFORMATION);

    @FXML
    void returnBtnClick(ActionEvent event) throws IOException {
    	Parent menuView = FXMLLoader.load(getClass().getResource("MenuView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			model = PracticeModule.createInstance();
			questionLabel.setText(model.getCurrentQuestion());
			tagLabel.setText(model.getCurrentTag());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * fires when the submit button is clicked
     * if its empty lets user know to type something
     * if user input is correct then correct alert opens
     * if user input is incorrect then incorrect alert opens with correct answer
     */
    @FXML
    void checkAnswer(ActionEvent event) throws IOException {
    	if (answerField.getText().isEmpty() || answerField.getText() == null) {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText("Please enter an answer");
        	alert.showAndWait();	
    	}
    	else if (model.checkAnswer(answerField.getText())) {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText("Correct!");
        	model.ttsAnswerIsCorrect();
        	alert.showAndWait();
        	returnToPractice(event);
    	}
    	else {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText(String.format("Incorrect, the answer is %s", model.getCurrentAnswer()));
        	model.ttsAnswerIsIncorrect();
        	alert.showAndWait();	
        	returnToPractice(event);
    	}
    }
    
    /**
     * returns to Practice model screen
     */
    void returnToPractice(ActionEvent event) throws IOException {
    	Parent menuView = FXMLLoader.load(getClass().getResource("PractiseModelView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }

}
