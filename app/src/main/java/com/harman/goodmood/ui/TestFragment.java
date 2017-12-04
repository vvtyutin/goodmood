package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harman.goodmood.mqtt.SmartBulbManager;
import com.harman.goodmood.recognizer.SpeachRecognizerListener;
import com.harman.goodmood.recognizer.SpeachRecognizerManager;

import goodmood.harman.com.goodmood.R;

public class TestFragment extends Fragment implements SpeachRecognizerListener {

    private SmartBulbManager mBulbManager;
    private SpeachRecognizerManager mSpeachManager;

    private TextView mSpeachTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBulbManager = SmartBulbManager.getInstance(getActivity());
        mSpeachManager = SpeachRecognizerManager.getInstance(getActivity());
        mSpeachManager.addListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        mSpeachTextView = (TextView) view.findViewById(R.id.commandText);

        view.findViewById(R.id.redButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGBComponents(0xFF, 0, 0);
                } catch (Exception exc) {
                }
            }
        });

        view.findViewById(R.id.greenButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGBComponents(0, 0xFF, 0);
                } catch (Exception exc) {
                }
            }
        });

        view.findViewById(R.id.blueButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGBComponents(0, 0, 0xFF);
                } catch (Exception exc) {
                }
            }
        });

        view.findViewById(R.id.offButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBulbManager.setRGB(0);
                } catch (Exception exc) {
                }
            }
        });

        view.findViewById(R.id.stopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeachTextView.setText("");
                mSpeachManager.stopListening();
            }
        });

        view.findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeachTextView.setText("");
                mSpeachManager.startListening();
            }
        });

        return view;
    }

    @Override
    public void onBeginSpeach() {

    }

    @Override
    public void onEndSpeach() {

    }

    @Override
    public void onPartialResult(String text) {
        mSpeachTextView.setText(text);
        if (text.startsWith("set color")) {
            boolean isExecuted = false;
            String[] components = text.split(" ");
            if (components.length > 2) {
                String color = components[2];
                if (color.equals("red")) {
                    try {
                        mBulbManager.setRGBComponents(0, 0xFF, 0);
                        isExecuted = true;
                    } catch (Exception exc) {
                    }
                } else if (color.equals("green")) {
                    try {
                        mBulbManager.setRGBComponents(0, 0xFF, 0);
                        isExecuted = true;
                    } catch (Exception exc) {
                    }
                } else if (color.equals("blue")) {
                    try {
                        mBulbManager.setRGBComponents(0, 0, 0xFF);
                        isExecuted = true;
                    } catch (Exception exc) {
                    }
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
