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
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Paths;

//Look at GameInfo for all the variables that can be utilized to help make the UI

public class WordGuessClient extends Application {

	HashMap<String, Scene> sceneMap;
	
	ListView<String> clientLog;
	ObservableList<String> info;
	
	Button connect;
	TextField IPInput, portInput;
	MenuItem exit, mute, unmute;
	
	Label correctGuessesRemaining;
	Label correctGuesses;
	
	Label category;
	MenuButton categories;
	MenuItem animals,states,food;
	
	MenuButton letterChoices;
	MenuItem letA,letB,letC,letD,letE,letF,letG,letH,letI,letJ,letK,letL,letM,letN,letO,letP,letQ,letR,letS,letT,letU,letV,letW,letX,letY,letZ;
	
	Image defaultGreenBox = new Image("defaultLetterScreen.png");
	Image whiteBox = new Image("whiteSpace.png");
	ImageView firstBox, secondBox, thirdBox, fourthBox, fifthBox, sixthBox, seventhBox, eighthBox, ninthBox, tenthBox;
	
	MediaPlayer mediaPlayer;

	int port;
	String ip;
	Client clientConnection;
	
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

		listenForCategory();
	}
	


	public void connectToServer() {
		clientConnection = new Client(data -> Platform.runLater(()->
		{
			clientLog.getItems().add(String.valueOf(data));
		})
		);

		try {
//			clientConnection.socketClient= new Socket("127.0.0.1", 5555);
			//TODO: UnComment this when IPInput and PortInput is setup at the end --Done,
			clientConnection.socketClient= new Socket(IPInput.getText(), Integer.parseInt(portInput.getText()));
		} catch (IOException e) {
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
		
		options.getItems().addAll(unmute,mute,exit);
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
		categories.setPrefSize(215, 40);
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
		letterChoices.setPrefSize(215, 40);
		letterChoices.setStyle("-fx-background-color: coral;");
		letterChoices.setTextFill(Color.WHITE);
		letterChoices.setLayoutX(400);
		letterChoices.setLayoutY(550);
		/* Drop down menu for letter selection */
		
		/* Client log */
		info = FXCollections.observableArrayList();
		clientLog = new ListView<String>(info);
		clientLog.setPrefSize(325, 120);
		clientLog.setOpacity(0.8);
		clientLog.setLayoutX(640);
		clientLog.setLayoutY(540);
		/* Client log */
		
		gameplay.getChildren().addAll(menuBar,letters,category,categories,letterChoices,clientLog);
		return new Scene(gameplay, 1000, 800);
		
	}
	
	public void music() {
		String path = WordGuessClient.class.getResource("WOFTheme.mp3").toString();
		Media h = new Media(path);
		mediaPlayer = new MediaPlayer(h);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
		mediaPlayer.play();
	}

	public void listenForCategory() {
		states.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = true;
			clientConnection.myPlayerInfo.playingFoodCategory = false;
			clientConnection.myPlayerInfo.playingAnimalsCategory = false;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on States category");
		});

		food.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = false;
			clientConnection.myPlayerInfo.playingFoodCategory = true;
			clientConnection.myPlayerInfo.playingAnimalsCategory = false;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Food category");
		});

		animals.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = false;
			clientConnection.myPlayerInfo.playingFoodCategory = false;
			clientConnection.myPlayerInfo.playingAnimalsCategory = true;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Animals category");
		});
	}

	public void listenForLetter() {

	}
}
