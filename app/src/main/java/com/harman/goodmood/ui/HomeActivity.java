package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.harman.goodmood.beacon.BeaconRecognitionManager;
import com.harman.goodmood.util.weather.DayLightHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import goodmood.harman.com.goodmood.R;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private BottomNavigationViewEx mBottomNavigationViewEx;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navigation);
//        BottomNavigationHelper.disableShiftMode(mBottomNavigationViewEx);

        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set test page as default
        mBottomNavigationViewEx.setCurrentItem(4);
        mBottomNavigationViewEx.enableShiftingMode(false);

        setFragmentIntoContainer(new TestFragment());
        DayLightHelper.loadDaylight();

        BeaconRecognitionManager.getInstance(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BeaconRecognitionManager.getInstance(this).unbind(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_light:
                    setFragmentIntoContainer(new LightParentFragment());
                    return true;
                case R.id.navigation_voice:
                    setFragmentIntoContainer(new VoiceFragment());
                    return true;
                case R.id.navigation_music:
                    setFragmentIntoContainer(new MusicFragment());
                    return true;
                case R.id.navigation_settings:
                    setFragmentIntoContainer(new SettingFragment());
                    return true;
                case R.id.navigation_test:
                    setFragmentIntoContainer(new TestFragment());
                    return true;
            }
            return false;
        }
    };

    private void setFragmentIntoContainer(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
