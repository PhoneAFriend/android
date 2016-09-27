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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private EditText emailText;
    private EditText passText;
    private Button button;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        emailText = (EditText) findViewById(R.id.signin_emailText);
        passText = (EditText) findViewById(R.id.signin_passwordText);
        button = (Button) findViewById(R.id.login_button);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(emailText.getText().toString() , passText.getText().toString());
            }
        };
        button.setOnClickListener(onClickListener);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.v("AuthStateChanged" , " User ID "+user.getUid() + " signed in");
                }else{
                    Log.v("AuthStateChanged" , " User signed out");
                }
            }
        };
    }

    protected void gotoSignUp(View view){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }

    public void login(String email , String password){
        auth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v("Sign in attempt : " , "Completed");
                        if(task.isSuccessful()){
                            Log.v("Sign in status:" , "Success");
                        }else{
                            Log.v("Sign in status:", "Failure");
                        }
                    }
                });
    }
}
