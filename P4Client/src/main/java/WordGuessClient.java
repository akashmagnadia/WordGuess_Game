import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class WordGuessClient extends Application {

	HashMap<String, Scene> sceneMap;
	
	static ListView<String> clientLog;
	ObservableList<String> info;
	
	Button connect;
	TextField IPInput, portInput;
	MenuItem exit, mute, unmute, newGame;
	
	Label correctGuessesRemaining;
	static Label correctGuesses;
	Label correctAttemptsRemaining;
	static Label attemptsLeft;
	
	static Label category;
	static MenuButton categories;
	static MenuItem animals;
	static MenuItem states;
	static MenuItem food;
	
	static MenuButton letterChoices;
	static MenuItem letA,letB,letC,letD,letE,letF,letG,letH,letI,letJ,letK,letL,letM,letN,letO,letP,letQ,letR,letS,letT,letU,letV,letW,letX,letY,letZ;
	static MenuItem[] lettersMenuItem = {letA,letB,letC,letD,letE,letF,letG,letH,letI,letJ,letK,letL,letM,letN,letO,letP,letQ,letR,letS,letT,letU,letV,letW,letX,letY,letZ};

	static String[] lettersArray = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
	Image defaultGreenBox = new Image("defaultLetterScreen.png");
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
	static Label result;
	
	MediaPlayer mediaPlayer;

	int port;
	String ip;
	static Client clientConnection;
	
	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("(Client) Word Guess!!!");
//		music();
		
		sceneMap = new HashMap<>();
		sceneMap.put("clientStart", createClientGUIStartScreen());
		sceneMap.put("clientGameplay", createClientGUIGameplay());
		sceneMap.put("clientResults", createResultScene());
		
		/* Initial Scene Configuration */
		primaryStage.setScene(sceneMap.get("clientStart"));
//		primaryStage.setScene(sceneMap.get("clientResults"));
		primaryStage.setResizable(false);
		primaryStage.show();
		
		unmute.setOnAction(e-> music());
		
		mute.setOnAction(e-> mediaPlayer.stop());

		exit.setOnAction(e->{
			Platform.exit();
			System.exit(0);
		});
		
		quit.setOnAction(e->{
			Platform.exit();
			System.exit(0);
		});
		
		playAgain.setOnAction(e-> resetGame(primaryStage, sceneMap));
		
		newGame.setOnAction(e-> resetGame(primaryStage, sceneMap));

		connect.setOnAction(e -> {
			System.out.println("Connecting to server...");
			connectToServer();
			primaryStage.setScene(sceneMap.get("clientGameplay"));
			primaryStage.show();
		});
		
		/* Allow the program to fully close upon exiting, allows current port to be freed up */
		primaryStage.setOnCloseRequest(t -> {
			Platform.exit();
			System.exit(0);
		});

		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				checkWonOrLose(primaryStage, sceneMap);
			}
		}, 10000, 1000);

		listenFor();
	}
	


	public void connectToServer() {
		try {
			port = Integer.parseInt(portInput.getText()); // convert string input to integer
			ip = IPInput.getText(); // set IP address
			clientConnection = new Client(data-> Platform.runLater(()-> clientLog.getItems().add(String.valueOf(data))), port, ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		clientConnection.start();
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

		/* Instructions Label */
		Label instructionsText = new Label("								Instructions:");
		instructionsText.setFont(Font.font("Rockwell",20));
		instructionsText.setTextFill(Color.GOLD);

		Label instructionToWin = new Label("		Guess at least one word from each category to win the game");
		instructionToWin.setFont(Font.font("Rockwell",20));
		instructionToWin.setTextFill(Color.GOLD);

		Label instructionToLose = new Label("You will lose the game if you don't guess a word in three consecutive attempts");
		instructionToLose.setFont(Font.font("Rockwell",20));
		instructionToLose.setTextFill(Color.GOLD);

		VBox instructions = new VBox(10, instructionsText, instructionToWin, instructionToLose);
		instructions.setLayoutX(150);
		instructions.setLayoutY(650);
		/* Instructions Label */
		
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
		
		startScreen.getChildren().addAll(welcomeLabels,ipSelection,portSelection,connect, instructions);
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
		categories.setPrefSize(150, 40);
		categories.setStyle("-fx-background-color: aqua;");
		categories.setLayoutX(170);
		categories.setLayoutY(550);
		/* Drop down menu for categories */
		
		/* Drop down menu for letter selection */
		letterChoices = new MenuButton();
		int count = 0;
		for (MenuItem menuItem: lettersMenuItem) {
			menuItem = new MenuItem(lettersArray[count].toUpperCase());
			letterChoices.getItems().add(menuItem);

			int finalCount = count;
			MenuItem finalMenuItem = menuItem;
			menuItem.setOnAction(event -> {
				if (clientConnection != null) {
					clientConnection.myPlayerInfo.selectedLetter = lettersArray[finalCount];
					clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter " + lettersArray[finalCount].toUpperCase());
					finalMenuItem.setDisable(true);
				}
			});
			count++;
		}
		
		letterChoices.setText("Guess a letter!");
		letterChoices.setPrefSize(150, 40);
		letterChoices.setStyle("-fx-background-color: coral;");
		letterChoices.setTextFill(Color.WHITE);
		letterChoices.setLayoutX(330);
		letterChoices.setLayoutY(550);
		letterChoices.setDisable(true);
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
		guessesBox.setLayoutX(490);
		guessesBox.setLayoutY(530);
		/* Guesses Remaining Labels */

		/* Attempts Remaining Labels */
		correctAttemptsRemaining = new Label("Attempts Remaining:");
		attemptsLeft = new Label("3");

		correctAttemptsRemaining.setFont(Font.font("Rockwell",20));
		correctAttemptsRemaining.setTextFill(Color.GOLD);

		attemptsLeft.setStyle("-fx-border-color: gold;");
		attemptsLeft.setPrefSize(200, 100);
		attemptsLeft.setTextAlignment(TextAlignment.CENTER);
		attemptsLeft.setAlignment(Pos.CENTER);
		attemptsLeft.setFont(Font.font("Rockwell",64));
		attemptsLeft.setTextFill(Color.GOLD);

		VBox attemptsBox = new VBox(5,correctAttemptsRemaining, attemptsLeft);
		attemptsBox.setLayoutX(700);
		attemptsBox.setLayoutY(530);
		/* Attempts Remaining Labels */
		
		/* Client log */
		info = FXCollections.observableArrayList();
		clientLog = new ListView<>(info);
		clientLog.setPrefSize(325, 120);
		clientLog.setOpacity(0.8);
		clientLog.setLayoutX(640);
		clientLog.setLayoutY(540);
		clientLog.setVisible(false);
		/* Client log */
		
		gameplay.getChildren().addAll(menuBar,letters,category,categories,letterChoices, guessesBox, attemptsBox, clientLog);
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
		result.setPrefSize(650,100);
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
		playAgain.setPrefSize(305, 50);
		
		quit = new Button("Quit");
		quit.setStyle("-fx-background-color: purple;" + "-fx-font: 25 Rockwell;");
		quit.setTextFill(Color.WHITE);
		quit.setEffect(shadow);
		quit.setPrefSize(305, 50);
		
		HBox resultButtons = new HBox(40,playAgain,quit);
		/* Play Again and Quit Buttons */
		
		VBox resultScreen = new VBox(20,result,resultButtons);
		resultScreen.setLayoutX(200);
		resultScreen.setLayoutY(200);

		results.getChildren().addAll(resultScreen);
		return new Scene(results, 1000,600);
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
	}

	public static void updateOnChange() throws InterruptedException {
		updateLetterBox();
		checkEndOfRound();
		updateWhiteLetterBoxForLength();
		disableCategoryOnCompletion();

		Platform.runLater(() -> {
			correctGuesses.setText(String.valueOf(clientConnection.myPlayerInfo.guessLeft));
			attemptsLeft.setText(String.valueOf(3-clientConnection.myPlayerInfo.attempts));
		});
	}

	private static void disableCategoryOnCompletion() {
		if (clientConnection.myPlayerInfo.animalsCategory_WordThreeSolved) {
			animals.setDisable(true);
		} else if (clientConnection.myPlayerInfo.foodCategory_WordThreeSolved) {
			food.setDisable(true);
		} else if (clientConnection.myPlayerInfo.statesCategory_WordThreeSolved) {
			states.setDisable(true);
		}
	}

	public void enableAllCategories() {
		animals.setDisable(false);
		food.setDisable(false);
		states.setDisable(false);
	}

	public static void updateWhiteLetterBoxForLength() {
		int length = clientConnection.myPlayerInfo.lengthOfWorkingWordForLength;

		if (length != -1) {
			Image whiteBox = new Image("whiteSpace.png");
			if (1 <= length) {
				firstBox.setImage(whiteBox);
			}
			if (2 <= length) {
				secondBox.setImage(whiteBox);
			}
			if (3 <= length) {
				thirdBox.setImage(whiteBox);
			}
			if (4 <= length) {
				fourthBox.setImage(whiteBox);
			}
			if (5 <= length) {
				fifthBox.setImage(whiteBox);
			}
			if (6 <= length) {
				sixthBox.setImage(whiteBox);
			}
			if (7 <= length) {
				seventhBox.setImage(whiteBox);
			}
			if (8 <= length) {
				eighthBox.setImage(whiteBox);
			}
			if (9 <= length) {
				ninthBox.setImage(whiteBox);
			}
			if (10 <= length) {
				tenthBox.setImage(whiteBox);
			}

			//so that white boxes are not set again until next round
			clientConnection.myPlayerInfo.lengthOfWorkingWordForLength = -1;
		}
	}

	public static void checkWonOrLose(Stage primaryStage, HashMap<String, Scene> sceneMap) {
		if (clientConnection != null) {
			if (clientConnection.myPlayerInfo.gameWon) {
				Platform.runLater(() -> {
					category.setText("Game Won");
					result.setText("You Won the Game!");
					primaryStage.setScene(sceneMap.get("clientResults"));
					primaryStage.setResizable(false);
					primaryStage.show();
				});
			} else if (clientConnection.myPlayerInfo.gameLost) {
				Platform.runLater(() -> {
					category.setText("Game Lost");
					result.setText("You Lost the Game!");
					primaryStage.setScene(sceneMap.get("clientResults"));
					primaryStage.setResizable(false);
					primaryStage.show();
				});
			}
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
		for (String letter: lettersArray) {
			if (clientConnection.myPlayerInfo.selectedLetter.equals(letter)) {
				boxToModify.setImage(new Image("letter" + letter.toUpperCase() + ".png"));
			}
		}
	}

	public static void checkEndOfRound() throws InterruptedException {
		ClientSideGameInfo gameInfo = clientConnection.myPlayerInfo;

		if (gameInfo.guessLeft == 0) {
			Thread.sleep(1500);
			resetForNextRound();
		}

		if ((gameInfo.animalsCategory_WordOneSolved || gameInfo.animalsCategory_WordTwoSolved || gameInfo.animalsCategory_WordThreeSolved) &&
				(gameInfo.statesCategory_WordOneSolved || gameInfo.statesCategory_WordTwoSolved || gameInfo.statesCategory_WordThreeSolved) &&
				(gameInfo.foodCategory_WordOneSolved || gameInfo.foodCategory_WordTwoSolved || gameInfo.foodCategory_WordThreeSolved)) {
			gameInfo.gameWon = true;
		}

		if (gameInfo.attempts == 3) {
			gameInfo.gameLost = true;
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
		letterChoices.setDisable(true);

		Platform.runLater(() -> category.setText("Next Round - Pick a category"));

		clientConnection.myPlayerInfo.selectedLetter = "";
		clientConnection.myPlayerInfo.indexOfLetter = -1;

		clientConnection.myPlayerInfo.playingStatesCategory = false;
		clientConnection.myPlayerInfo.playingFoodCategory = false;
		clientConnection.myPlayerInfo.playingAnimalsCategory = false;

		letterChoices.getItems().clear();
		int count = 0;
		for (MenuItem menuItem: lettersMenuItem) {
			menuItem = new MenuItem(lettersArray[count].toUpperCase());
			letterChoices.getItems().add(menuItem);

			int finalCount = count;
			MenuItem finalMenuItem = menuItem;
			menuItem.setOnAction(event -> {
				if (clientConnection != null) {
					clientConnection.myPlayerInfo.selectedLetter = lettersArray[finalCount];
					clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter " + lettersArray[finalCount].toUpperCase());
					finalMenuItem.setDisable(true);
				}
			});
			count++;
		}

		clientConnection.myPlayerInfo.guessLeft = 6;

		clientConnection.send(clientConnection.myPlayerInfo, "Next Round - Pick a category");
	}
	
	private void resetGame(Stage primaryStage, HashMap<String, Scene> sceneMap) {
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
		category.setText("Pick a Category!");

		letterChoices.getItems().clear();
		int count = 0;
		for (MenuItem menuItem: lettersMenuItem) {
			menuItem = new MenuItem(lettersArray[count].toUpperCase());
			letterChoices.getItems().add(menuItem);

			int finalCount = count;
			MenuItem finalMenuItem = menuItem;
			menuItem.setOnAction(event -> {
				if (clientConnection != null) {
					clientConnection.myPlayerInfo.selectedLetter = lettersArray[finalCount];
					clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Letter " + lettersArray[finalCount].toUpperCase());
					finalMenuItem.setDisable(true);
				}
			});
			count++;
		}

		int tempClientNumber = clientConnection.myPlayerInfo.clientNumber;

		clientConnection.myPlayerInfo = new ClientSideGameInfo();
		clientConnection.myPlayerInfo.clientNumber = tempClientNumber;
		clientConnection.myPlayerInfo.newGame = true;

		clientLog.getItems().clear();
		clientConnection.send(clientConnection.myPlayerInfo, "New Game Has Started!");

		enableAllCategories();

		primaryStage.setScene(sceneMap.get("clientGameplay"));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void listenForCategory() {
		states.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = true;
			clientConnection.myPlayerInfo.playingFoodCategory = false;
			clientConnection.myPlayerInfo.playingAnimalsCategory = false;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on States category");

			categories.setDisable(true);
			letterChoices.setDisable(false);
			Platform.runLater(() -> category.setText("States"));
		});

		food.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = false;
			clientConnection.myPlayerInfo.playingFoodCategory = true;
			clientConnection.myPlayerInfo.playingAnimalsCategory = false;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Food category");

			categories.setDisable(true);
			letterChoices.setDisable(false);
			Platform.runLater(() -> category.setText("Food"));
		});

		animals.setOnAction(event -> {
			clientConnection.myPlayerInfo.playingStatesCategory = false;
			clientConnection.myPlayerInfo.playingFoodCategory = false;
			clientConnection.myPlayerInfo.playingAnimalsCategory = true;
			clientConnection.send(clientConnection.myPlayerInfo, "Clicked on Animals category");

			categories.setDisable(true);
			letterChoices.setDisable(false);
			Platform.runLater(() -> category.setText("Animals"));
		});
	}
}
