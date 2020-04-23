public class GameInfo {
    int clientNumber = -1;
    String categoryOne = "categoryOne";
    String categoryTwo = "categoryTwo";
    String categoryThree = "categoryThree";

    //as the user guesses the word, the char array will be filled with the letters
    String categoryOne_WordOne = "wordOne";
    char[] categoryOne_WordOneArray = new char[categoryOne_WordOne.length()];
    String categoryOne_WordTwo = "wordTwo";
    char[] categoryOne_WordTwoArray = new char[categoryOne_WordTwo.length()];
    String categoryOne_WordThree = "wordThree";
    char[] categoryOne_WordThreeArray = new char[categoryOne_WordThree.length()];

    String categoryTwo_WordOne = "wordOne";
    char[] categoryTwo_WordOneArray = new char[categoryTwo_WordOne.length()];
    String categoryTwo_WordTwo = "wordTwo";
    char[] categoryTwo_WordTwoArray = new char[categoryTwo_WordTwo.length()];
    String categoryTwo_WordThree = "wordThree";
    char[] categoryTwo_WordThreeArray = new char[categoryTwo_WordThree.length()];

    String categoryThree_WordOne = "wordOne";
    char[] categoryThree_WordOneArray = new char[categoryThree_WordOne.length()];
    String categoryThree_WordTwo = "wordTwo";
    char[] categoryThree_WordTwoArray = new char[categoryThree_WordTwo.length()];
    String categoryThree_WordThree = "wordThree";
    char[] categoryThree_WordThreeArray = new char[categoryThree_WordThree.length()];

    Boolean playingCategoryOne = false;
    Boolean categoryOneSolved = false;
    Boolean categoryOne_WordOneSolved = false;
    Boolean categoryOne_WordTwoSolved = false;
    Boolean categoryOne_WordThreeSolved = false;

    Boolean playingCategoryTwo = false;
    Boolean categoryTwoSolved = false;
    Boolean categoryTwo_WordOneSolved = false;
    Boolean categoryTwo_WordTwoSolved = false;
    Boolean categoryTwo_WordThreeSolved = false;

    Boolean playingCategoryThree = false;
    Boolean categoryThreeSolved = false;
    Boolean categoryThree_WordOneSolved = false;
    Boolean categoryThree_WordTwoSolved = false;
    Boolean categoryThree_WordThreeSolved = false;
}

//    // Creating array of string length
//    char[] ch = new char[str.length()];
//
//// Copy character by character into array
//        for (int i = 0; i < str.length(); i++) {
//        ch[i] = str.charAt(i);
//        }
