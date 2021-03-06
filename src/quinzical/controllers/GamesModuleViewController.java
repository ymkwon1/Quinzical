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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import quinzical.model.QuinzicalModel;

/**
 * Controller for the games module view.
 */

public class GamesModuleViewController implements Initializable{
	
	@FXML
	private Button returnBtn;
	@FXML
	private GridPane gridPane;
    @FXML
    private Label winningsLabel;
    
	private QuinzicalModel _model;
	private List<String> _categories;

	/**
	 * Initialize all components for the games module.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			_model = QuinzicalModel.createInstance();
			_categories = _model.getFiveRandomCategories();
			_model.setAnsweredQuestions();
			int currentWinnings = _model.getWinnings();
			winningsLabel.setText(String.format("Points: %d", currentWinnings));
			int numRows=5;
			int row=0;
			for (String category: _categories) {
				Label label = new Label(category);
				label.setWrapText(true);
				label.getStylesheets().add("quinzical/views/theme.css");
				label.getStyleClass().add("label-categories");
				gridPane.add(label, 0, row,1,1);
				
				//The only button available is the lowest value unanswered question.
				for (int col = 1;col < 6; col++) {
					String value = String.valueOf(100*(col));
					Button button = new Button(value);
					button.getStylesheets().add("quinzical/views/theme.css");
					button.getStyleClass().add("button-1");
					if(Integer.valueOf(value)/100 == _model.getAnsweredQuestions(row)+1) {
						button.setDisable(false);
					} else {
						button.setDisable(true);
					}
					int questionIndex = col-1;
					int categoryIndex = row;
					button.setOnAction(new EventHandler<ActionEvent>() {
	    				@Override
	    				public void handle(ActionEvent e) {
	    					_model.loadClue(category,questionIndex);
	    					_model.addAnsweredQuestions(categoryIndex);
	    					try {
								askQuestion(e);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
	    				}
	    			});
					gridPane.add(button, col, row,1,1);
				}
				row++;
			}
			RowConstraints rc = new RowConstraints();
			rc.setPercentHeight(100d/numRows);
			
			for (int i = 0; i < numRows-1; i++) {
				gridPane.getRowConstraints().add(rc);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Ask a question view.
	 * @param event
	 * @throws IOException
	 */
	void askQuestion(ActionEvent event) throws IOException {
    	Parent askQuestionView = FXMLLoader.load(getClass().getResource("/quinzical/views/GamesQuestionView.fxml"));
    	Scene askQuestionScene = new Scene(askQuestionView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(askQuestionScene);
    	window.show();
	}
	
	/**
	 * Return to the menu view.
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
