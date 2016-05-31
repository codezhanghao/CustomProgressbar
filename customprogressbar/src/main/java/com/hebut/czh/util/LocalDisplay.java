package com.hebut.czh.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * Created by hzh on 2016/5/30.
 */
public class LocalDisplay
{
    public static boolean mIsInit = false;
    public static DisplayMetrics mDisplayMetrics;

    public static int SCREEN_WIDTH_PIXELS;
    public static int SCREEN_HEIGHT_PIXELS;
    public static float SCREEN_DENSITY;
    public static int SCREEN_WIDTH_DP;
    public static int SCREEN_HEIGHT_DP;

    public static void init(Context context)
    {
        if(context == null || mIsInit) {
            return;
        }
        mIsInit = true;
        mDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
        SCREEN_WIDTH_PIXELS = mDisplayMetrics.widthPixels;
        SCREEN_HEIGHT_PIXELS = mDisplayMetrics.heightPixels;
        SCREEN_DENSITY = mDisplayMetrics.density;
        SCREEN_WIDTH_DP = (int) (SCREEN_WIDTH_PIXELS / mDisplayMetrics.density);
        SCREEN_HEIGHT_DP = (int) (SCREEN_HEIGHT_PIXELS / mDisplayMetrics.density);
    }

    public static int dp2px(int dp) {
        if(!mIsInit) {
            throw new IllegalStateException("please invoke init method first");
        }
        final float scale = SCREEN_DENSITY;
        return (int) (dp * scale + 0.5f);
    }

    public static int sp2px(int sp) {
        if(!mIsInit) {
            throw new IllegalStateException("please invoke init method first");
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp,
                mDisplayMetrics);
    }

}
