package sample.ClientMain;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Controller implements Runnable {

    @FXML

    private TextField userText, ipAdr;
    @FXML
    private TextArea chatWindow;
    @FXML
    private Label onlineOffline;
    public DataInputStream getFromServer = null;
    private DataOutputStream sendToServer = null;
    private String message = "";
    private Socket serverConectionState;
    private String serverIP, msg;
    private Thread iThread;
    private volatile boolean getMessageThreadSwitch;


    public void connectToServer() {
        try {
            connectionStart();
            streamsStart();
            getMessage();
            //checkConnection();
        } catch (EOFException eofexception) {
            chatWindow.appendText("\n Client terminated the connection");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("finally");
            //    closeConnection();
        }

        //onlineOffline.setText("Online");
        //onlineOffline.setTextFill(javafx.scene.paint.Color.web("#0076a3"));
    }

    public void connectionStart() throws IOException {
        serverConectionState = new Socket("127.0.0.1", 6789);
    }

    public void streamsStart() throws IOException {
        getFromServer = new DataInputStream(serverConectionState.getInputStream());
        sendToServer = new DataOutputStream(serverConectionState.getOutputStream());
    }

    public void checkConnection() {
        Thread threadCheckConnection = new Thread(() -> {
            while (true) {
                if (serverConectionState==null || !serverConectionState.isConnected()) {
                    try {
                        Platform.runLater(() -> System.out.print("YOU"));
                        Thread.sleep(1000);
                        System.out.print("SLEEP");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                        System.out.println("no");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadCheckConnection.start();
    }


    public void closeConnection() {
        try {
            getMessageThreadSwitch = true;
            sendToServer.close();
            getFromServer.close();
            serverConectionState.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }

    }

    public void sendMessage() {
        try {
            message = userText.getText();
            sendToServer.writeUTF(message);
            sendToServer.flush();

            chatWindow.appendText("\n" + message);

        } catch (IOException ioException) {
            chatWindow.appendText("\nConnection error.");
        }
        userText.setText("");
    }

    public void getMessage() {
        iThread = new Thread(this);
        iThread.start();
    }

    public void run() {
        do {
            try {
                String msg = getFromServer.readUTF();
                Platform.runLater(() -> chatWindow.appendText("\n" + msg));
            } catch (SocketException socketIsClose) {
                socketIsClose.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!getMessageThreadSwitch);
    }
}

