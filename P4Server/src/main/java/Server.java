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
                        tempThread.clientInfo = performLogic(receivedInfo);

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

            public GameInfo performLogic(GameInfo receivedInfo) {

                if (receivedInfo.playingCategoryOne) {

                } else if (receivedInfo.playingCategoryTwo) {

                } else if (receivedInfo.playingCategoryThree) {

                } else {
                    System.out.println("This shouldn't happen");
                }
                return receivedInfo;
            }


            public void send(GameInfo info, String message) {
                try {
                    out.writeObject(info);
                    callBack.accept(message);
                    System.out.println(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } //end of client thread
    }
}
