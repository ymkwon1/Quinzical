package application.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.model.QuinzicalModel;
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

	private QuinzicalModel model;
	
	Alert alert = new Alert(AlertType.INFORMATION);
	
	@Override
	public void initialize (URL arg0, ResourceBundle arg1) {
		try {
			model = QuinzicalModel.createInstance();
			System.out.println(model.getCurrentAnswer());
			model.tts(model.getCurrentQuestion());
			tagLabel.setText(model.getCurrentTag());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Event Listener on Button[#submitBtn].onAction
	@FXML
	public void checkAnswer(ActionEvent event) throws IOException {
		if (model.checkAnswer(answerField.getText())) {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText("Correct!");
        	model.tts("Correct");
        	alert.showAndWait();
        	returnToGames(event);
    	} else {
    		alert.setTitle("Answer");
        	alert.setHeaderText(null);
        	alert.setContentText("Incorrect!");
        	model.tts("Incorrect");
        	alert.showAndWait();
        	returnToGames(event);
    	}
	}
	// Event Listener on Button[#noIdeaBtn].onAction
	@FXML
	public void noIdeaBtnClick(ActionEvent event) throws IOException {
		Parent menuView = FXMLLoader.load(getClass().getResource("GamesModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
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
