package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.model.Player;
import application.model.PlayerRankings;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PlayerRankingViewController implements Initializable{

    @FXML
    private TableView<Player> table;

    @FXML
    private TableColumn<Player, String> nameColumn;

    @FXML
    private TableColumn<Player, Integer> pointsColumn;

    @FXML
    private Button returnBtn;

    @FXML
    private Button resetBtn;
    
    private PlayerRankings _playerList;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<Player, String>("Name"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<Player, Integer>("Points"));
		
		
		_playerList = PlayerRankings.getInstance();
		table.setItems(_playerList.getPlayerList());
		
		if (!_playerList.getPlayerList().isEmpty()) {
		pointsColumn.setSortType(TableColumn.SortType.DESCENDING);
		table.getSortOrder().add(pointsColumn);
		table.sort();
		
		}
		else {
			table.setPlaceholder(new Label(""));
		}
		pointsColumn.setSortable(false);
		table.setSelectionModel(null);
	}

    @FXML
    void resetBtnClick(ActionEvent event) throws IOException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to reset the rankings");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
	    	_playerList.reset();
	    	Parent menuView = FXMLLoader.load(getClass().getResource("/application/views/PlayerRankingView.fxml"));
	    	Scene menuScene = new Scene(menuView);
	    	
	    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	    	
	    	window.setScene(menuScene);
	    	window.show();
		}
    }

    @FXML
    void returnBtnClick(ActionEvent event) throws IOException {
    	Parent menuView = FXMLLoader.load(getClass().getResource("/application/views/MenuView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }


}
