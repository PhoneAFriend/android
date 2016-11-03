package seniordesign.phoneafriend;

import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by The Alex on 9/25/2016.
 */
public class PhoneAFriend extends Application {

    private String username;

    static PhoneAFriend myAppInstance;
    public PhoneAFriend() {
        myAppInstance = this;
    }

    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
    }

    public static PhoneAFriend getInstance() {
        return myAppInstance;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String newUsername){
        this.username = newUsername;
    }

    public void onDestroy(){

    }

}
