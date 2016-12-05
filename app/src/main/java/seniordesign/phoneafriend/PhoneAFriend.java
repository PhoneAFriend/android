package seniordesign.phoneafriend;

import android.app.Application;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import seniordesign.phoneafriend.contacts.Contacts;
import seniordesign.phoneafriend.contacts.contactListAdapter;
import seniordesign.phoneafriend.messaging.InboxListAdapter;
import seniordesign.phoneafriend.messaging.Message;

/**
 * Created by The Alex on 9/25/2016.
 */
public class PhoneAFriend extends Application {

    private String username;
    private String userKey;
    private ArrayList<String> contactDisplayList = new ArrayList<>();
    private contactListAdapter contactAdapt; //This will mostly serve as a pointer
    private InboxListAdapter inboxAdapt;    //another pointer
    private ArrayList<Contacts> activeContacts = new ArrayList<>();
    private ArrayList<Contacts> inactiveContacts = new ArrayList<>();
    private ArrayList<Message> receivedMessages = new ArrayList<>();

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

    /* Methods for getting and Setting the String Value that contains our logged in users, username*/
    public String getUsername(){
        return username;
    }
    public String getUserKey() {return userKey;}

    public void setUsername(String newUsername){
        this.username = newUsername;
    }
    public void setUserKey(String key) {this.userKey = key; }
    /* end of setter and getter methods for global username */

    /*Methods For the String ArrayList used to display Contact Usernames in contacts Section */
    //Method to get the DisplayList
    public ArrayList<String> getContactDisplayList(){
        return contactDisplayList;
    }
    //Method to add to the Display List
    public void addDisplayContact(String contactUsername){
        contactDisplayList.add(contactUsername);
    }
    //Method to remove from the Display List
    public void removeContact(String contactUsername){
        contactDisplayList.remove(contactUsername);
    }
    //Method to sort Display List in Alphabetic order (ignore case)
    public void sortContactDiplayList(){
        Collections.sort(contactDisplayList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
    }
    //Method to completely Clear the Display List
    public void clearContactList(){
        contactDisplayList.clear();
    }

    /*Dark Magic Code used to reference our contactListAdapter outside of the fragment where it is created*/
    //We want a pointer to our adapter that will handle our contact username strings, also there is an adapter for messages
    public void setContactAdapt(contactListAdapter theAdapter){
        contactAdapt = theAdapter;
    }
    public void setInboxAdapt( InboxListAdapter theAdapter) { inboxAdapt = theAdapter; }
    //when we add or remove a contact, lets notify the adapter of that change
    public void notifyContactListChange(){
        contactAdapt.notifyDataSetChanged();
    }
    public void notifyInboxListChange() {inboxAdapt.notifyDataSetChanged();}
    /*End of dark magic code*/

    /* End of Methods for contactDisplay List */

    /* Methods for using our active and inactive contact lists */
    //get active contacts list
    public ArrayList<Contacts> getActiveContacts(){ return activeContacts; }
    public ArrayList<Contacts> getInactiveContacts() { return inactiveContacts; }
    public void clearActive() { activeContacts.clear(); }
    public void clearInactive() { inactiveContacts.clear(); }

    /* Methods for accessing received messages */
    public ArrayList<Message> getReceivedMessages() {return receivedMessages;}
    public void clearReceivedMessages() { receivedMessages.clear();}



    public void onDestroy(){

    }

}
