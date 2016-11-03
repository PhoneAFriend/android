package seniordesign.phoneafriend.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.main_screen;

public class SignUp extends AppCompatActivity {
    protected Firebase ref;
    protected DatabaseReference db;
    protected EditText emailText;
    protected EditText passText;
    protected EditText nameText;
    protected EditText confirmText;
    protected TextView errorText;
    protected Button button;
    protected SignUp thisContext;
    private boolean dupFlag; //true for dup found. false for no dup

    protected FirebaseAuth auth;
    protected FirebaseAuth.AuthStateListener authListener;
    private View.OnClickListener onClickListener;

    public static Intent intent;

    //Delete Later
    protected Button testButton;
    protected View.OnClickListener testClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ref = new Firebase("https://phoneafriend-7fb6b.firebaseio.com");
        emailText = (EditText) findViewById(R.id.signup_emailText);
        passText = (EditText) findViewById(R.id.signup_passwordText);
        confirmText = (EditText) findViewById(R.id.signup_passwordConfirm);
        nameText = (EditText) findViewById(R.id.signup_usernameText);
        errorText = (TextView) findViewById(R.id.signup_errorText);
        intent = new Intent(this, main_screen.class);
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        dupFlag = false;

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

        //Delete Later
        testButton = (Button) findViewById(R.id.signup_test);
        testClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkDuplicateUsername("TheAlex");
            }
        };
        testButton.setOnClickListener(testClickListener);
    }
    @Override
    public void onStart() {
        super.onStart();
        //auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if(authListener != null) {
            //auth.removeAuthStateListener(authListener);
        }
        emailText.setText("");
        errorText.setText("");
        passText.setText("");
        nameText.setText("");
        confirmText.setText("");
    }

    protected void createUser(View view){
        String eString  = emailText.getText().toString();
        if(!TextUtils.isEmpty(emailText.getText().toString()) &&
                !TextUtils.isEmpty(passText.getText().toString()) &&
                !TextUtils.isEmpty(confirmText.getText().toString()) &&
                !TextUtils.isEmpty(nameText.getText().toString())
                ) {

            Log.v("SignUP: Pass Null check", "Pass");
            checkDuplicateandMake(nameText.getText().toString());
            //makeUser();
        }
            else{
                Toast.makeText(SignUp.this, "Error: One or more fields have been left blank", Toast.LENGTH_LONG).show();
            }

        return;
    }

    /* Returns true on pass, false on failure */
    private void checkDuplicateandMake(final String username){
        //Query the database to see if there is an entry with the matching username
        db.child("users").orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    //if there is a matching entry diplay error
                    Toast.makeText(SignUp.this, "Error: Username already taken", Toast.LENGTH_LONG).show();
                }else{
                    Log.v("Test Result: " , "DATASNAPSHOT IS NULL");
                    //if there was not, then make the new user
                    makeUser();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void makeUser(){
            //Get the password strings and compare them
            String pString = passText.getText().toString();
            String cString = confirmText.getText().toString();
            if (pString.equals(cString)) {
                Log.v("SignUP: Sign up check ", "Pass");
                //attempt to create user with email and password
                auth.createUserWithEmailAndPassword(emailText.getText().toString(), passText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.v("createUser complete", "status: " + task.isSuccessful());
                                if (task.isSuccessful()) {
                                    //if creation successful, post to users on database
                                    FirebaseUser user = auth.getCurrentUser();//get current user(should be newly created user)
                                    User thisUser = new User(user.getUid(), user.getEmail(), nameText.getText().toString());//Make new user object
                                    String key = db.child("users").push().getKey();//generate random key
                                    db.child("users").child(key).setValue(thisUser.toMap());//post to database
                                    ((PhoneAFriend) getApplication()).setUsername(thisUser.getUsername());// set up the global username var for our app
                                    startActivity(SignUp.intent);
                                }
                                else{
                                    Log.v("  Failure reason ", task.getException().toString());
                                    String[] errorString = task.getException().toString().split(":");
                                    Toast.makeText(SignUp.this,"Error: " + errorString[1], Toast.LENGTH_LONG).show();
                                }
                            }

                        });

            } else {
                Toast.makeText(SignUp.this, "Error: Your passwords did not match", Toast.LENGTH_LONG).show();
            }

    }


}
