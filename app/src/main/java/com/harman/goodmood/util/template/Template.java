package com.harman.goodmood.util.template;

import java.util.ArrayList;

/**
 * Created by eugenegusev on 07.12.17.
 */

public class Template {
    private String mTitle;
    private int mResourceID;
    private ArrayList<Long> mColors;

    public Template(String title, int resourceID, ArrayList<Long> colors) {
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

    public ArrayList<Long> getColors() {
        return mColors;
    }
}
