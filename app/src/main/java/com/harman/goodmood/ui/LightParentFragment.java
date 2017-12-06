package com.harman.goodmood.ui;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.harman.goodmood.mqtt.SmartBulbManager;

import goodmood.harman.com.goodmood.R;

public class LightParentFragment extends Fragment {
    private ImageView mToolbarIconLight;
    private ImageView mToolbarIconLamp;
    private Switch mSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        mToolbarIconLight = (ImageView) view.findViewById(R.id.toolbar_icon_light);
        mToolbarIconLamp = (ImageView) view.findViewById(R.id.toolbar_icon_lamp);
        mSwitch = (Switch) view.findViewById(R.id.toolbar_switch);
        mSwitch.setOnCheckedChangeListener(mSwitchListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean enable = SmartBulbManager.getInstance(getActivity()).isEnable();
        mSwitch.setChecked(enable);
        tintIcon(enable);
    }

    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LightSettingsFragment.newInstance(mParentFragmentCallback);
                case 1:
                    return new LightTemplateFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Settings";
                case 1:
                    return "Template";
            }
            return null;
        }
    }

    private LightCallback mParentFragmentCallback = new LightCallback() {
        @Override
        public void onColorSelected(int color) {
            mSwitch.setChecked(true);
            tintIcons(color);
        }
    };

    private void tintIcons(int color) {
        tintIcon(mToolbarIconLamp, color);
        tintIcon(mToolbarIconLight, color);
    }

    private void tintIcon(ImageView imageView, int color) {
        Drawable drawable = imageView.getBackground();
        drawable.mutate();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        imageView.setBackground(drawable);
    }


    interface LightCallback {
        void onColorSelected(int color);
    }

    CompoundButton.OnCheckedChangeListener mSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            tintIcon(isChecked);
        }
    };

    private void tintIcon(boolean isChecked) {
        if (isChecked) {
            SmartBulbManager.getInstance(getActivity()).setRGB(SmartBulbManager.getInstance(getActivity()).getColor());
            mToolbarIconLight.setVisibility(View.VISIBLE);
            int color = (int) SmartBulbManager.getInstance(getActivity()).getColor();
            tintIcons(color);
        } else {
            tintIcons(getResources().getColor(R.color.lampDefaultColor));
            SmartBulbManager.getInstance(getActivity()).setRGB(0);
            mToolbarIconLight.setVisibility(View.INVISIBLE);
        }
    }
}
