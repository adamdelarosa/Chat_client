package sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Connection {

    public DataInputStream getFromServer = null;
    private DataOutputStream sendToServer = null;
    private String message = "";
    private Socket serverConectionState;
    private String serverIP,msg;



    public Connection(){

    }

    public void connectToServer(){
        try {
            serverConectionState = new Socket("127.0.0.1", 6789);
            getFromServer = new DataInputStream(serverConectionState.getInputStream());
        } catch (IOException ex){


        }
        try {
            do {
                try {
                    Object msg = getFromServer.readUTF();
                    System.out.print(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (getFromServer.available() > 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}