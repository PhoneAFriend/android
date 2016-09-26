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

    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
    }
}
