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
		
		/* Allow the program to fully close upon exiting, allows current port to be freed up */
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
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

	public GameInfo updateIndexOfLetter(GameInfo receivedInfo) {
		//run this function when letter is clicked, which will update indexOfLetter and return it

		GameInfo toReturnInfo = receivedInfo;
		if (receivedInfo.playingAnimalsCategory) {
			toReturnInfo = checkLetterClicked("Animals", receivedInfo);

			//if the word is solved
			if (toReturnInfo.workingWord.length() <= 0) {
				if (!toReturnInfo.animalsCategory_WordOneSolved) {
					toReturnInfo.animalsCategory_WordOneSolved = true;
				} else if (!toReturnInfo.animalsCategory_WordTwoSolved) {
					toReturnInfo.animalsCategory_WordTwoSolved = true;
				} else if (!toReturnInfo.animalsCategory_WordThreeSolved) {
					toReturnInfo.animalsCategory_WordThreeSolved = true;
				} else {
					System.out.println("In updateIndexOfLetter animal category: This shouldn't happen");
				}
			}

		} else if (receivedInfo.playingFoodCategory) {
			toReturnInfo = checkLetterClicked("Food", receivedInfo);

			//if the word is solved
			if (toReturnInfo.workingWord.length() <= 0) {
				if (!toReturnInfo.foodCategory_WordOneSolved) {
					toReturnInfo.foodCategory_WordOneSolved = true;
				} else if (!toReturnInfo.foodCategory_WordTwoSolved) {
					toReturnInfo.foodCategory_WordTwoSolved = true;
				} else if (!toReturnInfo.foodCategory_WordThreeSolved) {
					toReturnInfo.foodCategory_WordThreeSolved = true;
				} else {
					System.out.println("In updateIndexOfLetter food category: This shouldn't happen");
				}
			}

		} else if (receivedInfo.playingStatesCategory) {
			toReturnInfo = checkLetterClicked("States", receivedInfo);

			//if the word is solved
			if (toReturnInfo.workingWord.length() <= 0) {
				if (!toReturnInfo.statesCategory_WordOneSolved) {
					toReturnInfo.statesCategory_WordOneSolved = true;
				} else if (!toReturnInfo.statesCategory_WordTwoSolved) {
					toReturnInfo.statesCategory_WordTwoSolved = true;
				} else if (!toReturnInfo.statesCategory_WordThreeSolved) {
					toReturnInfo.statesCategory_WordThreeSolved = true;
				} else {
					System.out.println("In updateIndexOfLetter states category: This shouldn't happen");
				}
			}

		} else {
			System.out.println("This shouldn't happen");
		}
		return toReturnInfo;

		//indexOfLetter will be changing,
		//set to -2 to reset
		//-2 means nothing has been changed
		//-1 means index is not found
		//otherwise letter found at that index which is stored in indexOfLetter
	}

	public GameInfo validGuessChecker(String word, String letter, GameInfo receivedInfo) {
		int index = word.indexOf(letter);
		if (index == -1) {
			receivedInfo.guessLeft--;
		}
		receivedInfo.indexOfLetter = index;
		receivedInfo.letter = letter;

		if (receivedInfo.workingWord == null) {
			receivedInfo.workingWord = word;
		}

		receivedInfo.workingWord = receivedInfo.workingWord.replace(letter, "");

		//checks if working word is solved
		return receivedInfo;
	}

	public GameInfo checkLetterClickedHelper(String category, String letter, GameInfo receivedInfo) {
		switch (category) {
			case "Animals":
				if (!receivedInfo.animalsCategory_WordOneSolved) {
					return validGuessChecker(receivedInfo.animalsCategory_WordOne, letter, receivedInfo);
				} else if (!receivedInfo.animalsCategory_WordTwoSolved) {
					return validGuessChecker(receivedInfo.animalsCategory_WordTwo, letter, receivedInfo);
				} else if (!receivedInfo.animalsCategory_WordThreeSolved) {
					return validGuessChecker(receivedInfo.animalsCategory_WordThree, letter, receivedInfo);
				} else {
					System.out.println("In Animals Category: This shouldn't happen");
				}
				break;
			case "Food":
				if (!receivedInfo.foodCategory_WordOneSolved) {
					return validGuessChecker(receivedInfo.foodCategory_WordOne, letter, receivedInfo);
				} else if (!receivedInfo.foodCategory_WordTwoSolved) {
					return validGuessChecker(receivedInfo.foodCategory_WordTwo, letter, receivedInfo);
				} else if (!receivedInfo.foodCategory_WordThreeSolved) {
					return validGuessChecker(receivedInfo.foodCategory_WordThree, letter, receivedInfo);
				} else {
					System.out.println("In Food Category: This shouldn't happen");
				}
				break;
			case "States":
				if (!receivedInfo.statesCategory_WordOneSolved) {
					return validGuessChecker(receivedInfo.statesCategory_WordOne, letter, receivedInfo);
				} else if (!receivedInfo.statesCategory_WordTwoSolved) {
					return validGuessChecker(receivedInfo.statesCategory_WordTwo, letter, receivedInfo);
				} else if (!receivedInfo.statesCategory_WordThreeSolved) {
					return validGuessChecker(receivedInfo.statesCategory_WordThree, letter, receivedInfo);
				} else {
					System.out.println("In States Category: This shouldn't happen");
				}
				break;
			default:
				System.out.println("In checking letter clicked: This shouldn't happen");
				break;
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
}
