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

import seniordesign.phoneafriend.R;

public class updatePassword extends AppCompatActivity {

    private EditText curPass;
    private EditText newPass;
    private EditText confPass;
    private Button submitButton;
    private View.OnClickListener onClickListener;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private AuthCredential cred;
    private String userEmail;
    private ProgressDialog myProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password_layout);

        /* Get the edit fields */
        curPass = (EditText) findViewById(R.id.current_password);
        newPass = (EditText) findViewById(R.id.new_pass_text);
        confPass = (EditText) findViewById(R.id.confirm_pass_text);

        //Make progress dialog
        myProgress = new ProgressDialog(this);

        /* Instantiate Firebase Variables */
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        userEmail = currentUser.getEmail();

        /* Instantiate submit button and set a listener for it */
        submitButton = (Button) findViewById(R.id.submit_pass);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(curPass.getText().toString()) || TextUtils.isEmpty(newPass.getText().toString()) || TextUtils.isEmpty(confPass.getText().toString()) ) {
                    Toast.makeText(updatePassword.this, "Error: Must fill out current, new and confirmation password fields!",Toast.LENGTH_LONG).show();
                }
                else{
                    if(newPass.getText().toString().equals(confPass.getText().toString())) {
                        cred = EmailAuthProvider
                                .getCredential(userEmail, curPass.getText().toString());
                        myProgress.setMessage("Updating your password...");
                        myProgress.setCancelable(false);
                        myProgress.show();
                        reauthChange(cred);
                    }
                    else{
                        Toast.makeText(updatePassword.this, "Error: Passwords don't match!",Toast.LENGTH_LONG).show();
                    }
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
                            Toast.makeText(updatePassword.this,"Error: "+errorString[1],Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    private void processChange(){
        //Update User Password
        currentUser.updatePassword(newPass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("UPDATE PASSWORD", "User password updated.");
                            myProgress.dismiss();
                            finish();
                        }
                        else{
                            Log.d("UPDATE PASSWORD", "User password not updated.");
                            Log.v("  Failure reason ", task.getException().toString());
                            String[] errorString = task.getException().toString().split(":");
                            myProgress.dismiss();
                            Toast.makeText(updatePassword.this,"Error: "+errorString[1],Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

}
