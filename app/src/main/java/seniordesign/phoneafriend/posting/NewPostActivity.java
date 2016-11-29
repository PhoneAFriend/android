package seniordesign.phoneafriend.posting;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewGroupCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import seniordesign.phoneafriend.Manifest;
import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.messaging.NewMessageActivity;

public class NewPostActivity extends AppCompatActivity {

    private DatabaseReference db;
    private EditText titleText;
    private EditText subjectText;
    private EditText bodyText;
    private EditText imagePath;
    private Button captureButton;
    private Button addButton;
    private Button resetImg;
    private Button postButton;
    private View.OnClickListener onClickListener;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private Uri captureUri;
    private StorageReference myStorageRef;
    private ProgressDialog myProgress;

    private final String[] subjects = {"Math", "Science", "Computer Science", "Writing", "Other" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        db = FirebaseDatabase.getInstance().getReference();
        myStorageRef = FirebaseStorage.getInstance().getReference();
        myProgress = new ProgressDialog(this);
        myProgress.setCancelable(false);
        //Get the EditText field for our questions title
        titleText = (EditText) findViewById(R.id.newPostTitle);

        //Get the EditText for our subject, we enable it in a way that it works with a numberpicker custom dialog
        subjectText = (EditText) findViewById(R.id.subject_text);
        subjectText.setKeyListener(null);
        subjectText.setFocusable(false);
        subjectText.setClickable(true);
        subjectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickSubject();
            }
        });

        //Get the content text EditText of our question
        bodyText = (EditText) findViewById(R.id.newPostBody);

        //Get EditText for imagePath
        imagePath = (EditText) findViewById(R.id.image_path_text);
        imagePath.setKeyListener(null);

        //Get the image buttons
        captureButton = (Button) findViewById(R.id.capture_image);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });

        addButton = (Button) findViewById(R.id.add_image);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Select an image from your gallery
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_REQUEST_CODE);
            }
        });

        //Reset imagePath to None
        resetImg = (Button) findViewById(R.id.reset_img);
        resetImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imagePath.getText().toString().equals("None")) {
                    imagePath.setText("None");
                    Toast.makeText(NewPostActivity.this, "Image reset", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Get the post button and set its listeners
        postButton = (Button) findViewById(R.id.newPost_postButton);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPost();
            }
        };
        postButton.setOnClickListener(onClickListener);



        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            captureUri = data.getData();
            imagePath.setText(captureUri.getPath());
            Toast.makeText(NewPostActivity.this,"Image Selected",Toast.LENGTH_SHORT).show();
        }

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK ){
            captureUri = data.getData();
            imagePath.setText(captureUri.getPath());
            Toast.makeText(NewPostActivity.this,"Image Selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void submitPost(){
        final String key = db.child("posts").push().getKey();

        if(!TextUtils.isEmpty(titleText.getText()) && !TextUtils.isEmpty(bodyText.getText()) && !TextUtils.isEmpty(subjectText.getText()))
        {

            if(imagePath.getText().toString().equals("None")){
                //Post a post that doesn't have an image
                //Create the post
                Post post = new Post(titleText.getText().toString() , bodyText.getText().toString() , PhoneAFriend.getInstance().getUsername(), subjectText.getText().toString(), imagePath.getText().toString(), key);
                //Start the Spinner dialog
                myProgress.setMessage("Uploading Post...");
                myProgress.show();
                //Post to database and wait until done
                db.child("posts").child(key).setValue(post.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //On success, hide dialog, diplay a message, and finish activity
                            myProgress.hide();
                            Toast.makeText(NewPostActivity.this,"Question has been posted!",Toast.LENGTH_LONG).show();
                            finish();
                        }else{
                            //On failure, stop spinner and display error
                            myProgress.hide();
                            Toast.makeText(NewPostActivity.this,"Error: Unable to post question,try again later!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }else{
                //If a post has an image, we must upload the image to firebase storage before posting
                StorageReference filePath = myStorageRef.child("images").child(key);
                myProgress.setMessage("Uploading Post Image, this may take a while...");
                myProgress.show();
                filePath.putFile(captureUri).addOnSuccessListener(NewPostActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        myProgress.setMessage("Uploading Post");
                        String imageURL = taskSnapshot.getDownloadUrl().toString();
                        Log.d("Up Image is ",imageURL);
                        Post post = new Post(titleText.getText().toString() , bodyText.getText().toString() , PhoneAFriend.getInstance().getUsername(), subjectText.getText().toString(), imageURL, key);
                        myProgress.setMessage("Uploading Post...");
                        myProgress.show();
                        db.child("posts").child(key).setValue(post.toMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    myProgress.hide();
                                    Toast.makeText(NewPostActivity.this,"Question has been posted!",Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    myProgress.hide();
                                    Toast.makeText(NewPostActivity.this,"Error: Unable to post question,try again later!",Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                }).addOnFailureListener(NewPostActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myProgress.hide();
                        Toast.makeText(NewPostActivity.this,"Error: Unable to upload post image! Try again later, or remove image!",Toast.LENGTH_LONG).show();
                    }
                });


            }
            //Toast.makeText(NewPostActivity.this,"well then " + subjectText.getText()+" "+key,Toast.LENGTH_LONG).show();

           /* Post post = new Post(titleText.getText().toString() , bodyText.getText().toString() , currentUser.getUid().toString());
            db.child("posts").child(post.getPostId()).setValue(post.toMap());
            Intent postListIntent = new Intent(this , PostListActivity.class);
            titleText.setText("");
            bodyText.setText("");
            startActivity(postListIntent);*/
        }else{
            Toast.makeText(NewPostActivity.this,"Title, Subject, and Content cannot be empty!",Toast.LENGTH_LONG).show();
        }


    }

    public void pickSubject()
    {
        //Create a new dialog that will help select the subject from a pre-set list
        final Dialog d = new Dialog(NewPostActivity.this);
        d.setTitle("Pick a Subject");
        d.setContentView(R.layout.numberpicker_dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        //Create a numberpicker to be used as a holder for our string values
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        //Set the Max and Min values of the picker (We have 5 subjects, these are essential the index values)
        np.setMaxValue(4);
        np.setMinValue(0);
        //Set the displayed values to be those of our list
        np.setDisplayedValues( subjects );
        //Disable the soft keyboard for the numberpicker
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //No wrapping around our selections
        np.setWrapSelectorWheel(false);
        //What to do when b1 or the set button is clicked
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //If user clicks set, then set the EditText to the correct subject from our list
                subjectText.setText(subjects[np.getValue()]);
                d.dismiss();
            }
        });
        //What to do when b2 or the cancel button is clicked
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //If the user cancels, then dismiss the dialog
                d.dismiss();
            }
        });
        //Show the dialog
        d.show();


    }



}
