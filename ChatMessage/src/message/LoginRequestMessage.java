package message;

import java.io.Serializable;

public class LoginRequestMessage implements Serializable {
    private String userName;

    public LoginRequestMessage(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
