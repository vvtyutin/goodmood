package com.harman.goodmood.util.weather;

import android.content.Context;
import android.content.SharedPreferences;

import com.harman.goodmood.beacon.BeaconRecognitionManager;

/**
 * TODO add class description
 *
 * @author alexander
 * @since 07/12/2017
 */
public class Settings {

    private static String SHARED_PREFERENCES = "goodmood.settings";

    public static final String KEY_PROXIMITY_STATE = "proximity_state";

    private static SharedPreferences mPrefs;

    private Settings() {
    }

    public static void setup(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static int getProximityState() {
        return mPrefs.getInt(KEY_PROXIMITY_STATE, BeaconRecognitionManager.STATE_DISABLED);
    }

    public static void setProximityState(int state) {
        mPrefs.edit()
                .putInt(KEY_PROXIMITY_STATE, state)
                .apply();
    }
}
