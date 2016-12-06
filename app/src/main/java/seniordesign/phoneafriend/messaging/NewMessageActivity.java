package seniordesign.phoneafriend.messaging;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;

public class NewMessageActivity extends AppCompatActivity {

    private TextView username_label;
    private EditText username_text;
    private EditText subject_text;
    private EditText message_content;
    private Button send_msg;
    private DatabaseReference db;
    private String postKey;

    private Bundle recvExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        //Get the information saved from when we clicked the message we wanted to view
        recvExtras = getIntent().getExtras();
        //Get a reference for the database for when we send our message
        db = FirebaseDatabase.getInstance().getReference();

        //Set values for the recipient editText and subject editText if we have data for it
        username_text = (EditText) findViewById(R.id.username_text);
        username_text.setKeyListener(null); //set recipient EditText as uneditable
        username_text.setText(recvExtras.getString("recipient"));

        subject_text = (EditText) findViewById(R.id.subject_text);
        if(recvExtras.getString("subject") != null){
            subject_text.setText("Reply:"+recvExtras.getString("subject"));
        }
        //Put a listener that allows us to hide the keyboard when you click on the background
        subject_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        message_content = (EditText) findViewById(R.id.message_text);
        //Put a listener that allows us to hide the keyboard when you click on the background
        message_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        if(recvExtras.getString("postKey") != null){
            postKey = recvExtras.getString("postKey");
        }
        else{
            postKey = "";
        }
        //Toast.makeText(NewMessageActivity.this,"Post Key is <"+postKey+">",Toast.LENGTH_LONG).show();

        //Find our send button and implement what to do when we click it
        send_msg = (Button) findViewById(R.id.msg_button);
        send_msg.setText("Send");
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disable button so user doesn't accidently send multiple messages
                send_msg.setClickable(false);
                //Verify all fields are filled in
                if(fieldsVerified()){
                    //If all is correct, post to the database
                    String key = db.child("messages").push().getKey();
                    Message sentMsg = new Message(message_content.getText().toString(), username_text.getText().toString(), PhoneAFriend.getInstance().getUsername(), subject_text.getText().toString(), true, key, postKey);
                    //Toast.makeText(NewMessageActivity.this,"Post Key is <"+sentMsg.getPostKey()+">",Toast.LENGTH_LONG).show();
                    db.child("messages").child(key).setValue(sentMsg.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Once we finish posting successfully, let the user know, and let our send button be clickable
                                Toast.makeText(NewMessageActivity.this,"Message sent to "+username_text.getText().toString(),Toast.LENGTH_LONG).show();
                                send_msg.setClickable(true);
                                //finish so this activity is removed from the back stack
                                finish();
                            }else{
                                //If we were not succssful in posting, let the user know and let the send button be clickable again
                                Toast.makeText(NewMessageActivity.this,"Unable to send message to "+username_text.getText().toString(),Toast.LENGTH_LONG).show();
                                send_msg.setClickable(true);
                            }
                        }
                    });
                }else {
                    //If fields were incorrect, just set send button as clickable again
                    send_msg.setClickable(true);
                }

            }
        });

    }

    //Method used to hide the keyboard when clicking outside the keyboard, we do this when focus is changed
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*Method that checks correctness of all EditText fields that need to be filled in displays relevant error message*/
    public boolean fieldsVerified(){
        if(TextUtils.isEmpty(username_text.getText())){
            Toast.makeText(NewMessageActivity.this,"Error: recipient field is empty!",Toast.LENGTH_LONG).show();
            return false;
        }else if(TextUtils.isEmpty(subject_text.getText())){
            Toast.makeText(NewMessageActivity.this,"Error: subject cannot be empty!",Toast.LENGTH_LONG).show();
            //Toast.makeText(NewMessageActivity.this,"Error: subject cannot be empty and must be less than 60 characters!",Toast.LENGTH_LONG).show();
            return false;
        }else if(TextUtils.isEmpty(message_content.getText())){
            Toast.makeText(NewMessageActivity.this,"Error: Message cannot be empty!",Toast.LENGTH_LONG).show();
            //Toast.makeText(NewMessageActivity.this,"Error: Message cannot be empty and must be less than x characters!",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
