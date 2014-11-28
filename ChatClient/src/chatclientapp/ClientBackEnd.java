
package chatclientapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import javafx.application.Platform;
import message.ChatMessage;
import message.LoginResponseMessage;
import message.UserUpdateMessage;

public class ClientBackEnd implements Runnable{
    
    private Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private FXMLDocumentController controller;
    
    public ClientBackEnd(FXMLDocumentController controller){
       
        try {
            clientSocket = new Socket("127.0.0.1",3010);
            this.controller = controller;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        //read and write from socket until user closes the app
        while(true){
            try {
                final Serializable m = (Serializable)input.readObject();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (m instanceof LoginResponseMessage) {
                            LoginResponseMessage lm = (LoginResponseMessage) m;
                            controller.loginAccepted(lm);
                        } else {
                            if (m instanceof ChatMessage) {
                                ChatMessage cm = (ChatMessage) m;
                                controller.updateTextArea(
                                    cm.getUserName() +
                                    (cm.isPrivate()
                                        ? (controller.myUserName().equals(cm.getUserName())
                                            ? " (private to " + cm.getPrivateName() + "): "
                                            : " (private): ")
                                        : ": ") +
                                    cm.getChatMessage());
                            } else {
                                if (m instanceof UserUpdateMessage) {
                                    UserUpdateMessage um = (UserUpdateMessage) m;
                                    controller.updateUsers(um.getUsers());
                                }
                            }
                        }                       
                    }
                });
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            } 
        }
    }
    
    public void sendMessage(Serializable msg){
        
        try {
            output.writeObject(msg);
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }   
}
