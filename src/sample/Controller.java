package sample;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.*;
import javafx.scene.paint.Paint;
import sun.font.TextLabel;

import java.awt.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Controller  {


    @FXML private TextField userText,ipAdr;
    @FXML private TextArea chatWindow;
    @FXML private Label onlineOffline;
    public java.lang.String userDataText;
    private Socket serverConectionState;


    DataOutputStream sendToServer = null;
    DataInputStream getFromServer = null;

    public String serverIp = "127.0.0.1";

    public void connectToServer(){
        try {
            serverConectionState = new Socket(serverIp, 6789);
            getFromServer = new DataInputStream(serverConectionState.getInputStream());
            sendToServer = new DataOutputStream(serverConectionState.getOutputStream());
            chatWindow.appendText("Connect to server!\n");
            onlineOffline.setText("Online");
            onlineOffline.setTextFill(javafx.scene.paint.Color.web("#0076a3"));
        }catch (IOException ex){
            chatWindow.appendText("Could not connect to server!\n");
        }
    }



    public void sendMessage() {

        //getting current date and time using Date class

        // DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        // Date date = new Date();

        //print data to text box and take user data

        //if (userText.getText().equals("") || userText.getText().isEmpty()) {
        //do noting if no String
        // } else {
        //  userDataText = userText.getText();
        //   chatWindow.appendText(df.format(date) + ": " + userDataText + "\n");


        try {

            sendToServer.writeUTF(userText.getText());
            sendToServer.flush();

            String messege = getFromServer.readUTF();

            //  chatWindow.appendText(df.format(date) + ": " + userDataText + "\n");
            chatWindow.appendText(messege + "\n");
        } catch (EOFException eofException) {
            chatWindow.appendText("Could not send/receive message! \n");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }finally {
            closeConnection();
        }
            userText.clear();
        }

    //}

    public void getMessage(){

    }

    public void closeConnection(){
        onlineOffline.setText("Closing connection. . .");
        try {
            sendToServer.close();
            getFromServer.close();
            serverConectionState.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}