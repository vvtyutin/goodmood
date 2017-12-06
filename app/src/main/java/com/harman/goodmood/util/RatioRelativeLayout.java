package com.harman.goodmood.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import goodmood.harman.com.goodmood.R;

public class RatioRelativeLayout extends RelativeLayout {
    private float mRatio;

    public RatioRelativeLayout(Context context) {
        super(context);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.RatioRelativeLayout,
                0, 0);

        try {
            mRatio = a.getFloat(R.styleable.RatioRelativeLayout_ratio, 1f);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int calcH = (int) (w * mRatio);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calcH, MeasureSpec.EXACTLY));
    }

    public float getRatio() {
        return mRatio;
    }

    public void setRatio(float ratio) {
        mRatio = ratio;
    }
}