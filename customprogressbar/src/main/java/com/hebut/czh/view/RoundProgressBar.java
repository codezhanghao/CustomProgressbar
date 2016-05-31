package com.hebut.czh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.hebut.czh.R;
import com.hebut.czh.util.LocalDisplay;

/**
 * Created by hzh on 2016/5/30.
 */
public class RoundProgressBar extends ProgressBar
{
    /**
     * 为自定义属性提供默认值
     */
    private static final int DEFAULT_TEXT_SIZE = 10; //sp
    private static final int DEFAULT_TEXT_COLOR = 0xfffc00d1;
    private static final int DEFAULT_UNREACH_COLOR = 0xffd3d6da;
    private static final int DEFAULT_UNREACH_HEIGHT = 2; //dp
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_REACH_HEIGHT = 2;  //dp
    private static final int DEFAULT_RADIUS = 30;   //dp

    private int mTextSize = LocalDisplay.sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnreachColor = DEFAULT_UNREACH_COLOR;
    private int mUnreachHeight = LocalDisplay.dp2px(DEFAULT_UNREACH_HEIGHT);
    private int mReachColor = DEFAULT_REACH_COLOR;
    private int mReachHeight = LocalDisplay.dp2px(DEFAULT_REACH_HEIGHT);
    private int mRadius = LocalDisplay.dp2px(DEFAULT_RADIUS);

    private Paint mPaint = new Paint();
    private int mMaxPaintWidth;

    public RoundProgressBar(Context context)
    {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        LocalDisplay.init(context);

        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);
        mTextSize = (int) ta.getDimension(R.styleable.RoundProgressBar_progress_text_size,
                mTextSize);
        mTextColor = ta.getColor(R.styleable.RoundProgressBar_progress_text_color,
                mTextColor);
        mUnreachColor = ta.getColor(R.styleable.RoundProgressBar_progress_unreach_color,
                mUnreachColor);
        mUnreachHeight = (int) ta.getDimension(R.styleable.RoundProgressBar_progress_unreach_height,
                mUnreachHeight);
        mReachColor = ta.getColor(R.styleable.RoundProgressBar_progress_reach_color,
                mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.RoundProgressBar_progress_reach_height,
                mReachHeight);
        mRadius = (int) ta.getDimension(R.styleable.RoundProgressBar_progress_radius,
                mRadius);

        ta.recycle();

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        mMaxPaintWidth = Math.max(mReachHeight, mUnreachHeight);

        int expectSize = mRadius * 2 + mMaxPaintWidth + getPaddingLeft() + getPaddingRight();

        //系统提供测量的模板代码
        int width = resolveSize(expectSize, widthMeasureSpec);
        int height = resolveSize(expectSize, heightMeasureSpec);

        //当用户把宽度和高度设置成不一样的值
        int realWidth = Math.min(width, height);

        mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

        setMeasuredDimension(realWidth, realWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        canvas.save();

        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2,
                getPaddingTop() + mMaxPaintWidth / 2);
        //draw unreach
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mUnreachHeight);
        mPaint.setColor(mUnreachColor);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);

        //draw arc
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        RectF rectF = new RectF(0, 0, mRadius * 2, mRadius * 2);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(rectF, 0, sweepAngle, false, mPaint);

        //draw text
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        String text = getProgress() + "%";
        int offset = (int) (mPaint.descent() + mPaint.ascent()) / 2;
        int textWidth = (int) mPaint.measureText(text);

        canvas.drawText(text,
                mRadius + mMaxPaintWidth / 2 - textWidth / 2,
                mRadius + mMaxPaintWidth / 2 - offset,
                mPaint);

        canvas.restore();
    }
}
