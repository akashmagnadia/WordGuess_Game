import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

//Look at GameInfo for all the variables that can be utilized to help make the UI

public class WordGuessServer extends Application {

	ListView<String> serverLog;
	Server serverConnection;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(server) Playing word guess!!!");

		playGame();

		Scene scene = new Scene(new HBox(),600,600);
		primaryStage.setScene(scene);
//		primaryStage.show();
	}

	public void connectTheServer() {
		try {
			serverConnection = new Server(data -> Platform.runLater(()->
			{
				serverLog.getItems().add(String.valueOf(data));
			})
			);
		} catch (IOException e) {
			e.printStackTrace();
		}

		serverConnection.server.port = 5555; //default
		//TODO: After getting server port is figured out
//		serverConnection.server.port = Integer.parseInt(portInput.getText());
	}

	public void updateUIForServer() {
		for(int i = 0; i < serverConnection.clients.size(); i++) {

		}
	}

	public void playGame() {

	}
}
