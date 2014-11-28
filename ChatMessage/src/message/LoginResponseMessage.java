
package message;

import java.io.Serializable;
import java.util.List;

public class LoginResponseMessage implements Serializable {
    private String acceptedUserName;
    private List<String> users;

    public LoginResponseMessage(String acceptedUserName, List<String> users) {
        this.acceptedUserName = acceptedUserName;
        this.users = users;
    }

    public String getAcceptedUserName() {
        return acceptedUserName;
    }

    public void setAcceptedUserName(String acceptedUserName) {
        this.acceptedUserName = acceptedUserName;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }  
}
