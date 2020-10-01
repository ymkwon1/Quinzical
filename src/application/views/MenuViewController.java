package application.views;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuViewController {

    @FXML
    private Button practiceBtn;
    @FXML
    private Button gamesBtn;
    @FXML
    private Button ttsSettingsBtn;

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
}
