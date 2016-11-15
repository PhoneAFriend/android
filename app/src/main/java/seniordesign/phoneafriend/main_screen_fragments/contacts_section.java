package seniordesign.phoneafriend.main_screen_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import seniordesign.phoneafriend.R;

public class contacts_section extends Fragment {

    private ListView contactsListView;
    private TextView emptyText;
    //private contactListAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.contacts_layout, container, false);
        //return inflater.inflate(R.layout.contacts_layout, container, false);






        return v;


    }
}
