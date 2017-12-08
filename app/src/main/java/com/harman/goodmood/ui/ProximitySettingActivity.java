package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.harman.goodmood.beacon.BeaconRecognitionManager;
import com.harman.goodmood.util.weather.Settings;

import goodmood.harman.com.goodmood.R;

/**
 * Created by SSyukov on 07-Dec-17.
 */

public class ProximitySettingActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    private android.widget.LinearLayout mOffLayout;
    private android.widget.LinearLayout mConstantlyLayout;
    private android.widget.LinearLayout mDayNightLayout;

    private android.widget.RadioButton mRBOff;
    private android.widget.RadioButton mRBConstantly;
    private android.widget.RadioButton mRBDayNight;

    private LinearLayout[] mLayoutArray = new LinearLayout[3];
    private RadioButton[] mButtonArray = new RadioButton[3];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_settings);

        mLayoutArray[0] = (LinearLayout) findViewById(R.id.off_layout);
        mLayoutArray[1] = (LinearLayout) findViewById(R.id.constantly_layout);
        mLayoutArray[2] = (LinearLayout) findViewById(R.id.day_night_layout);

        mButtonArray[0] = (RadioButton) findViewById(R.id.rb_off);
        mButtonArray[1] = (RadioButton) findViewById(R.id.rb_constantly);
        mButtonArray[2] = (RadioButton) findViewById(R.id.rb_day_night);

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

        switch (Settings.getProximityState()) {
            case BeaconRecognitionManager.STATE_DISABLED:
                mButtonArray[0].setChecked(true);
                break;
            case BeaconRecognitionManager.STATE_CONSTANTLY:
                mButtonArray[1].setChecked(true);
                break;
            case BeaconRecognitionManager.STATE_DAY_NIGHT:
                mButtonArray[2].setChecked(true);
                break;
        }

    }

    public void setChecked(int index) {
        for (int i = 0; i < 3; i++) {
            if (i == index) {
                mButtonArray[i].setChecked(true);
                Settings.setProximityState(index);
                finish();
                break;
            } else {
                mButtonArray[i].setChecked(false);
            }
        }
    }
}
