package com.hebut.czh.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.hebut.czh.R;
import com.hebut.czh.util.LocalDisplay;

/**
 * Created by hzh on 2016/5/30.
 */
public class HorizontalProgressBar extends ProgressBar
{
    /**
     * 自定义属性提供默认值
     */
    private static final int DEFAULT_TEXT_SIZE = 10; //sp
    private static final int DEFAULT_TEXT_COLOR = 0xfffc00d1;
    private static final int DEFAULT_UNREACH_COLOR = 0xffd3d6da;
    private static final int DEFAULT_UNREACH_HEIGHT = 2; //dp
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_REACH_HEIGHT = 2;  //dp
    private static final int DEFAULT_TEXT_BAR_SPACE = 10;   //dp

    private int mTextSize = LocalDisplay.sp2px(DEFAULT_TEXT_SIZE);
    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mUnreachColor = DEFAULT_UNREACH_COLOR;
    private int mUnreachHeight = LocalDisplay.dp2px(DEFAULT_UNREACH_HEIGHT);
    private int mReachColor = DEFAULT_REACH_COLOR;
    private int mReachHeight = LocalDisplay.dp2px(DEFAULT_REACH_HEIGHT);
    private int mTextBarSpace = LocalDisplay.dp2px(DEFAULT_TEXT_BAR_SPACE);

    private Paint mPaint = new Paint();

    private int mRealWidth;

    public HorizontalProgressBar(Context context)
    {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);

        LocalDisplay.init(context);

        //获取自动以属性
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.HorizontalProgressBar);
        mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_text_size,
                mTextSize);
        mTextColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_text_color,
                mTextColor);
        mUnreachColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_unreach_color,
                mUnreachColor);
        mUnreachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_unreach_height,
                mUnreachHeight);
        mReachColor = ta.getColor(R.styleable.HorizontalProgressBar_progress_reach_color,
                mReachColor);
        mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_reach_height,
                mReachHeight);
        mTextBarSpace = (int) ta.getDimension(R.styleable.HorizontalProgressBar_progress_text_bar_space,
                mTextBarSpace);
        ta.recycle();

        mPaint.setTextSize(mTextSize);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //宽度不支持wrap_cotent,用户必须给出一个明确值
        int withSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSize = measureHeight(heightMeasureSpec);

        setMeasuredDimension(withSize, heightSize);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int measureHeight(int heightMeasureSpec)
    {
        int result = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        if(mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() +
                    Math.max(Math.max(mReachHeight, mUnreachHeight), Math.abs(textHeight));

            //不能超过父控件的高度
            if(mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        canvas.save();
        //移动canva的坐标，方便下面绘制
        canvas.translate(getPaddingLeft(), getHeight() / 2);

        //draw reached
        boolean noNeedForUnreach = false;

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        float ratio = getProgress() * 1.0f / getMax();
        float progressX = ratio * mRealWidth;
        if(progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            noNeedForUnreach = true;
        }

        float endX = progressX - mTextBarSpace / 2;
        if(endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }

        //draw test
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, y, mPaint);

        //draw unreached
        if(!noNeedForUnreach) {
            float start = progressX + textWidth + mTextBarSpace / 2;
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(start, 0, mRealWidth, 0, mPaint);
        }

        canvas.restore();
    }
}
