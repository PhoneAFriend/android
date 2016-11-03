package seniordesign.phoneafriend.userSearch;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import seniordesign.phoneafriend.R;
import seniordesign.phoneafriend.authentication.User;

public class searchUsers extends AppCompatActivity {

    private SearchView searcher;
    private ListView searchList;
    private TextView emptyText;
    private searchUserListAdapter adapter;
    private ArrayList<String> list;
    private DatabaseReference db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        //Get a reference to the database, and the current user
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();


        //Get the SearchView Widget
        searcher = (SearchView) findViewById(R.id.search_user);


        //Create a listener for when the query text is changed
        SearchView.OnQueryTextListener searchListener = new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextChange(String newText) {
                // What to do if there is any text change
                return false;
            }


            @Override
            public boolean onQueryTextSubmit(String query) {
                // What to do when user submits the query string
                //Output the new list with the query results
                //Toast.makeText(searchUsers.this, searcher.getQuery(), Toast.LENGTH_LONG).show();
                //list.clear();
                //String t = searcher.getQuery().toString();
                //if(isEmailQuery(t))
                //    Toast.makeText(searchUsers.this,"EMAIL",Toast.LENGTH_LONG).show();
                //list.add(t);
                //adapter.notifyDataSetChanged();
                /*db.child("users").orderByChild("username").startAt(t).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null) {
                            for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                //String name = (String) userSnap.child("username").getValue();
                                User temp = (User) userSnap.getValue(User.class);
                                Log.d("user is: ",temp.getName());
                                /*if(!isCurrentUser(currentUser,temp.getUID())){
                                    list.add(temp.getName());
                                }
                            }
                        }else{
                            Log.d("DATA D","NULL dataSnapShot Search");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
                //User bob = new User("123","bob@mail.com","bobby");
                //Log.d("id is",bob.getUID());



                return false;
            }
        };
        //add the listener to our widget
        searcher.setOnQueryTextListener(searchListener);

        //get the listView and set its empty text
        searchList = (ListView) findViewById(R.id.search_user_list_view);
        emptyText = (TextView) findViewById(R.id.empty_text);
        emptyText.setText("Search User by inputting username or email");
        searchList.setEmptyView(emptyText);


        //generate list
        list = new ArrayList<String>();
        list.add("username1");
        list.add("username2");

        adapter = new searchUserListAdapter(this,list);
        searchList.setAdapter(adapter);

    }

    private void searchUserList(){
        Toast.makeText(searchUsers.this,searcher.getQuery(),Toast.LENGTH_LONG).show();
    }

    private boolean isEmailQuery(String str) {
        return str.contains("@");
    }

    private boolean isCurrentUser(FirebaseUser user, String uID){
        //Compares current user with a given id to see if it is the same user
        return user.getUid().equals(uID);
    }
}
