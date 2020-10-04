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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TTSSettingsViewController implements Initializable{

	@FXML
	private Button testBtn;

	@FXML
	private Button decreaseBtn;

	@FXML
	private Button increaseBtn;

	@FXML
	private Label speedLabel;

	@FXML
	private Button returnBtn;

	private QuinzicalModel model;

	@FXML
	void decreaseBtnClick(ActionEvent event) {
		if (model.getTTSSpeed() > 10) {
			model.decreaseTTSSpeed();
			speedLabel.setText(String.format("%d wpm",model.getTTSSpeed()));
		}
	}

	@FXML
	void increaseBtnClick(ActionEvent event) {
		model.increaseTTSSpeed();
		speedLabel.setText(String.format("%d wpm",model.getTTSSpeed()));
	}

	@FXML
	void testBtnClick(ActionEvent event) {
		model.tts("Testing the speed at which text is read");
	}

	@FXML
	void returnBtnClick(ActionEvent event) throws IOException {
		if (model.getPreviousScene() == 1) {
			Parent menuView = FXMLLoader.load(getClass().getResource("MenuView.fxml"));
			Scene menuScene = new Scene(menuView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(menuScene);
			window.show();
		}
		else {
			Parent menuView = FXMLLoader.load(getClass().getResource("GamesQuestionView.fxml"));
			Scene menuScene = new Scene(menuView);

			Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

			window.setScene(menuScene);
			window.show();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			model = QuinzicalModel.createInstance();
			speedLabel.setText(String.format("%d wpm",model.getTTSSpeed()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
