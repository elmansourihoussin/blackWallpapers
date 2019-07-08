package com.islamiat.blackwallpapers.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.GridLayoutManager;

public class DisplayHelper {

    public static void updateSpan(Context context, GridLayoutManager mLayoutManager, int column){

        int orientation =  context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager.setSpanCount(column+2);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager.setSpanCount(column);
        }
    }

    public static void updateSpan(int orientation, GridLayoutManager mLayoutManager, int column){
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLayoutManager.setSpanCount(column+2);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager.setSpanCount(column);
        }
    }

}
