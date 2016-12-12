package seniordesign.phoneafriend.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import seniordesign.phoneafriend.R;

import static seniordesign.phoneafriend.R.id.tab_horz_scroller;
import static seniordesign.phoneafriend.R.id.textView;

public class SessionActivity extends AppCompatActivity {
    private String postId;
    private DatabaseReference db;
    private String senderName;
    private String recipientName;
    private TextView postTitle;
    private TextView postBody;
    private SessionView blackboard;
    private View.OnTouchListener blackboardListener;
    private Path path;
    private String sessionKey;
    private TabHost tabHost;
    private TabHost.OnTabChangeListener tabChangeListener;
    private int currentTab;
    //Need to consider multiple pages later on
    private String strokeKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        postId = getIntent().getStringExtra("POST_ID");
        senderName = getIntent().getStringExtra("SENDER_NAME");
        recipientName = getIntent().getStringExtra("RECEIVER_NAME");
        postTitle = (TextView) findViewById(R.id.session_postTitle);
        postTitle.setText(getIntent().getStringExtra("POST_TITLE"));
        postBody = (TextView) findViewById(R.id.session_postBody);
        postBody.setText((getIntent().getStringExtra("POST_BODY")));
        blackboard = (SessionView) findViewById(R.id.session_blackboard);
        path = new Path();
        db = FirebaseDatabase.getInstance().getReference();
        sessionKey = db.child("Sessions").push().getKey();
        db.child("Sessions").child(sessionKey).child("senderName").setValue(senderName);
        db.child("Sessions").child(sessionKey).child("recipientName").setValue(recipientName);
        db.child("Sessions").child(sessionKey).child("postRef").setValue(postId);
        Log.d("SESSION INFO" , "Session Key: " + sessionKey);
        blackboardListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    changeStrokeKey();
                    path = new Path();
                    //Log.d("Screen pressed", "Path at "+event.getX()+" , "+event.getY());
                    path.moveTo(event.getX(), event.getY());
                    postPoint(event.getX() , event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //Log.d("Touch moving" , "Path at "+event.getX()+ " , " + event.getY());
                    path.lineTo(event.getX(), event.getY());
                    path.moveTo(event.getX(), event.getY());
                    postPoint(event.getX() , event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                   //Log.d("Screen unpressed" , "Path ended at " + event.getX()+" , "+event.getY());
                    path.lineTo(event.getX(), event.getY());
                    path.close();
                    postPoint(event.getX() , event.getY());

                }
                blackboard.drawStroke(path);

                return true;
            }
        };

        blackboard.setOnTouchListener(blackboardListener);
        initTabHost();
    }

    @Override
    protected void onStop(){
        super.onStop();
        blackboard.clear();
    }

    @Override
    protected void onPause(){
        super.onPause();
        blackboard.clear();
    }

    //Need to get rid of clutter in the DB
    @Override
    protected void onDestroy(){
        super.onDestroy();
        deleteSession();
    }

    private void postPoint(float x , float y){
       String pointKey  = db.child("Sessions").child(sessionKey).child("Strokes").child(strokeKey).child("Points").push().getKey();
       db.child("Sessions").child(sessionKey).child("Strokes").child(strokeKey).child("Points").child(pointKey).child("x").setValue(x);
        db.child("Sessions").child(sessionKey).child("Strokes").child(strokeKey).child("Points").child(pointKey).child("y").setValue(y);
    }

    private void changeStrokeKey(){
        strokeKey = db.child("Sessions").child(sessionKey).child("Strokes").push().getKey();
        db.child("Sessions").child(sessionKey).child("Strokes").child(strokeKey).child("color").setValue(blackboard.getPaintColor());
        db.child("Sessions").child(sessionKey).child("Strokes").child(strokeKey).child("width").setValue(blackboard.getPaintWidth());
    }

    private void initTabHost(){
        tabHost = (TabHost)findViewById(R.id.session_tabHost);
        tabHost.setup();

        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec("Post");
        spec.setContent(R.id.session_postLayout);
        spec.setIndicator("Question");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Blackboard");
        spec.setContent(R.id.session_blackboardLayout);
        spec.setIndicator("Blackboard");
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Chat");
        spec.setContent(R.id.session_chatLayout);
        spec.setIndicator("Chat");
        tabHost.addTab(spec);

        tabChangeListener = new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                setTabColors();
            }
        };

        tabHost.setOnTabChangedListener(tabChangeListener);

        setTabColors();

    }

    private void deleteSession(){
        db.child("Sessions").child(sessionKey).removeValue();
    }

    private void setTabColors(){
        for(int i = 0 ;i< 3 ;i++){
            tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.parseColor("#2196F3")); //myBlue
        }
        tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#CDDC39")); //myGreen
    }
}

