package com.harman.goodmood.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import goodmood.harman.com.goodmood.R;

/**
 * Created by vtyutin on 04/12/2017.
 */

public class MqttService extends Service implements MqttCallback {
    private final static String TAG = MqttService.class.getSimpleName();

    public final static String BROADCAST_ACTION = "com.harman.goodmood.message";

    public final static String PAYLOAD = "payload";
    public final static String MESSAGE = "message";

    public final static String LOGIN = "navigator07@inbox.ru";
    public final static String PASSWORD = "GoodMood";
    public final static String FAKE_USER_ID = "ANDROID_V1.0";

    public final static String MESSAGE_TYPE = "type";
    public final static int MESSAGE_RECEIVED = 1;
    public final static int MQTT_CONNECTED = 2;
    public final static int MQTT_DISCONNECTED = 3;

    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private Handler connectHandler = new Handler();
    private HashMap<String, String> tokenMap = new HashMap<>();
    private boolean isReconnectRequired = true;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d("###", "SERVICE BINDED");
        return new ServiceBinder();
    }

    public void setReconnectRequired(boolean required) {
        isReconnectRequired = required;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Log.d("###", "connectionLost");
        Intent intent = new Intent(BROADCAST_ACTION);
        intent.putExtra(MESSAGE_TYPE, MQTT_DISCONNECTED);
        sendBroadcast(intent);
        connectHandler.postDelayed(connectMQTTTask, 10000);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Log.d("###", "messageArrived: " + s + " => " + mqttMessage.toString());
        String deviceId = s.split("/")[0];
        String storedToken = "" + tokenMap.get(deviceId);
        String[] elements = mqttMessage.toString().split(":");
        String token = elements[0];
        if ((token != null) && (token.equals(storedToken))) {
            Intent intent = new Intent(BROADCAST_ACTION);
            intent.putExtra(MESSAGE_TYPE, MESSAGE_RECEIVED);
            intent.putExtra(PAYLOAD, s);
            intent.putExtra(MESSAGE, mqttMessage.toString().substring(token.length() + 1));
            sendBroadcast(intent);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //Log.d("###", "deliveryComplete");
    }



    private void publishMQTTMessage(String topic, String message, String token) {
        if ((mqttClient == null) || !mqttClient.isConnected()) {
            return;
        }
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(0);
        mqttMessage.setRetained(false);

        String[] elements = topic.split("/");
        String deviceId = elements[0];
        tokenMap.put(deviceId, elements[1].equals("get") ? token : null);
        mqttMessage.setPayload((elements[1].equals("get") ? String.format("%s:%s", token, message) : message).getBytes());
        try {
            Log.d("###", "============== publish: ================= \n" + topic + " message: " + mqttMessage);
            mqttClient.publish(topic, mqttMessage);
        } catch (MqttException mex) {
            Log.e(TAG, mex.getMessage());
            mex.printStackTrace();
        }
    }

    public void publishMQTTMessage(String topic, String message) {
        if ((mqttClient == null) || !mqttClient.isConnected()) {
            return;
        }
        long MILLIS_IN_DAY = 86400000;
        long currentTime = System.currentTimeMillis();
        long millisTillNow = currentTime % MILLIS_IN_DAY;
        String deviceToken = "" + millisTillNow;
        publishMQTTMessage(topic, message, deviceToken);
    }

    public class ServiceBinder extends Binder {
        public MqttService getService() {
            Log.d("###", "GET SERVICE");
            return MqttService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("###", "SERVICE STARTED");
        new Thread(connectMQTTTask).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("###", "SERVICE DESTROYED");

        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.d("###", "SERVICE CREATED");

        super.onCreate();
    }

    private final Runnable connectMQTTTask = new Runnable() {
        @Override
        public void run() {
            if (isReconnectRequired == false) {
                connectHandler.postDelayed(connectMQTTTask, 10000);
                return;
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Connect to Broker
                    try
                    {
                        Log.d("###","Try connect to mqtt");
                        mqttConnectOptions = new MqttConnectOptions();
                        mqttConnectOptions.setCleanSession(true);
                        mqttConnectOptions.setConnectionTimeout(10);
                        mqttConnectOptions.setKeepAliveInterval(60);
                        mqttConnectOptions.setUserName(LOGIN);
                        mqttConnectOptions.setPassword(PASSWORD.toCharArray());
                        mqttConnectOptions.setSocketFactory(getSocketFactory(getAssets().open("ca1024.crt")));

                        mqttClient = new MqttClient(getString(R.string.mqtt_url), getDeviceId(), new MemoryPersistence());
                        mqttClient.setCallback(MqttService.this);
                        mqttClient.connect(mqttConnectOptions);
                        mqttClient.subscribe("+/get/+/response/+");
                        Log.d("###", "Connected to mqtt");
                        connectHandler.removeCallbacks(connectMQTTTask);
                        Intent intent = new Intent(BROADCAST_ACTION);
                        intent.putExtra(MESSAGE_TYPE, MQTT_CONNECTED);
                        sendBroadcast(intent);
                    }
                    catch (Exception e) {
                        //e.printStackTrace();
                        Log.d("###", "mqtt connect error --- " + e.getMessage());
                        connectHandler.postDelayed(connectMQTTTask, 10000);
                    }
                }
            });
            t.start();
        }
    };

    private String getDeviceId() {
        String deviceId = null;
        if (!checkThePermissions()) {
            return FAKE_USER_ID;
        }
        final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            if ((deviceId == null) || (deviceId.length() == 0)) {
                Random generator = new Random();
                deviceId = "" + (generator.nextInt(96) + 32);
            }
            Log.d("###", "device id: " + deviceId);
        } catch (SecurityException sex) {
            Log.e(TAG, "No permission granted");
            return FAKE_USER_ID;
        }
        return deviceId;
    }

    private boolean checkThePermissions() {
        String[] permissions = getResources().getStringArray(R.array.permissions);
        ArrayList<String> missedPermissions = new ArrayList<>();
        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(MqttService.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                missedPermissions.add(permission);
            }
        }

        boolean allPermissionsGranted = missedPermissions.size() == 0 ? true : false;
        if (!allPermissionsGranted) {
            return false;
        }
        return true;
    }

    private SSLSocketFactory getSocketFactory (final InputStream caCrtStream) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        PEMReader reader = new PEMReader(new InputStreamReader(caCrtStream));
        X509Certificate caCert = (X509Certificate)reader.readObject();
        reader.close();

        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(caKs);
        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());

        return context.getSocketFactory();
    }
}
