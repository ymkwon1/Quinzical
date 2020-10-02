package application.views;

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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class GamesModuleViewController implements Initializable{
	
	@FXML
	private Button returnBtn;
	
	@FXML
	private GridPane gridPane;
	
	private QuinzicalModel _model;
	private List<String> _categories;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			_model = QuinzicalModel.createInstance();
			_categories = _model.getFiveRandomCategories();
			System.out.println(_categories);
			
			int numRows=5;
			int row=0;
			for (String category: _categories) {
				Label label = new Label(category);
				label.setWrapText(true);
				gridPane.add(label, 0, row,1,1);
				
				//The only button available is the lowest value unanswered question.
				for (int col = 1;col < 6; col++) {
					String value = String.valueOf(100*(col));
					Button button = new Button(value);
					if(Integer.valueOf(value)/100 == _model.getAnsweredQuestions(row)+1) {
						button.setDisable(false);
					} else {
						button.setDisable(true);
					}
					int categoryIndex = row;
					int questionIndex = col-1;
					button.setOnAction(new EventHandler<ActionEvent>() {
	    				@Override
	    				public void handle(ActionEvent e) {
	    					_model.loadClue(category,questionIndex);
	    					_model.setAnsweredQuetsions(categoryIndex);
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
	 * fires when any category button is clicked to go the askQuestion scene
	 */
	void askQuestion(ActionEvent event) throws IOException {
    	Parent askQuestionView = FXMLLoader.load(getClass().getResource("GamesQuestionView.fxml"));
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
