package com.example.hw9weathersearch.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.hw9weathersearch.R;
import com.example.hw9weathersearch.graphTab;
import com.example.hw9weathersearch.picturesTab;
import com.example.hw9weathersearch.todayTab;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2 , R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                System.out.println("today tab..");
                todayTab todayDeetsObj =  new todayTab();
                return  todayDeetsObj;
            case 1:
                System.out.println("graph tab..");
                graphTab graphTabObj = new graphTab();
                return  graphTabObj;
            case 2:
                picturesTab picTabObj = new picturesTab();
                return picTabObj;
            default:
                    return null;

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return  "TODAY";
            case 1:
                return  "WEEKLY";
            case 2:
                return  "PHOTOS";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}