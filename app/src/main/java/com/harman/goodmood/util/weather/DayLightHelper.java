package com.harman.goodmood.util.weather;

import android.content.Context;
import java.util.Date;

/**
 * Created by evgenygusev on 06/12/2017.
 */

public class DayLightHelper {

    private static DayLight sDayLight;

    //USE THIS METHOD TO DETECT DAY
    public static boolean isDay() {
        if (sDayLight == null) {
            return false;
        }

        Date currentDate = new Date();
        return currentDate.after(sDayLight.getSunriseDate()) && currentDate.before(sDayLight.getSunsetDate());
    }

    public static void loadDaylight() {
        new GetDaylightTask(new GetDaylightTask.LoadDaylightCallback() {
            @Override
            public void onDaylightLoaded(DayLight dayLight) {
                sDayLight = dayLight;
            }
        });
    }
}
