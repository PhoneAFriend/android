package seniordesign.phoneafriend.session;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by The Alex on 12/5/2016.
 */


public class SessionView extends View {
    private Paint paint;
    private Canvas memcanvas;
    private Bitmap bitmap;
    private List<Path> strokes;
    private Path stroke;

    public SessionView(Context context, AttributeSet attributeSet){
        super(context , attributeSet);
        initPaint();
        strokes = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w , int h , int oldw , int oldh){
        bitmap = Bitmap.createBitmap(w , h , Bitmap.Config.ARGB_8888);
        memcanvas = new Canvas(bitmap);
        memcanvas.drawColor(Color.WHITE);
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
        this.stroke = stroke;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(stroke != null && !stroke.isEmpty()) {
            memcanvas.drawPath(stroke, paint);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

    public void clear(){
        strokes.clear();
    }

    public void setPaintColor(int color){
        paint.setColor(color);
    }

    public void setPaintWidth(float width){
        paint.setStrokeWidth(width);
    }

    public int getPaintColor(){ return paint.getColor();}
    public float getPaintWidth(){ return paint.getStrokeWidth();}



}