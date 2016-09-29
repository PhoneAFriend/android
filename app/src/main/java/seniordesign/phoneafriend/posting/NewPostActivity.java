package seniordesign.phoneafriend.posting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import seniordesign.phoneafriend.R;

public class NewPostActivity extends AppCompatActivity {

    private DatabaseReference db;
    private EditText titleText;
    private EditText bodyText;
    private Button postButton;
    private View.OnClickListener onClickListener;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        db = FirebaseDatabase.getInstance().getReference();
        titleText = (EditText) findViewById(R.id.newPostTitle);
        bodyText = (EditText) findViewById(R.id.newPostBody);
        postButton = (Button) findViewById(R.id.newPost_postButton);
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPost();
            }
        };
        postButton.setOnClickListener(onClickListener);
    }

    private void submitPost(){
        if(!titleText.getText().toString().matches("") || !bodyText.getText().toString().matches("")){
            Post post = new Post(titleText.getText().toString() , bodyText.getText().toString() , currentUser.getUid().toString());
            db.child("posts").child(post.getPostId()).setValue(post.toMap());
        }
    }



}
