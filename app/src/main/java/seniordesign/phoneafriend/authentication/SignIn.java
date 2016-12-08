package seniordesign.phoneafriend.authentication;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.contacts.Contacts;
import seniordesign.phoneafriend.main_screen;
import seniordesign.phoneafriend.messaging.Message;
import seniordesign.phoneafriend.posting.NewPostActivity;


public class SignIn extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference db;
    private EditText emailText;
    private EditText passText;
    private Button button;
    private View.OnClickListener onClickListener;
    private Intent intent;
    private Toast toast;
    private ProgressDialog myDialog;

    /* Delete Later */
    private Button alexButton;
    private View.OnClickListener alexClickListener;
    /*              */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        myDialog = new ProgressDialog(this);
        db = FirebaseDatabase.getInstance().getReference();
        emailText = (EditText) findViewById(R.id.signin_emailText);
        passText = (EditText) findViewById(R.id.signin_passwordText);
        intent = new Intent(this, main_screen.class);
        button = (Button) findViewById(R.id.login_button);
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailText.getText().toString().equals("") && !passText.getText().toString().equals("")) {
                    login(emailText.getText().toString(), passText.getText().toString());
                }else{
                    Toast.makeText(SignIn.this,"Error: No email or no password given!", Toast.LENGTH_LONG).show();
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
        final String neededEmail = email;
        auth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v("Sign in attempt : " , "Completed");
                        if(task.isSuccessful()){
                            Log.v("Sign in status:" , "Success");
                            setAppUsernameContactsAndGo(neededEmail);
                            //startActivity(intent);
                        }else{
                            Log.v("Sign in status:", "Failure");
                            passText.setText("");
                            Toast.makeText(SignIn.this,"Error: Your email or password was incorrect!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
    /* Delete Later */
    private void alexIn(){


        myDialog.setMessage("Getting Username...");
        myDialog.setCancelable(false);
        myDialog.show();
        auth.signInWithEmailAndPassword("thealex123@gmail.com" , "password123" )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.v("Sign in attempt : " , "Completed");
                        if(task.isSuccessful()){
                            Log.v("Sign in status:" , "Success");
                            db.child("users").orderByChild("useremail").equalTo("thealex123@gmail.com").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if( dataSnapshot.getChildrenCount() == 1)
                                        for (DataSnapshot u : dataSnapshot.getChildren()){
                                            User s = (User) u.getValue(User.class);
                                            ((PhoneAFriend) getApplication()).setUsername(s.getUsername());
                                            PhoneAFriend.getInstance().setUserKey(u.getKey());
                                        }
                                    setUserContactsAndGo(((PhoneAFriend) getApplication()).getUsername());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(SignIn.this,"There was a problem getting dev username", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Log.v("Sign in status:", "Failure");
                            myDialog.dismiss();
                            Toast.makeText(SignIn.this,"There was a problem signing you in, Try Again Later!", Toast.LENGTH_LONG).show();
                            passText.setText("");
                        }
                    }
                });
    }

    private void setAppUsernameContactsAndGo(String email){
        myDialog.setMessage("Getting Username...");
        myDialog.setCancelable(false);
        myDialog.show();
        db.child("users").orderByChild("useremail").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if( dataSnapshot.getChildrenCount() == 1)
                    for (DataSnapshot u : dataSnapshot.getChildren()){
                        User s = (User) u.getValue(User.class);
                        ((PhoneAFriend) getApplication()).setUsername(s.getUsername());
                        PhoneAFriend.getInstance().setUserKey(u.getKey());
                    }
                setUserContactsAndGo(((PhoneAFriend) getApplication()).getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                myDialog.dismiss();
                Toast.makeText(SignIn.this,"There was a problem getting you username!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUserContactsAndGo(final String currentUsername){
        myDialog.setMessage("Getting Contacts...");
        //Clear all contact lists incase there was leftover information
        PhoneAFriend.getInstance().clearContactList();
        PhoneAFriend.getInstance().clearActive();
        PhoneAFriend.getInstance().clearInactive();
        //Query username1 values that are equal to our currentUsername in search of contacts
        db.child("Contacts").orderByChild("username1").equalTo(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Check to see if the snapshot is null, this means there are no contacts where current user is username1
                if (dataSnapshot != null) {
                    //If we had data, lets find the element where u12 is true, meaning they are a contact
                    for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                        //Create a temp user so we can grab the data and get u12
                        Contacts temp = new Contacts(userSnap);
                        //Lets check if username2 is a contact by checking if u12 is true
                            if(temp.isU12()) {
                                //This is a contact! Lets print a log message
                                Log.d("Contact Res ","We got one on U12! "+temp.getUsername2());
                                //add username to our display list
                                ((PhoneAFriend) getApplication()).addDisplayContact(temp.getUsername2());
                                //add contact to active contacts
                                PhoneAFriend.getInstance().getActiveContacts().add(temp);
                            }else{
                                //if u12 was false, then they are currently not a contact, we save
                                //this contact value into an inactive contacts list in case we want to add
                                //them later
                                PhoneAFriend.getInstance().getInactiveContacts().add(temp);
                            }

                    }
                }
                //We nest to deal with Firebase being an asynchronous beast!
                //Therefore we wait until we've checked where username1 is the current username
                //Query username2 values that are equal to our currentUsername in search of contacts
                db.child("Contacts").orderByChild("username2").equalTo(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check to see if the snapshot is null, this means there are no contacts where current user is username2
                        if (dataSnapshot != null) {
                            //If we had data, lets find the element where u21 is true, meaning they are a contact
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                //Create a temp user so we can grab the data and get u21
                                Contacts temp = new Contacts(userSnap);
                                //Lets check if username2 is a contact by checking if u12 is true
                                if(temp.isU21()) {
                                    //This user is a contact of our current user lets print a log message
                                    Log.d("Contact res ","We got one! on U21 "+temp.getUsername1());
                                    //add username to display list
                                    ((PhoneAFriend) getApplication()).addDisplayContact(temp.getUsername1());
                                    //add contact to active list
                                    PhoneAFriend.getInstance().getActiveContacts().add(temp);
                                }else{
                                    //if u21 was false add to inactive list
                                    PhoneAFriend.getInstance().getInactiveContacts().add(temp);
                                }


                            }
                        }
                        //Once we are done getting contacts, lets get any received messages!
                        //sort the list first
                        PhoneAFriend.getInstance().sortContactDiplayList();
                        //method to get received messages
                        setReceivedMessagesAndGo(currentUsername);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        myDialog.dismiss();
                        Toast.makeText(SignIn.this,"There was a problem getting your contacts, Try Again Later!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                myDialog.dismiss();
                Toast.makeText(SignIn.this,"There was a problem getting your contacts, Try Again Later!", Toast.LENGTH_LONG).show();
            }
        });




        }

    public void setReceivedMessagesAndGo(final String currentUsername){
        myDialog.setMessage("Getting your messages...");
        //Clear the global list which stores received messages
        PhoneAFriend.getInstance().clearReceivedMessages();
        //Query the database based on messages where user signing in is the recipient
        db.child("messages").orderByChild("recipientUsername").equalTo(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    //If we had data, lets add it to our received messages list
                    for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                        Message m = new Message(dataSnap);
                        Log.d("New Message ","It's from "+m.getSenderUsername());
                        //add to list
                        PhoneAFriend.getInstance().getReceivedMessages().add(0,m);

                    }
                }
                //When we finish getting all messages, got to the main menu
                Go();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                myDialog.dismiss();
                Toast.makeText(SignIn.this,"There was a problem getting received messages, Try Again Later!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void Go(){
        //Go to the main menu activity
        myDialog.dismiss();
        startActivity(intent);
        finish();//finish activity so it is removed from our backstack
    }
    /*              */


}
