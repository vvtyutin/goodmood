package com.harman.goodmood.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.harman.goodmood.mqtt.SmartBulbManager;
import com.harman.goodmood.util.FrameAnimation;
import com.harman.goodmood.util.weather.MoodIconLayout;
import com.projects.alshell.vokaturi.Emotion;
import com.projects.alshell.vokaturi.EmotionProbabilities;
import com.projects.alshell.vokaturi.Vokaturi;
import com.projects.alshell.vokaturi.VokaturiException;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import goodmood.harman.com.goodmood.R;

public class VoiceFragment extends Fragment {

    private static final int LISTENING_DURATION = 7 * 1000;

    private static final int[] COLORS = new int[]{0xFFFF00, 0x00FFCC, 0x999999, 0xFF0000, 0x00FF00};

    private MoodIconLayout[] mMoodLayoutArray = new MoodIconLayout[5];
    private ImageView mMicView;

    private boolean mMicIsEnable;
    private int mCheckedMood = 0;

    private FrameAnimation mFrameAnimation;

    private Vokaturi mVokaturiApi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Voice");

        mMicView = (ImageView) view.findViewById(R.id.mic_view);

        mMoodLayoutArray[0] = (MoodIconLayout) view.findViewById(R.id.mood_neutrality);
        mMoodLayoutArray[1] = (MoodIconLayout) view.findViewById(R.id.mood_sadness);
        mMoodLayoutArray[2] = (MoodIconLayout) view.findViewById(R.id.mood_happiness);
        mMoodLayoutArray[3] = (MoodIconLayout) view.findViewById(R.id.mood_fear);
        mMoodLayoutArray[4] = (MoodIconLayout) view.findViewById(R.id.mood_angel);

        mMoodLayoutArray[0].setResources(R.drawable.neutrality, R.drawable.neutrality_clr, R.drawable.neutrality_bg);
        mMoodLayoutArray[1].setResources(R.drawable.sadness, R.drawable.sadness_clr, R.drawable.sadness_bg);
        mMoodLayoutArray[2].setResources(R.drawable.happiness, R.drawable.happiness_clr, R.drawable.happiness_bg);
        mMoodLayoutArray[3].setResources(R.drawable.fear, R.drawable.fear_clr, R.drawable.fear_bg);
        mMoodLayoutArray[4].setResources(R.drawable.angel, R.drawable.angel_clr, R.drawable.angel_bg);

        for (int i = 0; i < 5; i++) {
            final int finalIndex = i;
            mMoodLayoutArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCheckedMood(finalIndex);
                }
            });
        }

        ArrayList<Integer> animationFrames = new ArrayList<>();

        animationFrames.add(R.drawable.mic_anim_1);
        animationFrames.add(R.drawable.mic_anim_2);
        animationFrames.add(R.drawable.mic_anim_3);
        animationFrames.add(R.drawable.mic_anim_4);
        animationFrames.add(R.drawable.mic_anim_5);
        animationFrames.add(R.drawable.mic_anim_6);
        animationFrames.add(R.drawable.mic_anim_7);
        animationFrames.add(R.drawable.mic_anim_8);
        animationFrames.add(R.drawable.mic_anim_9);

        mFrameAnimation = new FrameAnimation(getActivity());
        mFrameAnimation.setFrames(animationFrames);
        mFrameAnimation.setLooped(true);


        mMicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMicIsEnable) {
                    setMicEnable(false);
                    try {
                        highlight(mVokaturiApi.stopListeningAndAnalyze());
                    } catch (VokaturiException e) {
                        e.printStackTrace();
                    }
                } else {
                    setMicEnable(true);
                    startListen();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mVokaturiApi = Vokaturi.getInstance(getContext());
        } catch (VokaturiException e) {
            e.printStackTrace();
        }

        //        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.how_are_you);
        //        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        //            @Override
        //            public void onCompletion(MediaPlayer mediaPlayer) {
        //                if (!isDetached()) {
        //                    startListen();
        //                }
        //            }
        //        });
        //        mediaPlayer.start();

    }

    @Override
    public void onPause() {
        super.onPause();

        if (mMicIsEnable) {
            setMicEnable(false);
            try {
                mVokaturiApi.stopListeningAndAnalyze();
            } catch (VokaturiException e) {
                e.printStackTrace();
            }
        }


    }

    private void startListen() {
        try {
            mVokaturiApi.startListeningForSpeech();
        } catch (VokaturiException e) {
            if (e.getErrorCode() == VokaturiException.VOKATURI_DENIED_PERMISSIONS) {
                Toast.makeText(getContext(), "Grant Microphone and Storage permissions in the app settings to proceed", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "There was some problem to start listening audio", Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }
    }

    private void stopListenTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setMicEnable(false);
                            try {
                                highlight(mVokaturiApi.stopListeningAndAnalyze());
                            } catch (VokaturiException e) {
                                e.printStackTrace();
                                if (e.getErrorCode() == VokaturiException.VOKATURI_NOT_ENOUGH_SONORANCY) {
                                    Toast.makeText(getContext(), "Please speak a more clear and avoid noise around your environment", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, LISTENING_DURATION);
    }

    public void setMicEnable(boolean isEnable) {
        mMicIsEnable = isEnable;
        if (isEnable) {
            mMicView.setImageDrawable(getResources().getDrawable(R.drawable.mic_anim_1));
            mFrameAnimation.start(mMicView);
        } else {
            mFrameAnimation.stop();
            mMicView.setImageDrawable(getResources().getDrawable(R.drawable.mic_anim_1));
        }
    }

    public void setCheckedMood(int index) {
        SmartBulbManager.getInstance(getContext()).setRGB(COLORS[index]);
        for (int i = 0; i < 5; i++) {
            mMoodLayoutArray[i].setChecked(i == index);
        }
    }

    private void highlight(EmotionProbabilities emotionProbabilities) {
        Emotion emotion = Vokaturi.extractEmotion(emotionProbabilities);
        int index = 0;
        switch (emotion) {
            case Neutral:
                index = 0;
                break;
            case Sad:
                index = 1;
                break;
            case Happy:
                index = 2;
                break;
            case Angry:
                index = 3;
                break;
            case Feared:
                index = 4;
                break;
        }
        setCheckedMood(index);
    }
}
