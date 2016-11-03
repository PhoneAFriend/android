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
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.StringTokenizer;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.authentication.User;

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
                String s = PhoneAFriend.getInstance().getUsername();
                Toast.makeText(context,"ADDING " + values.get(position) +" "+s,Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
