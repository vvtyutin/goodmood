package com.harman.goodmood.mqtt;

/**
 * Created by vtyutin on 06/12/2017.
 */

public interface SmartBulbListener {
    void onRGBUpdated(long rgb);
}
