import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;

public class ServerSideGameInfo implements Serializable {
    int clientNumber = -1;
    String animalsCategory = "Animals";
    String foodCategory = "Food";
    String statesCategory = "States";

    String animalsCategory_WordOne;
    char[] animalsCategory_WordOneArray;
    String animalsCategory_WordTwo;
    char[] animalsCategory_WordTwoArray;
    String animalsCategory_WordThree;
    char[] animalsCategory_WordThreeArray;

    String foodCategory_WordOne;
    char[] foodCategory_WordOneArray;
    String foodCategory_WordTwo;
    char[] foodCategory_WordTwoArray;
    String foodCategory_WordThree;
    char[] foodCategory_WordThreeArray;

    String statesCategory_WordOne;
    char[] statesCategory_WordOneArray;
    String statesCategory_WordTwo;
    char[] statesCategory_WordTwoArray;
    String statesCategory_WordThree;
    char[] statesCategory_WordThreeArray;

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
    String workingWordForLength = null;
    int lengthOfWorkingWordForLength = -1;

    Boolean gameWon = false;
    Boolean gameLost = false;
    Boolean newGame = false;

    int attempts = 0;

    public ServerSideGameInfo() throws IOException, ParseException {
        //credit to https://github.com/twinters/datamuse-java for implementation of datamuse API

        DatamuseAPIParser animalDatamuseAPIParser = new DatamuseAPIParser(
                "topics",
                new String[]{"animal", "mammal"});
        String[] animalWords = animalDatamuseAPIParser.getWords();
        //less than 11 letter

        DatamuseAPIParser foodDatamuseAPIParser = new DatamuseAPIParser(
                "topics",
                new String[]{"food","snack","dinner","lunch"});
        String[] foodWords = foodDatamuseAPIParser.getWords();
        //more than 3 and less than 11

        DatamuseAPIParser stateDatamuseAPIParser = new DatamuseAPIParser(
                "topics",
                new String[]{"state","United States"});
        String[] stateWords = stateDatamuseAPIParser.getWords();
        //chose until 43 and size less than 11
        //size - 57 for random function that would be the max and min is of course 0

        //think for case where no internet therefore list is empty have default word to take over

        if (animalWords.length > 0) {
            while (animalsCategory_WordOne == null && animalsCategory_WordTwo == null && animalsCategory_WordThree == null) {
                if (animalsCategory_WordOne == null) {
                    int randomNum = getRandomInRange(0, animalWords.length);
                    String wordPick = animalWords[randomNum];
                    if (!(wordPick.length() > 10)) {
                        if (!((animalsCategory_WordTwo != null && animalsCategory_WordTwo.equals(wordPick)) ||
                                (animalsCategory_WordThree != null && animalsCategory_WordThree.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            animalsCategory_WordOne = animalWords[randomNum].toLowerCase();
                            animalsCategory_WordOneArray = new char[animalsCategory_WordOne.length()];
                        }
                    }
                }

                if (animalsCategory_WordTwo == null) {
                    int randomNum = getRandomInRange(0, animalWords.length);
                    String wordPick = animalWords[randomNum];
                    if (!(wordPick.length() > 10)) {
                        if (!((animalsCategory_WordOne != null && animalsCategory_WordOne.equals(wordPick)) ||
                                (animalsCategory_WordThree != null && animalsCategory_WordThree.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            animalsCategory_WordTwo = animalWords[randomNum].toLowerCase();
                            animalsCategory_WordTwoArray = new char[animalsCategory_WordTwo.length()];
                        }
                    }
                }

                if (animalsCategory_WordThree == null) {
                    int randomNum = getRandomInRange(0, animalWords.length);
                    String wordPick = animalWords[randomNum];
                    if (!(wordPick.length() > 10)) {
                        if (!((animalsCategory_WordOne != null && animalsCategory_WordOne.equals(wordPick)) ||
                                (animalsCategory_WordTwo != null && animalsCategory_WordTwo.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            animalsCategory_WordThree = animalWords[randomNum].toLowerCase();
                            animalsCategory_WordThreeArray = new char[animalsCategory_WordThree.length()];
                        }
                    }
                }
            }
        } else {
            //if there is no internet then it defaults
            animalsCategory_WordOne = "cheetah".toLowerCase();
            animalsCategory_WordOneArray = new char[animalsCategory_WordOne.length()];
            animalsCategory_WordTwo = "lion".toLowerCase();
            animalsCategory_WordTwoArray = new char[animalsCategory_WordTwo.length()];
            animalsCategory_WordThree = "tiger".toLowerCase();
            animalsCategory_WordThreeArray = new char[animalsCategory_WordThree.length()];
        }

        if (foodWords.length > 0) {
            while (foodCategory_WordOne == null && foodCategory_WordTwo == null && foodCategory_WordThree == null) {
                if (foodCategory_WordOne == null) {
                    int randomNum = getRandomInRange(0, foodWords.length);
                    String wordPick = foodWords[randomNum];
                    if (!(wordPick.length() > 10 || wordPick.length() < 4)) {
                        if (!((foodCategory_WordTwo != null && foodCategory_WordTwo.equals(wordPick)) ||
                                (foodCategory_WordThree != null && foodCategory_WordThree.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            foodCategory_WordOne = foodWords[randomNum].toLowerCase();
                            foodCategory_WordOneArray = new char[foodCategory_WordOne.length()];
                        }
                    }
                }

                if (foodCategory_WordTwo == null) {
                    int randomNum = getRandomInRange(0, foodWords.length);
                    String wordPick = foodWords[randomNum];
                    if (!(wordPick.length() > 10 || wordPick.length() < 4)) {
                        if (!((foodCategory_WordOne != null && foodCategory_WordOne.equals(wordPick)) ||
                                (foodCategory_WordThree != null && foodCategory_WordThree.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            foodCategory_WordTwo = foodWords[randomNum].toLowerCase();
                            foodCategory_WordTwoArray = new char[foodCategory_WordTwo.length()];
                        }
                    }
                }

                if (foodCategory_WordThree == null) {
                    int randomNum = getRandomInRange(0, foodWords.length);
                    String wordPick = foodWords[randomNum];
                    if (!(wordPick.length() > 10 || wordPick.length() < 4)) {
                        if (!((foodCategory_WordTwo != null && foodCategory_WordTwo.equals(wordPick)) ||
                                (foodCategory_WordOne != null && foodCategory_WordOne.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            foodCategory_WordThree = foodWords[randomNum].toLowerCase();
                            foodCategory_WordThreeArray = new char[foodCategory_WordThree.length()];
                        }
                    }
                }
            }
        } else {
            foodCategory_WordOne = "fries".toLowerCase();
            foodCategory_WordOneArray = new char[foodCategory_WordOne.length()];
            foodCategory_WordTwo = "pancake".toLowerCase();
            foodCategory_WordTwoArray = new char[foodCategory_WordTwo.length()];
            foodCategory_WordThree = "pizza".toLowerCase();
            foodCategory_WordThreeArray = new char[foodCategory_WordThree.length()];
        }

        if (stateWords.length > 0) {
            while (statesCategory_WordOne == null && statesCategory_WordTwo == null && statesCategory_WordThree == null) {
                if (statesCategory_WordOne == null) {
                    int randomNum = getRandomInRange(0, 43);
                    String wordPick = stateWords[randomNum];
                    if (!(wordPick.length() > 10 || wordPick.length() < 4)) {
                        if (!((statesCategory_WordTwo != null && statesCategory_WordTwo.equals(wordPick)) ||
                                (statesCategory_WordThree != null && statesCategory_WordThree.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            statesCategory_WordOne = stateWords[randomNum].toLowerCase();
                            statesCategory_WordOneArray = new char[statesCategory_WordOne.length()];
                        }
                    }
                }

                if (statesCategory_WordTwo == null) {
                    int randomNum = getRandomInRange(0, 43);
                    String wordPick = stateWords[randomNum];
                    if (!(wordPick.length() > 10 || wordPick.length() < 4)) {
                        if (!((statesCategory_WordThree != null && statesCategory_WordThree.equals(wordPick)) ||
                                (statesCategory_WordOne != null && statesCategory_WordOne.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            statesCategory_WordTwo = stateWords[randomNum].toLowerCase();
                            statesCategory_WordTwoArray = new char[statesCategory_WordTwo.length()];
                        }
                    }
                }

                if (statesCategory_WordThree == null) {
                    int randomNum = getRandomInRange(0, 43);
                    String wordPick = stateWords[randomNum];
                    if (!(wordPick.length() > 10)) {
                        if (!((statesCategory_WordTwo != null && statesCategory_WordTwo.equals(wordPick)) ||
                                (statesCategory_WordOne != null && statesCategory_WordOne.equals(wordPick)))) {
                            //make sure other two doesn't have the same word
                            statesCategory_WordThree = stateWords[randomNum].toLowerCase();
                            statesCategory_WordThreeArray = new char[statesCategory_WordThree.length()];
                        }
                    }
                }
            }
        } else {
            statesCategory_WordOne = "illinois".toLowerCase();
            statesCategory_WordOneArray = new char[statesCategory_WordOne.length()];
            statesCategory_WordTwo = "florida".toLowerCase();
            statesCategory_WordTwoArray = new char[statesCategory_WordTwo.length()];
            statesCategory_WordThree = "wisconsin".toLowerCase();
            statesCategory_WordThreeArray = new char[statesCategory_WordThree.length()];
        }
    }

    private int getRandomInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
