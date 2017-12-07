package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import goodmood.harman.com.goodmood.R;

/**
 * Created by SSyukov on 07-Dec-17.
 */

public class ProximitySettingActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;
    private android.widget.LinearLayout mConstantlyLayout;
    private android.widget.RadioButton mRBConstantly;
    private android.widget.RadioButton mRBDayNight;
    private android.widget.LinearLayout mDayNightLayout;
    private android.widget.RadioButton mRBTtimeSchedule;
    private android.widget.LinearLayout mTimeScheduleLayout;

    private LinearLayout[] mLayoutArray = new LinearLayout[3];
    private RadioButton[] mButtonArray = new RadioButton[3];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_settings);

        mLayoutArray[0] = (LinearLayout) findViewById(R.id.time_schedule_layout);
        mLayoutArray[1] = (LinearLayout) findViewById(R.id.day_night_layout);
        mLayoutArray[2] = (LinearLayout) findViewById(R.id.constantly_layout);

        mButtonArray[0] = (RadioButton) findViewById(R.id.rb_time_schedule);
        mButtonArray[1] = (RadioButton) findViewById(R.id.rb_day_night);
        mButtonArray[2] = (RadioButton) findViewById(R.id.rb_constantly);

        for (int i = 0; i < 3; i++) {
            final int index = i;
            mButtonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChecked(index);
                }
            });

            mLayoutArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChecked(index);
                }
            });
        }
    }

    public void setChecked(int index) {
        for (int i = 0; i < 3; i++) {
            if (i == index) {
                mButtonArray[i].setChecked(true);
            } else {
                mButtonArray[i].setChecked(false);
            }
        }
    }
}
