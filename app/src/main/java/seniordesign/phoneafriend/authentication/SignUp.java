package seniordesign.phoneafriend.authentication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        intent = new Intent(this, SignIn.class);
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
                checkDupilcateUsername("TheAlex");
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
        String cString = null;
        String pString = null;
        String eString  = emailText.getText().toString();
        if(!emailText.getText().toString().equals("") ||
                !passText.getText().toString().equals("") ||
                !confirmText.getText().toString().equals("") ||
                !nameText.getText().toString().equals("")
                ) {
            pString = passText.getText().toString();
            cString = confirmText.getText().toString();
            Log.v("SignUP: Pass Null check" , "Pass" );
            checkDupilcateUsername(nameText.getText().toString());
            if(!dupFlag) {
                if (pString.equals(cString)) {
                    Log.v("SignUP: Sign up check ", "Pass");
                    auth.createUserWithEmailAndPassword(emailText.getText().toString(), passText.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.v("createUser complete", "status: " + task.isSuccessful());
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        User thisUser = new User(user.getUid().toString() , nameText.getText().toString());
                                        db.child("users").child("-"+user.getUid().toString()).setValue(thisUser.toMap());

                                        startActivity(SignUp.intent);
                                    }
                                    if (task.isSuccessful() == false) {
                                        Log.v("  Failure reason ", task.getException().toString());
                                        String[] errorString = task.getException().toString().split(":");
                                        errorText.setText("Error; " + errorString[1]);
                                    }
                                }

                            });

                } else {
                    errorText.setText("Error: Your passwords did not match");
                }
            }else{
                errorText.setText("Error: Username already taken");
                dupFlag = false;
            }
        }else{
            errorText.setText("Error: One or more fields have been left blank");
        }

        return;
    }

    /* Returns true on pass, false on failure */
    private void checkDupilcateUsername(final String username){
        HashMap<String , Object> queryResults;
        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    setDuplicateUsername(dataSnapshot , username);
                }else{
                    Log.v("Test Result: " , "DATASNAPSHOT IS NULL");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setDuplicateUsername(DataSnapshot dataSnapshot , String username){
        HashMap<String , Object> queryResults = (HashMap<String , Object>) dataSnapshot.getValue();
        Collection<Object> userCollection = queryResults.values();
        Iterator<Object> userIterator = userCollection.iterator();
        ArrayList<Object> userList = new ArrayList<>();
        while(userIterator.hasNext()){
            userList.add(userIterator.next());
        }
        HashMap<String , Object> userData = new HashMap<>();
        String tempUsername;
        for(int i = 0; i < userList.size(); i++){
            userData = (HashMap<String , Object>) userList.get(i);
            Log.v("UserData: " , userData.toString());
            tempUsername = (String) userData.get("username");
            if(tempUsername.equals(username)){
                Log.v("Duplicate found at: ", userData.toString());
                dupFlag = true;
                return;
            }
        }
    }
}
