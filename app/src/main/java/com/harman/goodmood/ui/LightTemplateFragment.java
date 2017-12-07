package com.harman.goodmood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harman.goodmood.util.template.Template;
import com.harman.goodmood.util.template.TemplatesAdapter;

import java.util.ArrayList;

import goodmood.harman.com.goodmood.R;

public class LightTemplateFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TemplatesAdapter mTemplatesAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_light_templates, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);

        ArrayList templatesList = new ArrayList<>();
        templatesList.add(new Template("Spartak", R.drawable.spartak, new ArrayList<Long>()));
        templatesList.add(new Template("Party", R.drawable.party, new ArrayList<Long>()));
        templatesList.add(new Template("Christmas", R.drawable.christmas, new ArrayList<Long>()));

        mTemplatesAdapter = new TemplatesAdapter(templatesList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTemplatesAdapter);
        mTemplatesAdapter.notifyDataSetChanged();
    }
}
