import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{
    GameInfo myPlayerInfo;

    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    boolean clientNumberCaptured; //used to get the client number

    private Consumer<Serializable> callBack;
    Client(Consumer<Serializable> call){
        callBack = call;
        myPlayerInfo = new GameInfo();
        clientNumberCaptured = false;
    }

    public void run() {

        try {
//            socketClient= new Socket("127.0.0.1",5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception ignored) {}

        while(true) {
            try {
                GameInfo receivedInfo = (GameInfo) in.readObject();

                //since when the client is made it is assigned a client number
                if (!clientNumberCaptured) {
                    myPlayerInfo.clientNumber = receivedInfo.clientNumber;
                    clientNumberCaptured = true;
                }

                //TODO: Repeat this in for loop to pass info to correct client
                if (myPlayerInfo.clientNumber == receivedInfo.clientNumber) {
                    myPlayerInfo = receivedInfo;
                }
            }
            catch(Exception ignored) {}
        }
    }

    public void send(GameInfo info, String message) {
        try {
            out.writeObject(info);
            callBack.accept(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
