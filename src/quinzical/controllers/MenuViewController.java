package quinzical.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;
import quinzical.model.QuinzicalModel;

/**
 * Controller for the menu view.
 */
public class MenuViewController implements Initializable{

	@FXML
	private Button practiceBtn;
	@FXML
	private Button gamesBtn;
	@FXML
	private Button ttsSettingsBtn;
	@FXML
	private Button resetBtn;
    @FXML
    private Button playerRankBtn;
    @FXML
    private Button internationalBtn;
    @FXML
    private Tooltip tooltip;

	private QuinzicalModel _model;
	private Alert _alert;
	
	/**
	 * Initialize all the components for the menu view.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			try {
				_model = QuinzicalModel.createInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (_model.InternationalUnlocked()) {
				internationalBtn.setDisable(false);
				tooltip.setText("");
			}
			else {
				internationalBtn.setDisable(true);
				tooltip.setShowDelay(Duration.seconds(0));
				tooltip.setShowDuration(Duration.seconds(9999));
			}
		
	}

	/**
	 * Go to the practice module.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void practiceBtnClick(ActionEvent event) throws IOException {
		_model._internationalSelected = false;
		Parent practiceModuleView = FXMLLoader.load(getClass().getResource("/quinzical/views/PracticeModuleView.fxml"));
		Scene practiceModuleScene = new Scene(practiceModuleView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(practiceModuleScene);
		window.show();
	}

	/**
	 * Go to the game module.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void gamesBtnClick(ActionEvent event) throws IOException {
		if (_model.getCurrentPlayer() == null) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Player Name");
			dialog.setHeaderText("Please enter a name");
			dialog.setContentText("Name:");
			dialog.setGraphic(null);
			dialog.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				_model.setCurrentPlayer(result.get());
				_model.saveCurrentPlayer();
				Parent gamesModuleView = FXMLLoader.load(getClass().getResource("/quinzical/views/GamesModuleView.fxml"));
				Scene gamesModuleScene = new Scene(gamesModuleView);

				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

				window.setScene(gamesModuleScene);
				window.show();
			}
		}
		else if (_model.gameCompleted()) {
			Parent gamesModuleView = FXMLLoader.load(getClass().getResource("/quinzical/views/RewardView.fxml"));
			Scene gamesModuleScene = new Scene(gamesModuleView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(gamesModuleScene);
			window.show();
		}
		else {
			Parent gamesModuleView = FXMLLoader.load(getClass().getResource("/quinzical/views/GamesModuleView.fxml"));
			Scene gamesModuleScene = new Scene(gamesModuleView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(gamesModuleScene);
			window.show();
		}
	}

	/**
	 * Go to the international module.
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void internationalBtnClick(ActionEvent event) throws IOException {
    	_model._internationalSelected = true;
    	Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/InternationalModuleView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }
	
    /**
	 * Go to the text to speech setting.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void ttsSettingsBtnClick(ActionEvent event) throws IOException {
		Parent TtsSettingsView = FXMLLoader.load(getClass().getResource("/quinzical/views/TTSSettingsView.fxml"));
		Scene TtsSettingsScene = new Scene(TtsSettingsView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(TtsSettingsScene);
		window.show();
	}

	/**
	 * Reset quinzical
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void resetBtnClick (ActionEvent event) throws IOException {
		_alert = new Alert(AlertType.CONFIRMATION);
		_alert.setTitle("Reset Quinzical");
		_alert.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());
		_alert.setHeaderText(null);
		_alert.setGraphic(null);
		_alert.setContentText("Are you sure you want to reset the game?");
		Optional<ButtonType> result = _alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			_model.reset();
		}
		Parent TtsSettingsView = FXMLLoader.load(getClass().getResource("/quinzical/views/MenuView.fxml"));
		Scene TtsSettingsScene = new Scene(TtsSettingsView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(TtsSettingsScene);
		window.show();
	}
	
	/**
	 * Go the player rankings.
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void playerRankBtnClick(ActionEvent event) throws IOException {
		Parent view = FXMLLoader.load(getClass().getResource("/quinzical/views/PlayerRankingView.fxml"));
		Scene scene = new Scene(view);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(scene);
		window.show();
    }

}
