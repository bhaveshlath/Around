package com.example.blath.around.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.blath.around.activities.HomeActivity;
import com.example.blath.around.fragments.HomeMessageFragment;
import com.example.blath.around.fragments.HomePostsFragment;
import com.example.blath.around.fragments.HomeProfileFragment;
import com.example.blath.around.fragments.HomeSearchFragment;

/**
 * Created by blath on 1/14/18.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
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