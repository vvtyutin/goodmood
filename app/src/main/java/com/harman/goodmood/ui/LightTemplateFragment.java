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

import com.harman.goodmood.mqtt.SmartBulbManager;
import com.harman.goodmood.util.template.Template;
import com.harman.goodmood.util.template.TemplatesAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import goodmood.harman.com.goodmood.R;

public class LightTemplateFragment extends Fragment {
    LightParentFragment.LightCallback mCallback;

    private static final int PERIOD = 5000;

    private RecyclerView mRecyclerView;
    private TemplatesAdapter mTemplatesAdapter;
    private Timer mTimer = new Timer();
    private TimerTask mTimerTask;
    private ArrayList<Integer> mColors;
    private int mCurrentColor;

    public interface TemplateClicked {
        void onTemplateCliked(View v);
    }

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

        final ArrayList spartakColors = new ArrayList<Integer>();
        spartakColors.add(Integer.valueOf(16711680));
        spartakColors.add(Integer.valueOf(-1));
        templatesList.add(new Template("Spartak", R.drawable.spartak, spartakColors));

        final ArrayList partyColors = new ArrayList<Integer>();
        partyColors.add(Integer.valueOf(-56078));
        partyColors.add(Integer.valueOf(-16760577));
        templatesList.add(new Template("Party", R.drawable.party, partyColors));

        final ArrayList christmasColors = new ArrayList<Integer>();
        christmasColors.add(Integer.valueOf(-56242));
        christmasColors.add(Integer.valueOf(-14614784));
        templatesList.add(new Template("Christmas", R.drawable.christmas, christmasColors));

        mTemplatesAdapter = new TemplatesAdapter(templatesList, getActivity(), new TemplateClicked() {
            @Override
            public void onTemplateCliked(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                ArrayList newColors;
                switch (position) {
                    case 0:
                        newColors = spartakColors;
                        break;
                    case 1:
                        newColors = partyColors;
                        break;
                    case 2:
                        newColors = christmasColors;
                        break;
                    default:
                        newColors = spartakColors;
                }

                if (newColors == mColors) {
                    mColors = null;
                } else {
                    mColors = newColors;
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mTemplatesAdapter);
        mTemplatesAdapter.notifyDataSetChanged();
    }

    public void startTemplate() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mColors != null && mColors.size() > 0 && getActivity() != null) {
                    mCurrentColor = mColors.get((mColors.indexOf(mCurrentColor) + 1) % mColors.size());
                    //   mCallback.onColorSelected(mCurrentColor);
                    SmartBulbManager.getInstance(getActivity()).setRGB(mCurrentColor);
                }
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0, PERIOD);
    }

    public void stopTemplate() {
        mTimer.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopTemplate();
    }
}
