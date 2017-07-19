package com.scott.computercontrollerclient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/7/19 0019.
 */

@SuppressLint("AppCompatCustomView")
public class ScreenImageView extends ImageView{
    private int x;
    private int y;
    private Paint mPaint;
    private OnMouseMoveListenner l;
    public static interface OnMouseMoveListenner {
        void onMove(float x,float y);
    }

    public void setOnMouseListenner(OnMouseMoveListenner l)  {
        this.l = l;
    }

    public void setMousePoint(int x,int y) {
        this.x = x;
        this.y = y;

        postInvalidate();
    }
    public ScreenImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(1080,MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(1920,MeasureSpec.AT_MOST));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(x - 10,y - 10,x + 10,y + 10,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: {
                l.onMove(event.getX(),event.getY());
            }break;
        }
        return super.onTouchEvent(event);
    }
}
