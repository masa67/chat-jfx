
package chatserver2;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import message.ChatMessage;
import message.UserUpdateMessage;

public class ChatServer2 {

    static ArrayList<ServerClientBackEnd> clients = new ArrayList();
    static private final Object lockClients = new Object();
    
    public static void main(String[] args) {
        try {
            //Start the server to listen port 3010
            ServerSocket server = new ServerSocket(3010);
            
            //Start to listen and wait connections
            while(true){
                //Wait here the client
                Socket temp = server.accept();
                ServerClientBackEnd backEnd = new ServerClientBackEnd(temp);
                synchronized (lockClients) {
                    clients.add(backEnd);
                }
                Thread t = new Thread(backEnd);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void broadcastMessage(Serializable msg){
        synchronized (lockClients) {
            for (ServerClientBackEnd cl : clients) {
                cl.sendMessage(msg);
            }
        }
    }
    
    public static void sendMessage(String user, Serializable msg) {
        synchronized (lockClients) {
            for (ServerClientBackEnd cl : clients) {
                if (cl.getUserName().equals(user)) {
                    cl.sendMessage(msg);
                    break;
                }
            }
        }
    }
    
    public static boolean validateUserName(String userName) {
        synchronized (lockClients) {
            for (ServerClientBackEnd cl : clients) {
                String n = cl.getUserName();
                if (n != null && n.equals(userName)) {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    public static void dropUser(UUID id) {
        
        synchronized (lockClients) {
            ServerClientBackEnd cl = null;
            boolean found = false;
            for (Iterator<ServerClientBackEnd> it = clients.iterator(); it.hasNext();) {
                cl = it.next();
                if (cl.getId().equals(id)) {
                    it.remove();
                    found = true;
                    break;
                }
            }

            if (found && cl != null && !cl.getUserName().isEmpty()) {
                ChatMessage cm = new ChatMessage(
                    "system",
                    "user '" + cl.getUserName() + "' left the discussion.");
                broadcastMessage(cm);

                UserUpdateMessage um = new UserUpdateMessage(getUsers());
                broadcastMessage(um);
            }
        }
    }
    
    public static List<String> getUsers() {
        List<String> users = new ArrayList<>();
        
        synchronized (lockClients) {
            for (ServerClientBackEnd cl : clients) {
                if (!cl.getUserName().isEmpty()) {
                    users.add(cl.getUserName());
                }
            }
        }
        
        return users;
    }
    
    
}
