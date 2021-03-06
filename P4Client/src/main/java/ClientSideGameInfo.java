import java.io.Serializable;

public class ClientSideGameInfo implements Serializable {
    int clientNumber = -1;
    String animalsCategory = "Animals";
    String foodCategory = "Food";
    String statesCategory = "States";

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
    int lengthOfWorkingWordForLength = -1;

    Boolean gameWon = false;
    Boolean gameLost = false;
    Boolean newGame = false;

    int attempts = 0;
}
