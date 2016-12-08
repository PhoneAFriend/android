package seniordesign.phoneafriend.session;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by The Alex on 12/5/2016.
 */


public class SessionView extends SurfaceView{
    private Paint paint;
    private ArrayList<Path> strokes;
    private SurfaceHolder holder;


    public SessionView(Context context, AttributeSet attributeSet){
        super(context , attributeSet);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder){attemptToDraw();}
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder){}
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){attemptToDraw();}
        });
        initPaint();
        strokes = new ArrayList<Path>();



    }

    private void initPaint() {
        paint = new Paint();
        paint.setDither(true);
        paint.setStrokeWidth(50);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void addStroke(Path stroke){
        strokes.add(stroke);
        return;
    }

    private void drawStrokesToCanvas(Canvas canvas){
        Log.d("DRAWING" , "drawing to Canvas");
        canvas.drawColor(Color.WHITE);
        if(strokes != null ) {
            synchronized (holder) {
                for (Path paths : strokes) {
                    canvas.drawPath(paths , paint);
                }
            }

        }
    }

    public void attemptToDraw(){
            Log.d("DRAWING" , "Attempting to draw");
            if(holder.getSurface().isValid()){
                Canvas canvas = holder.lockCanvas();
                if(canvas == null){
                    Log.d("DRAWING" , "CANVAS IS NULL");
                    //do nothing. No Canvas
                    return;
                }else{
                    drawStrokesToCanvas(canvas);
                    Log.d("DRAWING" , "UnlockCanvasAndPost");
                    holder.unlockCanvasAndPost(canvas);
                }
            }
    }

}