package com.example.blath.around.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.blath.around.R;
import com.example.blath.around.fragments.NewPostTypeFragment;
import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.DateRange;

import java.util.Date;

public class NewPostActivity extends AppCompatActivity implements INewPostListener{

    public static final String KEY_NEW_POST_SPORT_NAME = "New_Post_Sport_Name";
    public static final String KEY_NEW_POST_DATE = "New_Post_Date";
    public static final String KEY_NEW_POST_LOCATION = "New_Post_Location";
    public static final int KEY_NEW_POST_LOCATION_PERMISSION = 01;

    DateRange mDateRange ;
    AroundLocation mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        if(findViewById(R.id.new_post_container) != null){
            if (savedInstanceState != null) {
                return;
            }

            NewPostTypeFragment newPostTypeFragment = new NewPostTypeFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.new_post_container, newPostTypeFragment).commit();

        }
    }

    @Override
    public DateRange getDateRange() {
        if(mDateRange == null){
            return null;
        }else{
            return mDateRange;
        }
    }

    @Override
    public AroundLocation getLocation() {
        return null;
    }

    @Override
    public void setDateRange(Date startDate, Date endDate) {
        mDateRange = new DateRange(startDate, endDate);
    }

    @Override
    public void setLocation(long latitude, long longitude, String city, String postalCode, String country) {
        mLocation = new AroundLocation(latitude, longitude, city, postalCode, country);
    }
}
