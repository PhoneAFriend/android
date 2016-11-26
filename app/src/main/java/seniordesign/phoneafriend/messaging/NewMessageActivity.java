package seniordesign.phoneafriend.messaging;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;

public class NewMessageActivity extends AppCompatActivity {

    private TextView username_label;
    private EditText username_text;
    private EditText subject_text;
    private EditText message_content;
    private Button send_msg;
    private DatabaseReference db;

    private Bundle recvExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        recvExtras = getIntent().getExtras();

        username_text = (EditText) findViewById(R.id.username_text);
        username_text.setKeyListener(null);
        username_text.setText(recvExtras.getString("recipient"));

        subject_text = (EditText) findViewById(R.id.subject_text);
        subject_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        message_content = (EditText) findViewById(R.id.message_text);
        message_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        send_msg = (Button) findViewById(R.id.msg_button);
        send_msg.setText("Send");
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldsVerified()){
                    String key = db.child("messages").push().getKey();
                    Message sentMsg = new Message(message_content.getText().toString(), username_text.getText().toString(), PhoneAFriend.getInstance().getUsername(), subject_text.getText().toString(), true, key);
                    db.child("messages").child(key).setValue(sentMsg.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(NewMessageActivity.this,"Message sent to "+username_text.getText().toString(),Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(NewMessageActivity.this,"Unable to send message to "+username_text.getText().toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    //Method used to hide the keyboard when clicking outside the keyboard, we do this when focus is changed
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public boolean fieldsVerified(){
        //if(username_text.getText().toString().isEmpty())


        return true;
    }
}
