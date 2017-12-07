package com.harman.goodmood.util.template;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import goodmood.harman.com.goodmood.R;

/**
 * Created by eugenegusev on 07.12.17.
 */

public class TemplatesAdapter extends RecyclerView.Adapter<TemplateViewHolder> {

    private Context mContext;
    private ArrayList<Template> mTemplatesList;

    public TemplatesAdapter(ArrayList templates, Context context) {
        mContext = context;
        mTemplatesList = templates;
    }

    @Override
    public TemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.template, parent, false);

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
