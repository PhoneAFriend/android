package seniordesign.phoneafriend.session;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by The Alex on 12/14/2016.
 */

public class ChatMessage {
    private String sender;
    private String message;

    public ChatMessage(){
        sender = null;
        message  = null;
    }

    public ChatMessage(String sender , String message){
        this.sender = sender;
        this.message = message;
    }

    public ChatMessage(DataSnapshot dataSnapshot , String messageKey){
        sender = dataSnapshot.child(messageKey).child("sender").getValue().toString();
        message = dataSnapshot.child(messageKey).child("message").getValue().toString();

    }

    public String getMessage(){return  message;}

    public String getSender(){return sender;}
}
