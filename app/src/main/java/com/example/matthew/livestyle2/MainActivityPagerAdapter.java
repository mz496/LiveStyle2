package com.example.matthew.livestyle2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Matthew on 10/25/2015.
 */
public class MainActivityPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainActivityPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ScanFeedFragment tab0 = new ScanFeedFragment();
                return tab0;
            case 1:
                OutfitFeedFragment tab1 = new OutfitFeedFragment();
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}