package com.harman.goodmood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.harman.goodmood.mqtt.SmartBulbManager;

import goodmood.harman.com.goodmood.R;

public class MainActivity extends AppCompatActivity {

    private SmartBulbManager mBulbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBulbManager = SmartBulbManager.getInstance(this);

        findViewById(R.id.redButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGBComponents(0xFF, 0, 0);
                } catch (Exception exc) {}
            }
        });

        findViewById(R.id.greenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGBComponents(0, 0xFF, 0);
                } catch (Exception exc) {}
            }
        });

        findViewById(R.id.blueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGBComponents(0, 0, 0xFF);
                } catch (Exception exc) {}
            }
        });

        findViewById(R.id.offButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGB(0);
                } catch (Exception exc) {}
            }
        });
    }
}
