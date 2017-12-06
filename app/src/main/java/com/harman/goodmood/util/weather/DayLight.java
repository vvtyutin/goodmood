package com.harman.goodmood.util.weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by evgenygusev on 06/12/2017.
 */

public class DayLight {
    private static final String JSON_SYS = "sys";
    private static final String JSON_SUNSET = "sunset";
    private static final String JSON_SUNRISE = "sunrise";

    private Date mSunrise;
    private Date mSunset;

    public DayLight(String jsonString) throws JSONException {
        JSONObject json = new JSONObject(jsonString);
        mSunset = new Date(json.getJSONObject(JSON_SYS).getLong(JSON_SUNSET) * 1000L);
        mSunrise = new Date(json.getJSONObject(JSON_SYS).getLong(JSON_SUNRISE) * 1000L);
    }

    public Date getSunriseDate() {
        return mSunrise;
    }

    public Date getSunsetDate() {
        return mSunset;
    }
}
