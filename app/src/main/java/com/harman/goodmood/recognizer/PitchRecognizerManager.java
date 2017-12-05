package com.harman.goodmood.recognizer;

import android.content.Context;

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

    private static int SAMPLE_RATE = 22050;
    private static int BUFFER_SIZE = 1024;
    private static int OVERLAP = 0;

    private AudioDispatcher mAudioDispatcher;
    private PitchProcessor mPitchProcessor;
    private Thread mAudioThread;

    private double mTimeStampPreviousPitch = -1.;
    private int mPinchCounter = 0;
    private double mTotalPinches = 0;

    PitchDetectionHandler mPitchDetectionHandler = new PitchDetectionHandler() {
        @Override
        public void handlePitch(PitchDetectionResult res, AudioEvent e) {
            final float pitchInHz = res.getPitch();

            if (pitchInHz != -1 /*&& !e.isSilence(silenceThreshold)*/) {

                if (mTimeStampPreviousPitch == -1.) {
                    mTimeStampPreviousPitch = System.currentTimeMillis();
                } else {
                    final long timestamp = System.currentTimeMillis();
                    if (timestamp - mTimeStampPreviousPitch <= 1000) {
                        mPinchCounter++;
                        mTotalPinches +=pitchInHz;
                    } else {

                        if (listeners != null) {
                            for (PitchRecognizerListener listener : listeners) {
                                double average = mPinchCounter>0 ? mTotalPinches /mPinchCounter : 0;
                                listener.onBitPerMinutes(average, mPinchCounter);
                            }
                        }

                        mTimeStampPreviousPitch = timestamp;
                        mPinchCounter = 0;
                        mTotalPinches = 0;
                    }

                }

                if (listeners != null) {
                    for (PitchRecognizerListener listener : listeners) {
                        listener.onPitchChanged(pitchInHz);
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
            mAudioThread = null;
        }
    }

    public interface PitchRecognizerListener {
        void onPitchChanged(float pitch);
        void onBitPerMinutes(double averagePinch, int numberOfPinchInSecond);
    }
}
