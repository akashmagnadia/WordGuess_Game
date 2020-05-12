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

            ServerSideGameInfo serverSideClientInfo;
            ClientSideGameInfo clientSideClientInfo;

            ClientThread(Socket socket, int clientCount) throws IOException {
                this.connection = socket;

                this.serverSideClientInfo = new ServerSideGameInfo();
                this.clientSideClientInfo = new ClientSideGameInfo();

                this.serverSideClientInfo.clientNumber = clientCount;
                this.clientSideClientInfo.clientNumber = clientCount;
            }

            //TODO: Needs rework for play again or quit button
            public void cleanClientsInfo() {
                for(int i = 0; i < clients.size(); i++) {
                    ClientThread thread = clients.get(i);

                    ServerSideGameInfo infoToSend = new ServerSideGameInfo();
                    infoToSend.clientNumber = thread.serverSideClientInfo.clientNumber;

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

                    out.writeUnshared(clientSideClientInfo); //test
//                    out.writeObject(clientInfo);
                }
                catch(Exception e) {
                    System.out.println("Streams not open");
                }

                while(true) {
                    try {
                        ClientSideGameInfo receivedInfo = (ClientSideGameInfo) in.readObject();

                        //update player info in client thread array
                        ClientThread tempThread = null;
                        for (ClientThread thread: clients) {
                            if (thread.clientSideClientInfo.clientNumber == receivedInfo.clientNumber) {
                                tempThread = thread;
                                tempThread.clientSideClientInfo = receivedInfo;
                            }
                        }

//                        ClientThread tempThread = clients.get(receivedInfo.clientNumber - 1);
//                        tempThread.clientSideClientInfo = receivedInfo;

                        //transfer client side info to server side and send it off for logic
                        //transferClientToServerGameInfo() returns server side converted gameInfo

                        do {
                            //loop until the selected letter has been shown to the client in case there are multiple letter in the word
                            tempThread.serverSideClientInfo = updateIndexOfLetter
                                    (transferClientToServerGameInfo
                                            (tempThread.serverSideClientInfo, tempThread.clientSideClientInfo)); //perform logic

                            //returns client side converted gameInfo and sends it back to the client
                            tempThread.clientSideClientInfo = transferServerToClientGameInfo(tempThread.serverSideClientInfo, tempThread.clientSideClientInfo);

                            callBack.accept("Received something from Player " + receivedInfo.clientNumber);

                            //return logic performed info to send it back to client
                            out.writeUnshared(tempThread.clientSideClientInfo); //test with writeUnshared
//                        out.writeObject(receivedInfo);
                        }
                        while (tempThread.serverSideClientInfo.workingWord != null &&
                                tempThread.serverSideClientInfo.workingWord.contains(tempThread.serverSideClientInfo.selectedLetter));
                    }
                    catch(Exception e) {
                        callBack.accept(this.clientSideClientInfo.clientNumber + "....closing down!");
                        clients.remove(this);
                        break;
                    }
                }
            } //end of run

            public ServerSideGameInfo updateIndexOfLetter(ServerSideGameInfo receivedInfo) {
                //run this function when letter is clicked, which will update indexOfLetter and return it

                ServerSideGameInfo toReturnInfo = receivedInfo;
                if (receivedInfo.playingAnimalsCategory) {
                    toReturnInfo = checkLetterClicked("Animals", receivedInfo);

                    //if the word is solved
                    if (toReturnInfo.workingWordForLength != null && toReturnInfo.workingWordForLength.length() == 0) {
                        toReturnInfo.attempts = 0;
                        toReturnInfo.guessLeft = 0;
                        toReturnInfo.workingWordForLength = null;
                        toReturnInfo.workingWord = null;
                        if (!toReturnInfo.animalsCategory_WordOneSolved) {
                            toReturnInfo.animalsCategory_WordOneSolved = true;
                            System.out.println("Animals Category: Word one solved");
                        } else if (!toReturnInfo.animalsCategory_WordTwoSolved) {
                            toReturnInfo.animalsCategory_WordTwoSolved = true;
                            System.out.println("Animals Category: Word two solved");
                        } else if (!toReturnInfo.animalsCategory_WordThreeSolved) {
                            toReturnInfo.animalsCategory_WordThreeSolved = true;
                            System.out.println("Animals Category: Word three solved");
                        } else {
                            System.out.println("In updateIndexOfLetter animal category: This shouldn't happen");
                        }
                    }

                    //if the word is not solved withing 6 guesses
                    else if (toReturnInfo.guessLeft == 0){
                        toReturnInfo.attempts++;
                        toReturnInfo.workingWordForLength = null;
                        toReturnInfo.workingWord = null;
                        if (!toReturnInfo.animalsCategory_WordOneSolved) {
                            System.out.println("Animals Category: Word one not solved");
                        } else if (!toReturnInfo.animalsCategory_WordTwoSolved) {
                            System.out.println("Animals Category: Word two not solved");
                        } else if (!toReturnInfo.animalsCategory_WordThreeSolved) {
                            System.out.println("Animals Category: Word three not solved");
                        } else {
                            System.out.println("In updateIndexOfLetter animal category: This shouldn't happen");
                        }
                    }

                } else if (receivedInfo.playingFoodCategory) {
                    toReturnInfo = checkLetterClicked("Food", receivedInfo);

                    //if the word is solved
                    if (toReturnInfo.workingWordForLength != null && toReturnInfo.workingWordForLength.length() <= 0) {
                        toReturnInfo.attempts = 0;
                        toReturnInfo.guessLeft = 0;
                        toReturnInfo.workingWordForLength = null;
                        toReturnInfo.workingWord = null;
                        if (!toReturnInfo.foodCategory_WordOneSolved) {
                            toReturnInfo.foodCategory_WordOneSolved = true;
                            System.out.println("Food Category: Word one solved");
                        } else if (!toReturnInfo.foodCategory_WordTwoSolved) {
                            toReturnInfo.foodCategory_WordTwoSolved = true;
                            System.out.println("Food Category: Word two solved");
                        } else if (!toReturnInfo.foodCategory_WordThreeSolved) {
                            toReturnInfo.foodCategory_WordThreeSolved = true;
                            System.out.println("Food Category: Word three solved");
                        } else {
                            System.out.println("In updateIndexOfLetter food category: This shouldn't happen");
                        }
                    }

                    //if the word is not solved withing 6 guesses
                    else if (toReturnInfo.guessLeft == 0){
                        toReturnInfo.attempts++;
                        toReturnInfo.workingWordForLength = null;
                        toReturnInfo.workingWord = null;
                        if (!toReturnInfo.foodCategory_WordOneSolved) {
                            System.out.println("Food Category: Word one not solved");
                        } else if (!toReturnInfo.foodCategory_WordTwoSolved) {
                            System.out.println("Food Category: Word two not solved");
                        } else if (!toReturnInfo.foodCategory_WordThreeSolved) {
                            System.out.println("Food Category: Word three not solved");
                        } else {
                            System.out.println("In updateIndexOfLetter food category: This shouldn't happen");
                        }
                    }

                } else if (receivedInfo.playingStatesCategory) {
                    toReturnInfo = checkLetterClicked("States", receivedInfo);

                    //if the word is solved
                    if (toReturnInfo.workingWordForLength != null && toReturnInfo.workingWordForLength.length() <= 0) {
                        toReturnInfo.attempts = 0;
                        toReturnInfo.guessLeft = 0;
                        toReturnInfo.workingWordForLength = null;
                        toReturnInfo.workingWord = null;
                        if (!toReturnInfo.statesCategory_WordOneSolved) {
                            toReturnInfo.statesCategory_WordOneSolved = true;
                            System.out.println("States Category: Word one solved");
                        } else if (!toReturnInfo.statesCategory_WordTwoSolved) {
                            toReturnInfo.statesCategory_WordTwoSolved = true;
                            System.out.println("States Category: Word two solved");
                        } else if (!toReturnInfo.statesCategory_WordThreeSolved) {
                            toReturnInfo.statesCategory_WordThreeSolved = true;
                            System.out.println("States Category: Word three solved");
                        } else {
                            System.out.println("In updateIndexOfLetter states category: This shouldn't happen");
                        }
                    }

                    //if the word is not solved withing 6 guesses
                     else if (toReturnInfo.guessLeft == 0){
                        toReturnInfo.attempts++;
                        toReturnInfo.workingWordForLength = null;
                        toReturnInfo.workingWord = null;
                        if (!toReturnInfo.statesCategory_WordOneSolved) {
                            System.out.println("States Category: Word one not solved");
                        } else if (!toReturnInfo.statesCategory_WordTwoSolved) {
                            System.out.println("States Category: Word two not solved");
                        } else if (!toReturnInfo.statesCategory_WordThreeSolved) {
                            System.out.println("States Category: Word three not solved");
                        } else {
                            System.out.println("In updateIndexOfLetter states category: This shouldn't happen");
                        }
                    }

                } else {
                    System.out.println("In updateIndexOfLetter: Not playing any category");
                }
                return toReturnInfo;

                //indexOfLetter will be changing,
                //set to -2 to reset
                //-2 means nothing has been changed
                //-1 means index is not found
                //otherwise letter found at that index which is stored in indexOfLetter
            }

            public ServerSideGameInfo validGuessChecker(String word, String letter, ServerSideGameInfo receivedInfo) {
                if (receivedInfo.workingWord == null) {
                    receivedInfo.workingWord = word;
                    receivedInfo.workingWordForLength = word;
                    receivedInfo.lengthOfWorkingWordForLength = word.length();
                }

                int index = receivedInfo.workingWord.toLowerCase().indexOf(letter);

                if (index == -1 && !letter.equals("1")) {
                    receivedInfo.guessLeft--;
                }

                receivedInfo.indexOfLetter = index;

                receivedInfo.selectedLetter = letter;
                receivedInfo.workingWord = receivedInfo.workingWord.replaceFirst(letter, "_");
                receivedInfo.workingWordForLength = receivedInfo.workingWordForLength.replaceFirst(letter, "");

                //checks if working word is solved
                return receivedInfo;
            }

            public ServerSideGameInfo checkLetterClickedHelper(String category, String letter, ServerSideGameInfo receivedInfo) {
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

            public ServerSideGameInfo checkLetterClicked(String category, ServerSideGameInfo receivedInfo) {
                switch (receivedInfo.selectedLetter) {
                    case "a":
                        return checkLetterClickedHelper(category, "a", receivedInfo);
                    case "b":
                        return checkLetterClickedHelper(category, "b", receivedInfo);
                    case "c":
                        return checkLetterClickedHelper(category, "c", receivedInfo);
                    case "d":
                        return checkLetterClickedHelper(category, "d", receivedInfo);
                    case "e":
                        return checkLetterClickedHelper(category, "e", receivedInfo);
                    case "f":
                        return checkLetterClickedHelper(category, "f", receivedInfo);
                    case "g":
                        return checkLetterClickedHelper(category, "g", receivedInfo);
                    case "h":
                        return checkLetterClickedHelper(category, "h", receivedInfo);
                    case "i":
                        return checkLetterClickedHelper(category, "i", receivedInfo);
                    case "j":
                        return checkLetterClickedHelper(category, "j", receivedInfo);
                    case "k":
                        return checkLetterClickedHelper(category, "k", receivedInfo);
                    case "l":
                        return checkLetterClickedHelper(category, "l", receivedInfo);
                    case "m":
                        return checkLetterClickedHelper(category, "m", receivedInfo);
                    case "n":
                        return checkLetterClickedHelper(category, "n", receivedInfo);
                    case "o":
                        return checkLetterClickedHelper(category, "o", receivedInfo);
                    case "p":
                        return checkLetterClickedHelper(category, "p", receivedInfo);
                    case "q":
                        return checkLetterClickedHelper(category, "q", receivedInfo);
                    case "r":
                        return checkLetterClickedHelper(category, "r", receivedInfo);
                    case "s":
                        return checkLetterClickedHelper(category, "s", receivedInfo);
                    case "t":
                        return checkLetterClickedHelper(category, "t", receivedInfo);
                    case "u":
                        return checkLetterClickedHelper(category, "u", receivedInfo);
                    case "v":
                        return checkLetterClickedHelper(category, "v", receivedInfo);
                    case "w":
                        return checkLetterClickedHelper(category, "w", receivedInfo);
                    case "x":
                        return checkLetterClickedHelper(category, "x", receivedInfo);
                    case "y":
                        return checkLetterClickedHelper(category, "y", receivedInfo);
                    case "z":
                        return checkLetterClickedHelper(category, "z", receivedInfo);
                    default:
                        System.out.println("In check letter Clicked: No letter is clicked.");
                        return checkLetterClickedHelper(category, "1", receivedInfo); //used to update white boxes in client
                }
            }

            public ServerSideGameInfo transferClientToServerGameInfo(ServerSideGameInfo serverSideGameInfo, ClientSideGameInfo clientSideGameInfo) {
                //returns server side converted gameInfo

                serverSideGameInfo.playingAnimalsCategory = clientSideGameInfo.playingAnimalsCategory;
                serverSideGameInfo.animalsCategorySolved = clientSideGameInfo.animalsCategorySolved;
                serverSideGameInfo.animalsCategory_WordOneSolved = clientSideGameInfo.animalsCategory_WordOneSolved;
                serverSideGameInfo.animalsCategory_WordTwoSolved = clientSideGameInfo.animalsCategory_WordTwoSolved;
                serverSideGameInfo.animalsCategory_WordThreeSolved = clientSideGameInfo.animalsCategory_WordThreeSolved;

                serverSideGameInfo.playingFoodCategory = clientSideGameInfo.playingFoodCategory;
                serverSideGameInfo.foodCategorySolved = clientSideGameInfo.foodCategorySolved;
                serverSideGameInfo.foodCategory_WordOneSolved = clientSideGameInfo.foodCategory_WordOneSolved;
                serverSideGameInfo.foodCategory_WordTwoSolved = clientSideGameInfo.foodCategory_WordTwoSolved;
                serverSideGameInfo.foodCategory_WordThreeSolved = clientSideGameInfo.foodCategory_WordThreeSolved;

                serverSideGameInfo.playingStatesCategory = clientSideGameInfo.playingStatesCategory;
                serverSideGameInfo.statesCategorySolved = clientSideGameInfo.statesCategorySolved;
                serverSideGameInfo.statesCategory_WordOneSolved = clientSideGameInfo.statesCategory_WordOneSolved;
                serverSideGameInfo.statesCategory_WordTwoSolved = clientSideGameInfo.statesCategory_WordTwoSolved;
                serverSideGameInfo.statesCategory_WordThreeSolved = clientSideGameInfo.statesCategory_WordThreeSolved;

                serverSideGameInfo.guessLeft = clientSideGameInfo.guessLeft;
                serverSideGameInfo.indexOfLetter = clientSideGameInfo.indexOfLetter;
                serverSideGameInfo.attempts = clientSideGameInfo.attempts;
                serverSideGameInfo.selectedLetter = clientSideGameInfo.selectedLetter;
                serverSideGameInfo.lengthOfWorkingWordForLength = clientSideGameInfo.lengthOfWorkingWordForLength;

                return serverSideGameInfo;
            }

            public ClientSideGameInfo transferServerToClientGameInfo(ServerSideGameInfo serverSideGameInfo, ClientSideGameInfo clientSideGameInfo) {
                //returns client side converted gameInfo

                clientSideGameInfo.playingAnimalsCategory = serverSideGameInfo.playingAnimalsCategory;
                clientSideGameInfo.animalsCategorySolved = serverSideGameInfo.animalsCategorySolved;
                clientSideGameInfo.animalsCategory_WordOneSolved = serverSideGameInfo.animalsCategory_WordOneSolved;
                clientSideGameInfo.animalsCategory_WordTwoSolved = serverSideGameInfo.animalsCategory_WordTwoSolved;
                clientSideGameInfo.animalsCategory_WordThreeSolved = serverSideGameInfo.animalsCategory_WordThreeSolved;

                clientSideGameInfo.playingFoodCategory = serverSideGameInfo.playingFoodCategory;
                clientSideGameInfo.foodCategorySolved = serverSideGameInfo.foodCategorySolved;
                clientSideGameInfo.foodCategory_WordOneSolved = serverSideGameInfo.foodCategory_WordOneSolved;
                clientSideGameInfo.foodCategory_WordTwoSolved = serverSideGameInfo.foodCategory_WordTwoSolved;
                clientSideGameInfo.foodCategory_WordThreeSolved = serverSideGameInfo.foodCategory_WordThreeSolved;

                clientSideGameInfo.playingStatesCategory = serverSideGameInfo.playingStatesCategory;
                clientSideGameInfo.statesCategorySolved = serverSideGameInfo.statesCategorySolved;
                clientSideGameInfo.statesCategory_WordOneSolved = serverSideGameInfo.statesCategory_WordOneSolved;
                clientSideGameInfo.statesCategory_WordTwoSolved = serverSideGameInfo.statesCategory_WordTwoSolved;
                clientSideGameInfo.statesCategory_WordThreeSolved = serverSideGameInfo.statesCategory_WordThreeSolved;

                clientSideGameInfo.guessLeft = serverSideGameInfo.guessLeft;
                clientSideGameInfo.indexOfLetter = serverSideGameInfo.indexOfLetter;
                clientSideGameInfo.attempts = serverSideGameInfo.attempts;
                clientSideGameInfo.selectedLetter = serverSideGameInfo.selectedLetter;
                clientSideGameInfo.lengthOfWorkingWordForLength = serverSideGameInfo.lengthOfWorkingWordForLength;

                return clientSideGameInfo;
            }
        } //end of client thread
    }
}
