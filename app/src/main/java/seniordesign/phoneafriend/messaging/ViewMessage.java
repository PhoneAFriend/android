package seniordesign.phoneafriend.messaging;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;

/**
 * Created by REB
 */

public class ViewMessage extends AppCompatActivity {

    private TextView username_label;
    private EditText username_text;
    private EditText subject_text;
    private EditText message_content;
    private Bundle recvExtras;
    private Button replyButton;
    private Button deleteButton;
    private String messageKey;
    private DatabaseReference db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        /* Get db reference */
        db = FirebaseDatabase.getInstance().getReference();

        //Get data we will need in order to view the message
        recvExtras = getIntent().getExtras();

        messageKey = recvExtras.getString("key");

        /* Get all our textviews, and EditTexts and set values where needed, also set all EditText to be uneditable*/
        username_label = (TextView) findViewById(R.id.username_label);
        username_label.setText("Sent From");

        username_text = (EditText) findViewById(R.id.username_text);
        username_text.setText(recvExtras.get("senderUsername").toString());
        username_text.setKeyListener(null); //This stops user from being able to edit the text

        subject_text = (EditText) findViewById(R.id.subject_text);
        subject_text.setText(recvExtras.get("subject").toString());
        subject_text.setKeyListener(null);

        message_content = (EditText) findViewById(R.id.message_text);
        message_content.setText(recvExtras.get("message").toString());
        message_content.setKeyListener(null);

        //Get our reply button and implement what happens when you click it
        replyButton = (Button) findViewById(R.id.msg_button);
        replyButton.setText("Reply");

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make an intent to create a new message
                Intent newMsg = new Intent(getApplicationContext(), NewMessageActivity.class);
                //Save the data we will need in the next activity, in this case we save username_text because it holds the username of the user that
                //sent us the current message, also we send the subject
                newMsg.putExtra("recipient",username_text.getText().toString());
                newMsg.putExtra("subject",subject_text.getText().toString());
                //Start the new message activity
                startActivity(newMsg );
            }
        });

        //Create a dialog interface which will ask the user if they really want to remove a message
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button delete the message
                        removeMessage(messageKey);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked do nothing
                        break;
                }
            }
        };




        deleteButton = (Button) findViewById(R.id.delete_button);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the Alert Dialog and Show it! If the user presses remove button on a contact
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMessage.this);
                builder.setMessage("Are you sure you want to delete this message?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    public void removeMessage(String key){
        //printMessageTitles();
        //Toast.makeText(ViewMessage.this,"Key "+key,Toast.LENGTH_LONG).show();
        //System.out.println("Message Count before "+PhoneAFriend.getInstance().getReceivedMessages().size());
        final Iterator<Message> messageIterator = PhoneAFriend.getInstance().getReceivedMessages().iterator();
        Message temp;
        while(messageIterator.hasNext()){
            temp = messageIterator.next();
            if(temp.getKey().equals(key)){
                messageIterator.remove();
             db.child("messages").child(key).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful()){
                         //messageIterator.remove();
                         //Some Debug Statements
                         //printMessageTitles();
                         //System.out.println("Message Count "+PhoneAFriend.getInstance().getReceivedMessages().size());
                         //PhoneAFriend.getInstance().notifyInboxListChange(); //Not needed if we do notifychange on resume for inbox activity
                         //Call finish to go back to the inbox
                         finish();
                     }else{
                         //System.out.println("Message Count "+PhoneAFriend.getInstance().getReceivedMessages().size());
                         Toast.makeText(ViewMessage.this,"Could not delete this message, try again later",Toast.LENGTH_LONG).show();
                     }
                 }
             });
            }
        }
    }

    public void printMessageTitles(){
        final Iterator<Message> messageIterator = PhoneAFriend.getInstance().getReceivedMessages().iterator();
        Message t;
        while(messageIterator.hasNext()){
            t = messageIterator.next();
            System.out.println("Title is "+t.getSubject());
        }
    }


}
