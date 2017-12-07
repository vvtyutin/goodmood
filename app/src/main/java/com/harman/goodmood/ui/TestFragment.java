package com.harman.goodmood.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.harman.goodmood.mqtt.SmartBulbManager;
import com.harman.goodmood.recognizer.PitchRecognizerManager;
import com.harman.goodmood.recognizer.SpeachRecognizerListener;
import com.harman.goodmood.recognizer.SpeachRecognizerManager;

import goodmood.harman.com.goodmood.R;

public class TestFragment extends Fragment implements SpeachRecognizerListener, PitchRecognizerManager.PitchRecognizerListener {

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;
    private SmartBulbManager mBulbManager;
    private SpeachRecognizerManager mSpeachManager;
    private TextView mSpeachTextView;
    private TextView mPitchTextView;
    private TextView mPitchCounterTextView;
    private Button mStartRecordButton;
    private Button mStopRecordButton;
    private View mBackgroundView;

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
        mPitchTextView = (TextView) view.findViewById(R.id.pitchHzID);
        mStartRecordButton = (Button) view.findViewById(R.id.start_record_button_id);
        mStopRecordButton = (Button) view.findViewById(R.id.stop_record_button_id);
        mPitchCounterTextView = (TextView) view.findViewById(R.id.pitchCounterTextView);
        mBackgroundView = (View)view.findViewById(R.id.backgroundView);

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


        mStartRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    PitchRecognizerManager.getInstance(getActivity()).startListening();
                }
            }
        });

        mStopRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    PitchRecognizerManager.getInstance(getActivity()).stopListening();
                }
            }
        });

//        requestAudioPermissions();

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

//    private void requestAudioPermissions() {
//        if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                    Manifest.permission.RECORD_AUDIO)) {
//                Toast.makeText(getActivity(), "Please grant permissions to record audio", Toast.LENGTH_LONG).show();
//
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.RECORD_AUDIO},
//                        MY_PERMISSIONS_RECORD_AUDIO);
//
//            } else {
//
//                ActivityCompat.requestPermissions(getActivity(),
//                        new String[]{Manifest.permission.RECORD_AUDIO},
//                        MY_PERMISSIONS_RECORD_AUDIO);
//            }
//        } else if (ContextCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.RECORD_AUDIO)
//                == PackageManager.PERMISSION_GRANTED) {
//
//            PitchRecognizerManager.getInstance(getActivity()).addListener(this);
//        }
//    }
//
//    //Handling callback
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_RECORD_AUDIO: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    PitchRecognizerManager.getInstance(getActivity()).addListener(this);
//                } else {
//                    Toast.makeText(getActivity(), "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }

    @Override
    public void onPitchCounterInSeconds(final int counter, final int seconds) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPitchCounterTextView.setText("" + counter + " pitch(es) in " + seconds + " seconds");
            }
        });
    }

    @Override
    public void onPitchResultChanged(final float pitch, final double timeStamp, final float probability, final double rms) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = String.format("Pitch detected at %.2fs: %.2fHz ( %.2f probability, RMS: %.5f )\n", timeStamp, pitch, probability, rms);
                mPitchTextView.setText(message);
            }
        });
    }

    @Override
    public void onRGBUpdated(final int r, final int g, final int b) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                int color = Color.rgb(r,g,b);
                mBackgroundView.setBackgroundColor(color);
            }
        });
    }
}
