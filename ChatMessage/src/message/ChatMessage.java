
package message;

import java.io.Serializable;

public class ChatMessage implements Serializable {
    
    private String userName;
    private String chatMessage;
    private String messageColor;
    private int fontSize;
    private boolean isPrivate;
    private String privateName;

    public ChatMessage() {       
    }
    
    public ChatMessage(String userName, String chatMessage) {
        this.userName = userName;
        this.chatMessage = chatMessage;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public void setMessageColor(String messageColor) {
        this.messageColor = messageColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getPrivateName() {
        return privateName;
    }

    public void setPrivateName(String privateName) {
        this.privateName = privateName;
    }
}
