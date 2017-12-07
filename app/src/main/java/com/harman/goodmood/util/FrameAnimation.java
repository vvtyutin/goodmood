
package com.harman.goodmood.util;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FrameAnimation {

    private final static String TAG = FrameAnimation.class.getSimpleName();

    private static final int DEFAULT_FPS = 25;

    private Activity mHost;
    private ImageView mView;
    private List<Integer> mFrameIds;
    private int mCurFrameNumber;
    private Drawable mCurFrame;
    private boolean mIsLooped;

    private AnimationTask mAnimationTask;
    private Timer mAnimationTimer;
    private int mFPS = DEFAULT_FPS;

    public FrameAnimation(Activity host) {
        mHost = host;
    }

    public void setFrames(List<Integer> resIds) {
        mFrameIds = resIds;
    }

    public void setFrames(int framesListId) {
        TypedArray array = mHost.getResources().obtainTypedArray(framesListId);
        int size = array.length();

        ArrayList<Integer> frames = new ArrayList<>(size);
        for (int n = 0; n < size; n++) {
            int resId = array.getResourceId(n, 0);
            if (resId > 0) {
                frames.add(resId);
            } else {
                break;
            }
        }
        array.recycle();
        setFrames(frames);
    }

    public void setFPS(int fps) {
        mFPS = fps;
    }

    public void setLooped(boolean looped) {
        mIsLooped = looped;
    }

    public void start(ImageView view) {
        mView = view;
        mCurFrameNumber = 0;
        mCurFrame = getFrame(0);
        mAnimationTask = new AnimationTask();
        mAnimationTimer = new Timer();
        mAnimationTimer.schedule(mAnimationTask, 0, 1000 / mFPS);
    }

    public void stop() {
        mView = null;
        mCurFrameNumber = 0;
        mCurFrame = null;
        if (mAnimationTask != null) {
            mAnimationTask.cancel();
            mAnimationTask = null;
        }
        if (mAnimationTimer != null) {
            mAnimationTimer.cancel();
            mAnimationTimer = null;
        }

    }

    private class AnimationTask extends TimerTask {
        @Override
        public void run() {
            if (mView != null) {
                if (mCurFrame != null) {
                    mCurFrame = getFrame(mCurFrameNumber++);
                    mHost.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mView != null && mCurFrame != null) {
                                mView.setImageDrawable(mCurFrame);
                            }
                        }
                    });
                    return;
                }
            }
            cancel();
        }
    }

    private synchronized Drawable getFrame(int index) {
        if (mFrameIds != null) {
            int total = mFrameIds.size();
            if (total > 0) {
                if (mIsLooped) {
                    index = index % total;
                }
                if (index < total) {
                    int resId = mFrameIds.get(index);
                    if (resId > 0) {
                        try {
                            return mHost.getResources().getDrawable(resId);
                        } catch (Exception error) {
                            Log.e(TAG, "Animation exception", error);
                        } catch (Error error) {
                            Log.e(TAG, "Animation error", error);
                        }
                    }
                }
            }
        }
        return null;
    }
}