package com.example.blath.around.activities;

import android.support.annotation.Nullable;

import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.DateRange;

import java.util.Date;

/**
 * Created by blath on 4/17/17.
 */

public interface INewPostListener {

    DateRange getDateRange();

    AroundLocation getLocation();

    void setDateRange(Date startDate, Date endDate);
    void setLocation(long latitude, long longitude, String city, String postalCode, String country);

}
