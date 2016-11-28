package seniordesign.phoneafriend.userSearch;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.authentication.User;
import seniordesign.phoneafriend.contacts.Contacts;
import seniordesign.phoneafriend.messaging.NewMessageActivity;

/**
 * Created by REB.
 */

public class searchUserListAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private ArrayList<String> values = new ArrayList<String>();
    private DatabaseReference db;

    public searchUserListAdapter(Context context, ArrayList<String> values, DatabaseReference db){
        this.context = context;
        this.values = values;
        this.db = db;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int pos) {
        return values.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    public DatabaseReference getDb(){
        return db;
    }

    @Override
    public View getView(final int position , View convertView , ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.user_search_list_element,null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.username_string);
        listItemText.setText(values.get(position));

        //Handle buttons and add onClickListeners
        Button msgBtn = (Button)view.findViewById(R.id.msg_btn);
        Button addBtn = (Button)view.findViewById(R.id.add_btn);

        msgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Go to the create message activity!
                //Toast.makeText(context,"MESSAGING " + values.get(position),Toast.LENGTH_LONG).show();
                //Make a new intent
                Intent newMsg = new Intent(context, NewMessageActivity.class);
                //Put the string of the username you want to send a message into a bundle in your intent
                newMsg.putExtra("recipient",values.get(position));
                //Start the new message activity
                context.startActivity(newMsg );

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //get the logged in users username
                final String currentUsername = PhoneAFriend.getInstance().getUsername();
                //check if user we are trying to add is already a contact
                if(PhoneAFriend.getInstance().getContactDisplayList().contains(values.get(position))){
                    Toast.makeText(context,"This user is already a contact!",Toast.LENGTH_LONG).show();
                }else{
                    //if not we iterate through our inactive list to see if a db entry exists, but we have just not
                    //added them yet! This is faster than multiple queries to the db
                    Iterator<Contacts> contactsIterator = PhoneAFriend.getInstance().getInactiveContacts().iterator();
                    Contacts temp;
                    while(contactsIterator.hasNext()){
                        temp = contactsIterator.next();
                        //check contacts where the user we are trying to add is username2
                        if(temp.getUsername2().equals(values.get(position)) && !temp.isU12()){
                            PhoneAFriend.getInstance().addDisplayContact(values.get(position)); //add to string display list
                            temp.setU12(true); //change u12 value to true
                            PhoneAFriend.getInstance().getActiveContacts().add(temp); //add it to our active contacts list
                            contactsIterator.remove(); //removes temp from inactive contacts
                            db.child("Contacts").child(temp.getKey()).child("u12").setValue(true); //post change to database
                            //possibly sort list here??
                            PhoneAFriend.getInstance().notifyContactListChange();//notify contact list adapter of change
                            Toast.makeText(context,"Q1 ADDED "+values.get(position)+" as a contact!",Toast.LENGTH_LONG).show();
                            return;//return because we are done
                        }

                        //check contacts where the user we are trying to add is username1
                        if(temp.getUsername1().equals(values.get(position)) && !temp.isU21()){
                            PhoneAFriend.getInstance().addDisplayContact(values.get(position)); //add to string display list
                            temp.setU21(true); //change u21 value to true
                            PhoneAFriend.getInstance().getActiveContacts().add(temp); //add it to our active contacts list
                            contactsIterator.remove(); //removes temp from inactive contacts
                            db.child("Contacts").child(temp.getKey()).child("u21").setValue(true); //push new value of u21 to database
                            //possibly sort list here??
                            PhoneAFriend.getInstance().notifyContactListChange();//notify contact list adapter of change
                            Toast.makeText(context,"Q2 ADDED "+values.get(position)+" as a contact!",Toast.LENGTH_LONG).show(); //display add message
                            return;//return because we are done
                        }

                    }

                //This special case will account for when two users are logged in at the same time and one of them adds the other first
                //assuming e were added by someone else first, query for where username1 is the other users username
                db.child("Contacts").orderByChild("username1").equalTo(values.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if( dataSnapshot.getChildrenCount() != 0){
                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                Contacts temp = new Contacts(data);
                                //check to make sure username2 is the current users username and that they are not a contact of username2
                                if(temp.getUsername2().equals(currentUsername) && !temp.isU21()){
                                    PhoneAFriend.getInstance().addDisplayContact(values.get(position)); //add to our string array
                                    PhoneAFriend.getInstance().getActiveContacts().add(temp); //add it to our active contacts list
                                    db.child("Contacts").child(temp.getKey()).child("u21").setValue(true); //post u21 change to database
                                    //possibly sort list here??
                                    PhoneAFriend.getInstance().notifyContactListChange();//notify contact list adapter of change
                                    Toast.makeText(context,"QDB ADDED "+values.get(position)+" as a contact!",Toast.LENGTH_LONG).show(); //display add message
                                    return; //return because we are done
                                }
                            }
                        }
                        //If we get here then that means we did not find a contact! Lets make a new one
                        Contacts newContact = new Contacts(currentUsername,values.get(position),true,false); //create a new Contacts object
                        String key = getDb().child("Contacts").push().getKey(); //get the key
                        newContact.setKey(key);//set the key on your Contacts object
                        PhoneAFriend.getInstance().addDisplayContact(values.get(position));//add to string list
                        PhoneAFriend.getInstance().getActiveContacts().add(newContact);//add to active list
                        db.child("Contacts").child(key).setValue(newContact.toMap());//push to Contacts under the key we got
                        //possibly sort list here??
                        PhoneAFriend.getInstance().notifyContactListChange();//notify contact list adapter of change
                        Toast.makeText(context,"NEW ADDED "+values.get(position)+" as a contact!",Toast.LENGTH_LONG).show();//display message to user
                        return;//return because we are done

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context,"Error: Problem accessing the database, try again later!",Toast.LENGTH_LONG).show();
                    }
                });




                }


            }
        });

        return view;
    }

}
