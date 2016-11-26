package seniordesign.phoneafriend.messaging;

import com.google.firebase.database.DataSnapshot;

import java.util.HashMap;

/**
 * Created by REB
 */

public class Message {

    private String message;
    private String recipientUsername;
    private String senderUsername;
    private String subject;
    private boolean unread;
    private String key;

    public Message() {}

    public Message(String message, String recipientUsername, String senderUsername, String subject, boolean unread, String key){
        this.message = message;
        this.recipientUsername = recipientUsername;
        this.senderUsername =senderUsername;
        this.subject = subject;
        this.unread = unread;
        this.key = key;
    }

    public Message(DataSnapshot messageData){
        this.message = messageData.child("message").getValue().toString();
        this.recipientUsername = messageData.child("recipientUsername").getValue().toString();
        this.senderUsername = messageData.child("senderUsername").getValue().toString();
        this.subject = messageData.child("subject").getValue().toString();
        this.unread = (boolean) messageData.child("unread").getValue();
        this.key = messageData.getKey();

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String , Object> map = new HashMap<>();
        map.put("message",message);
        map.put("recipientUsername",recipientUsername);
        map.put("senderUsername",senderUsername);
        map.put("subject",subject);
        map.put("unread",unread);

        return map;
    }



}
