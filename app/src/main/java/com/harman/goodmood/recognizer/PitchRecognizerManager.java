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

    private AudioDispatcher mAudioDispatcher;
    private PitchProcessor mPitchProcessor;
    private Thread mAudioThread;

    private double mTimeStampPreviousPitch = -1.;
    private int mPitchCounter = 0;

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

                        PitchRecognizerManager.this.sendColorForPitches();

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
        mAudioDispatcher =
                AudioDispatcherFactory.fromDefaultMicrophone(SAMPLE_RATE, BUFFER_SIZE, OVERLAP);
        mPitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, SAMPLE_RATE, BUFFER_SIZE, mPitchDetectionHandler);
        mAudioDispatcher.addAudioProcessor(mPitchProcessor);
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

        mAudioThread = new Thread(mAudioDispatcher, "Audio Thread");
        mAudioThread.start();
    }

    public void stopListening() {

        if (mAudioThread != null) {
            mAudioDispatcher.stop();
            mAudioThread = null;
        }
    }

    private void sendColorForPitches() {
        int r1 = 0, r2 = 0;
        int g1 = 0, g2 = 0;
        int b1 = 0, b2 = 0;

        if (mPitchCounter > FAST_TEMP_BOTTOM_THRESHOLD && mPitchCounter <= FAST_TEMP_TOP_THRESHOLD) {
            r1 = 255;
            r2 = 255;
            g2 = 255;
        } else if (mPitchCounter > MID_TEMP_BOTTOM_THRESHOLD && mPitchCounter <= MID_TEMP_TOP_THRESHOLD) {
            r1 = 255;
            g1 = 255;
            g2 = 255;
        } else {
            g1 = 255;
            b2 = 255;
        }

        SmartBulbManager.getInstance(mContext).setRGBComponents(r1, g1, b1);
        SmartBulbManager.getInstance(mContext).setRGBComponents(r2, g2, b2);

    }

    public interface PitchRecognizerListener {
        void onPitchCounterInSeconds(int counter, int seconds);

        void onPitchResultChanged(float pitch, double timeStamp, float probability, double rms);
    }
}
