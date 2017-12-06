package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.harman.goodmood.mqtt.SmartBulbManager;

import goodmood.harman.com.goodmood.R;


public class LightSettingsFragment extends Fragment {
    LightParentFragment.LightCallback mCallback;

    private ColorPickerView mColorPicker;


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
    }

    @Override
    public void onResume() {
        super.onResume();
        long color = SmartBulbManager.getInstance(getActivity()).getColor();
        if (color != 0) {
            mColorPicker.setColor(((int) color), true);
        }
    }

    private void setLampColor(int color) {
        mCallback.onColorSelected(color);
        SmartBulbManager.getInstance(getActivity()).setRGB(color);
    }
}
