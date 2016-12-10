package seniordesign.phoneafriend.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The Alex on 12/5/2016.
 */


public class SessionView extends View {
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private Path stroke;
    private List<Path> strokes;



    public SessionView(Context context, AttributeSet attributeSet){
        super(context , attributeSet);
        initPaint();
        stroke = new Path();
        strokes = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w , int h , int oldw , int oldh){
        bitmap = Bitmap.createBitmap(w , h , Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setDither(true);
        paint.setStrokeWidth(30);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }






    public void drawStroke(Path stroke) {
                    strokes.add(stroke);
                    invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap , 0 , 0 , paint);
         for(Path stroke : strokes){
            Log.d("DRAWING", "Drawing stroke");
            canvas.drawPath(stroke, paint);
        }
    }

    public void clear(){
        strokes.clear();
    }



}