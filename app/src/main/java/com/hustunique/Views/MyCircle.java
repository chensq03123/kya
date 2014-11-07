package com.hustunique.Views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;


public class MyCircle extends View{
    private static int DIAMETER = 130; //Diameter英文为直径，该常量表示小圆直径的dp值
    private static int STROKE_WIDTH = 25; // 该常量表示圆环宽度2倍的dp值
    
    private Paint mPaint;
    private float mProgress=300;// 表示进度
    private RectF mRect;
    private int mDiameter=10; // Diameter英文为直径，在该View中要绘制圆环，圆环由两个圆形确定（大圆和小圆），这个整形值表示小圆直径。
    private int mWidth=20;// 这个值表示圆环的宽度的2倍（大圆直径-小圆直径）
    
    private final int defaultColor=Color.rgb(216,143,5);
    private int mbgcolor=Color.rgb(0xcf, 0xcf, 0xcf);//进度条背景颜色
    private int processColor=Color.BLUE; //进度条进度颜色
    
    
    public MyCircle(Context context){
    	super(context);
    }
    
    public MyCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        processColor =Color.rgb(0x25, 0xdc, 0xca);
        init();
    }
    
    private void init() {
        Resources res = getResources();
        //getDisplayMetrics()返回当前展示的metrics.
        DisplayMetrics metrics = res.getDisplayMetrics();
        //TypedValue.applyDimension(int unit, float value, DisplayMetrics metrics)
        //该方法中unit表示要转换成的单位，value表示数值，metrics表示当前的度量方式
        //DIAMETER是常量0x1E,十进制为30，下面语句就表示tmp的值为30dp换算成的像素数值
        float tmp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                DIAMETER, metrics);
        //ceil函数表示向上取整
        mDiameter = (int) Math.ceil(tmp);
        tmp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                STROKE_WIDTH, metrics);
        mWidth = (int) Math.ceil(tmp);
        
        Paint p = new Paint();
        //Paint.Style.STROKE表示空心，Paint.Style.FILL表示实心，Paint.Style.FILL_AND_STROKE表示空心与实心
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        //setStrokeWidth()设置画笔宽度
//        p.setStrokeWidth(0.5F*mWidth+0.5F*mDiameter);
        p.setStrokeWidth(0.5F*mWidth);
        p.setStrokeCap(Cap.SQUARE);
        p.setColor(defaultColor);
        mPaint = p;
        
        float rightTop = (float) (mWidth / 2.0);//这个值就是圆环宽度（大圆半径-小圆半径）
        mRect = new RectF(rightTop, rightTop, mDiameter+rightTop, mDiameter+rightTop);
        mProgress = 0.5f;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        Paint paint = mPaint;
       /* if (mProgress < 360) {
        	paint.setStrokeWidth(0.5f*(mWidth));
            paint.setAlpha(0x7f);
            paint.setColor(Color.WHITE);
            canvas.drawArc(mRect, 128, 284, false, paint);
        }*/
        paint.setStrokeWidth(0.5F*mWidth);
        //如果mProgress<360,则圆形进度条还未旋转完成，则用0x7f的透明度绘制一个完整的圆形作为进度条背景
        //注意要先绘制背景条，再绘制进度条，因为后绘制的会覆盖在先绘制的上面
        if (mProgress < 360) {
            paint.setAlpha(127);
            paint.setColor(mbgcolor);
            canvas.drawArc(mRect, 130,280, false, paint);
        }
        {
            float degree = (float) (280f * mProgress);
            paint.setAlpha(0xff);
            paint.setColor(processColor);
            //drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
            //该方法参数：
            //oval表示绘制椭圆的矩形边界；
            //startAngle表示起始角度；
            //useCenter
            //paint表示要使用的画笔；
            canvas.drawArc(mRect, 130, degree, false, paint);
        }
    }
    
    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //mDiameter表示小圆直径，mWidth表示圆环宽度的2倍，所以meas表示大圆直径
        //所以View的hight，width都为meas
        final int meas = mDiameter + mWidth;
        setMeasuredDimension(meas, meas);
    }
    
    public void setProgress(int p) {
        mProgress = p;
        invalidate();
    }
    
    public void setParas(float progress,boolean isoveruse){
    	if(isoveruse){
    		this.mProgress=1;
    	}else 
    		mProgress=progress;
    	this.postInvalidate();
    }
    
    public void postProgress(final int p) {
        post(new Runnable(){
            @Override
            public void run() {
                setProgress(180);
            }
        });
    }
}