package application.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.model.QuinzicalModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class PracticeModuleViewController implements Initializable{

	@FXML
	private GridPane gridPane;

    @FXML
    private Button returnBtn;

	
	private QuinzicalModel model;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			model = QuinzicalModel.createInstance();
			List<String> _categories = model.getCategories();
			int numRows;
			if (_categories.size()%3 == 0) {
				numRows = _categories.size()/3;
			}
			else {
				numRows = _categories.size()/3 + 1;
			}
			for (int i = 0; i < numRows; i++) {
				gridPane.getRowConstraints().add(new RowConstraints(80));
			}
			for (int i = 0; i < 3; i++) {
				ColumnConstraints cc = new ColumnConstraints(180);
				cc.setHalignment(HPos.CENTER);
				gridPane.getColumnConstraints().add(cc);
			}

			int col=0;
			int row=0;
			for (String category: _categories) {
					Button button = new Button(category);
					button.setPrefWidth(150);
					button.setPrefHeight(50);
					button.getStylesheets().add("application/views/theme.css");
					button.getStyleClass().add("button-1");
					gridPane.add(button, col, row,1,1);
					button.setOnAction(new EventHandler<ActionEvent>() {
	    				@Override
	    				public void handle(ActionEvent e) {
	    					try {
								model.loadRandomClue(category);
	    						askQuestion(e);    						
							} catch (IOException e1) {
								e1.printStackTrace();
							}
	    				}
	    			});
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
	
	/**
	 * fires when any category button is clicked to go the askQuestion scene
	 */
	void askQuestion(ActionEvent event) throws IOException {
    	Parent askQuestionView = FXMLLoader.load(getClass().getResource("/application/views/PracticeQuestionView.fxml"));
    	Scene askQuestionScene = new Scene(askQuestionView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(askQuestionScene);
    	window.show();
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
