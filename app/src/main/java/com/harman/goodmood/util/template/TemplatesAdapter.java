package com.harman.goodmood.util.template;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harman.goodmood.ui.LightTemplateFragment;

import java.util.ArrayList;

import goodmood.harman.com.goodmood.R;

/**
 * Created by eugenegusev on 07.12.17.
 */

public class TemplatesAdapter extends RecyclerView.Adapter<TemplateViewHolder> {

    private Context mContext;
    private ArrayList<Template> mTemplatesList;
    private LightTemplateFragment.TemplateClicked mTemplateCliked;

    public TemplatesAdapter(ArrayList templates, Context context, LightTemplateFragment.TemplateClicked callback) {
        mContext = context;
        mTemplatesList = templates;
        mTemplateCliked = callback;
    }

    @Override
    public TemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.template, parent, false);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTemplateCliked.onTemplateCliked(v);
            }
        });
        return new TemplateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TemplateViewHolder holder, int position) {
        Template template = mTemplatesList.get(position);
        holder.setTitle(template.getTitle());
        holder.setBackground(mContext.getResources().getDrawable(template.getResourceID()));
    }

    @Override
    public int getItemCount() {
        return mTemplatesList.size();
    }
}
