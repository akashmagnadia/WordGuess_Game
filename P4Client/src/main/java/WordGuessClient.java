import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

//Look at GameInfo for all the variables that can be utilized to help make the UI

public class WordGuessClient extends Application {

	HashMap<String, Scene> sceneMap;
	
	ListView<String> clientLog;
	ObservableList<String> info;
	
	Button connect;
	TextField IPInput, portInput;
	MenuItem exit, mute, unmute, newGame;
	
	Label correctGuessesRemaining;
	Label correctGuesses;
	
	static Label category;
	static MenuButton categories;
	MenuItem animals,states,food;
	
	MenuButton letterChoices;
	MenuItem letA,letB,letC,letD,letE,letF,letG,letH,letI,letJ,letK,letL,letM,letN,letO,letP,letQ,letR,letS,letT,letU,letV,letW,letX,letY,letZ;
	
	Image defaultGreenBox = new Image("defaultLetterScreen.png");
	Image whiteBox = new Image("whiteSpace.png");
	static ImageView firstBox;
	static ImageView secondBox;
	static ImageView thirdBox;
	static ImageView fourthBox;
	static ImageView fifthBox;
	static ImageView sixthBox;
	static ImageView seventhBox;
	static ImageView eighthBox;
	static ImageView ninthBox;
	static ImageView tenthBox;
	
	Button playAgain;
	Button quit;
	Label result;
	
	MediaPlayer mediaPlayer;

	int port;
	String ip;
	static Client clientConnection;
	
	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("(Client) Word Guess!!!");
//		music(); //TODO:Uncomment
		
		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("clientStart", createClientGUIStartScreen());
		sceneMap.put("clientGameplay", createClientGUIGameplay());
		sceneMap.put("clientResults", createResultScene());
		
		/* Initial Scene Configuration */
		primaryStage.setScene(sceneMap.get("clientStart"));
		primaryStage.setResizable(false);
		primaryStage.show();
		
		unmute.setOnAction(e->{
			music();
		});
		
		mute.setOnAction(e->{
			mediaPlayer.stop();
		});

		exit.setOnAction(e->{
			Platform.exit();
			System.exit(0);
		});
		
		quit.setOnAction(e->{
			Platform.exit();
			System.exit(0);
		});

		connect.setOnAction(e -> {
			System.out.println("Connecting to server...");
			connectToServer();
			primaryStage.setScene(sceneMap.get("clientGameplay"));
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

		listenFor();
	}
	


	public void connectToServer() {
		try {
			port = Integer.parseInt(portInput.getText()); // convert string input to integer
			ip = IPInput.getText(); // set IP address
			clientConnection = new Client(data->{
				Platform.runLater(()->{
					clientLog.getItems().add(String.valueOf(data));
				});
			}, port, ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		clientConnection.start();
	}

	public void playGame() {
		clientConnection.send(clientConnection.myPlayerInfo, "Sending Player Info");
	}
	
	/* First scene is where the client is prompted to enter IP and Port # to connect to server and begin game */
	public Scene createClientGUIStartScreen() {
		Pane startScreen = new Pane();
		DropShadow shadow = new DropShadow();
		
		/* ===== Setup background ===== */
		Image backgroundImage = new Image("clientStartBackground.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		startScreen.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,bSize)));
		/* ===== Setup background ===== */
		
		/* Title Label */
		Label welcome = new Label("Welcome to Wheel of Fortune!");
		welcome.setFont(Font.font("Rockwell", 40));
		welcome.setTextFill(Color.DEEPSKYBLUE);
		
		Label edition = new Label("Edition 342");
		edition.setFont(Font.font("Rockwell", 20));
		edition.setTextFill(Color.DARKTURQUOISE);

		VBox welcomeLabels = new VBox(10,welcome,edition);
		welcomeLabels.setLayoutX(40);
		welcomeLabels.setLayoutY(50);
		/* Title Label */
		
		/* IP and Port Fields */
		Label enterIP = new Label("Enter IP Address: ");
		enterIP.setFont(Font.font("Comic Sans", 20));
		enterIP.setTextFill(Color.WHITESMOKE);
		Label enterPort = new Label("Enter Port Number: ");
		enterPort.setFont(Font.font("Comic Sans", 20));
		enterPort.setTextFill(Color.WHITESMOKE);
		IPInput = new TextField();
		portInput = new TextField();
		portInput.setPrefWidth(145);
		
		HBox ipSelection = new HBox(10,enterIP,IPInput);
		ipSelection.setLayoutX(40);
		ipSelection.setLayoutY(200);
		HBox portSelection = new HBox(10, enterPort, portInput);
		portSelection.setLayoutX(40);
		portSelection.setLayoutY(240);
		/* IP and Port Fields end */
		
		/* Connect Button */
		connect = new Button("Connect!");
		connect.setStyle("-fx-background-color: purple;" + "-fx-font: 30 Rockwell;");
		connect.setTextFill(Color.WHITE);
		connect.setEffect(shadow);
		connect.setPrefSize(200, 50);
		connect.setLayoutX(100);
		connect.setLayoutY(350);
		/* Connect Button ends */
		
		startScreen.getChildren().addAll(welcomeLabels,ipSelection,portSelection,connect);
		return new Scene(startScreen, 1000, 770);
	}
	
	public Scene createClientGUIGameplay() {
		Pane gameplay = new Pane();
		DropShadow shadow = new DropShadow();

		/* ===== Setup background ===== */
		Image backgroundImage = new Image("gameplayBack2.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		gameplay.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,bSize)));
		/* ===== Setup background ===== */
		
		
		/* ===== Menubar setup ===== */
		MenuBar menuBar = new MenuBar();
		menuBar.setPrefWidth(1280);
		
		Menu options = new Menu("Options");
		exit = new MenuItem("Exit");
		mute = new MenuItem("Mute Music");
		unmute = new MenuItem("Play Music");
		newGame = new MenuItem("New Game");
		
		options.getItems().addAll(unmute,mute,newGame,exit);
		menuBar.getMenus().addAll(options);
		/* ===== Menubar setup ===== */
		
		/* Initializing the image views that are going to contain blank spaces depending on the length of the word 
		 * I'm assuming our maximum word length is going to be 10 which is why there are 10 ImageViews
		 * Once a user gets a word to guess, the default green boxes will turn white depending on the number of letters in the word
		 * Once the user correctly guesses a letter, the ImageView corresponding to the index of that letter will display the letter
		 * */
		firstBox = new ImageView(defaultGreenBox);
		firstBox.setFitHeight(75);
		firstBox.setFitWidth(60);
		secondBox = new ImageView(defaultGreenBox);
		secondBox.setFitHeight(75);
		secondBox.setFitWidth(60);
		thirdBox = new ImageView(defaultGreenBox);
		thirdBox.setFitHeight(75);
		thirdBox.setFitWidth(60);
		fourthBox = new ImageView(defaultGreenBox);
		fourthBox.setFitHeight(75);
		fourthBox.setFitWidth(60);
		fifthBox = new ImageView(defaultGreenBox);
		fifthBox.setFitHeight(75);
		fifthBox.setFitWidth(60);
		sixthBox = new ImageView(defaultGreenBox);
		sixthBox.setFitHeight(75);
		sixthBox.setFitWidth(60);
		seventhBox = new ImageView(defaultGreenBox);
		seventhBox.setFitHeight(75);
		seventhBox.setFitWidth(60);
		eighthBox = new ImageView(defaultGreenBox);
		eighthBox.setFitHeight(75);
		eighthBox.setFitWidth(60);
		ninthBox = new ImageView(defaultGreenBox);
		ninthBox.setFitHeight(75);
		ninthBox.setFitWidth(60);
		tenthBox = new ImageView(defaultGreenBox);
		tenthBox.setFitHeight(75);
		tenthBox.setFitWidth(60);
		
		HBox letters = new HBox(firstBox, secondBox, thirdBox, fourthBox, fifthBox, sixthBox, seventhBox, eighthBox, ninthBox, tenthBox);
		letters.setLayoutX(238);
		letters.setLayoutY(267);
		/* Letter Box initialization ends */
		
		/* Category Bar */
		category = new Label("Category will go here");
		category.setPrefSize(770, 30);
		category.setFont(Font.font("Rockwell",20));
		category.setStyle("-fx-background-color: purple;");
		category.setTextFill(Color.WHITE);
		category.setTextAlignment(TextAlignment.CENTER);
		category.setAlignment(Pos.CENTER);
		category.setLayoutX(150);
		category.setLayoutY(120);
		/* Category Bar ends */
		
		/* Drop down menu for category selection */		
		categories = new MenuButton();
		animals = new MenuItem("Animals");
		food = new MenuItem("Food");
		states = new MenuItem("States");
		
		categories.setText("Pick a category");
		categories.getItems().addAll(animals,food,states);
		categories.setPrefSize(120, 40);
		categories.setStyle("-fx-background-color: aqua;");
		categories.setLayoutX(170);
		categories.setLayoutY(550);
		/* Drop down menu for categories */
		
		/* Drop down menu for letter selection */
		letterChoices = new MenuButton();
		letA = new MenuItem("A");
		letB = new MenuItem("B");
		letC = new MenuItem("C");
		letD = new MenuItem("D");
		letE = new MenuItem("E");
		letF = new MenuItem("F");
		letG = new MenuItem("G");
		letH = new MenuItem("H");
		letI = new MenuItem("I");
		letJ = new MenuItem("J");
		letK = new MenuItem("K");
		letL = new MenuItem("L");
		letM = new MenuItem("M");
		letN = new MenuItem("N");
		letO = new MenuItem("O");
		letP = new MenuItem("P");
		letQ = new MenuItem("Q");
		letR = new MenuItem("R");
		letS = new MenuItem("S");
		letT = new MenuItem("T");
		letU = new MenuItem("U");
		letV = new MenuItem("V");
		letW = new MenuItem("W");
		letX = new MenuItem("X");
		letY = new MenuItem("Y");
		letZ = new MenuItem("Z");
		
		letterChoices.setText("Guess a letter!");
		letterChoices.getItems().addAll(letA,letB,letC,letD,letE,letF,letG,letH,letI,
				letJ,letK,letL,letM,letN,letO,letP,letQ,letR,letS,letT,letU,letV,letW,letX,letY,letZ);
		letterChoices.setPrefSize(120, 40);
		letterChoices.setStyle("-fx-background-color: coral;");
		letterChoices.setTextFill(Color.WHITE);
		letterChoices.setLayoutX(300);
		letterChoices.setLayoutY(550);
		/* Drop down menu for letter selection */

		/* Guesses Remaining Labels */
		correctGuessesRemaining = new Label("Guesses Remaining:");
		correctGuesses = new Label("6");

		correctGuessesRemaining.setFont(Font.font("Rockwell",20));
		correctGuessesRemaining.setTextFill(Color.GOLD);

		correctGuesses.setStyle("-fx-border-color: gold;");
		correctGuesses.setPrefSize(200, 100);
		correctGuesses.setTextAlignment(TextAlignment.CENTER);
		correctGuesses.setAlignment(Pos.CENTER);
		correctGuesses.setFont(Font.font("Rockwell",64));
		correctGuesses.setTextFill(Color.GOLD);

		VBox guessesBox = new VBox(5,correctGuessesRemaining,correctGuesses);
		guessesBox.setLayoutX(430);
		guessesBox.setLayoutY(530);
		/* Guesses Remaining Labels */
		
		/* Client log */
		info = FXCollections.observableArrayList();
		clientLog = new ListView<String>(info);
		clientLog.setPrefSize(325, 120);
		clientLog.setOpacity(0.8);
		clientLog.setLayoutX(640);
		clientLog.setLayoutY(540);
		/* Client log */
		
		gameplay.getChildren().addAll(menuBar,letters,category,categories,letterChoices, guessesBox, clientLog);
		return new Scene(gameplay, 1000, 800);
	}
	
	public Scene createResultScene() {
		Pane results = new Pane();
		DropShadow shadow = new DropShadow();

		/* ===== Setup background ===== */
		Image backgroundImage = new Image("resultsBack.jpg");
		BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
		results.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,BackgroundPosition.CENTER,bSize)));
		/* ===== Setup background ===== */
		
		/* Result label */
		result = new Label();
		result.setStyle("-fx-border-color: gold;");
		result.setPrefSize(440,250);
		result.setTextAlignment(TextAlignment.CENTER);
		result.setAlignment(Pos.CENTER);
		result.setFont(Font.font("Rockwell",64));
		result.setTextFill(Color.GOLD);
		/* Result label */
		
		/* Play Again and Quit Buttons */
		playAgain = new Button("Play Again");
		playAgain.setStyle("-fx-background-color: purple;" + "-fx-font: 25 Rockwell;");
		playAgain.setTextFill(Color.WHITE);
		playAgain.setEffect(shadow);
		playAgain.setPrefSize(200, 50);
		playAgain.setLayoutX(100);
		playAgain.setLayoutY(350);
		
		quit = new Button("Quit");
		quit.setStyle("-fx-background-color: purple;" + "-fx-font: 25 Rockwell;");
		quit.setTextFill(Color.WHITE);
		quit.setEffect(shadow);
		quit.setPrefSize(200, 50);
		quit.setLayoutX(100);
		quit.setLayoutY(350);
		
		HBox resultButtons = new HBox(40,playAgain,quit);
		/* Play Again and Quit Buttons */
		
		VBox resultScreen = new VBox(20,result,resultButtons);
		resultScreen.setLayoutX(30);
		resultScreen.setLayoutY(20);
		
		results.getChildren().addAll(resultScreen);
		return new Scene(results, 500,400);
	}
	
	public void music() {
		String path = WordGuessClient.class.getResource("WOFTheme.mp3").toString();
		Media h = new Media(path);
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();
	}

	public void listenFor() {
		listenForCategory();
		listenForLetter();
	}

	public static void updateOnChange() {
		updateLetterBox();
		checkEndOfRound();
		checkAttempts();
		checkWonOrLose();
	}

	public static void checkWonOrLose() {
		if (clientConnection.myPlayerInfo.gameWon) {
			resetForNextRound();
			category.setText("Game Won");
		} else if (clientConnection.myPlayerInfo.gameLost) {
			resetForNextRound();
			category.setText("Game Lost");
		}
	}

	public static void checkAttempts() {
		if (clientConnection.myPlayerInfo.attempts >= 3) {
			clientConnection.myPlayerInfo.gameLost = true;
		}
	}

	public static void updateLetterBox() {
		if (clientConnection != null) {
			if (clientConnection.myPlayerInfo.indexOfLetter == 0) {
				updateLetterBoxHelper(firstBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 1) {
				updateLetterBoxHelper(secondBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 2) {
				updateLetterBoxHelper(thirdBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 3) {
				updateLetterBoxHelper(fourthBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 4) {
				updateLetterBoxHelper(fifthBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 5) {
				updateLetterBoxHelper(sixthBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 6) {
				updateLetterBoxHelper(seventhBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 7) {
				updateLetterBoxHelper(eighthBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 8) {
				updateLetterBoxHelper(ninthBox);
			} else if (clientConnection.myPlayerInfo.indexOfLetter == 9) {
				updateLetterBoxHelper(tenthBox);
			}
		}
	}

	public static void updateLetterBoxHelper(ImageView boxToModify) {
		if (clientConnection.myPlayerInfo.selectedLetter.equals("a")) {
			boxToModify.setImage(new Image("letterA.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("b")) {
			boxToModify.setImage(new Image("letterB.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("c")) {
			boxToModify.setImage(new Image("letterC.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("d")) {
			boxToModify.setImage(new Image("letterD.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("e")) {
			boxToModify.setImage(new Image("letterE.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("f")) {
			boxToModify.setImage(new Image("letterF.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("g")) {
			boxToModify.setImage(new Image("letterG.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("h")) {
			boxToModify.setImage(new Image("letterH.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("i")) {
			boxToModify.setImage(new Image("letterI.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("j")) {
			boxToModify.setImage(new Image("letterJ.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("k")) {
			boxToModify.setImage(new Image("letterK.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("l")) {
			boxToModify.setImage(new Image("letterL.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("m")) {
			boxToModify.setImage(new Image("letterM.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("n")) {
			boxToModify.setImage(new Image("letterN.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("o")) {
			boxToModify.setImage(new Image("letterO.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("p")) {
			boxToModify.setImage(new Image("letterP.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("q")) {
			boxToModify.setImage(new Image("letterQ.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("r")) {
			boxToModify.setImage(new Image("letterR.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("s")) {
			boxToModify.setImage(new Image("letterS.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("t")) {
			boxToModify.setImage(new Image("letterT.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("u")) {
			boxToModify.setImage(new Image("letterU.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("v")) {
			boxToModify.setImage(new Image("letterV.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("w")) {
			boxToModify.setImage(new Image("letterW.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("x")) {
			boxToModify.setImage(new Image("letterX.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("y")) {
			boxToModify.setImage(new Image("letterY.png"));
		} else if (clientConnection.myPlayerInfo.selectedLetter.equals("z")) {
			boxToModify.setImage(new Image("letterZ.png"));
		} else {
			System.out.println("No letter is selected yet");
		}
	}

	public static void checkEndOfRound() {
		ClientSideGameInfo gameInfo = clientConnection.myPlayerInfo;

		if (gameInfo.playingAnimalsCategory) {
			if (gameInfo.animalsCategory_WordThreeSolved) {
				gameInfo.gameWon = true;
			} else if (gameInfo.animalsCategory_WordTwoSolved) {
				resetForNextRound();
			} else if (gameInfo.animalsCategory_WordOneSolved) {
				resetForNextRound();
			} else {

			}
		} else if (gameInfo.playingFoodCategory) {
			if (!gameInfo.foodCategory_WordOneSolved) {
				gameInfo.gameWon = true;
			} else if (!gameInfo.foodCategory_WordTwoSolved) {
				resetForNextRound();
			} else if (!gameInfo.foodCategory_WordThreeSolved) {
				resetForNextRound();
			} else {

			}
		} else if (gameInfo.playingStatesCategory) {
			if (!gameInfo.statesCategory_WordOneSolved) {
				gameInfo.gameWon = true;
			} else if (!gameInfo.statesCategory_WordTwoSolved) {
				resetForNextRound();
			} else if (!gameInfo.statesCategory_WordThreeSolved) {
				resetForNextRound();
			} else {

			}
		} else {
			//not playing any category
		}

		clientConnection.myPlayerInfo = gameInfo;
	}

	private static void resetForNextRound() {
		firstBox.setImage(new Image("defaultLetterScreen.png"));
		secondBox.setImage(new Image("defaultLetterScreen.png"));
		thirdBox.setImage(new Image("defaultLetterScreen.png"));
		fourthBox.setImage(new Image("defaultLetterScreen.png"));
		fifthBox.setImage(new Image("defaultLetterScreen.png"));
		sixthBox.setImage(new Image("defaultLetterScreen.png"));
		seventhBox.setImage(new Image("defaultLetterScreen.png"));
		eighthBox.setImage(new Image("defaultLetterScreen.png"));
		ninthBox.setImage(new Image("defaultLetterScreen.png"));
		tenthBox.setImage(new Image("defaultLetterScreen.png"));

		categories.setDisable(false);

		category.setText("Next Round - Pick a category");

		clientConnection.myPlayerInfo.attempts++;
		clientConnection.myPlayerInfo.selectedLetter = "";
		clientConnection.myPlayerInfo.indexOfLetter = -1;

		clientConnection.myPlayerInfo.playingStatesCategory = false;
		clientConnection.myPlayerInfo.playingFoodCategory = false;
		clientConnection.myPlayerInfo.playingAnimalsCategory = false;

		clientConnection.send(clientConnection.myPlayerInfo, "Next Round");
	}

	public void listenForCategory() {
		states.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = true;
			clientConnection.myPlayerInfo.playingFoodCategory = false;
			clientConnection.myPlayerInfo.playingAnimalsCategory = false;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on States category");

			categories.setDisable(true);
			category.setText("States");
		});

		food.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = false;
			clientConnection.myPlayerInfo.playingFoodCategory = true;
			clientConnection.myPlayerInfo.playingAnimalsCategory = false;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Food category");

			categories.setDisable(true);
			category.setText("Food");
		});

		animals.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = false;
			clientConnection.myPlayerInfo.playingFoodCategory = false;
			clientConnection.myPlayerInfo.playingAnimalsCategory = true;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Animals category");

			categories.setDisable(true);
			category.setText("Animals");
		});
	}

	public void listenForLetter() {
		letA.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "a";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter A");
		});

		letB.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "b";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter B");
		});

		letC.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "c";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter C");
		});

		letD.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "d";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter D");
		});

		letE.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "e";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter E");
		});

		letF.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "f";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter F");
		});

		letG.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "g";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter G");
		});

		letH.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "h";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter H");
		});

		letI.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "i";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter I");
		});

		letJ.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "j";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter J");
		});

		letK.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "k";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter K");
		});

		letL.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "l";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter L");
		});

		letM.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "m";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter M");
		});

		letN.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "n";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter N");
		});

		letO.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "o";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter O");
		});

		letP.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "p";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter P");
		});

		letQ.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "q";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter Q");
		});

		letR.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "r";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter R");
		});

		letS.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "s";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter S");
		});

		letT.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "t";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter T");
		});

		letU.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "u";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter U");
		});

		letV.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "v";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter V");
		});

		letW.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "w";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter W");
		});

		letX.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "x";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter X");
		});

		letY.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "y";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter Y");
		});

		letZ.setOnAction(event -> {
			clientConnection.myPlayerInfo.selectedLetter = "z";
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter Z");
		});
	}
}
