package seniordesign.phoneafriend.posting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import seniordesign.phoneafriend.R;

public class PostListActivity extends AppCompatActivity {

    private Intent newPostIntent;
    private Button newPostButton;
    private Button refreshButton;
    private View.OnClickListener newPostListener;
    private View.OnClickListener refreshListener;
    private DatabaseReference db;
    private ListView postListView;
    private PostListAdapter postListViewAdapter;
    private ArrayList<Post> postArrayList = new ArrayList<>();
    private ProgressDialog myProgress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        newPostIntent = new Intent(this , NewPostActivity.class);
        db = FirebaseDatabase.getInstance().getReference();
        myProgress = new ProgressDialog(this);
        newPostButton = (Button) findViewById(R.id.postList_newPostButton);
        refreshButton = (Button) findViewById(R.id.postList_refreshButton);
        newPostIntent = new Intent(this , NewPostActivity.class);
        newPostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newPostIntent);
            }
        };

        init(false);

        refreshListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init(true);
            }
        };
        newPostButton.setOnClickListener(newPostListener);
        refreshButton.setOnClickListener(refreshListener);
        postListView = (ListView) findViewById(R.id.postList_postList);

    }

    private void init(final boolean isaRefresh){
        //If we are refreshing or actutal initializing we will display different messages
        if(isaRefresh){
            myProgress.setMessage("Refreshing your posts...");
            myProgress.show();
        }else{
            myProgress.setMessage("Retrieving posts...");
            myProgress.show();
        }

        //Here we clear our list and pull from the database
        postArrayList.clear();
        db.child("posts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null) {
                            //If we had data, lets add it to our Post list
                            for (DataSnapshot dataSnap : dataSnapshot.getChildren()) {
                                Post p = new Post(dataSnap);
                                Log.d("New Post ","It's from "+p.getPostedBy());
                                //add to list (top of list)
                                postArrayList.add(0,p);
                            }
                        }

                        //If we are not refreshing, initialize our adapter, if we are refreshing
                        //then our adapter has already been made, just notify it of the data set change
                        if(!isaRefresh){
                            initAdapter();
                        }else{
                            myProgress.dismiss();
                            Toast.makeText(PostListActivity.this,"Posts refreshed!",Toast.LENGTH_LONG).show();
                            postListViewAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                            Log.v("PostList Fetching DB:" , "CANCELLED");
                        //If our database failed to be queried, we still need to initialize our adapter if this is not a refresh
                        if(!isaRefresh){
                           initAdapter();
                        }
                    }
                }
        );

    }

    private void initAdapter(){
        //Set the adapter
        postListViewAdapter = new PostListAdapter(this , postArrayList);
        postListView.setAdapter(postListViewAdapter);
        //Set an item click listener, when we click on a post in the list we go to a view activity
        //After sending relevant data
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent viewPostIntent = new Intent(getApplicationContext(), ViewPostActivity.class);
                Post thisPost = (Post) adapterView.getItemAtPosition(i);
                viewPostIntent.putExtra("questionTitle" , thisPost.getQuestionTitle());
                viewPostIntent.putExtra("questionText", thisPost.getQuestionText());
                viewPostIntent.putExtra("datePosted", thisPost.getDatePosted());
                viewPostIntent.putExtra("postedBy", thisPost.getPostedBy());
                viewPostIntent.putExtra("subject",thisPost.getSubject());
                viewPostIntent.putExtra("questionImageURL",thisPost.getQuestionImageURL());
                viewPostIntent.putExtra("postKey",thisPost.getPostKey());
                startActivity(viewPostIntent);
            }
        });
        //Dismiss progress bar once adapter initialization is done
        myProgress.dismiss();


    }

    /*@Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        init(true);
    }*/

}
