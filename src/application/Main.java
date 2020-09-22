package application;

import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{

	/**
	 * start with the menu scene, if x is clicked confirm with user if he wants to quit
	 * then quit the program
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("views/MenuView.fxml"));
		primaryStage.setTitle("Quinzical");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
		    	Alert alert = new Alert(AlertType.CONFIRMATION);
		    	alert.setTitle("Confirmation Dialog");
		    	alert.setHeaderText(null);
		    	alert.setContentText("Are you sure you want to quit the game?");
		    	Optional<ButtonType> result = alert.showAndWait();
		    	if (result.get() == ButtonType.OK) {
		    		Platform.exit();
		    	}
		    	else {
		    		event.consume();
		    	}
			}
		});
	}
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
}