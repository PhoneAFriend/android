package seniordesign.phoneafriend.posting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.messaging.NewMessageActivity;

public class ViewCurrentUserPost extends AppCompatActivity {

    private EditText questionTitleView;
    private EditText questionTextView;
    private EditText postedByView;
    private EditText subjectView;
    private String imageUrl;
    private Button imageButton;
    private Button delete_button;
    private Button answer_button;
    private Bundle recvExtras;
    private String answered;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        recvExtras = getIntent().getExtras();
        db = FirebaseDatabase.getInstance().getReference();

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

        delete_button = (Button) findViewById(R.id.first_button);
        delete_button.setVisibility(View.VISIBLE);
        delete_button.setBackgroundResource(R.drawable.custom_red_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewCurrentUserPost.this,"Cannot Delete Posts Yet!",Toast.LENGTH_LONG).show();
                /*db.child("posts").child(recvExtras.getString("key")).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(!recvExtras.getString("questionImageURL").equals("None"))
                                //deleteStoredImage(recvExtras.getString("questionImageURL"));
                            //finish();
                        }else{
                            Toast.makeText(ViewCurrentUserPost.this,"Error: could not delete post!",Toast.LENGTH_LONG).show();
                        }
                    }
                });*/

            }
        });

        answered = recvExtras.getString("answered");
        answer_button = (Button) findViewById(R.id.second_button);
        if(answered.equals("true")){
            answer_button.setText("Set Unanswered");
        }else {
            answer_button.setText("Set Answered");
        }

        answer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(answered.equals("true")){
                    db.child("posts").child(recvExtras.getString("key")).child("answered").setValue("false");
                    answer_button.setText("Set Answered");
                    answered = "false";
                }else {
                    db.child("posts").child(recvExtras.getString("key")).child("answered").setValue("true");
                    answer_button.setText("Set Unanswered");
                    answered = "true";
                }
            }
        });


        imageButton = (Button) findViewById(R.id.view_image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUrl.equals("None")){
                    Toast.makeText(ViewCurrentUserPost.this,"This question does not have an image!",Toast.LENGTH_LONG).show();
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

    /*public void deleteStoredImage(String Url){
        StorageReference myStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference deleteRef = myStorageRef.child("images").child(recvExtras.getString("key"));
        // Delete the file
        deleteRef.delete().addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });
    }*/
}
