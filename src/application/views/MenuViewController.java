package application.views;

import java.io.IOException;

import application.model.PracticeModule;
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
    void practiceBtnClick(ActionEvent event) throws IOException {
    	try {
			PracticeModule model = new PracticeModule();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Parent practiceModuleView = FXMLLoader.load(getClass().getResource("PracticeModuleView.fxml"));
		Scene practiceModuleScene = new Scene(practiceModuleView);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(practiceModuleScene);
		window.show();
    }

}
