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

import goodmood.harman.com.goodmood.R;

public class MusicFragment extends Fragment {
    private ImageView mMicView;
    private boolean mMicIsEnable;

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

        return view;
    }

    public void setMicEnable(boolean isEnable) {
        mMicIsEnable = isEnable;
        if (isEnable) {
            mMicView.setBackground(getResources().getDrawable(R.drawable.mic_anim));
            ((AnimationDrawable) mMicView.getBackground()).start();

        } else {
            ((AnimationDrawable) mMicView.getBackground()).stop();
            mMicView.setBackground(getResources().getDrawable(R.drawable.mic_anim_1));

        }
    }
}
