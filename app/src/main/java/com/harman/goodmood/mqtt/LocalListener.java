package com.harman.goodmood.mqtt;

/**
 * Created by SSyukov on 06-Dec-17.
 */

public interface LocalListener {
    void onColorChange(long color);

    void onStateChange(boolean isEnable);
}
