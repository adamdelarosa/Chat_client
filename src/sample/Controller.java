package sample;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;



public class Controller  {


    @FXML private TextField userText,ipAdr;
    @FXML private TextArea chatWindow;
    @FXML private Label onlineOffline;
    private DataOutputStream sendToServer;
    private DataInputStream getFromServer;
    private String message = "";
    private Socket serverConectionState;
    private String serverIP,msg;







    public void connectToServer(){
        try {
            serverConectionState = new Socket("127.0.0.1", 6789);
            getFromServer = new DataInputStream(serverConectionState.getInputStream());
            sendToServer = new DataOutputStream(serverConectionState.getOutputStream());
            onlineOffline.setText("Online");
            onlineOffline.setTextFill(javafx.scene.paint.Color.web("#0076a3"));


            whileChatting();
        } catch (IOException ex){
            chatWindow.appendText("Could not connect to server!\n");

        }
    }



    //update chat window
    public void showMessage(final String message){
        Platform.runLater(
                new Runnable() {
                    public void run() {
                        chatWindow.appendText(message);
                    }
                }
        );
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

    public void sendMessage() {
        try {
            message = userText.getText();
            sendToServer.writeUTF(message);
            sendToServer.flush();

            System.out.print(message);

            chatWindow.appendText(message + "\n");

        }catch (IOException ioException){
            chatWindow.appendText("Connection error.");
        }
        userText.setText("");



    }
    public void getMessage() {
     //   do {
            Platform.runLater(() -> {
                try {
                    msg = getFromServer.readUTF();
                    chatWindow.appendText(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
      //  }while (!message.equals("SERVER - END"));
    }
    private void whileChatting() throws IOException{
        String message = " You are now connected! ";
       // sendMessage(message);
        do{
            try{
                message = (String) getFromServer.readUTF();
               // showMessage("\n" + message);
            }catch(IOException ex){
              //  showMessage("The user has sent an unknown object!");
            }
        }while(!message.equals("CLIENT - END"));
    }

}








//getting current date and time using Date class

// DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
// Date date = new Date();

//print data to text box and take user data

//if (userText.getText().equals("") || userText.getText().isEmpty()) {
//do noting if no String
// } else {
//  userDataText = userText.getText();
//   chatWindow.appendText(df.format(date) + ": " + userDataText + "\n");
//  chatWindow.appendText(df.format(date) + ": " + userDataText + "\n");
