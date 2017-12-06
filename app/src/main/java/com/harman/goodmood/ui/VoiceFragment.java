package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harman.goodmood.util.weather.MoodIconLayout;

import goodmood.harman.com.goodmood.R;

public class VoiceFragment extends Fragment {

    private MoodIconLayout[] mMoodLayoutArray = new MoodIconLayout[5];

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Voice");

        mMoodLayoutArray[0] = (MoodIconLayout) view.findViewById(R.id.mood_neutrality);
        mMoodLayoutArray[1] = (MoodIconLayout) view.findViewById(R.id.mood_sadness);
        mMoodLayoutArray[2] = (MoodIconLayout) view.findViewById(R.id.mood_happiness);
        mMoodLayoutArray[3] = (MoodIconLayout) view.findViewById(R.id.mood_fear);
        mMoodLayoutArray[4] = (MoodIconLayout) view.findViewById(R.id.mood_angel);

        mMoodLayoutArray[0].setResources(R.drawable.neutrality, R.drawable.happiness_clr, R.drawable.neutrality_bg);
        mMoodLayoutArray[1].setResources(R.drawable.sadness, R.drawable.sadness_clr, R.drawable.sadness_bg);
        mMoodLayoutArray[2].setResources(R.drawable.happiness, R.drawable.happiness_clr, R.drawable.happiness_bg);
        mMoodLayoutArray[3].setResources(R.drawable.fear, R.drawable.fear_clr, R.drawable.fear_bg);
        mMoodLayoutArray[4].setResources(R.drawable.angel, R.drawable.angel_clr, R.drawable.angel_bg);

        return view;
    }

    public void setCheckedMood(int index) {
        if (index >= 0 && index <= 5) {
            for (int i = 0; i < 5; i++) {
                mMoodLayoutArray[i].setChecked(i == index);
            }
        }
    }
}
