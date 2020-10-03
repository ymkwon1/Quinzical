package application.views;

import java.io.IOException;
import java.util.Optional;

import application.model.QuinzicalModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class MenuViewController {

    @FXML
    private Button practiceBtn;
    @FXML
    private Button gamesBtn;
    @FXML
    private Button ttsSettingsBtn;
    @FXML
    private Button resetBtn;
    
    private QuinzicalModel _model;

    @FXML
    void practiceBtnClick(ActionEvent event) throws IOException {
		Parent practiceModuleView = FXMLLoader.load(getClass().getResource("PracticeModuleView.fxml"));
		Scene practiceModuleScene = new Scene(practiceModuleView);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(practiceModuleScene);
		window.show();
    }
    
    @FXML
    void gamesBtnClick(ActionEvent event) throws IOException {
		Parent gamesModuleView = FXMLLoader.load(getClass().getResource("GamesModuleView.fxml"));
		Scene gamesModuleScene = new Scene(gamesModuleView);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(gamesModuleScene);
		window.show();
    }

    @FXML
    void ttsSettingsBtnClick(ActionEvent event) throws IOException {
		Parent TtsSettingsView = FXMLLoader.load(getClass().getResource("TTSSettingsView.fxml"));
		Scene TtsSettingsScene = new Scene(TtsSettingsView);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(TtsSettingsScene);
		window.show();
    }
    
    @FXML
    void resetBtnClick (ActionEvent event) throws IOException {
    	try {
			_model = QuinzicalModel.createInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to reset the game?");
    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK) {
    		_model.reset();
    	}
    }
}
