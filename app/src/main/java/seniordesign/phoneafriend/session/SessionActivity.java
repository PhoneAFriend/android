package seniordesign.phoneafriend.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import seniordesign.phoneafriend.R;

public class SessionActivity extends AppCompatActivity {
    private String postId;
    private String senderName;
    private String receiverName;
    private TextView textView;
    private Display display;
    private Canvas canvas;
    private Bitmap bitmap;
    private SurfaceView blackboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        postId = getIntent().getStringExtra("POST_ID");
        senderName = getIntent().getStringExtra("SENDER_NAME");
        receiverName = getIntent().getStringExtra("RECEIVER_NAME");
        textView = (TextView) findViewById(R.id.session_postTitle);
        textView.setText(postId);
        display = getWindowManager().getDefaultDisplay();
        bitmap = null;
        blackboard = (SurfaceView) findViewById(R.id.session_blackboard);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bitmap != null) {
            bitmap = Bitmap.createBitmap(blackboard.getWidth(), blackboard.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Path path = new Path();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            path.moveTo(event.getX(), event.getY());
            path.lineTo(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            path.lineTo(event.getX(), event.getY());
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            path.lineTo(event.getX(), event.getY());
        }
        return true;

    }

    class SessionView extends SurfaceView {
        private Paint paint;

        public SessionView(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            setFocusable(true);
            setFocusableInTouchMode(true);
            initPaint();
        }

        private void initPaint() {
            paint = new Paint();
            paint.setDither(true);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        @Override
        public void onDraw(Canvas thisCanvas) {
            thisCanvas.drawLine(10, 10, 70, 70, paint);
            /*
              for (Path path : _graphics) {
                //canvas.drawPoint(graphic.x, graphic.y, mPaint);
             canvas.drawPath(path, mPaint);
                    }
             */
        }

    }
}

