package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import java.io.*;
import java.net.Socket;

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

    public void connectToServer(){
        try {
            serverConectionState = new Socket("127.0.0.1", 6789);
            getFromServer = new DataInputStream(serverConectionState.getInputStream());
            sendToServer = new DataOutputStream(serverConectionState.getOutputStream());
            onlineOffline.setText("Online");
            onlineOffline.setTextFill(javafx.scene.paint.Color.web("#0076a3"));
        } catch (IOException ex){
            chatWindow.appendText("Could not connect to server!\n");
        }
    }

    public void sendMessage() {
        try {
            message = userText.getText();
            sendToServer.writeUTF(message);
            sendToServer.flush();

            chatWindow.appendText(message + "\n");

        }catch (IOException ioException){
            chatWindow.appendText("Connection error.");
        }
        userText.setText("");
    }

    public void getMessage() { // <-- This is a button for test.
        iThread = new Thread(this);
        iThread.run();
    }

    public void run(){
        do {
            try {
                String msg = getFromServer.readUTF(); // <--- freeze GUI
                Platform.runLater(() -> chatWindow.appendText(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (true);
    }
}

