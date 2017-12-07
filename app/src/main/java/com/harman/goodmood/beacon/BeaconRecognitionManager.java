package com.harman.goodmood.beacon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import com.harman.goodmood.mqtt.SmartBulbManager;
import com.harman.goodmood.util.weather.DayLightHelper;
import com.harman.goodmood.util.weather.Settings;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * This class provides beacon detection and reaction to beacon detection.
 *
 * @author Alexander Davydov
 * @since 06/12/2017
 */
public class BeaconRecognitionManager {

    private static final String TAG = BeaconRecognitionManager.class.getSimpleName();

    public static final String IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";

    public static final long DEFAULT_BEACON_NOT_IN_RAINGE_TIME = 20000;

    public static final int STATE_DISABLED = 0;
    public static final int STATE_CONSTANTLY = 1;
    public static final int STATE_DAY_NIGHT = 2;

    private final Activity mActivity;

    private BeaconManager mBeaconManager;
    private long mBeaconVisibilityTime = 0;

    private static BeaconRecognitionManager mInstance;

    /**
     * @param activity to bind
     * @return BeaconRecognitionManager instance
     */
    public static BeaconRecognitionManager getInstance(Activity activity) {
        if (mInstance == null) {
            mInstance = new BeaconRecognitionManager(activity);
        }
        return mInstance;
    }

    private BeaconRecognitionManager(Activity activity) {

        mActivity = activity;

        // setup beacon detector
        mBeaconManager = BeaconManager.getInstanceForApplication(activity);

        // Sets the delay between each scans according to the settings
        mBeaconManager.setForegroundBetweenScanPeriod(0L);

        // Add all the beacon types we want to discover
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT));
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_TLM_LAYOUT));

        mBeaconManager.bind(mConsumer);
    }

    /**
     * Deattach activity from beacon scanner
     *
     * @param activity - that binded to manager
     */
    public void unbind(Activity activity) {
        if (mActivity == activity) {
            mBeaconManager.unbind(mConsumer);
        }

        mInstance = null;
    }

    private BeaconConsumer mConsumer = new BeaconConsumer() {
        @Override
        public void onBeaconServiceConnect() {
            Log.d(TAG, "onBeaconServiceConnect");

            mBeaconManager.addRangeNotifier(new RangeNotifier() {
                @Override
                public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                    int proximityState = Settings.getProximityState();

                    if (proximityState == STATE_DISABLED) {
                        return;
                    }

                    if (beacons.size() > 0) {
                        long currentTimeMillis = System.currentTimeMillis();

                        if (beacons.iterator().next().getDistance() <= 3.0) {
                            if (mBeaconVisibilityTime + DEFAULT_BEACON_NOT_IN_RAINGE_TIME < currentTimeMillis) {
                                SmartBulbManager smartBulbManager = SmartBulbManager.getInstance(mActivity);

                                if (proximityState == STATE_CONSTANTLY) {
                                    if (!smartBulbManager.isEnabled()) {
                                        smartBulbManager.setRGB(smartBulbManager.getColor());
                                    }
                                } else if (proximityState == STATE_DAY_NIGHT) {
                                    if (!DayLightHelper.isDay()) {
                                        if (!smartBulbManager.isEnabled()) {
                                            smartBulbManager.setRGB(smartBulbManager.getColor());
                                        }
                                    }
                                }
                            }
                        } else {
                            SmartBulbManager smartBulbManager = SmartBulbManager.getInstance(mActivity);
                            if (smartBulbManager.isEnabled()) {
                                smartBulbManager.setRGB(0);
                            }
                        }
                        mBeaconVisibilityTime = currentTimeMillis;
                        Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().getDistance() + " meters away.");
                    }
                }
            });
            try {
                Log.d(TAG, "startRangingBeaconsInRegion");
                mBeaconManager.startRangingBeaconsInRegion(new Region("com.harman.goodmood", null, null, null));
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        }

        @Override
        public Context getApplicationContext() {
            return mActivity.getApplicationContext();
        }

        @Override
        public void unbindService(ServiceConnection serviceConnection) {
            mActivity.unbindService(serviceConnection);

        }

        @Override
        public boolean bindService(Intent intent, ServiceConnection serviceConnection, int flags) {
            return mActivity.bindService(intent, serviceConnection, flags);
        }

    };


}
