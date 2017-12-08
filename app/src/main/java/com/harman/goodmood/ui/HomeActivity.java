package com.harman.goodmood.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.harman.goodmood.beacon.BeaconRecognitionManager;
import com.harman.goodmood.mqtt.SmartBulbManager;
import com.harman.goodmood.util.weather.DayLightHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import goodmood.harman.com.goodmood.R;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    private static final int PERMISSIONS = 2;

    private BottomNavigationViewEx mBottomNavigationViewEx;

    private int mCurrentFragmrntId = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mBottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.navigation);
        //        BottomNavigationHelper.disableShiftMode(mBottomNavigationViewEx);

        mBottomNavigationViewEx.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set test page as default
        mBottomNavigationViewEx.setCurrentItem(0);
        mBottomNavigationViewEx.enableShiftingMode(false);

        //        setFragmentIntoContainer(new TestFragment());
        DayLightHelper.loadDaylight();

        BeaconRecognitionManager.getInstance(this);

        SmartBulbManager.getInstance(this);

        requestPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BeaconRecognitionManager.getInstance(this).unbind(this);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSIONS);
    }

    //Handling callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            if (item.getItemId() != mCurrentFragmrntId) {

                mCurrentFragmrntId = item.getItemId();

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
                }
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
