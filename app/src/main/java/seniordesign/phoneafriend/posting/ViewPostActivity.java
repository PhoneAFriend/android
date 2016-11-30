package seniordesign.phoneafriend.posting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.messaging.NewMessageActivity;

/**
 * Created by The Alex on 10/7/2016.
 */

public class ViewPostActivity extends AppCompatActivity {

    private EditText questionTitleView;
    private EditText questionTextView;
    private EditText postedByView;
    private EditText subjectView;
    private String imageUrl;
    private Button imageButton;
    private Button reply_button;
    private Bundle recvExtras;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        recvExtras = getIntent().getExtras();

        questionTextView = (EditText) findViewById(R.id.viewPostBody);
        questionTextView.setKeyListener(null);
        questionTextView.setText(recvExtras.getString("questionText"));

        questionTitleView = (EditText) findViewById(R.id.viewPostTitle);
        questionTitleView.setKeyListener(null);
        questionTitleView.setText(recvExtras.getString("questionTitle"));

        postedByView = (EditText) findViewById(R.id.viewPostAuthor);
        postedByView.setKeyListener(null);
        postedByView.setText(recvExtras.getString("postedBy"));

        subjectView = (EditText) findViewById(R.id.subject_text);
        subjectView.setKeyListener(null);
        subjectView.setText(recvExtras.getString("subject"));

        imageUrl = recvExtras.getString("questionImageURL");
        System.out.println(imageUrl);

        reply_button = (Button) findViewById(R.id.second_button);
        reply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make an intent to create a new message
                Intent newMsg = new Intent(getApplicationContext(), NewMessageActivity.class);
                //Save the data we will need in the next activity, in this case we save username_text because it holds the username of the user that
                //sent us the current message, also we send the subject
                newMsg.putExtra("recipient",postedByView.getText().toString());
                newMsg.putExtra("subject",questionTitleView.getText().toString());
                //Start the new message activity
                startActivity(newMsg );
            }
        });


        imageButton = (Button) findViewById(R.id.view_image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUrl.equals("None")){
                    Toast.makeText(ViewPostActivity.this,"This question does not have an image!",Toast.LENGTH_LONG).show();
                }else {
                    //Make an intent to view the image
                    Intent viewImage = new Intent(getApplicationContext(), viewPostImage.class);
                    viewImage.putExtra("imageURL", imageUrl);
                    startActivity(viewImage);
                }
            }
        });

        if(imageUrl.equals("None")){
            imageButton.setVisibility(View.GONE);
        }



    }
}
