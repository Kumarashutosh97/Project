package com.ashutosh.internship.project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ashutosh on 8/2/2017.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public PageAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeTab homeTab = new HomeTab();
                return homeTab;
            case 1:
                FavouriteTab favouriteTab = new FavouriteTab();
                return favouriteTab;
            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        return tabCount;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        String Title = "";
        if (position == 0) {
            Title = "HOME";
        } else if (position == 1) {
            Title = "FAVOURITE";
        }
        return Title;
    }
}
