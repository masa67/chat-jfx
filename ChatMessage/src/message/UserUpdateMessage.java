
package message;

import java.io.Serializable;
import java.util.List;

public class UserUpdateMessage implements Serializable {
    List<String> users;

    public UserUpdateMessage(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
