package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Controller implements Runnable {

    @FXML private TextField userText,ipAdr;
    @FXML private TextArea chatWindow;
    @FXML private Label onlineOffline;
    public DataInputStream getFromServer = null;
    private DataOutputStream sendToServer = null;
    private String message = "";
    private Socket serverConectionState;
    private String serverIP,msg;
    private Thread iThread;
    private boolean getMessageThreadSwitch;


    public void connectToServer(){
        try {
            serverConectionState = new Socket("127.0.0.1", 6789);
            getFromServer = new DataInputStream(serverConectionState.getInputStream());
            sendToServer = new DataOutputStream(serverConectionState.getOutputStream());
            onlineOffline.setText("Online");
            onlineOffline.setTextFill(javafx.scene.paint.Color.web("#0076a3"));
        } catch (EOFException eofexception){
            eofexception.printStackTrace();
        } catch (IOException ex){
            chatWindow.appendText("Could not connect to server!\n");
        }finally {
           // closeConnection();
        }
        getMessage();
    }

    public void closeConnection(){
        chatWindow.appendText("Closing the connection . . .");
        try {
            getMessageThreadSwitch = true;
            sendToServer.close();
            getFromServer.close();
            serverConectionState.close();
        }catch (IOException ioexception){
            ioexception.printStackTrace();
        }

    }

    public void sendMessage() {
        try {
            message = userText.getText();
            sendToServer.writeUTF(message);
            sendToServer.flush();

            chatWindow.appendText("\n" + message);

        }catch (IOException ioException){
            chatWindow.appendText("Connection error.");
        }
        userText.setText("");
    }

    public void getMessage() {
        iThread = new Thread(this);
        iThread.start();
    }

    public void run(){
        do {
            try {
                String msg = getFromServer.readUTF();
                Platform.runLater(() -> chatWindow.appendText(msg + "\n"));
            } catch (SocketException socketIsClose){
                socketIsClose.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!getMessageThreadSwitch);
    }
}

