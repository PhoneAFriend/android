package seniordesign.phoneafriend.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.main_screen;
import seniordesign.phoneafriend.posting.NewPostActivity;


public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private EditText emailText;
    private EditText passText;
    private TextView errorText;
    private Button button;
    private View.OnClickListener onClickListener;
    private Intent intent;
    private Toast toast;

    /* Delete Later */
    private Button alexButton;
    private View.OnClickListener alexClickListener;
    /*              */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailText = (EditText) findViewById(R.id.signin_emailText);
        passText = (EditText) findViewById(R.id.signin_passwordText);
        intent = new Intent(this, main_screen.class);
        errorText = (TextView) findViewById(R.id.signin_errorText);
        button = (Button) findViewById(R.id.login_button);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailText.getText().toString().equals("") && !passText.getText().toString().equals("")) {
                    login(emailText.getText().toString(), passText.getText().toString());
                }else{
                    errorText.setText("Error: No email or no password given!");
                }

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

        /* Delete Later */
        alexButton = (Button) findViewById(R.id.signin_alexIn);
        alexClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alexIn();
            }
        };
        alexButton.setOnClickListener(alexClickListener);
        /*              */

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
                            startActivity(intent);
                        }else{
                            Log.v("Sign in status:", "Failure");
                            passText.setText("");
                            errorText.setText("Error: Your email or password was incorrect!");

                        }
                    }
                });

    }
    /* Delete Later */
    private void alexIn(){
        auth.signInWithEmailAndPassword("thealex123@gmail.com" , "ilovethisapp" )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v("Sign in attempt : " , "Completed");
                        if(task.isSuccessful()){
                            Log.v("Sign in status:" , "Success");
                            startActivity(intent);
                        }else{
                            Log.v("Sign in status:", "Failure");
                            passText.setText("");
                        }
                    }
                });
    }
    /*              */


}
