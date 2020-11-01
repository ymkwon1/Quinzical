package quinzical;

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

/**
 * Main class for the Quinzical application.
 * @authors Youngmin Kwon, Dylan Xin
 */

public class Main extends Application{

	/**
	 * Start the application.
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
		    	alert.setTitle("Quit Quinzical");
		    	alert.setHeaderText(null);
		    	alert.getDialogPane().getStylesheets().add(getClass().getResource("/quinzical/views/theme.css").toExternalForm());
		    	alert.setGraphic(null);
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