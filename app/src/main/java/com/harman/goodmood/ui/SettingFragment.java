package com.harman.goodmood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import goodmood.harman.com.goodmood.R;

public class SettingFragment extends Fragment {

    private Toolbar toolbar;
//    private android.widget.TextView mScheduleSubtitle;
//    private LinearLayout mScheduleLayout;
    private android.widget.TextView mProximitySubtitle;
    private LinearLayout mProximityLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mProximityLayout = (LinearLayout) view.findViewById(R.id.proximity_layout);
        mProximitySubtitle = (TextView) view.findViewById(R.id.proximity_subtitle);
//        mScheduleLayout = (LinearLayout) view.findViewById(R.id.schedule_layout);
//        mScheduleSubtitle = (TextView) view.findViewById(R.id.schedule_subtitle);

        mProximityLayout.setOnClickListener(mProximityClickListener);
        return view;
    }

//    public void setScheduleSubtitle(String text) {
//        mScheduleSubtitle.setText(text);
//    }

    public void setProximitySubtitle(String text) {
        mProximitySubtitle.setText(text);
    }

    private View.OnClickListener mProximityClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ProximitySettingActivity.class);
            startActivity(intent);
        }
    };

//    private View.OnClickListener mScheduleClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    };
}
