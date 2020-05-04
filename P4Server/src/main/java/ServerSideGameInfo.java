import java.io.Serializable;

public class ServerSideGameInfo implements Serializable {
    int clientNumber = -1;
    String animalsCategory = "Animals";
    String foodCategory = "Food";
    String statesCategory = "States";

    //as the user guesses the word, the char array will be filled with the letters
    String animalsCategory_WordOne = "tiger";
    char[] animalsCategory_WordOneArray = new char[animalsCategory_WordOne.length()];
    String animalsCategory_WordTwo = "lion";
    char[] animalsCategory_WordTwoArray = new char[animalsCategory_WordTwo.length()];
    String animalsCategory_WordThree = "cheetah";
    char[] animalsCategory_WordThreeArray = new char[animalsCategory_WordThree.length()];

    String foodCategory_WordOne = "wordOne";
    char[] foodCategory_WordOneArray = new char[foodCategory_WordOne.length()];
    String foodCategory_WordTwo = "wordTwo";
    char[] foodCategory_WordTwoArray = new char[foodCategory_WordTwo.length()];
    String foodCategory_WordThree = "wordThree";
    char[] foodCategory_WordThreeArray = new char[foodCategory_WordThree.length()];

    String statesCategory_WordOne = "wordOne";
    char[] statesCategory_WordOneArray = new char[statesCategory_WordOne.length()];
    String statesCategory_WordTwo = "wordTwo";
    char[] statesCategory_WordTwoArray = new char[statesCategory_WordTwo.length()];
    String statesCategory_WordThree = "wordThree";
    char[] statesCategory_WordThreeArray = new char[statesCategory_WordThree.length()];

    Boolean playingAnimalsCategory = false;
    Boolean animalsCategorySolved = false;
    Boolean animalsCategory_WordOneSolved = false;
    Boolean animalsCategory_WordTwoSolved = false;
    Boolean animalsCategory_WordThreeSolved = false;

    Boolean playingFoodCategory = false;
    Boolean foodCategorySolved = false;
    Boolean foodCategory_WordOneSolved = false;
    Boolean foodCategory_WordTwoSolved = false;
    Boolean foodCategory_WordThreeSolved = false;

    Boolean playingStatesCategory = false;
    Boolean statesCategorySolved = false;
    Boolean statesCategory_WordOneSolved = false;
    Boolean statesCategory_WordTwoSolved = false;
    Boolean statesCategory_WordThreeSolved = false;

    int guessLeft = 6;

    //this is dynamic variable and will keep changing
    int indexOfLetter = -2;
    String selectedLetter = "";

    //when the word length is zero then word is solved
    String workingWord = null;
    String solvingWord = null;

    Boolean gameWon = false;
    Boolean gameLost = false;

    int attempts = 0;
}

//    // Creating array of string length
//    char[] ch = new char[str.length()];
//
//// Copy character by character into array
//        for (int i = 0; i < str.length(); i++) {
//        ch[i] = str.charAt(i);
//        }
