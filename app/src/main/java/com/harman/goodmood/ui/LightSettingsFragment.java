package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.harman.goodmood.mqtt.LocalListener;
import com.harman.goodmood.mqtt.SmartBulbManager;

import goodmood.harman.com.goodmood.R;


public class LightSettingsFragment extends Fragment {
    LightParentFragment.LightCallback mCallback;

    private ColorPickerView mColorPicker;
    private LinearLayout mDisabledMessage;


    public static LightSettingsFragment newInstance(LightParentFragment.LightCallback parentFragmentCallback) {
        LightSettingsFragment fragment = new LightSettingsFragment();
        fragment.mCallback = parentFragmentCallback;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_settings, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColorPicker = (ColorPickerView) view.findViewById(R.id.color_picker_view);

        mColorPicker.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int i) {
                setLampColor(i);
            }
        });

        mColorPicker.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int i) {
                setLampColor(i);
            }
        });

        mDisabledMessage = (LinearLayout) view.findViewById(R.id.message);
    }

    @Override
    public void onResume() {
        super.onResume();
        long color = SmartBulbManager.getInstance(getActivity()).getColor();
        if (color != 0) {
            mColorPicker.setColor(((int) color), true);
        }
        boolean isEnable = SmartBulbManager.getInstance(getActivity()).isEnabled();
        updateUI(isEnable);
        SmartBulbManager.getInstance(getActivity()).registerLocalListener(mLocalLampListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        SmartBulbManager.getInstance(getActivity()).unregisterLocalListener(mLocalLampListener);
    }

    private void updateUI(boolean lampIsEnable) {
        if (lampIsEnable) {
            mDisabledMessage.setVisibility(View.GONE);
            mColorPicker.setVisibility(View.VISIBLE);
        } else {
            mDisabledMessage.setVisibility(View.VISIBLE);
            mColorPicker.setVisibility(View.GONE);
        }
        mCallback.onSwitchChanged(lampIsEnable);
    }

    private void setLampColor(int color) {
        mCallback.onColorSelected(color);
        SmartBulbManager.getInstance(getActivity()).setRGBComponents(0xFF,0,0);
    }

    private LocalListener mLocalLampListener = new LocalListener() {
        @Override
        public void onColorChange(long color) {

        }

        @Override
        public void onStateChange(final boolean isEnable) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateUI(isEnable);
                }
            });

        }
    };
}
