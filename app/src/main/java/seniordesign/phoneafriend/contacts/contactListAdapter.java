package seniordesign.phoneafriend.contacts;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Iterator;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;

/**
 * Created by REB.
 */

public class contactListAdapter extends BaseAdapter implements ListAdapter{
    private Context context;
    private ArrayList<String> values = new ArrayList<String>();
    private DatabaseReference db;

    public contactListAdapter(Context context, ArrayList<String> values, DatabaseReference db){
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

    public void removeContact(int position){

        final String usernameOfRemoved = values.get(position);
        //We iterate through our active list until we find our matching entry, we remove the values username string
        Iterator<Contacts> contactsIterator = PhoneAFriend.getInstance().getActiveContacts().iterator();
        Contacts temp;
        while (contactsIterator.hasNext()) {
            temp = contactsIterator.next();
            //check contacts where the user we are trying to remove is username2 and they are currently contacts
            if (temp.getUsername2().equals(values.get(position)) && temp.isU12()) {
                PhoneAFriend.getInstance().removeContact(values.get(position)); //remove string from display list
                temp.setU12(false); //change u12 value to false
                PhoneAFriend.getInstance().getInactiveContacts().add(temp); //add it to our inactive contacts list
                contactsIterator.remove(); //removes temp from active contacts
                db.child("Contacts").child(temp.getKey()).child("u12").setValue(false); //post change to database
                //possibly sort list here??
                notifyDataSetChanged();//notify adapter of change
                Toast.makeText(context, "Q1 REMOVED " + usernameOfRemoved + " from contacts!", Toast.LENGTH_LONG).show();
                return;//return because we are done
            }

            //check contacts where the user we are trying to remove is username1 and they are currently contacts
            if (temp.getUsername1().equals(values.get(position)) && temp.isU21()) {
                PhoneAFriend.getInstance().removeContact(values.get(position)); //remove string from display list
                temp.setU21(false); //change u21 value to false
                PhoneAFriend.getInstance().getInactiveContacts().add(temp); //add it to our inactive contacts list
                contactsIterator.remove(); //removes temp from active contacts
                db.child("Contacts").child(temp.getKey()).child("u21").setValue(false); //post change to database
                //possibly sort list here??
                notifyDataSetChanged();//notify adapter of change
                Toast.makeText(context, "Q2 REMOVED " + usernameOfRemoved + " from contacts!", Toast.LENGTH_LONG).show();
                return;//return because we are done
            }

        }
    }

    @Override
    public View getView(final int position , View convertView , ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_list_element, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.username_string);
        listItemText.setText(values.get(position));

        //Handle buttons and add onClickListeners
        Button msgBtn = (Button) view.findViewById(R.id.msg_btn);
        Button rmvBtn = (Button) view.findViewById(R.id.rmv_btn);

        //Create a dialog interface which will ask the user if they really want to remove a contact
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked remove contact!
                        removeContact(position);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked do nothing
                        break;
                }
            }
        };


        rmvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Create the Alert Dialog and Show it! If the user presses remove button on a contact
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to remove "+values.get(position)+" from your contacts?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });


        return view;
    }

}
