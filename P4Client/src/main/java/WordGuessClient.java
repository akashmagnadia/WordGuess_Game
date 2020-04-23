import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

//Look at GameInfo for all the variables that can be utilized to help make the UI

public class WordGuessClient extends Application {

	ListView<String> clientLog;
	Client clientConnection;
	GameInfo myPlayerInfo;

	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("(Client) Word Guess!!!");

		clientLog = new ListView<>();

		playGame();

		Scene scene = new Scene(new HBox(),600,600);
		primaryStage.setScene(scene);
//		primaryStage.show();
	}

	public void connectToServer() {
		clientConnection = new Client(data -> Platform.runLater(()->
		{
			clientLog.getItems().add(String.valueOf(data));
		})
		);

		try {
			clientConnection.socketClient= new Socket("127.0.0.1", 5555);
			//TODO: UnComment this when IPInput and PortInput is setup at the end
//			clientConnection.socketClient= new Socket(IPInput.getText(), Integer.parseInt(portInput.getText()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientConnection.start();
	}

	public void playGame() {
		clientConnection.send(myPlayerInfo, "Sending Player Info");
	}
}
