import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("serverStart", createServerGUIStartScreen());
		sceneMap.put("serverGameplay", createServerGUIGameplay());
		
		/* Initialize the scene */
		primaryStage.setScene(sceneMap.get("serverStart"));
		primaryStage.show();

		/* Button actions */
		powerOn.setOnAction(e-> {
			System.out.println("Starting up Server...");
			connectTheServer();

			primaryStage.setScene(sceneMap.get("serverGameplay"));
			primaryStage.show();
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

		serverConnection.server.port = 5555; //default
		//TODO: After getting server port is figured out
//		serverConnection.server.port = Integer.parseInt(portSelect.getText());
	}

	public void updateUIForServer() {
		for(int i = 0; i < serverConnection.clients.size(); i++) {

		}
	}

	public void playGame() {

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
		triangle.getPoints().addAll(new Double[]{
	            300.0, 185.0,
	            400.0, 115.0,
	            400.0, 365.0 });
		triangle.setFill(Color.WHITE);
		triangle.setStyle("-fx-border-color: white;");
		/* Speech bubble detail */
		
		/* Game Info */
		info = FXCollections.observableArrayList();
		serverLog = new ListView<String>(info);
		serverLog.setPrefSize(400, 250);
		serverLog.setOpacity(1.0);
		serverLog.setLayoutX(400);
		serverLog.setLayoutY(115);
		serverLog.setStyle("-fx-border-color: white;");
		/* Game Info Ends */
		
		gameplay.getChildren().addAll(triangle,gameHost,serverLog);
		return new Scene(gameplay, 970, 700);
	}

	public GameInfo performLogic(GameInfo receivedInfo) {

		if (receivedInfo.playingAnimalsCategory) {
			return checkLetterClicked("Animals", receivedInfo);
		} else if (receivedInfo.playingFoodCategory) {
			return checkLetterClicked("Food", receivedInfo);
		} else if (receivedInfo.playingStatesCategory) {
			return checkLetterClicked("States", receivedInfo);
		} else {
			System.out.println("This shouldn't happen");
		}
		return receivedInfo;
	}

	public GameInfo checkLetterClicked(String category, GameInfo receivedInfo) {
		if (receivedInfo.selectedLetter[0]) {
			return checkLetterClickedHelper(category, "a", receivedInfo);
		} else if (receivedInfo.selectedLetter[1]) {
			return checkLetterClickedHelper(category, "b", receivedInfo);
		} else if (receivedInfo.selectedLetter[2]) {
			return checkLetterClickedHelper(category, "c", receivedInfo);
		} else if (receivedInfo.selectedLetter[3]) {
			return checkLetterClickedHelper(category, "d", receivedInfo);
		} else if (receivedInfo.selectedLetter[4]) {
			return checkLetterClickedHelper(category, "e", receivedInfo);
		} else if (receivedInfo.selectedLetter[5]) {
			return checkLetterClickedHelper(category, "f", receivedInfo);
		} else if (receivedInfo.selectedLetter[6]) {
			return checkLetterClickedHelper(category, "g", receivedInfo);
		} else if (receivedInfo.selectedLetter[7]) {
			return checkLetterClickedHelper(category, "h", receivedInfo);
		} else if (receivedInfo.selectedLetter[8]) {
			return checkLetterClickedHelper(category, "i", receivedInfo);
		} else if (receivedInfo.selectedLetter[9]) {
			return checkLetterClickedHelper(category, "j", receivedInfo);
		} else if (receivedInfo.selectedLetter[10]) {
			return checkLetterClickedHelper(category, "k", receivedInfo);
		} else if (receivedInfo.selectedLetter[11]) {
			return checkLetterClickedHelper(category, "l", receivedInfo);
		} else if (receivedInfo.selectedLetter[12]) {
			return checkLetterClickedHelper(category, "m", receivedInfo);
		} else if (receivedInfo.selectedLetter[13]) {
			return checkLetterClickedHelper(category, "n", receivedInfo);
		} else if (receivedInfo.selectedLetter[14]) {
			return checkLetterClickedHelper(category, "o", receivedInfo);
		} else if (receivedInfo.selectedLetter[15]) {
			return checkLetterClickedHelper(category, "p", receivedInfo);
		} else if (receivedInfo.selectedLetter[16]) {
			return checkLetterClickedHelper(category, "q", receivedInfo);
		} else if (receivedInfo.selectedLetter[17]) {
			return checkLetterClickedHelper(category, "r", receivedInfo);
		} else if (receivedInfo.selectedLetter[18]) {
			return checkLetterClickedHelper(category, "s", receivedInfo);
		} else if (receivedInfo.selectedLetter[19]) {
			return checkLetterClickedHelper(category, "t", receivedInfo);
		} else if (receivedInfo.selectedLetter[20]) {
			return checkLetterClickedHelper(category, "u", receivedInfo);
		} else if (receivedInfo.selectedLetter[21]) {
			return checkLetterClickedHelper(category, "v", receivedInfo);
		} else if (receivedInfo.selectedLetter[22]) {
			return checkLetterClickedHelper(category, "w", receivedInfo);
		} else if (receivedInfo.selectedLetter[23]) {
			return checkLetterClickedHelper(category, "x", receivedInfo);
		} else if (receivedInfo.selectedLetter[24]) {
			return checkLetterClickedHelper(category, "y", receivedInfo);
		} else if (receivedInfo.selectedLetter[25]) {
			return checkLetterClickedHelper(category, "z", receivedInfo);
		} else {
			System.out.println("In check letter Clicked: This shouldn't happen");
			return receivedInfo;
		}
	}

	public GameInfo checkLetterClickedHelper(String category, String letter, GameInfo receivedInfo) {
		if (category.equals("Animals")) {
			if (!receivedInfo.animalsCategory_WordOneSolved) {
				int index = receivedInfo.animalsCategory_WordOne.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else if (!receivedInfo.animalsCategory_WordTwoSolved) {
				int index = receivedInfo.animalsCategory_WordTwo.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else if (!receivedInfo.animalsCategory_WordThreeSolved) {
				int index = receivedInfo.animalsCategory_WordThree.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else {
				System.out.println("In Animals Category: This shouldn't happen");
			}
		} else if (category.equals("Food")) {
			if (!receivedInfo.foodCategory_WordOneSolved) {
				int index = receivedInfo.foodCategory_WordOne.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else if (!receivedInfo.foodCategory_WordTwoSolved) {
				int index = receivedInfo.foodCategory_WordTwo.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else if (!receivedInfo.foodCategory_WordThreeSolved) {
				int index = receivedInfo.foodCategory_WordThree.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else {
				System.out.println("In Food Category: This shouldn't happen");
			}
		} else if (category.equals("States")) {
			if (!receivedInfo.statesCategory_WordOneSolved) {
				int index = receivedInfo.statesCategory_WordOne.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else if (!receivedInfo.statesCategory_WordTwoSolved) {
				int index = receivedInfo.statesCategory_WordTwo.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else if (!receivedInfo.statesCategory_WordThreeSolved) {
				int index = receivedInfo.statesCategory_WordThree.indexOf(letter);
				if (index == -1) {
					receivedInfo.guessLeft--;
					receivedInfo.indexOfLetter = index;
				}
			} else {
				System.out.println("In States Category: This shouldn't happen");
			}
		} else {
			System.out.println("In checking letter clicked: This shouldn't happen");
		}
		return receivedInfo;
	}
}
