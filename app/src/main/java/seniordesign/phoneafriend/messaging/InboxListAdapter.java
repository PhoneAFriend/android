package seniordesign.phoneafriend.messaging;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import seniordesign.phoneafriend.R;

/**
 * Created by REB
 */

public class InboxListAdapter extends ArrayAdapter<Message>{
    private Context context;
    private ArrayList<Message> values = new ArrayList<>();

    public InboxListAdapter(Context context, ArrayList<Message> values){
        super(context , -1 , values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent){

        //Inflate our list using our element layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View elementView = inflater.inflate(R.layout.message_inbox_element , parent , false);

        //Get the text view for our sender, subject and unread labels
        TextView sender = (TextView) elementView.findViewById(R.id.sender_username);
        TextView subject = (TextView) elementView.findViewById(R.id.subject_text);
        TextView unread = (TextView) elementView.findViewById(R.id.unread_text);

        //If the message has been read, then make the unread textview hide
        if(!values.get(position).isUnread()){
            unread.setText("VIEWED");
            unread.setTextColor(Color.parseColor("#2196F3"));
        }

        //Set the text for the sender and subject textviews using the message values for those fields
        sender.setText(values.get(position).getSenderUsername());
        if(values.get(position).getSubject().length() > 20)
            subject.setText(values.get(position).getSubject().substring(0,20)+"...");
        else
            subject.setText(values.get(position).getSubject());

        return elementView;

    }

}
