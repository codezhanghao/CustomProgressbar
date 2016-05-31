package com.hebut.czh;

import android.app.Application;

import com.hebut.czh.util.LocalDisplay;

/**
 * Created by hzh on 2016/5/30.
 */
public class MyApplication extends Application
{

    @Override
    public void onCreate()
    {
        super.onCreate();

        LocalDisplay.init(this);
    }
}
