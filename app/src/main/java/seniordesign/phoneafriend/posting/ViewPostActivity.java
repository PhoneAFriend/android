package seniordesign.phoneafriend.posting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import seniordesign.phoneafriend.R;

/**
 * Created by The Alex on 10/7/2016.
 */

public class ViewPostActivity extends AppCompatActivity {

    private TextView questionTitleView;
    private TextView questionTextView;
    private TextView postedByView;
    private Bundle recvExtras;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        recvExtras = getIntent().getExtras();
        questionTextView = (TextView) findViewById(R.id.viewPostBody);
        questionTitleView = (TextView) findViewById(R.id.viewPostTitle);
        postedByView = (TextView) findViewById(R.id.viewPostAuthor);
        questionTitleView.setText(recvExtras.getString("questionTitle"));
        questionTextView.setText(recvExtras.getString("questionText"));
        postedByView.setText("By: " + recvExtras.getString("postedBy"));

    }
}
