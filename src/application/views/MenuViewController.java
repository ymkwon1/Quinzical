package application.views;

import java.io.IOException;
import java.util.Optional;

import application.model.Player;
import application.model.QuinzicalModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
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
    private Player _playerModel;
    private Alert _alert;

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
    	try {
			_model = QuinzicalModel.createInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	TextInputDialog dialog = new TextInputDialog("");
    	dialog.setTitle("Player Name");
    	dialog.setHeaderText("Please Enter A Name");
    	dialog.setContentText("Name:");

    	// Traditional way to get the response value.
    	Optional<String> result = dialog.showAndWait();
    	if (result.isPresent()){
    	    _playerModel = new Player();
    	    _playerModel.setPlayer(result.get());
    	}

		Parent gamesModuleView = FXMLLoader.load(getClass().getResource("GamesModuleView.fxml"));
		Scene gamesModuleScene = new Scene(gamesModuleView);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(gamesModuleScene);
		window.show();
		if (_model.gameCompleted()) {
			_alert = new Alert(AlertType.INFORMATION);
			_alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			_alert.setTitle("Congratulations");
			_alert.setHeaderText(String.format("You had %s points!",_model.getWinnings()));
			_alert.setContentText("You have answered every question, return to menu to reset the game!");
			_alert.showAndWait();
		}
    }

    @FXML
    void ttsSettingsBtnClick(ActionEvent event) throws IOException {
    	try {
			_model = QuinzicalModel.createInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
