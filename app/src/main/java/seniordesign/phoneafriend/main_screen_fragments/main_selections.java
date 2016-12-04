package seniordesign.phoneafriend.main_screen_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import seniordesign.phoneafriend.PhoneAFriend;
import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.main_screen;
import seniordesign.phoneafriend.posting.NewPostActivity;
import seniordesign.phoneafriend.posting.PostListActivity;
import seniordesign.phoneafriend.posting.currentUserPostsList;
import seniordesign.phoneafriend.session.SessionActivity;
import seniordesign.phoneafriend.userSearch.searchUsers;

public class main_selections extends Fragment {
    private Button postsButton;
    private View.OnClickListener postOnClickListener;
    private Button searchButton;
    private View.OnClickListener searchUserClickListener;
    private Button sessionButton;
    private View.OnClickListener sessionOnClickListener;
    private Intent postIntent;
    private Intent searchIntent;
    private Intent sessionIntent;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_selections_layout, container, false);
        postIntent = new Intent(getActivity(), PostListActivity.class);
        postsButton = (Button) view.findViewById(R.id.main_screen_posts_button);
        postOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPosts();
            }
        };
        if(postsButton!=null) {
            postsButton.setOnClickListener(postOnClickListener);
        }

        searchIntent = new Intent(getActivity(), searchUsers.class);
        searchButton = (Button) view.findViewById(R.id.search_button);
        searchUserClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSearch();
            }
        };
        searchButton.setOnClickListener(searchUserClickListener);

        Button myPostButton = (Button) view.findViewById(R.id.button4);
        myPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),currentUserPostsList.class));
            }
        });

        sessionIntent = new Intent(getActivity() , SessionActivity.class);
        sessionIntent.putExtra("SENDER_NAME" , ((PhoneAFriend) getActivity().getApplication()).getUsername() );
        sessionIntent.putExtra("POST_ID" , "Post Title");
        sessionIntent.putExtra("RECEIVER_NAME" , "Receiving User");
        sessionButton = (Button) view.findViewById(R.id.start_session_button);
        sessionOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(sessionIntent);
            }
        };
        sessionButton.setOnClickListener(sessionOnClickListener);
        return view;
    }

    private void gotoPosts(){
        startActivity(postIntent);
    }
    private void gotoSearch() { startActivity(searchIntent);}
}
