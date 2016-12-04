package seniordesign.phoneafriend.updateSettings;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;

/**
 * Created by REB.
 */

public class updateEmail extends AppCompatActivity {

    private EditText passText;
    private EditText emailText;
    private Button submitButton;
    private View.OnClickListener onClickListener;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private AuthCredential cred;
    private String userEmail;
    private DatabaseReference db;
    private ProgressDialog myProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_email_layout);

        /* Get Database Reference */
        db = FirebaseDatabase.getInstance().getReference();

        /* Get the authentication instance and set the current user */
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        /* get the input text fields for password and new email */
        passText = (EditText) findViewById(R.id.email_passverif);
        emailText = (EditText) findViewById(R.id.new_email);
        userEmail = currentUser.getEmail();

        //New Progress Dialog
        myProgress = new ProgressDialog(this);

        /* Set Listener for submit button */
        submitButton = (Button) findViewById(R.id.submit_email);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make sure fields aren't empty
                if(TextUtils.isEmpty(passText.getText().toString()) || TextUtils.isEmpty(emailText.getText().toString()) ){
                    //if empty display message
                    Toast.makeText(updateEmail.this,"Error: Must Input Current Password and new Email",Toast.LENGTH_LONG).show();
                }
                else {
                    //if no error, make user credentials from email and inputted password
                    //we need to verify user before making a change
                    myProgress.setMessage("Changing your email...");
                    myProgress.setCancelable(false);
                    myProgress.show();
                    cred = EmailAuthProvider
                            .getCredential(userEmail, passText.getText().toString());
                    //pass credentials into function that begins process for change
                    reauthChange(cred);
                }
            }
        };
        submitButton.setOnClickListener(onClickListener);

    }

    private void reauthChange(AuthCredential cred){

        // Prompt the user to re-provide their sign-in credentials
        //use credentials passed into the function
        currentUser.reauthenticate(cred)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.v("ReAuth attempt : " , "Completed");
                        if(task.isSuccessful()){
                            Log.d("ReAuth Successful", "User re-authenticated.");
                            processChange();
                        }
                        else {
                            Log.d("ReAuth Unsuccessful", "User not re-authenticated.");
                            Log.v("  Failure reason ", task.getException().toString());
                            String[] errorString = task.getException().toString().split(":");
                            myProgress.dismiss();
                            Toast.makeText(updateEmail.this,"Error: "+errorString[1],Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    private void processChange(){
        //Update User Email

        currentUser.updateEmail(emailText.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //If success change database email as well
                            Log.d("Email Change", "User email address updated.");
                            db.child("users").child(PhoneAFriend.getInstance().getUserKey()).child("useremail").setValue(emailText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //on success go back to previous screen trough finish
                                        myProgress.dismiss();
                                        Toast.makeText(updateEmail.this,"Email updated, may take some time for change to refresh on settings!",Toast.LENGTH_LONG).show();
                                        finish();
                                    }else{
                                        myProgress.dismiss();
                                        Toast.makeText(updateEmail.this,"Error: email changed for login, but not database",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            //If failure, display error message
                            Log.d("Email Change", "User email not updated.");
                            Log.v("  Failure reason ", task.getException().toString());
                            String[] errorString = task.getException().toString().split(":");
                            myProgress.dismiss();
                            Toast.makeText(updateEmail.this,"Error: "+errorString[1],Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    }
