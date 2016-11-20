package seniordesign.phoneafriend.main_screen_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.contacts.contactListAdapter;

public class contacts_section extends Fragment {

    private ListView contactsListView;
    private TextView emptyText;
    private contactListAdapter adapter;
    private DatabaseReference db;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.contacts_layout, container, false);
        //return inflater.inflate(R.layout.contacts_layout, container, false);

        //Get the database reference
        db = FirebaseDatabase.getInstance().getReference();

        //Find our listview and empty text
        contactsListView = (ListView) v.findViewById(R.id.contact_listview);
        emptyText = (TextView) v.findViewById(R.id.empty_text);

        //Set our empty text
        emptyText.setText("You have no contacts, go to search users to find some contacts!");
        contactsListView.setEmptyView(emptyText);

        //Create our adapter for use in our listview
        adapter = new contactListAdapter(getActivity(),((PhoneAFriend) getActivity().getApplication()).getContactDisplayList(),db);

        //On create we set our contact adapter pointer to this created adapter. This will allow us to immediately change the
        //List view when we add new users from userSearch. This is called everytime we create the view, not sure if there are any
        //long term issues with doing this. It wi=orks fine now with no errors
        ((PhoneAFriend) getActivity().getApplication()).setContactAdapt(adapter);

        //Set ListView with our adapter
        contactsListView.setAdapter(adapter);

        return v;


    }
}
