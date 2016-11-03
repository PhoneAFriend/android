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

import java.util.ArrayList;
import java.util.StringTokenizer;

import seniordesign.phoneafriend.R;

/**
 * Created by REB.
 */

public class searchUserListAdapter extends BaseAdapter implements ListAdapter {
    private Context context;
    private ArrayList<String> values = new ArrayList<String>();

    public searchUserListAdapter(Context context, ArrayList<String> values){
        this.context = context;
        this.values = values;
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
                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                Toast.makeText(context,"ADDING " + values.get(position),Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
        });

        return view;
    }

}
