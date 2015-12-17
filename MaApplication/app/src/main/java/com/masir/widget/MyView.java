package com.masir.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.masir.maapplication.R;

/**
 * Created by Masir on 2015/12/17.
 */
public class MyView extends View{
    private Paint mPaint;
    private Context context;
    private int s = 3;
    private int nums = 6;
    private int pinding = 6;
    private int width = 0;
    private int b = 0;
    private RectF rect;


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init (){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(context.getResources()
                .getColor(R.color.blue));
        mPaint.setStrokeWidth(6);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = (getWidth() - (pinding*5))/nums;

        int Start = 0;
        int stopX = 0;
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic1);

        for(int i = 1; i <= nums; i++){
            if(i == 1){
                Start = 0;
            }else{
                Start = Start + width + pinding;
            }
            if(i == 5){
                b = Start - mBitmap.getWidth()/2 - 3;
            }
            if(i < 3 ){
                mPaint.setColor(context.getResources()
                        .getColor(R.color.white));
            }else {
                mPaint.setColor(context.getResources()
                        .getColor(R.color.blue));
            }
            canvas.drawLine(Start, 100, Start + width, 100, mPaint);
//            canvas.drawLine(Start, 50, stopX, 50, mPaint);
//            Start = stopX + pinding;
//            System.out.print("iiiiiii     " + stopX);
        }
//        rect = new RectF(b, 50, width/2, 90);
//        canvas.drawBitmap(mBitmap,null,rect,mPaint);
        canvas.drawBitmap(mBitmap, b, 30, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth(), 200);
    }

}
