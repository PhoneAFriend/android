package seniordesign.phoneafriend.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.v4.content.res.ResourcesCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    private String postTitleText;
    private String postBodyText;
    private String senderName;
    private String recipientName;
    private TextView postTitle;
    private TextView postBody;
    private Spinner colorDropdown;
    private Spinner strokeDropdown;
    private Spinner.OnItemSelectedListener colorSpinnerListener;
    private Spinner.OnItemSelectedListener strokeSpinnerListener;
    private SessionView blackboard;
    private View.OnTouchListener blackboardListener;
    private Path path;
    private String sessionKey;
    private TabHost tabHost;
    private TabHost.OnTabChangeListener tabChangeListener;
    private int currentTab;
    
    //Need to consider multiple pages later on
    private String strokeKey;

    //Demo Only
    private boolean isDemoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        postId = getIntent().getStringExtra("POST_ID");
        senderName = getIntent().getStringExtra("SENDER_NAME");
        recipientName = getIntent().getStringExtra("RECEIVER_NAME");
        postTitleText = getIntent().getStringExtra("POST_TITLE");
        postBodyText = getIntent().getStringExtra("POST_BODY");
        isDemoSession = getIntent().getBooleanExtra("DEMO_SESSION" , true); //Change to false when real sessions working

        initSessionDB();
        initBlackboardTab();
        initQuestionTab();
        initTabHost();
        initChatTab();
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
        db.child("Sessions").child(sessionKey).child("Strokes").child(strokeKey).child("color").setValue(blackboard.getDBColor());
        db.child("Sessions").child(sessionKey).child("Strokes").child(strokeKey).child("width").setValue(blackboard.getPaintWidth());
    }

    private void initTabHost(){
        tabHost = (TabHost)findViewById(R.id.session_tabHost);
        tabHost.setup();

        TabHost.TabSpec spec;

        spec = tabHost.newTabSpec("Post");
        spec.setContent(R.id.session_postLayout);
        spec.setIndicator("Question" , ResourcesCompat.getDrawable(getResources(), R.drawable.ic_question_icon, null));
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Blackboard");
        spec.setContent(R.id.session_blackboardLayout);
        spec.setIndicator("Blackboard",  ResourcesCompat.getDrawable(getResources(), R.drawable.ic_blackboard_icon, null));
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("Chat");
        spec.setContent(R.id.session_chatLayout);
        spec.setIndicator("Chat",  ResourcesCompat.getDrawable(getResources(), R.drawable.ic_chat_icon, null));
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

    private void initSessionDB(){
        db = FirebaseDatabase.getInstance().getReference();
        sessionKey = db.child("Sessions").push().getKey();
        db.child("Sessions").child(sessionKey).child("senderName").setValue(senderName);
        db.child("Sessions").child(sessionKey).child("recipientName").setValue(recipientName);
        db.child("Sessions").child(sessionKey).child("postRef").setValue(postId);
        Log.d("SESSION INFO" , "Session Key: " + sessionKey);
    }

    private void deleteSession(){
        db.child("Sessions").child(sessionKey).removeValue();
    }

    private void initQuestionTab(){
        postTitle = (TextView) findViewById(R.id.session_postTitle);
        postBody = (TextView) findViewById(R.id.session_postBody);
        if(isDemoSession){
            postTitleText = "Demo Question";
            postBodyText = "Hello! This is an example of a Session in PhoneAFriend. If you have someone to connect to this session, give them your session key and they can see what you draw on your blackboard as well as chat with you!\n\nYour session key:\n"+sessionKey;
        }

        postTitle.setText(postTitleText);
        postBody.setText(postBodyText);

    }

    private void initBlackboardTab(){
        blackboard = (SessionView) findViewById(R.id.session_blackboard);
        path = new Path();
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

        colorDropdown = (Spinner)findViewById(R.id.session_colorSpinner);
        String[] colorItems = new String[]{"Black", "White", "Red"}; // TODO: Change to icons and add more options
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, colorItems);
        colorDropdown.setAdapter(colorAdapter);

        colorSpinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case(0):
                        blackboard.setPaintColor(Color.parseColor("#000000"));
                        blackboard.setDBColor("#000000");
                        break;
                    case(1):
                         blackboard.setPaintColor(Color.parseColor("#FFFFFF"));
                        blackboard.setDBColor("#FFFFFF");
                        break;
                    case(2):
                        blackboard.setPaintColor(Color.parseColor("#FF0000"));
                        blackboard.setDBColor("#FF0000");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                blackboard.setPaintColor(Color.parseColor("#000000"));
                blackboard.setDBColor("#000000");
            }
        };

        colorDropdown.setOnItemSelectedListener(colorSpinnerListener);

        strokeDropdown = (Spinner)findViewById(R.id.session_strokeSpinner);
        String[] strokeItems = new String[]{"Small", "Medium", "Large" , "Eraser"}; // TODO: Change to icons and add more options
        ArrayAdapter<String> strokeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strokeItems);
        strokeDropdown.setAdapter(strokeAdapter);

        strokeSpinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case(0):
                        blackboard.setPaintWidth(10);
                        break;
                    case(1):
                        blackboard.setPaintWidth(25);
                        break;
                    case(2):
                        blackboard.setPaintWidth(55);
                        break;
                    case(3):
                        blackboard.setPaintWidth(150);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                blackboard.setPaintWidth(25);
            }
        };

        strokeDropdown.setOnItemSelectedListener(strokeSpinnerListener);
    }

    private void initChatTab(){

    }
    private void setTabColors(){
        for(int i = 0 ;i< 3 ;i++){
            tabHost.getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.parseColor("#2196F3")); //myBlue
        }
        tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#CDDC39")); //myGreen
    }
}

