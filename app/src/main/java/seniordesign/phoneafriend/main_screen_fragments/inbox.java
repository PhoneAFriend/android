package seniordesign.phoneafriend.main_screen_fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.messaging.InboxListAdapter;
import seniordesign.phoneafriend.messaging.Message;
import seniordesign.phoneafriend.messaging.ViewMessage;

public class inbox extends Fragment {

    private ListView inboxListView;
    private TextView emptyText;
    private InboxListAdapter adapter;
    private DatabaseReference db;
    private Button refresh;
    private ProgressDialog refreshProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.inbox_layout, container, false);

        //Get the database reference
        db = FirebaseDatabase.getInstance().getReference();

        //Find our listview and empty text
        inboxListView = (ListView) v.findViewById(R.id.inbox_listview);
        emptyText = (TextView) v.findViewById(R.id.empty_text);

        //Set our empty text
        emptyText.setText("You have no messages! Refresh to see if you recieved messages!");
        inboxListView.setEmptyView(emptyText);

        adapter = new InboxListAdapter(getActivity(), PhoneAFriend.getInstance().getReceivedMessages());
        //PhoneAFriend.getInstance().setInboxAdapt(adapter);//not needed we renotifydatasetchange on resume
        inboxListView.setAdapter(adapter);

        inboxListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
                //Make an intent for the activity where we will view our message
                final Intent viewMessageIntent = new Intent(getActivity().getApplicationContext(), ViewMessage.class);
                //Get the message at the position we click in our listview
                final Message thisMsg = (Message) parent.getItemAtPosition(position);
                //Check if the message has not been read before
                if(thisMsg.isUnread()){
                    db.child("messages").child(thisMsg.getKey()).child("unread").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Message Read","Set unread flag as false");
                                //Set the local Message unread boolean as false
                                thisMsg.setUnread(false);
                                //After setting unread as false lets go look at the message
                                //First we send relevant data to our next intent
                                viewMessageIntent.putExtra("senderUsername",thisMsg.getSenderUsername());
                                viewMessageIntent.putExtra("subject",thisMsg.getSubject());
                                viewMessageIntent.putExtra("message",thisMsg.getMessage());
                                viewMessageIntent.putExtra("key",thisMsg.getKey());
                                //notify a change to the adapter since our unread value is now false
                                adapter.notifyDataSetChanged();
                                //open activity to view message
                                startActivity(viewMessageIntent);

                            }else{
                                Toast.makeText(getActivity(),"Error: Unable to open unread message!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    //Message has been read before, just save data we will be seeing and open activity
                    viewMessageIntent.putExtra("senderUsername",thisMsg.getSenderUsername());
                    viewMessageIntent.putExtra("subject",thisMsg.getSubject());
                    viewMessageIntent.putExtra("message",thisMsg.getMessage());
                    viewMessageIntent.putExtra("key",thisMsg.getKey());
                    startActivity(viewMessageIntent);

                }
            }
        });

        refresh = (Button) v.findViewById(R.id.refresh_btn);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disable button while query is being done, we don't want to let the user spam this on accident
                refresh.setClickable(false);
                //Method to query database for new messages, method will renable refresh button when done
                getReceivedMessages(PhoneAFriend.getInstance().getUsername());
            }
        });

        refreshProgress = new ProgressDialog(getActivity());

        return v;
    }

    public void getReceivedMessages(final String currentUsername){

        refreshProgress.setMessage("Refreshing...");
        refreshProgress.show();
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
                //When we finish getting all messages, tell the adapter!
                adapter.notifyDataSetChanged();
                //Give a message so user knows refresh is complete
                refreshProgress.dismiss();
                Toast.makeText(getActivity(),"Messages have been refreshed!", Toast.LENGTH_LONG).show();

                //Also set refresh as clickable again
                refresh.setClickable(true);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //If we could not get the messages, display error an set refresh button as clickable again
                refreshProgress.dismiss();
                Toast.makeText(getActivity(),"There was a problem getting received messages, Try Again Later!", Toast.LENGTH_LONG).show();
                refresh.setClickable(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
