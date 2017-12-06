package com.harman.goodmood.mqtt;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by vtyutin on 04/12/2017.
 */

public class SmartBulbManager {
    private static SmartBulbManager instance = null;

    private static ServiceConnection mServiceConn;
    private static MqttService mService = null;

    private static final String SMART_BULB_ID = "a10631";

    private static ArrayList<SmartBulbListener>listeners = new ArrayList<>();

    private static BroadcastReceiver mReceiver;

    private SmartBulbManager() {

    }

    public static SmartBulbManager getInstance(Context context) {
        if (instance == null) {
            instance = new SmartBulbManager();
        }
        if (mService == null) {
            initService(context);
        }
        return instance;
    }

    private static void initService(Context context) {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MqttService.BROADCAST_ACTION)) {
                    int messageType = intent.getIntExtra(MqttService.MESSAGE_TYPE, 0);
                    switch (messageType) {
                        case MqttService.MESSAGE_RECEIVED:
                            String payload = intent.getStringExtra(MqttService.PAYLOAD);
                            String message = intent.getStringExtra(MqttService.MESSAGE);
                            //Log.d("###", "======= mqtt message ========\n" + payload + ": " + message);
                            String[] paths = payload.split("/");
                            if ((paths.length == 5) && paths[1].equals("get") && paths[3].equals("response")) {
                                long rgb = Integer.parseInt(message);
                                for (SmartBulbListener listener: listeners) {
                                    listener.onRGBUpdated(rgb);
                                }
                            }
                            break;
                    }
                }
            }
        };
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = ((MqttService.ServiceBinder)iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
            }
        };

        Intent intent = new Intent(context, MqttService.class);
        context.startService(intent);
        context.bindService(intent, mServiceConn, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter(MqttService.BROADCAST_ACTION);
        context.registerReceiver(mReceiver, filter);
    }

    public void registerListener(SmartBulbListener listener) {
        if (listener == null) {
            return;
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unregisterListener(SmartBulbListener listener) {
        if (listener == null) {
            return;
        }
        listeners.remove(listener);
    }

    public void setRGB(long rgb) throws IllegalStateException {
        if (mService == null) {
            throw new IllegalStateException("Service is not connected");
        }
        mService.publishMQTTMessage(SMART_BULB_ID + "/set/rgb", "" + (rgb & 0x00FFFFFF));
    }

    public void setRGBComponents(int red, int green, int blue) throws IllegalStateException {
        if (mService == null) {
            throw new IllegalStateException("Service is not connected");
        }
        long rgb = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        mService.publishMQTTMessage(SMART_BULB_ID + "/set/rgb", "" + rgb);
    }

    public void requestRGB() {
        mService.publishMQTTMessage(SMART_BULB_ID + "/get/rgb", "");
    }
}
