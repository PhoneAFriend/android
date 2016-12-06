package seniordesign.phoneafriend.session;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by The Alex on 12/5/2016.
 */


public class SessionView extends SurfaceView implements Runnable {
    private Paint paint;
    private ArrayList<Path> strokes;
    private Thread thread;
    private SurfaceHolder holder;
    private boolean drawingReady;
    private Path stroke;

    public SessionView(Context context, AttributeSet attributeSet){
        super(context , attributeSet);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder){
                drawingReady = true;
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder){
                drawingReady = false;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
                //do nothing
            }
        });

        initPaint();
        strokes = new ArrayList<Path>();

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

    public void run(){
        while(drawingReady == true){
            if(holder.getSurface().isValid()){
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                if(strokes != null) {
                    for(Path paths: strokes){
                        canvas.drawPath(paths  , paint);
                    }
                }
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }


    public void pause(){
        drawingReady = false;
        while(true){
            try{
                thread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            break;
        }
        thread = null;
    }

    public void resume(){
        drawingReady = true;
        thread = new Thread(this);
        thread.start();
    }


    public void addStroke(Path stroke){
        strokes.add(stroke);
        return;
    }



}