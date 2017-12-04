package com.harman.goodmood;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.harman.goodmood.mqtt.SmartBulbManager;
import com.harman.goodmood.recognizer.SpeachRecognizerListener;
import com.harman.goodmood.recognizer.SpeachRecognizerManager;

import goodmood.harman.com.goodmood.R;

public class MainActivity extends AppCompatActivity implements SpeachRecognizerListener {

    private SmartBulbManager mBulbManager;
    private SpeachRecognizerManager mSpeachManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBulbManager = SmartBulbManager.getInstance(this);
        mSpeachManager = SpeachRecognizerManager.getInstance(this);
        mSpeachManager.addListener(this);

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

        findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeachManager.stopListening();
            }
        });

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeachManager.startListening();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBeginSpeach() {

    }

    @Override
    public void onEndSpeach() {

    }

    @Override
    public void onPartialResult(String text) {
        if (text.startsWith("set color")) {
            boolean isExecuted = false;
            String[] components = text.split(" ");
            if (components.length > 2) {
                String color = components[2];
                if (color.equals("red")) {
                    try {
                        mBulbManager.setRGBComponents(0, 0xFF, 0);
                        isExecuted = true;
                    } catch (Exception exc) {}
                } else if (color.equals("green")) {
                    try {
                        mBulbManager.setRGBComponents(0, 0xFF, 0);
                        isExecuted = true;
                    } catch (Exception exc) {}
                } else if (color.equals("blue")) {
                    try {
                        mBulbManager.setRGBComponents(0, 0, 0xFF);
                        isExecuted = true;
                    } catch (Exception exc) {}
                }
                if (isExecuted) {
                    mSpeachManager.stopListening();
                }
            }
        }
    }

    @Override
    public void onFinalResult(String text) {
        Log.d("", text);
    }

    @Override
    public void onInitCompleted() {

    }
}
