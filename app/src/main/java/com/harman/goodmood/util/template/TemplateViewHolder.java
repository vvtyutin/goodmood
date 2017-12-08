package com.harman.goodmood.util.template;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import goodmood.harman.com.goodmood.R;

/**
 * Created by eugenegusev on 08.12.17.
 */

public class TemplateViewHolder extends RecyclerView.ViewHolder {

    private TextView mTitleTextView;
    private ImageView mImageView;
    private View mBack;

    public TemplateViewHolder(View itemView) {
        super(itemView);

        mTitleTextView = (TextView) itemView.findViewById(R.id.template_text_view);
        mImageView = (ImageView) itemView.findViewById(R.id.template_image_view);
        mBack = itemView.findViewById(R.id.viewview);
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setBackground(Drawable drawable) {
        mImageView.setBackground(drawable);
    }

    public void setSelected(int color) {
        mBack.setBackgroundColor(color);
    }
}
