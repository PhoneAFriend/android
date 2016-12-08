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

import java.util.ArrayList;

import seniordesign.phoneafriend.R;

public class SessionActivity extends AppCompatActivity{
    private String postId;
    private String senderName;
    private String receiverName;
    private TextView textView;
    private SessionView blackboard;
    private View.OnTouchListener blackboardListener;
    private ArrayList<Point> strokePoints;
    private Path path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        postId = getIntent().getStringExtra("POST_ID");
        senderName = getIntent().getStringExtra("SENDER_NAME");
        receiverName = getIntent().getStringExtra("RECEIVER_NAME");
        textView = (TextView) findViewById(R.id.session_postTitle);
        textView.setText(postId);
        blackboard = (SessionView) findViewById(R.id.session_blackboard);
        strokePoints = new ArrayList<Point>();
        path = new Path();
        blackboardListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                blackboard.sleepThread(50);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    path = new Path();
                    Log.d("Screen pressed", "Path at "+event.getX()+" , "+event.getY());
                    path.moveTo(event.getX(), event.getY());
                    strokePoints.add(new Point((int) event.getX() , (int) event.getY()));
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d("Touch moving" , "Path at "+event.getX()+ " , " + event.getY());
                    path.lineTo(event.getX(), event.getY());
                    path.moveTo(event.getX(), event.getY());
                    strokePoints.add(new Point((int) event.getX() , (int) event.getY()));
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("Screen unpressed" , "Path ended at " + event.getX()+" , "+event.getY());
                    path.lineTo(event.getX(), event.getY());
                    path.close();
                    strokePoints.add(new Point((int) event.getX() , (int) event.getY()));
                }
                blackboard.addStroke(path);
                return true;
            }
        };

        blackboard.setOnTouchListener(blackboardListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        blackboard.resume();
    }

    protected void onPause(){
        super.onPause();
        blackboard.pause();
    }



}

