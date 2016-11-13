package seniordesign.phoneafriend.userSearch;

import android.content.Context;
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
import java.util.StringTokenizer;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.authentication.User;
import seniordesign.phoneafriend.contacts.Contacts;

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
                //do something
                Toast.makeText(context,"MESSAGING " + values.get(position),Toast.LENGTH_LONG).show();
                //User t = new User("123","email@lol.com",values.get(position));
                //String key = getDb().child("TestMSG").push().getKey();
                //getDb().child("TestMSG").child(key).setValue(values.get(position));
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                final String currentUsername = PhoneAFriend.getInstance().getUsername();

                getDb().child("Contacts").orderByChild("username1").equalTo(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Check to see if the snapshot is null, this means there are no contacts where current user is username1
                        if (dataSnapshot != null) {
                            //If we had data, lets find the element where username2 is the user we want to add
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                //Create a temp user so we can grab the data and get the username
                                Contacts temp = (Contacts) userSnap.getValue(Contacts.class);
                                //if username2 matches user we want to add, and boolean value checking status is false
                                //then change boolean flag for username1 to username2 contact status to true and push to database
                                if (temp.getUsername2().equals(values.get(position))) {
                                    if(!temp.isU12()) {
                                        temp.setU12(true);
                                        getDb().child("Contacts").child(userSnap.getKey()).setValue(temp.toMap());
                                        Toast.makeText(context, "Q1 ADDED " + values.get(position) + " to " + "contacts", Toast.LENGTH_LONG).show();
                                        //return as we have completed adding the contact
                                        return;
                                    }else{
                                        Toast.makeText(context, "User already a Contact!", Toast.LENGTH_LONG).show();
                                        //return as user is already a contact
                                        return;
                                    }

                                }

                            }
                        }
                            //If we get here, then we didn't find the contact as username1 = currentUser username2 = user to be added
                            //If user was not username1, let's see if there is an instance where username2 is current user
                            //and username1 is user we want to add
                            //Let us query based on username2
                            getDb().child("Contacts").orderByChild("username2").equalTo(currentUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Check to see if the snapshot is null, this means there are no contacts where current user is username1
                                    if (dataSnapshot != null) {
                                        //If we had data, lets find the element where username2 is the user we want to add
                                        for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                            //Create a temp user so we can grab the data and get the username
                                            Contacts temp = (Contacts) userSnap.getValue(Contacts.class);
                                            //if username2 matches user we want to add, and boolean value checking status is false
                                            //then change boolean flag for username1 to username2 contact status to true and push to database
                                            if(temp.getUsername1().equals(values.get(position))) {
                                                if(!temp.isU21()) {
                                                    temp.setU21(true);
                                                    getDb().child("Contacts").child(userSnap.getKey()).setValue(temp.toMap());
                                                    Toast.makeText(context, "Q2 ADDED " + values.get(position) + " to " + "contacts", Toast.LENGTH_LONG).show();
                                                    //return as we have completed adding the contact
                                                    return;
                                                }else{
                                                    Toast.makeText(context, "User already a Contact!", Toast.LENGTH_LONG).show();
                                                    //return as we found out the user is already a contact
                                                    return;
                                                }
                                            }

                                        }
                                    }

                                    //If we get here then we can create a new contact for Contacts in the database
                                        Contacts newContact = new Contacts(currentUsername, values.get(position),true,false);
                                        String key = getDb().child("Contacts").push().getKey();
                                        getDb().child("Contacts").child(key).setValue(newContact.toMap());
                                        Toast.makeText(context, "ADDED " + values.get(position) + " to " + "contacts", Toast.LENGTH_LONG).show();
                                        //program will end up normally returning from this point as we have done everything possible
                                        //try and add the contact
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return view;
    }

}
