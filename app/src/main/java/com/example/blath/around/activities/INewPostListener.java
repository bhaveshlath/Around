package com.example.blath.around.activities;

import android.support.annotation.Nullable;

import com.example.blath.around.models.DateRange;
import com.example.blath.around.models.Location;
import com.example.blath.around.models.Sport;

import java.util.Date;

/**
 * Created by blath on 4/17/17.
 */

public interface INewPostListener {

    @Nullable
    String getSportName();

    DateRange getDateRange();

    Location getLocation();

    void setSportName(String sportName);
    void setDateRange(Date startDate, Date endDate);
    void setLocation(long latitude, long longitude, String city, String country);

}
