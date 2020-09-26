package application.views;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class QuestionViewController {

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
    void returnBtnClick(ActionEvent event) throws IOException {
    	Parent menuView = FXMLLoader.load(getClass().getResource("MenuView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }

}
