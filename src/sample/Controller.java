package sample;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller implements EventHandler<ActionEvent>  {


    @FXML private TextField userText,ipAdr;
    @FXML private TextArea chatWindow;
    public java.lang.String userDataText;
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    public String ip = "127.0.0.1";


    public void handle(ActionEvent event) {

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
                toServer.writeUTF(userText.getText());
                toServer.flush();
                String userDataText = fromServer.readUTF();
              //  chatWindow.appendText(df.format(date) + ": " + userDataText + "\n");
                chatWindow.appendText(userDataText + "\n");
            } catch (IOException e) {
                chatWindow.appendText("Could not send/receive message! \n");
            }

            userText.clear();
        }

    //}
    public void Connect(){
        try {
            Socket socket = new Socket(ip, 6789);
            fromServer = new DataInputStream(socket.getInputStream());
            toServer = new DataOutputStream(socket.getOutputStream());
            chatWindow.appendText("Connect to server!\n");
        }catch (IOException ex){
            chatWindow.appendText("Could not connect to server!\n");
        }
    }

}