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
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import seniordesign.phoneafriend.R;

public class SessionActivity extends AppCompatActivity{
    private String postId;
    private DatabaseReference db;
    private String senderName;
    private String recipientName;
    private TextView textView;
    private SessionView blackboard;
    private View.OnTouchListener blackboardListener;
    private Path path;
    private String sessionKey;
    //Need to consider multiple pages later on
    private String pageKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        postId = getIntent().getStringExtra("POST_ID");
        senderName = getIntent().getStringExtra("SENDER_NAME");
        recipientName = getIntent().getStringExtra("RECEIVER_NAME");
        textView = (TextView) findViewById(R.id.session_postTitle);
        textView.setText(postId);
        blackboard = (SessionView) findViewById(R.id.session_blackboard);
        path = new Path();
        db = FirebaseDatabase.getInstance().getReference();
        sessionKey = db.child("Sessions").push().getKey();
        pageKey = db.child("Sessions").child("Strokes").push().getKey();
        db.child("Sessions").child(sessionKey).child("senderName").setValue(senderName);
        db.child("Sessions").child(sessionKey).child("recipientName").setValue(recipientName);
        db.child("Sessions").child(sessionKey).child("postRef").setValue(postId);
        Log.d("SESSION INFO" , "Session Key: " + sessionKey);
        blackboardListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
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

    private void postPoint(float x , float y){
       String pointKey  = db.child("Sessions").child(sessionKey).child("Strokes").child(pageKey).child("Points").push().getKey();
       db.child("Sessions").child(sessionKey).child("Strokes").child(pageKey).child("Points").child(pointKey).child("x").setValue(x);
        db.child("Sessions").child(sessionKey).child("Strokes").child(pageKey).child("Points").child(pointKey).child("y").setValue(y);

    }



}

