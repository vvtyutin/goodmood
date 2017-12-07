package com.harman.goodmood.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.harman.goodmood.util.FrameAnimation;
import com.harman.goodmood.util.weather.MoodIconLayout;

import java.util.ArrayList;

import goodmood.harman.com.goodmood.R;

public class VoiceFragment extends Fragment {

    private MoodIconLayout[] mMoodLayoutArray = new MoodIconLayout[5];
    private ImageView mMicView;

    private boolean mMicIsEnable;
    private int mCheckedMood = 0;

    private FrameAnimation mFrameAnimation;

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

        ArrayList<Integer> integers = new ArrayList<>();

        integers.add(R.drawable.mic_anim_1);
        integers.add(R.drawable.mic_anim_2);
        integers.add(R.drawable.mic_anim_3);
        integers.add(R.drawable.mic_anim_4);
        integers.add(R.drawable.mic_anim_5);
        integers.add(R.drawable.mic_anim_6);
        integers.add(R.drawable.mic_anim_7);
        integers.add(R.drawable.mic_anim_8);
        integers.add(R.drawable.mic_anim_9);

        mFrameAnimation = new FrameAnimation(getActivity());
        mFrameAnimation.setFrames(integers);
        mFrameAnimation.setLooped(true);


        mMicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMicEnable(!mMicIsEnable);
            }
        });

        return view;
    }

    public void setMicEnable(boolean isEnable) {
        mMicIsEnable = isEnable;
        if (isEnable) {
            mMicView.setImageDrawable(getResources().getDrawable(R.drawable.mic_anim));
            mFrameAnimation.start(mMicView);

            //TODO remove the following string
            setCheckedMood(mCheckedMood++ % 5);

        } else {
            mFrameAnimation.stop();
            mMicView.setImageDrawable(getResources().getDrawable(R.drawable.mic_anim_1));

            //TODO remove the following string
            setCheckedMood(-1);
        }
    }


    public void setCheckedMood(int index) {
        for (int i = 0; i < 5; i++) {
            mMoodLayoutArray[i].setChecked(i == index);
        }
    }
}
