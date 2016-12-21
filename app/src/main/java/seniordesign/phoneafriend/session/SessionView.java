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
    protected Paint paint;
    protected Canvas memcanvas;
    protected Bitmap bitmap;
    protected List<Path> strokes;
    protected Path stroke;
    protected String dbColor;


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

    protected void initPaint() {
        paint = new Paint();
        dbColor = "#000000";
        paint.setDither(true);
        paint.setStrokeWidth(30); //small = 20 , med = 30 , large = 40
        paint.setColor(Color.parseColor("#000000"));
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
    public String getDBColor(){return dbColor;}
    public void setDBColor(String dbColor){this.dbColor = dbColor;}



}