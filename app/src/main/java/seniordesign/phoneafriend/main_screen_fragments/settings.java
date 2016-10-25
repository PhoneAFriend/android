package seniordesign.phoneafriend.main_screen_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.authentication.SignIn;
import seniordesign.phoneafriend.updateSettings.updateEmail;

public class settings extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    //private Intent changeName;
    private Intent changeEmail;
    private Intent changePass;

    private RelativeLayout userSection;
    private RelativeLayout emailSection;
    private RelativeLayout passSection;
    private Button logoutButton;

    private FirebaseAuth.AuthStateListener authListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_layout, container, false);

        //get the authentication instance and set the current user
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        /* Updating Username Section */

        /* Set Up TextView which will display username Here */

        /* Set Up Listener for when Section is Pressed */
        userSection = (RelativeLayout) view.findViewById(R.id.update_username);
        View.OnClickListener usernameClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeUsername();
            }
        };
        userSection.setOnClickListener(usernameClickListener);

        /* Updating Email Section */

        /* Set Up TextView which will display user email Here */
        TextView emailText = (TextView) view.findViewById(R.id.email_text); //get the TextView
        emailText.setText(currentUser.getEmail()); //set TextView to email from firebase

        /* Set Up Listener for when Section is Pressed */
        emailSection = (RelativeLayout) view.findViewById(R.id.update_email); //Get section where contents are located
        View.OnClickListener emailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail();//on click preform function
            }
        };
        emailSection.setOnClickListener(emailClickListener);

        /* Updating Password Section */
        /* Set Up Listener for when Section is Pressed */
        passSection = (RelativeLayout) view.findViewById(R.id.update_password); //Get section where contents are located
        View.OnClickListener passClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();//on click preform function
            }
        };
        passSection.setOnClickListener(passClickListener);

        /* Logging Out */
        /* Set Up Listener for when Section is Pressed */
        logoutButton = (Button) view.findViewById(R.id.logout_button); //Get section where contents are located
        View.OnClickListener logoutClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();//on click preform function
            }
        };
        logoutButton.setOnClickListener(logoutClickListener);

        authListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.v("AuthStateChanged" , " User ID "+user.getUid() + " signed in");
                }else{
                    Log.v("AuthStateChanged" , " User signed out");
                    //If we detect the user logged out, then go back to the signin screen
                    startActivity(new Intent(getActivity(), SignIn.class));
                }
            }
        };


        return view;
    }

    private void changeUsername(){
        Toast.makeText(getActivity(), "Change Username", Toast.LENGTH_LONG ).show();
    }

    private void changeEmail(){
        //Take me to the screen where I can update my email
        startActivity(new Intent(getActivity(), updateEmail.class));
    }

    private void changePassword(){
        Toast.makeText(getActivity(), "Change Pass", Toast.LENGTH_LONG ).show();
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

    private void logOut(){
        //call the signout method
        auth.signOut();
    }

}
