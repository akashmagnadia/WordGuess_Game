import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Server {

    int clientCount = 1;
    ArrayList<TheServer.ClientThread> clients = new ArrayList<TheServer.ClientThread>();
    TheServer server;
    private Consumer<Serializable> callBack;


    Server(Consumer<Serializable> call) throws IOException {
        callBack = call;
        server = new TheServer();
        server.start();
    }



    public class TheServer extends Thread {
        int port;

        public void run() {
            try(ServerSocket mysocket = new ServerSocket(port)) {

                System.out.println("Server is waiting for a client!");
                callBack.accept("Server is waiting for a client!");

                while(true) {

                    ClientThread c = new ClientThread(mysocket.accept(), clientCount);
                    callBack.accept("client has connected to server: " + "client #" + clientCount);
                    clients.add(c);
                    c.start();

                    clientCount++;

//                    MorraInfo info = new MorraInfo();
//                    //when new client is made MorraInfo object is sent with client number
//                    info.client = clientCount;
//                    out.writeObject(info);
                }

            } //end of try
            catch (IOException e) {
                e.printStackTrace();
            }
        }


        class ClientThread extends Thread {
            Socket connection;
            ObjectInputStream in;
            ObjectOutputStream out;
            GameInfo clientInfo;

            ClientThread(Socket socket, int clientCount) throws IOException {
                this.connection = socket;

                this.clientInfo = new GameInfo();
                this.clientInfo.clientNumber = clientCount;
            }

            //TODO: Needs rework for play again or quit button
            public void cleanClientsInfo() {
                for(int i = 0; i < clients.size(); i++) {
                    ClientThread thread = clients.get(i);

                    GameInfo infoToSend = new GameInfo();
                    infoToSend.clientNumber = thread.clientInfo.clientNumber;

                    try {
                        thread.out.writeObject(infoToSend);
                    }
                    catch (Exception ignored) {
                    }
                }
            }

            public void run() {
                try {
                    in = new ObjectInputStream(connection.getInputStream());
                    out = new ObjectOutputStream(connection.getOutputStream());
                    connection.setTcpNoDelay(true);

                    out.writeUnshared(clientInfo); //test
//                    out.writeObject(clientInfo);
                }
                catch(Exception e) {
                    System.out.println("Streams not open");
                }

                while(true) {
                    try {
                        GameInfo receivedInfo = (GameInfo) in.readObject();

                        //update player info in client thread array
                        ClientThread tempThread = clients.get(receivedInfo.clientNumber - 1);

                        //after performing the logic
                        tempThread.clientInfo = updateIndexOfLetter(receivedInfo);

                        callBack.accept("Received something from Player " + receivedInfo.clientNumber);

                        //return logic performed info to send it back to client
                        out.writeUnshared(tempThread.clientInfo); //test with writeUnshared
//                        out.writeObject(receivedInfo);
                    }
                    catch(Exception e) {
                        callBack.accept("OOOOPPs...Something wrong with the socket from client: " + clientCount + "....closing down!");
                        clients.remove(this);
                        break;
                    }
                }
            } //end of run

            public void send(GameInfo info, String message) {
                try {
                    out.writeObject(info);
                    callBack.accept(message);
                    System.out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public GameInfo updateIndexOfLetter(GameInfo receivedInfo) {
                //run this function when letter is clicked, which will update indexOfLetter and return it

                GameInfo toReturnInfo = receivedInfo;
                if (receivedInfo.playingAnimalsCategory) {
                    toReturnInfo = checkLetterClicked("Animals", receivedInfo);

                    //if the word is solved
                    if (toReturnInfo.workingWord != null && toReturnInfo.workingWord.length() <= 0) {
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

                } else if (toReturnInfo.workingWord != null && receivedInfo.playingFoodCategory) {
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

                } else if (toReturnInfo.workingWord != null && receivedInfo.playingStatesCategory) {
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
                int index = word.toLowerCase().indexOf(letter);
                if (index == -1) {
                    receivedInfo.guessLeft--;
                }
                receivedInfo.indexOfLetter = index;
                receivedInfo.selectedLetter = letter;

                if (receivedInfo.workingWord == null) {
                    receivedInfo.workingWord = word;
                } else {
                    receivedInfo.workingWord = receivedInfo.workingWord.replace(letter, "");
                }

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
                if (receivedInfo.selectedLetter.equals("a")) {
                    return checkLetterClickedHelper(category, "a", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("b")) {
                    return checkLetterClickedHelper(category, "b", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("c")) {
                    return checkLetterClickedHelper(category, "c", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("d")) {
                    return checkLetterClickedHelper(category, "d", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("e")) {
                    return checkLetterClickedHelper(category, "e", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("f")) {
                    return checkLetterClickedHelper(category, "f", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("g")) {
                    return checkLetterClickedHelper(category, "g", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("h")) {
                    return checkLetterClickedHelper(category, "h", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("i")) {
                    return checkLetterClickedHelper(category, "i", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("j")) {
                    return checkLetterClickedHelper(category, "j", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("k")) {
                    return checkLetterClickedHelper(category, "k", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("l")) {
                    return checkLetterClickedHelper(category, "l", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("m")) {
                    return checkLetterClickedHelper(category, "m", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("n")) {
                    return checkLetterClickedHelper(category, "n", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("o")) {
                    return checkLetterClickedHelper(category, "o", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("p")) {
                    return checkLetterClickedHelper(category, "p", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("q")) {
                    return checkLetterClickedHelper(category, "q", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("r")) {
                    return checkLetterClickedHelper(category, "r", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("s")) {
                    return checkLetterClickedHelper(category, "s", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("t")) {
                    return checkLetterClickedHelper(category, "t", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("u")) {
                    return checkLetterClickedHelper(category, "u", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("v")) {
                    return checkLetterClickedHelper(category, "v", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("w")) {
                    return checkLetterClickedHelper(category, "w", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("x")) {
                    return checkLetterClickedHelper(category, "x", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("y")) {
                    return checkLetterClickedHelper(category, "y", receivedInfo);
                } else if (receivedInfo.selectedLetter.equals("z")) {
                    return checkLetterClickedHelper(category, "z", receivedInfo);
                } else {
                    System.out.println("In check letter Clicked: No letter is clicked.");
                    return receivedInfo;
                }
            }
        } //end of client thread
    }
}
