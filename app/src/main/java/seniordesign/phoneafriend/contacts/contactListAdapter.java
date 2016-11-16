package seniordesign.phoneafriend.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

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


        return view;
    }

}
