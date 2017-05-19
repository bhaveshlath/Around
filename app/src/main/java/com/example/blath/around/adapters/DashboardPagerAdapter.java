package com.example.blath.around.adapters;

import com.example.blath.around.fragments.DashboardMessageFragment;
import com.example.blath.around.fragments.DashboardPostsFragment;
import com.example.blath.around.activities.DashboardActivity;
import com.example.blath.around.fragments.DashboardProfileFragment;
import com.example.blath.around.fragments.DashboardSearchFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by blath on 4/12/17.
 */

public class DashboardPagerAdapter extends FragmentPagerAdapter {

    public DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0: fragment = new DashboardPostsFragment();
                    break;
            case 1: fragment = new DashboardSearchFragment();
                    break;
            case 2: fragment = new DashboardMessageFragment();
                    break;
            case 3: fragment = new DashboardProfileFragment();
                    break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return DashboardActivity.NUM_PAGES;
    }
}
