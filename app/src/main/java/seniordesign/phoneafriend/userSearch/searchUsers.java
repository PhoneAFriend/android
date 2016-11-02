package seniordesign.phoneafriend.userSearch;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import seniordesign.phoneafriend.R;

public class searchUsers extends AppCompatActivity {

    private SearchView searcher;
    private ListView searchList;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

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
                Toast.makeText(searchUsers.this, searcher.getQuery(), Toast.LENGTH_LONG).show();
                return false;
            }
        };
        //add the listener to our widget
        searcher.setOnQueryTextListener(searchListener);

        //get the listView and set its empty text
        searchList = (ListView) findViewById(R.id.search_user_list_view);
        emptyText = (TextView) findViewById(R.id.empty_text);
        searchList.setEmptyView(emptyText);

    }

    private void searchUserList(){
        Toast.makeText(searchUsers.this,searcher.getQuery(),Toast.LENGTH_LONG).show();
    }
}
