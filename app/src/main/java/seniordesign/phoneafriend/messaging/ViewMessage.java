package seniordesign.phoneafriend.messaging;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        //Get data we will need in order to view the message
        recvExtras = getIntent().getExtras();

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





    }
}
