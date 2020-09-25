package application.views;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.model.PracticeModule;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class PracticeModuleViewController implements Initializable{

	@FXML
	private GridPane gridPane;

	private PracticeModule model;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			model = PracticeModule.createInstance();
			List<String> _categories = model.getCategory();
			for (int i=0; i<2;i++) {
				gridPane.getColumnConstraints().add(new ColumnConstraints(-1, -1, -1, Priority.ALWAYS, HPos.CENTER, false));
			}
			for (int i=0; i<2;i++) {
				gridPane.getRowConstraints().add(new RowConstraints(-1, -1, -1, Priority.ALWAYS, VPos.CENTER, false));
			}
			
			gridPane.setGridLinesVisible(true);
			int col=0;
			int row=0;
			for (String category: _categories) {
					Button button = new Button(category);
					button.setWrapText(true);
					gridPane.add(button, col, row,1,1);
					col++;
					if (col > 2) {
						col=0;
						row++;
					}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
