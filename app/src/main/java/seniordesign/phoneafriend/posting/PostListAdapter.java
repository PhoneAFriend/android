package seniordesign.phoneafriend.posting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import seniordesign.phoneafriend.R;

/**
 * Created by The Alex on 10/7/2016.
 */

public class PostListAdapter extends ArrayAdapter<Post> {
    private Context context;
    private Post[] values;

    public PostListAdapter(Context context , Post [] values){
        super(context , -1 , values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position , View convertView , ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View elementView = inflater.inflate(R.layout.post_list_element_layout , parent , false);

        TextView questionTitle = (TextView) elementView.findViewById(R.id.postListElement_questionTitle);
        TextView postedBy = (TextView) elementView.findViewById(R.id.postListElement_authorText);
        TextView datePosted = (TextView) elementView.findViewById(R.id.postListElement_dateText);

        questionTitle.setText(values[position].getQuestionTitle());
        postedBy.setText(values[position].getPostedBy());
        datePosted.setText(values[position].getDatePosted());


        return elementView;

    }
}
