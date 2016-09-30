package seniordesign.phoneafriend;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class SignUp extends AppCompatActivity {
    protected Firebase ref;
    protected EditText emailText;
    protected EditText passText;
    protected EditText confirmText;
    protected Button button;
    protected SignUp thisContext;

    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authListener;
    private View.OnClickListener onClickListener;

    public static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ref = new Firebase("https://phoneafriend-7fb6b.firebaseio.com");
        emailText = (EditText) findViewById(R.id.signup_emailText);
        passText = (EditText) findViewById(R.id.signup_passwordText);
        confirmText = (EditText) findViewById(R.id.signup_passwordConfirm);
        intent = new Intent(this, SignIn.class);
        auth = FirebaseAuth.getInstance();


        button = (Button) findViewById(R.id.signup_button);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser(view);
                Log.v("SignUp Button" , "Clicked; Attempting to create user");
            }
        };
        button.setOnClickListener(onClickListener);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged( FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("FirebaseAuth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("FirebaseAuth", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        thisContext = this;

    }
    @Override
    public void onStart(){
        super.onStart();
        //auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authListener != null) {
            //auth.removeAuthStateListener(authListener);
        }
    }

    protected void createUser(View view){
        String cString = null;
        String pString = null;
        String eString  = emailText.getText().toString();
        if(passText.getText() != null && confirmText.getText() != null) {
            pString = passText.getText().toString();
            cString = confirmText.getText().toString();
            Log.v("SignUP: Pass Null check" , "Pass" );
            if (emailText.getText() != null && pString.equals(cString) && passText.getText() != null) {
                Log.v("SignUP: Sign up check " , "Pass");
                auth.createUserWithEmailAndPassword(emailText.getText().toString() , passText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.v("createUser complete" , "status: " + task.isSuccessful());
                                if(task.isSuccessful()){
                                    startActivity(SignUp.intent);
                                }
                                if(task.isSuccessful() == false){
                                    Log.v("  Failure reason " , task.getException().toString());
                                }
                            }

                        });

            }
        }

        return;
    }
}
