package seniordesign.phoneafriend.posting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        newPostIntent = new Intent(this , NewPostActivity.class);
        db = FirebaseDatabase.getInstance().getReference();
        init();
        newPostButton = (Button) findViewById(R.id.postList_newPostButton);
        newPostIntent = new Intent(this , NewPostActivity.class);
        newPostListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(newPostIntent);
            }
        };
        newPostButton.setOnClickListener(newPostListener);

    }

    private void init(){
        db.child("posts").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            posts = (HashMap<String,Object>) dataSnapshot.getValue();
                            Log.v("PostListFetching DB:" , "GOT POSTS");
                            Collection<Object> collection = posts.values();
                            Iterator<Object> iterator = collection.iterator();

                            Object obj = iterator.next();
                            Post post = new Post();
                            post.initFromMap((HashMap<String , String>) obj);
                            if(post.getQuestionText() != null){
                                Log.v("Post made" , post.getQuestionText());
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                            Log.v("PostList Fetching DB:" , "CANCELLED");
                    }
                }
        );

    }

    private void listPosts(Iterator<Object> iterator){
        while(iterator.hasNext()){

        }
    }

}
