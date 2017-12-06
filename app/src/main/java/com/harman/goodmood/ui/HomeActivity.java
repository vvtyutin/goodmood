package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.harman.goodmood.util.BottomNavigationHelper;
import com.harman.goodmood.util.weather.DayLightHelper;

import goodmood.harman.com.goodmood.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set test page as default
        navigation.getMenu().getItem(4).setChecked(true);
        setFragmentIntoContainer(new TestFragment());
        DayLightHelper.loadDaylight();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_light:
                    setFragmentIntoContainer(new LightFragment());
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
