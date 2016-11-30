package seniordesign.phoneafriend.posting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
    private String setAnsStr;
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

        /* Code for the delete button */
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

        //Create a dialog interface which will ask the user if they really want to remove a message
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button, toggle the answered value of the post
                        toggleAnswered();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked do nothing
                        break;
                }
            }
        };

        answered = recvExtras.getString("answered");//get the value for the posts answered field
        answer_button = (Button) findViewById(R.id.second_button);
        //Depending on the posts value for answered, have the set button display the correct text
        if(answered.equals("true")){
            setAnsStr = "Unanswered";
            answer_button.setText("Set Unanswered");
        }else {
            setAnsStr = "Answered";
            answer_button.setText("Set Answered");
        }

        answer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the Alert Dialog and Show it! If the user presses remove button on a contact
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCurrentUserPost.this);
                builder.setMessage("Are you sure you want to set post as "+setAnsStr+"?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


        imageButton = (Button) findViewById(R.id.view_image_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUrl.equals("None")){
                    Toast.makeText(ViewCurrentUserPost.this,"This question does not have an image!",Toast.LENGTH_LONG).show();
                }else {
                    //Make an intent to view the image, send the image url with the activity
                    Intent viewImage = new Intent(getApplicationContext(), viewPostImage.class);
                    viewImage.putExtra("imageURL", imageUrl);
                    startActivity(viewImage);
                }
            }
        });

        //If we don't have a valid imageURL hide the view image button
        if(imageUrl.equals("None")){
            imageButton.setVisibility(View.GONE);
        }

    }

    public void toggleAnswered(){

        //Based on if post was answered or no before, we toggle its value
        //We check to see that we could send update to the database, if we could
        //We finish and go back to our list, else we display a message
        if(answered.equals("true")){
            db.child("posts").child(recvExtras.getString("key")).child("answered").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        finish();
                    }else{
                        Toast.makeText(ViewCurrentUserPost.this,"Unable to set as unanswered, try again later...",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            db.child("posts").child(recvExtras.getString("key")).child("answered").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        finish();
                    }else{
                        Toast.makeText(ViewCurrentUserPost.this,"Unable to set as Answered, try again later...",Toast.LENGTH_LONG).show();
                    }
                }
            });
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
