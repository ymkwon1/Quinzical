package application.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PlayerRankingViewController {

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TableColumn<?, ?> pointsColumn;

    @FXML
    private Button returnBtn;

    @FXML
    private Button resetBtn;

    @FXML
    void resetBtnClick(ActionEvent event) {

    }

    @FXML
    void returnBtnClick(ActionEvent event) {

    }

}
