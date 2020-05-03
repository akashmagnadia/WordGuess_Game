import java.io.Serializable;
import java.util.ArrayList;

enum Categories {
    animal, food, state
}

public class GameInfo implements Serializable {

    int clientNumber = -1;
    int guessableWordsLeftInAnimalCategory = 3;
    int guessableWordsLeftInFoodCategory = 3;
    int guessableWordsLeftInStateCategory = 3;

    Categories currentCategory;

    Boolean gameWon = false;
    Boolean gameLost = false;
    Boolean gameInProgress = false;
    Boolean solvedAnimalCategory = false;
    Boolean solvedFoodCategory = false;
    Boolean solvedStateCategory = false;

    int guessLeft = 6;

    //this is dynamic variable and will keep changing
    Boolean[] letterIndices = {false, false, false, false, false, false, false, false, false, false};
    String letter = "";
    String word = "";

    //when the letter is guessed correctly decrement this number and when this number is at zero then word is solved
    String workingWord = null;
}