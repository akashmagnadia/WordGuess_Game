import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{
    ClientSideGameInfo myPlayerInfo;

    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    boolean clientNumberCaptured; //used to get the client number

    private final Consumer<Serializable> callBack;
    
	int port;
	String ip;
	
    Client(Consumer<Serializable> call, int portNum, String ipAddress){
        callBack = call;
        myPlayerInfo = new ClientSideGameInfo();
		port = portNum;
		ip = ipAddress;
        clientNumberCaptured = false;
    }

    public void run() {

        try {
            socketClient= new Socket(ip,port);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        }
        catch(Exception ignored) {}

        while (true) {
            try {
                ClientSideGameInfo receivedInfo = (ClientSideGameInfo) in.readObject();

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

    public void send(ClientSideGameInfo info, String message) {
        try {
            out.writeObject(info);
            callBack.accept(message);
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
