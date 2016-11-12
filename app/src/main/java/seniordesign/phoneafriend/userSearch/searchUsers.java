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

                //First Clear our list holding any previous search results
                list.clear();

                //Get the value inputted into our search
                String t = searcher.getQuery().toString();

                //Check if the user attempted to search via email (Checks for @ character in t)
                if(isEmailQuery(t)){
                    //If they did we look for an exact match on the email
                    db.child("users").orderByChild("useremail").equalTo(t).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Check to see if there were any results
                            if (dataSnapshot != null) {
                                //If we had data, and the username is not our name, add it to our list
                                //Essentially this for loop only looks at one value if we found an exact match
                                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                    //Create a temp user so we can grab the data and get the username
                                    User temp = (User) userSnap.getValue(User.class);
                                    Log.d("user is: ", temp.getUsername());
                                    //Add to list if user we get is not the currently signed in user
                                    if (!isCurrentUser(currentUser, temp.getID())) {
                                        list.add(temp.getUsername());
                                    }
                                }
                                //Tell our adapter our data has changed
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("DATA D", "NULL dataSnapShot Search");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                } else {
                    //If not an email search, we look for usernames that start with or match the inputted possible username
                    db.child("users").orderByChild("username").startAt(t).endAt(t + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Check to see if there were any results
                            if (dataSnapshot != null) {
                                //If we had data, and the username is not our name, add it to our list
                                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                                    //Create a temp user so we can grab the data and get the username
                                    User temp = (User) userSnap.getValue(User.class);
                                    Log.d("user is: ", temp.getUsername());
                                    //Add to list if user we get is not the currently signed in user
                                    if (!isCurrentUser(currentUser, temp.getID())) {
                                        list.add(temp.getUsername());
                                    }
                                }
                                //Tell our adapter our data has changed
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("DATA D", "NULL dataSnapShot Search");
                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                //Set empty text to say no users were found, this is what is now diplayed when no results come up
                emptyText.setText("No users found!");

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
        //list.add("username1");
        //list.add("username2");

        adapter = new searchUserListAdapter(this,list,db);
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
