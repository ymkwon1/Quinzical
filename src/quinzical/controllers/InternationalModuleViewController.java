package quinzical.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import quinzical.model.QuinzicalModel;

/**
 * Controller for the international module view.
 */
public class InternationalModuleViewController implements Initializable{

	@FXML
	private GridPane gridPane;
	
    @FXML
    private Button returnBtn;
	
	private QuinzicalModel _model;

	/**
	 * Initialize all the components in the international module.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			_model = QuinzicalModel.createInstance();
			List<String> _categories = _model.getCategories(_model._internationalSelected);
			int numRows;
			if (_categories.size()%3 == 0) {
				numRows = _categories.size()/3;
			}
			else {
				numRows = _categories.size()/3 + 1;
			}
			for (int i = 0; i < numRows; i++) {
				gridPane.getRowConstraints().add(new RowConstraints(100));
			}
			for (int i = 0; i < 3; i++) {
				ColumnConstraints cc = new ColumnConstraints(200);
				cc.setHalignment(HPos.CENTER);
				gridPane.getColumnConstraints().add(cc);
			}

			int col=0;
			int row=0;
			for (String category: _categories) {
					Button button = new Button(category);
					button.setPrefWidth(150);
					button.setPrefHeight(50);
					button.getStylesheets().add("quinzical/views/theme.css");
					button.getStyleClass().add("button--menu-1");
					gridPane.add(button, col, row,1,1);
					button.setOnAction(new EventHandler<ActionEvent>() {
	    				@Override
	    				public void handle(ActionEvent e) {
	    					try {
								_model.loadRandomClue("int",category);
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
	 * Ask a question.
	 * @param event
	 * @throws IOException
	 */
	void askQuestion(ActionEvent event) throws IOException {
    	Parent askQuestionView = FXMLLoader.load(getClass().getResource("/quinzical/views/InternationalQuestionView.fxml"));
    	Scene askQuestionScene = new Scene(askQuestionView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(askQuestionScene);
    	window.show();
	}
	
	/**
	 * Return to the menu.
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
