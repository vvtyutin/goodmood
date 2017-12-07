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

import java.util.ArrayList;

import goodmood.harman.com.goodmood.R;

public class MusicFragment extends Fragment {
    private ImageView mMicView;
    private boolean mMicIsEnable;

    private FrameAnimation mFrameAnimation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("Music");

        mMicView = (ImageView) view.findViewById(R.id.mic_view);
        mMicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMicEnable(!mMicIsEnable);
            }
        });

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


        mFrameAnimation = new FrameAnimation(getActivity(), mMicView);
        mFrameAnimation.setFrames(integers);
        mFrameAnimation.setLooped(true);

        return view;
    }

    public void setMicEnable(boolean isEnable) {
        mMicIsEnable = isEnable;
        if (isEnable) {
            mFrameAnimation.start();
        } else {
            mFrameAnimation.stop();
        }
    }
}
