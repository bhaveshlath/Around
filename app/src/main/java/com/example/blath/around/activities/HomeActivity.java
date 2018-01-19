package com.example.blath.around.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.blath.around.R;
import com.example.blath.around.adapters.HomePagerAdapter;
import com.example.blath.around.commons.Utils.UIUtils;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private int mSelectedMenuItem;
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mHomeViewPager;
    public static final int NUM_PAGES = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.blath.around.R.layout.activity_home);

        UIUtils.animateStatusBarColorTransition(this, R.color.around_background_end_color, R.color.around_background_end_color);
        mHomeViewPager = (ViewPager) findViewById(R.id.around_fragment_container);
        setupViewPager(mHomeViewPager);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mBottomNavigationView.setSelectedItemId(R.id.action_home);
    }

    private void setupViewPager(ViewPager viewPager){
        PagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mSelectedMenuItem = item.getItemId();
        switch (mSelectedMenuItem) {
            case R.id.action_home:
                mHomeViewPager.setCurrentItem(0);
                break;
            case R.id.action_search:
                mHomeViewPager.setCurrentItem(1);
                break;
            case R.id.action_messages:
                mHomeViewPager.setCurrentItem(2);
                break;
            case R.id.action_profile:
                mHomeViewPager.setCurrentItem(3);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        int pageIndex = mHomeViewPager.getCurrentItem();
        if(pageIndex!= 0){
            mBottomNavigationView.setSelectedItemId(R.id.action_home);
        }else{
            super.onBackPressed();
        }
    }
}
