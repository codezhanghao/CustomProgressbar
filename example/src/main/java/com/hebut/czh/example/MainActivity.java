package com.hebut.czh.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hebut.czh.view.HorizontalProgressBar;
import com.hebut.czh.view.RoundProgressBar;


public class MainActivity extends AppCompatActivity
{

    private HorizontalProgressBar mHorizontaPB;
    private RoundProgressBar mRoundPB;
    private ProgressRunnable mProgressRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHorizontaPB = (HorizontalProgressBar) findViewById(R.id.id_progressbar01);
        mRoundPB = (RoundProgressBar) findViewById(R.id.id_progressbar02);
        mProgressRunnable = new ProgressRunnable();
        mHorizontaPB.post(mProgressRunnable);
    }

    private class ProgressRunnable implements Runnable
    {
        @Override
        public void run()
        {
            int progress = mHorizontaPB.getProgress();
            if(progress >= 100) {
                mHorizontaPB.removeCallbacks(this);
                return;
            }
            mHorizontaPB.setProgress(++progress);
            mRoundPB.setProgress(progress);
            mHorizontaPB.postDelayed(this, 100);
        }
    }
}
