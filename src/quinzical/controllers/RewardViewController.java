package quinzical.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import quinzical.model.QuinzicalModel;

/**
 * Controller for the reward view
 */
public class RewardViewController implements Initializable{

    @FXML
    private Text congratPlayerLabel;

	@FXML
	private Label pointsLabel;

	@FXML
	private Button playerRankingBtn;

	@FXML
	private Button returnBtn;
	
    @FXML
    private Text nameLabel;

	private QuinzicalModel _model;

	/**
	 * Initialize all the components of the reward view.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			_model = QuinzicalModel.createInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (_model.getWinnings()< 0) {
			congratPlayerLabel.setText("Thanks for playing ");
		}
		else {
			congratPlayerLabel.setText("Congratulations ");
		}
		nameLabel.setText(_model.getCurrentPlayer());
		pointsLabel.setText(_model.getWinnings() + " points");
	}

	/**
	 * Go to the player rankings
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void playerRankingBtnClick(ActionEvent event) throws IOException {
		Parent view = FXMLLoader.load(getClass().getResource("/quinzical/views/PlayerRankingView.fxml"));
		Scene scene = new Scene(view);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(scene);
		window.show();
	}

	/**
	 * Return to the main menu.
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void returnBtnClick(ActionEvent event) throws IOException {
		Parent menuView = FXMLLoader.load(getClass().getResource("/quinzical/views/MenuView.fxml"));
		Scene menuScene = new Scene(menuView);

		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(menuScene);
		window.show();
	}

}
