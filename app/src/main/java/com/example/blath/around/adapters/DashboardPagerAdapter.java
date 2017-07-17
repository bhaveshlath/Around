package com.example.blath.around.adapters;

import com.example.blath.around.fragments.HomeMessageFragment;
import com.example.blath.around.fragments.HomePostsFragment;
import com.example.blath.around.activities.HomeActivity;
import com.example.blath.around.fragments.HomeProfileFragment;
import com.example.blath.around.fragments.HomeSearchFragment;

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
            case 0: fragment = new HomePostsFragment();
                    break;
            case 1: fragment = new HomeSearchFragment();
                    break;
            case 2: fragment = new HomeMessageFragment();
                    break;
            case 3: fragment = new HomeProfileFragment();
                    break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return HomeActivity.NUM_PAGES;
    }
}
