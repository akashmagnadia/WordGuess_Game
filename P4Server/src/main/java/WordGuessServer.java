import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;

//Look at GameInfo for all the variables that can be utilized to help make the UI

public class WordGuessServer extends Application {

	Button powerOn;
	TextField portSelect;
	HashMap<String, Scene> sceneMap;
	ListView<String> serverLog;
	ObservableList<String> info;
	Server serverConnection;
	int port;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("(server) Playing word guess!!!");

		/* Add all scenes to sceneMap for use later */
		sceneMap = new HashMap<>();
		sceneMap.put("serverStart", createServerGUIStartScreen());
		sceneMap.put("serverGameplay", createServerGUIGameplay());
		
		/* Initialize the scene */
		primaryStage.setScene(sceneMap.get("serverStart"));
		primaryStage.setResizable(false);
		primaryStage.show();

		/* Button actions */
		powerOn.setOnAction(e-> {
			System.out.println("Starting up Server...");
			connectTheServer();

			primaryStage.setScene(sceneMap.get("serverGameplay"));
			primaryStage.show();
		});
		
		/* Allow the program to fully close upon exiting, allows current port to be freed up */
		primaryStage.setOnCloseRequest(t -> {
			Platform.exit();
			System.exit(0);
		});
	}

	public void connectTheServer() {

		try {
			serverConnection = new Server(data -> Platform.runLater(()->
					serverLog.getItems().add(String.valueOf(data)))
			);
		} catch (IOException e) {
			e.printStackTrace();
		}

//		serverConnection.server.port = 5555; //default
		serverConnection.server.port = Integer.parseInt(portSelect.getText());
	}
	
	/* First scene is where the server is prompted to set a Port # and power on */
	public Scene createServerGUIStartScreen() {
		Pane startScreen = new Pane();
		DropShadow shadow = new DropShadow();
		
		/* ===== Setup background ===== */
		Image backgroundImage = new Image("serverStart.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		startScreen.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,bSize)));
		/* ===== Setup background ===== */
		
		/* Port Fields */
		Label enterPort = new Label("SET PORT: ");
		enterPort.setFont(Font.font("Comic Sans", 20));
		enterPort.setTextFill(Color.WHITESMOKE);
		portSelect = new TextField();
		portSelect.setPrefWidth(145);
		
		HBox portSelection = new HBox(10, enterPort, portSelect);
		portSelection.setLayoutX(30);
		portSelection.setLayoutY(100);
		/* Port Fields end */
		
		/* Power On Server Button */
		powerOn = new Button("Power On Server!");
		powerOn.setStyle("-fx-background-color: cornflowerblue;" + "-fx-font: 18 Rockwell;");
		powerOn.setTextFill(Color.WHITE);
		powerOn.setEffect(shadow);
		powerOn.setPrefSize(200, 50);
		powerOn.setLayoutX(50);
		powerOn.setLayoutY(150);
		/* Power On Server Button ends */
		
		startScreen.getChildren().addAll(portSelection,powerOn);
		return new Scene(startScreen, 1000, 800);
	}
	
	/* Second scene will display the state of the game in a listView */
	public Scene createServerGUIGameplay() {
		Pane gameplay = new Pane();
		DropShadow shadow = new DropShadow();
		
		/* ===== Setup background ===== */
		Image backgroundImage = new Image("serverMain.png");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		gameplay.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,bSize)));
		/* ===== Setup background ===== */
		
		/* Host Label */
		Label hostName = new Label("Pat Sajak");
		Label host = new Label("Host");
		
		hostName.setFont(Font.font("Rockwell",40));
		hostName.setTextFill(Color.WHITE);
		host.setFont(Font.font("Rockwell",20));
		host.setTextFill(Color.WHITE);
		
		VBox gameHost = new VBox(hostName,host);
		gameHost.setLayoutX(120);
		gameHost.setLayoutY(470);
		/* Host Label */
		
		/* Speech bubble detail */
		Polygon triangle = new Polygon();
		triangle.getPoints().addAll(300.0, 185.0,
				400.0, 115.0,
				400.0, 365.0);
		triangle.setFill(Color.WHITE);
		triangle.setStyle("-fx-border-color: white;");
		/* Speech bubble detail */
		
		/* Game Info */
		info = FXCollections.observableArrayList();
		serverLog = new ListView<>(info);
		serverLog.setPrefSize(400, 250);
		serverLog.setOpacity(1.0);
		serverLog.setLayoutX(400);
		serverLog.setLayoutY(115);
		serverLog.setStyle("-fx-border-color: white;");
		/* Game Info Ends */
		
		gameplay.getChildren().addAll(triangle,gameHost,serverLog);
		return new Scene(gameplay, 970, 700);
	}
}
