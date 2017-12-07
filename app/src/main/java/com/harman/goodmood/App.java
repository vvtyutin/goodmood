package com.harman.goodmood;

import android.app.Application;

import com.harman.goodmood.util.weather.Settings;

/**
 * TODO add class description
 *
 * @author alexander
 * @since 07/12/2017
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Settings.setup(this);
    }
}
