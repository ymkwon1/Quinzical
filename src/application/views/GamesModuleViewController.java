package application.views;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.model.PracticeModule;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class GamesModuleViewController implements Initializable{
	
	@FXML
	private Button returnBtn;
	
	@FXML
	private GridPane gridPane;
	
	private PracticeModule _model;
	private List<String> _categories;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			_model = PracticeModule.createInstance();
			_categories = _model.getFiveRandomCategories();
			System.out.println(_categories);
			
			int numRows;
			if (_categories.size()%3 == 0) {
				numRows = _categories.size()/3;
			}
			else {
				numRows = _categories.size()/3 + 1;
			}
			int col=0;
			int row=0;
			for (String category: _categories) {
					Button button = new Button(category);
					button.setWrapText(true);
					gridPane.add(button, col, row,1,1);
					button.setOnAction(new EventHandler<ActionEvent>() {
	    				@Override
	    				public void handle(ActionEvent e) {
	    					try {
								_model.setCurrentCategory(category);
								_model.loadRandomQuestionAndAnswer(category);
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
	 * fires when any category button is clicked to go the askQuestion scene
	 */
	void askQuestion(ActionEvent event) throws IOException {
    	Parent askQuestionView = FXMLLoader.load(getClass().getResource("QuestionView.fxml"));
    	Scene askQuestionScene = new Scene(askQuestionView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(askQuestionScene);
    	window.show();
	}
	
	@FXML
    void returnBtnClick(ActionEvent event) throws IOException {
    	Parent menuView = FXMLLoader.load(getClass().getResource("MenuView.fxml"));
    	Scene menuScene = new Scene(menuView);
    	
    	Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
    	
    	window.setScene(menuScene);
    	window.show();
    }



}
