package com.example.blath.around.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.UIUtils;

public class HomeActivity extends FragmentActivity{

    private TabLayout mTabLayout;

    public static final int NUM_PAGES = 4;
    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_mail,
            R.drawable.ic_profile };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.blath.around.R.layout.activity_home);

        UIUtils.animateStatusBarColorTransition(this, R.color.around_home_header_color, R.color.around_home_header_color);

        ViewPager pager = (ViewPager) findViewById(R.id.around_fragment_container);
        PagerAdapter pagerAdapter = new com.example.blath.around.adapters.DashboardPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(pager);
        setupTabIcons();

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplication(), R.color.gradient_light_green);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(getApplication(), R.color.white);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabIcons() {
        for(int i = 0; i <= 3; i++){
            mTabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }
}
