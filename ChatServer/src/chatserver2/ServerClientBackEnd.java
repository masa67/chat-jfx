
package chatserver2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.UUID;
import message.LoginRequestMessage;
import message.LoginResponseMessage;
import message.ChatMessage;
import message.UserUpdateMessage;

public class ServerClientBackEnd implements Runnable{
    
    final private UUID id;
    final private Socket socket;
    private String userName;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public ServerClientBackEnd(Socket sock){
        id = UUID.randomUUID();
        socket = sock;
        userName = "";
    }
    
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        
            //Start to wait data
            while(true){
                Serializable m = (Serializable)input.readObject();
                
                if (m instanceof LoginRequestMessage) {
                    LoginRequestMessage lm = (LoginRequestMessage) m;
                    LoginResponseMessage resp;
                    
                    int ndx = 0;
                    String baseName = lm.getUserName();
                    String userName = baseName;
                    while (!ChatServer2.validateUserName(userName)) {
                        userName = baseName + ++ndx;
                    }
                    
                    this.userName = userName;
                    List<String> users = ChatServer2.getUsers();
                        
                    resp = new LoginResponseMessage(this.userName, users);
                        
                    ChatMessage cm = new ChatMessage(
                        "system",
                        "user '" + this.userName + "' joined the discussion.");
                    ChatServer2.broadcastMessage(cm);                     
                    
                    UserUpdateMessage um = new UserUpdateMessage(users);
                    ChatServer2.broadcastMessage(um);
                    
                    sendMessage(resp);
                } else {
                    if (m instanceof ChatMessage) {
                        ChatMessage cm = (ChatMessage) m;
                        if (cm.isPrivate()) {
                            ChatServer2.sendMessage(cm.getPrivateName(), cm);
                            ChatServer2.sendMessage(cm.getUserName(), cm);
                        } else {
                            ChatServer2.broadcastMessage(cm);
                        }
                    }
                }
            }

        } catch (IOException | ClassNotFoundException ex) {
            if (ex instanceof SocketException && ex.getMessage().equals("Connection reset")) {
                ChatServer2.dropUser(this.id);
            } else {
                ex.printStackTrace();
            }
        }
    }
    
    public void sendMessage(Serializable msg) {
        try {
            output.writeObject(msg);
            output.flush();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
}
