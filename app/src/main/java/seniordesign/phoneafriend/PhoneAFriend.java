package seniordesign.phoneafriend;

import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by The Alex on 9/25/2016.
 */
public class PhoneAFriend extends Application {

    private String username;
    private ArrayList<String> contactList;

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

    public ArrayList<String> getContactList(){
        return contactList;
    }

    public void addContact(String contactUsername){
        contactList.add(contactUsername);
    }

    public void removeContact(String contactUsername){
        contactList.remove(contactUsername);
    }

    public void sortContactList(){
        Collections.sort(contactList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
    }

    public void onDestroy(){

    }

}
