package com.harman.goodmood.util.template;

import java.util.ArrayList;

/**
 * Created by eugenegusev on 07.12.17.
 */

public class Template {
    private String mTitle;
    private int mResourceID;
    private ArrayList<Integer> mColors;

    public Template(String title, int resourceID, ArrayList<Integer> colors) {
        mTitle = title;
        mResourceID = resourceID;
        mColors = colors;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getResourceID() {
        return mResourceID;
    }

    public ArrayList<Integer> getColors() {
        return mColors;
    }
}
