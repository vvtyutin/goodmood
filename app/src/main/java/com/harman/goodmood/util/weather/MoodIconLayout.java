package com.harman.goodmood.util.weather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.harman.goodmood.util.RatioRelativeLayout;

import goodmood.harman.com.goodmood.R;

/**
 * Created by SSyukov on 06-Dec-17.
 */

public class MoodIconLayout extends RatioRelativeLayout {

    private boolean mIsChecked;
    private boolean mResourcesIsSetted;

    @DrawableRes
    private int mNormalIcon;
    @DrawableRes
    private int mCheckedIcon;
    @DrawableRes
    private int mCheckedBackground;

    private Drawable mNormalCircle = getResources().getDrawable(R.drawable.mood_circle_normal);
    private Drawable mCheckedCircle = getResources().getDrawable(R.drawable.mood_circle_checked);

    private ImageView mIconView;
    private ImageView mBackgroundView;
    private ImageView mCircleView;

    public MoodIconLayout(Context context) {
        super(context);
    }

    public MoodIconLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoodIconLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setResources(@DrawableRes int normalIcon, @DrawableRes int checkedIcon, @DrawableRes int checkedBackground) {
        mNormalIcon = normalIcon;
        mCheckedIcon = checkedIcon;
        mCheckedBackground = checkedBackground;
        mResourcesIsSetted = true;
        updateUI();
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.mood_icon_layout, this);

        mIconView = (ImageView) findViewById(R.id.mood_icon);
        mBackgroundView = (ImageView) findViewById(R.id.background);
        mCircleView = (ImageView) findViewById(R.id.circle);
        updateUI();
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
        updateUI();
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    private void updateUI() {
        if (mResourcesIsSetted) {
            if (mIsChecked) {
                mIconView.setBackground(getResources().getDrawable(mCheckedIcon));
                mBackgroundView.setBackground(getResources().getDrawable(mCheckedBackground));
                mBackgroundView.setVisibility(VISIBLE);
                mCircleView.setBackground(mCheckedCircle);
            } else {
                mIconView.setBackground(getResources().getDrawable(mNormalIcon));
                mBackgroundView.setVisibility(INVISIBLE);
                mCircleView.setBackground(mNormalCircle);
            }
        }
    }
}
