package application.views;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class QuestionViewController implements Initializable{

	private QuinzicalModel model;
	
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
			model = QuinzicalModel.createInstance();
			model.tts(model.getCurrentQuestion());
			questionLabel.setText(model.getCurrentQuestion());
			tagLabel.setText(model.getCurrentTag());
			hintsLabel.setText("");
			attemptsLabel.setText(String.format("Attempts: %s/3",model.getAttempts()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    void returnBtnClick(ActionEvent event) throws IOException {
    	model.setAttempts(0);
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
    	if (model.checkAnswer(answerField.getText())) {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText("Correct!");
        	model.tts("Correct");
        	alert.showAndWait();
        	returnToPractice(event);
    	}
    	else {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	int attemptsLeft = 3 - Integer.parseInt(model.getAttempts());
        	alert.setContentText(String.format("Incorrect you have %d attempts remaining", attemptsLeft));
        	model.tts("Incorrect");
        	alert.showAndWait();	
        	attemptsLabel.setText(String.format("Attempts: %s/3",model.getAttempts()));
        	if (model.getAttempts().equals("2")) {
        		hintsLabel.setText(String.format("Hint: The answer starts with the letter \"%s\"", model.getCurrentAnswer().charAt(0)));
        	}
        	if (model.getAttempts().equals("3")) {
        		model.setAttempts(0);
        		model.tts(String.format("The answer was %s", model.getCurrentAnswer()));
        		answerLabel.setText(String.format("The answer was \"%s\"", model.getCurrentAnswer()));
        		submitBtn.setDisable(true);
        	}
        	
    	}
    }
    
    /**
     * returns to Practice model screen
     */
    void returnToPractice(ActionEvent event) throws IOException {
    	Parent menuView = FXMLLoader.load(getClass().getResource("PracticeModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }

}
