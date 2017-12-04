package com.harman.goodmood.recognizer;

/**
 * Created by vtyutin on 04/12/2017.
 */

public interface SpeachRecognizerListener {
    void onBeginSpeach();
    void onEndSpeach();
    void onPartialResult(String text);
    void onFinalResult(String text);
    void onInitCompleted();
}
