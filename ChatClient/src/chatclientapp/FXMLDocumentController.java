
package chatclientapp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import message.ChatMessage;
import message.LoginRequestMessage;
import message.LoginResponseMessage;

public class FXMLDocumentController implements Initializable {
    
    @FXML
    Button loginButton;
    
    @FXML
    Button sendButton;
    
    @FXML
    TextField name;
    
    @FXML
    ListView userList;
    
    @FXML
    TextField chatMessage;
    
    @FXML
    TextArea chatMessageArea;
    
    @FXML
    CheckBox sendPrivateCheckbox;
    
    @FXML
    TextField privateUser;
    
    ClientBackEnd backEnd;
    Thread backThread;
    
    @FXML
    public void tryLogin(ActionEvent ae) {
        if (name.getText().isEmpty()) {
            return;
        }
        loginButton.setDisable(true);
        name.setDisable(true);
        
        LoginRequestMessage lm = new LoginRequestMessage(name.getText());
        backEnd.sendMessage(lm);
    }
    
    public void loginAccepted(LoginResponseMessage lm) {
        name.setText(lm.getAcceptedUserName());
        chatMessage.setDisable(false);
        chatMessage.setEditable(true);
        sendButton.setDisable(false);
        
        updateUsers(lm.getUsers());
    }
    
    public void updateUsers(List<String> users) {
        userList.setItems(FXCollections.observableList(users));
    }
    
    @FXML
    public void sendChatMessage(ActionEvent ae) {
        ChatMessage cm = new ChatMessage();
        cm.setUserName(name.getText());
        cm.setChatMessage(chatMessage.getText());
        
        if (sendPrivateCheckbox.isSelected()) {
            cm.setIsPrivate(true);
            cm.setPrivateName(privateUser.getText());
        }
        backEnd.sendMessage(cm);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backEnd = new ClientBackEnd(this);
        backThread = new Thread(backEnd);
        backThread.setDaemon(true);
        backThread.start();
    }    
    
    public void updateTextArea(String message) {
        chatMessageArea.appendText(message + "\n");
    }
    
    public String myUserName() {
        return name.getText();
    }
}
