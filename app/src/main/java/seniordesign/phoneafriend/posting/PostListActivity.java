package seniordesign.phoneafriend.posting;

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
    private View.OnClickListener newPostListener;
    private DatabaseReference db;
    private Map<String , Object> posts;
    private TextView testView;
    private Iterator<Object> postIterator;
    private ListView postListView;
    private PostListAdapter postListViewAdapter;
    private ArrayList postArrayList;
    private Post[] listViewValues;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        newPostIntent = new Intent(this , NewPostActivity.class);
        postArrayList = new ArrayList();
        db = FirebaseDatabase.getInstance().getReference();
        newPostButton = (Button) findViewById(R.id.postList_newPostButton);
        newPostIntent = new Intent(this , NewPostActivity.class);
        newPostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newPostIntent);
            }
        };
        newPostButton.setOnClickListener(newPostListener);
        postListView = (ListView) findViewById(R.id.postList_postList);
        init();
    }

    private void init(){
        db.child("posts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            posts = (HashMap<String,Object>) dataSnapshot.getValue();
                            Log.v("PostListFetching DB:" , "GOT POSTS");
                            Collection<Object> collection = posts.values();
                            int size = collection.size();
                            String test = Integer.toString(size);
                            Log.v("Post amount recv : " , test);
                            Iterator<Object> iterator = collection.iterator();

                            initPosts(iterator);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                            Log.v("PostList Fetching DB:" , "CANCELLED");
                    }
                }
        );

    }

    private void initPosts(Iterator<Object> iterator){
        Post post;
        Object obj = new Object();
        while(iterator.hasNext()){
            post = new Post();
            obj = iterator.next();
            post.initFromMap((HashMap<String , String>) obj);
            Log.v("InitPost postText0:" , post.getQuestionText());
            postArrayList.add(post);
        }

        listViewValues = new Post[postArrayList.size()];
        Iterator<Post> arrayListIterator = postArrayList.iterator();
        int count = 0;
        while(arrayListIterator.hasNext()){
            listViewValues[count] = arrayListIterator.next();
            Log.v("InitPost postText1:" , listViewValues[count].getQuestionText());
            count++;

        }
        postListViewAdapter = new PostListAdapter(this , listViewValues);
        postListView.setAdapter(postListViewAdapter);
        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent viewPostIntent = new Intent(getApplicationContext(), ViewPostActivity.class);
                Post thisPost = (Post) adapterView.getItemAtPosition(i);
                viewPostIntent.putExtra("questionTitle" , thisPost.getQuestionTitle());
                viewPostIntent.putExtra("questionText", thisPost.getQuestionText());
                viewPostIntent.putExtra("datePosted", thisPost.getDatePosted());
                viewPostIntent.putExtra("postedBy", thisPost.getPostedBy());
                startActivity(viewPostIntent);
            }
        });
    }

}
