package com.harman.goodmood.recognizer;

import android.content.Context;

import com.harman.goodmood.mqtt.SmartBulbManager;

import java.util.ArrayList;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

/**
 * Created by antonsavinov on 05.12.17.
 */

public class PitchRecognizerManager {

    private static PitchRecognizerManager sInstance = null;
    private Context mContext;
    private ArrayList<PitchRecognizerListener> listeners = new ArrayList<>();

    private static int SAMPLE_RATE = 44100;
    private static int BUFFER_SIZE = 2048;
    private static int OVERLAP = 0;
    private static int BUCKET_SIZE = 2000;

    private static int PITCH_COLORS_SIZE = 11;

    private AudioDispatcher mAudioDispatcher;
    private PitchProcessor mPitchProcessor;
    private Thread mAudioThread;

    private double mTimeStampPreviousPitch = -1.;
    private int mPitchCounter = 0;
    private int mColorIndex = 0;
    private PitchColor[] mPitchColors = new PitchColor[PITCH_COLORS_SIZE];

    private static int FAST_TEMP_BOTTOM_THRESHOLD = 0;
    private static int FAST_TEMP_TOP_THRESHOLD = 7;
    private static int MID_TEMP_BOTTOM_THRESHOLD = 8;
    private static int MID_TEMP_TOP_THRESHOLD = 25;
    private static int LOW_TEMP_BOTTOM_THRESHOLD = 26;

    PitchDetectionHandler mPitchDetectionHandler = new PitchDetectionHandler() {
        @Override
        public void handlePitch(PitchDetectionResult pitchDetectionResult, AudioEvent audioEvent) {
            final float pitchInHz = pitchDetectionResult.getPitch();

            if (pitchInHz != -1 /*&& !e.isSilence(silenceThreshold)*/) {

                double timeStamp = audioEvent.getTimeStamp();
                float probability = pitchDetectionResult.getProbability();
                double rms = audioEvent.getRMS() * 100;


                if (mTimeStampPreviousPitch == -1.) {
                    mTimeStampPreviousPitch = System.currentTimeMillis();
                } else {

                    final long timestamp = System.currentTimeMillis();

                    if (timestamp - mTimeStampPreviousPitch < BUCKET_SIZE) {
                        mPitchCounter++;
                    } else {
                        if (listeners != null) {
                            for (PitchRecognizerListener listener : listeners) {
                                listener.onPitchCounterInSeconds(mPitchCounter, BUCKET_SIZE / 1000);
                            }
                        }

                        PitchRecognizerManager.this.sendColorForPitches2();

                        mTimeStampPreviousPitch = timestamp;
                        mPitchCounter = 0;
                    }

                }

                if (listeners != null) {
                    for (PitchRecognizerListener listener : listeners) {
                        listener.onPitchResultChanged(pitchInHz, timeStamp, probability, rms);
                    }
                }
            }
        }


    };

    private PitchRecognizerManager(Context context) {
        mContext = context;
        initializeColors();
    }

    private void prepareRecognizer() {
        if (mAudioDispatcher == null) {
            mAudioDispatcher =
                    AudioDispatcherFactory.fromDefaultMicrophone(SAMPLE_RATE, BUFFER_SIZE, OVERLAP);
            mPitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, SAMPLE_RATE, BUFFER_SIZE, mPitchDetectionHandler);
            mAudioDispatcher.addAudioProcessor(mPitchProcessor);
            mColorIndex = 0;
        }
    }

    public static PitchRecognizerManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PitchRecognizerManager(context);
        }
        return sInstance;
    }

    public void addListener(PitchRecognizerListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(PitchRecognizerListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void startListening() {

        if (mAudioThread != null) {
            mAudioThread = null;
        }

        prepareRecognizer();

        mAudioThread = new Thread(mAudioDispatcher, "Audio Thread");
        mAudioThread.start();
    }

    public void stopListening() {

        if (mAudioThread != null) {
            mAudioDispatcher.stop();
            mAudioDispatcher = null;
            mAudioThread = null;
        }
    }

    private void initializeColors() {
        mPitchColors[0] = new PitchColor(255,0,0);
        mPitchColors[1] = new PitchColor(232,64,0);
        mPitchColors[2] = new PitchColor(255,140,0);
        mPitchColors[3] = new PitchColor(232,174,0);
        mPitchColors[4] = new PitchColor(255,240,0);
        mPitchColors[5] = new PitchColor(136,232,0);
        mPitchColors[6] = new PitchColor(0,255,12);
        mPitchColors[7] = new PitchColor(0,255,132);
        mPitchColors[8] = new PitchColor(0,232,229);
        mPitchColors[9] = new PitchColor(0,133,255);
        mPitchColors[10] = new PitchColor(0,0,255);
    }

    private void sendColorForPitches1() {
        int r1 = 0, r2 = 0;
        int g1 = 0, g2 = 0;
        int b1 = 0, b2 = 0;

        if (mPitchCounter > 0 && mPitchCounter < 5) {
            r1 = 255;
        } else if (mPitchCounter >= 5 && mPitchCounter < 10) {
            r1 = 232;
            g1 = 64;
            b1 = 0;
        } else if (mPitchCounter >= 10 && mPitchCounter < 15) {
            r1 = 255;
            g1 = 140;
            b1 = 0;
        } else if (mPitchCounter >= 15 && mPitchCounter < 20) {
            r1 = 232;
            g1 = 174;
            b1 = 0;
        } else if (mPitchCounter >= 20 && mPitchCounter < 25) {
            r1 = 255;
            g1 = 240;
            b1 = 0;
        } else if (mPitchCounter >= 25 && mPitchCounter < 30) {
            r1 = 136;
            g1 = 232;
            b1 = 0;
        } else if (mPitchCounter >= 30 && mPitchCounter < 35) {
            r1 = 0;
            g1 = 255;
            b1 = 12;
        } else if (mPitchCounter >= 35 && mPitchCounter < 40) {
            r1 = 0;
            g1 = 255;
            b1 = 132;
        } else if (mPitchCounter >= 40 && mPitchCounter < 45) {
            r1 = 0;
            g1 = 232;
            b1 = 229;
        } else if (mPitchCounter >= 45 && mPitchCounter < 50) {
            r1 = 0;
            g1 = 133;
            b1 = 255;
        } else if (mPitchCounter >= 50) {
            r1 = 0;
            g1 = 0;
            b1 = 255;
        }

        SmartBulbManager.getInstance(mContext).setRGBComponents(r1, g1, b1);

        if (listeners != null) {
            for (PitchRecognizerListener listener : listeners) {
                listener.onRGBUpdated(r1, g1, b1);
            }
        }
    }

    private void sendColorForPitches2() {

        if (mPitchCounter < 5) {
            mColorIndex+=5;
        } else if (mPitchCounter < 10) {
            mColorIndex+=4;
        } else if (mPitchCounter < 15) {
            mColorIndex+=3;
        } else if (mPitchCounter < 20) {
            mColorIndex+=2;
        } else if (mPitchCounter < 25) {
            mColorIndex+=1;
        }

        if (mColorIndex >= mPitchColors.length ) {
            mColorIndex = mColorIndex - mPitchColors.length;
        }

        PitchColor currentColor = mPitchColors[mColorIndex];

        SmartBulbManager.getInstance(mContext).setRGBComponents(currentColor.getR(), currentColor.getG(), currentColor.getB());

        if (listeners != null) {
            for (PitchRecognizerListener listener : listeners) {
                listener.onRGBUpdated(currentColor.getR(), currentColor.getG(), currentColor.getB());
            }
        }
    }

    public interface PitchRecognizerListener {
        void onPitchCounterInSeconds(int counter, int seconds);

        void onPitchResultChanged(float pitch, double timeStamp, float probability, double rms);

        void onRGBUpdated(int r, int g, int b);
    }

    public class PitchColor {

        public PitchColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int getR() {
            return r;
        }


        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }

        private int r;
        private int g;
        private int b;
    }
}
