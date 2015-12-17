package com.masir.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.masir.com.masir.util.DisplayUtil;
import com.masir.maapplication.R;

public class LoadingView extends View {
	private final String TAG = "LoadingView";
	private int DEFAULTSIZE = 40;
	private RectF rectF = null;
	private Paint mTopCirclePaint = null;
	private Paint mTransparentPaint = null;
	private Paint mBallPaint = null;
	private float mTopCircleStroke = 2;
	private float mBallRadius = 10;
	private Context context;
	private int range = 0;
	private MyThread myThread = null;
	private float mBall_y = 10;
	private int time = 10;
	private int mBallStatus = 0;
	private Boolean open = false;

	private final static int THE_BALL_STATUS = 0;   //小球跟随外圆移动
	private final static int THE_BALL_STATUS2 = 1;  //小球垂直移动
	private final static int THE_BALL_STATUS3 = 2;  //小球外圆圆心

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		DEFAULTSIZE = DisplayUtil.dip2px(context, DEFAULTSIZE);
		init();
	}

	private void init() {

		mTopCirclePaint = new Paint();
		mTopCirclePaint.setAntiAlias(true);
		mTopCirclePaint.setColor(context.getResources()
				.getColor(R.color.col_22));
		mTopCirclePaint.setStrokeWidth(mTopCircleStroke);
		mTopCirclePaint.setStyle(Paint.Style.STROKE);

		mTransparentPaint = new Paint();
		mTransparentPaint.setAntiAlias(true);
		mTransparentPaint.setColor(context.getResources().getColor(
				R.color.transparent));
		mTransparentPaint.setStrokeWidth(mTopCircleStroke);
		mTransparentPaint.setStyle(Paint.Style.STROKE);

		mBallPaint = new Paint();
		mBallPaint.setAntiAlias(true);
		mBallPaint.setColor(context.getResources().getColor(R.color.col_22));
		mBallPaint.setStyle(Paint.Style.FILL);
		rectF = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawTopCircle(canvas);
		drawTransparentCircle(canvas);
		drawBall(canvas);
		if (myThread == null) {
			open = true;
			myThread = new MyThread();
			myThread.start();
		}

	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		switch (widthMode) {
		case MeasureSpec.AT_MOST:
			if (width > DEFAULTSIZE) {
				width = DEFAULTSIZE;
			}

			break;

		case MeasureSpec.UNSPECIFIED:
			break;
		case MeasureSpec.EXACTLY:
			break;
		}

		switch (heightMode) {
		case MeasureSpec.AT_MOST:
			if (height > DEFAULTSIZE) {
				height = DEFAULTSIZE;
			}
			break;

		case MeasureSpec.UNSPECIFIED:
			break;
		}
		setMeasuredDimension(width, height);
		rectF.set(0, 0, width, height);
	}

	private void drawTransparentCircle(Canvas canvas) {
		canvas.drawCircle(rectF.centerX(), rectF.centerY(),
				getTransparentCircleRadius(), mTransparentPaint);
	}

	private void drawTopCircle(Canvas canvas) {
		canvas.drawCircle(rectF.centerX(), rectF.centerY(),
				getTopCircleRadius(), mTopCirclePaint);
	}

	private void drawBall(Canvas canvas) {
		float y;
		float x;
		if (mBallStatus == THE_BALL_STATUS) {
			double sweepAngle = Math.PI / 180 * range + Math.PI / 180 * 270;
			y = (float) Math.sin(sweepAngle) * (getTransparentCircleRadius());
			x = (float) Math.cos(sweepAngle) * (getTransparentCircleRadius());
			canvas.translate(rectF.centerX(), rectF.centerY());
		} else {

			x = rectF.width() / 2;
			if (mBallStatus == THE_BALL_STATUS3) {

				y = rectF.height() / 2;
			} else {
				y = mBall_y;
			}
		}
		canvas.drawCircle(x, y, mBallRadius, mBallPaint);
	}

	private float getTransparentCircleRadius() {

		return rectF.width() / 2 - 10 - 1;
	}

	private float getTopCircleRadius() {

		return rectF.width() / 2 - mTopCircleStroke / 2;
	}
	
	
	class MyThread extends Thread {
		@Override
		public void run() {

			while (open) {// 你想在View不可见时设置open为false?dui ok
				if (mBallStatus == THE_BALL_STATUS) {
					range++;
					if (range <= 40) {
						time = 10;
					} else if (range > 40 && range < 320) {
						time = 3;
					} else {
						time = 10;
					}
					if (range == 360) {
						range = 0;
						mBallStatus = THE_BALL_STATUS2;
						time = 5;

					}

				} else if (mBallStatus == THE_BALL_STATUS2) {
					mBall_y++;
					if (mBall_y == rectF.height() / 2) {
						mBallStatus = THE_BALL_STATUS3;
						time = 30;
					}

				} else {
					mBallRadius++;
					if (mBallRadius == getTopCircleRadius() + 2) {
						mBallRadius = 10;
						mBall_y = 10;
						mBallStatus = 0;
					}

				}
				postInvalidate();
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void reset(){
		range = 0;
		mBallStatus = THE_BALL_STATUS;
		mBallRadius = 10;
		mBall_y = 10;
		myThread = null;
		open = false;
	}

}
