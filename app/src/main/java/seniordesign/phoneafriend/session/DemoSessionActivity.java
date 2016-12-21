package seniordesign.phoneafriend.session;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
/* This Activity is reached after pressing "Start a demo session" in the main screen.
    Here, user chooses to start a demo session or connect to an existing demo session
 */

public class DemoSessionActivity extends AppCompatActivity {
    private Button startButton;
    private EditText idEditText;
    private Button.OnClickListener startListener;
    private TextView.OnEditorActionListener idListener;
    private Intent sessionIntent;
    private Intent studentSessionIntent;
    private DatabaseReference db;
    private String receiverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_session);

        startButton = (Button) findViewById(R.id.demoSession_startSessionButton);
        idEditText = (EditText) findViewById(R.id.demoSession_editText);
        sessionIntent = new Intent(this , SessionActivity.class);
        studentSessionIntent = new Intent(this , StudentSessionActivity.class);

        startListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionIntent.putExtra("SENDER_NAME" , ((PhoneAFriend) getApplication()).getUsername() );
                sessionIntent.putExtra("POST_ID" , "Post Title");
                startActivity(sessionIntent);
            }
        };

        startButton.setOnClickListener(startListener);

        db = FirebaseDatabase.getInstance().getReference();

        idListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    db.child("Sessions").equalTo(idEditText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue()!= null){
                                studentSessionIntent.putExtra("RECEIVER_NAME" , ((PhoneAFriend) getApplication()).getUsername());
                                getApplicationContext().startActivity(studentSessionIntent);
                            }else{
                                Toast.makeText(DemoSessionActivity.this, "Can't find that Session ID!" , Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    return true;
                }
                return false;
            }
        };
    }
}
