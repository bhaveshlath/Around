package com.example.blath.around.models;

import java.util.Date;

/**
 * Created by blath on 4/17/17.
 */

public class DateRange {
    private Date mStartDate;
    private Date mEndDate;

    public DateRange(Date mStartDate, Date mEndDate) {
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }

    public void setEndDate(Date mEndDate) {
        this.mEndDate = mEndDate;
    }
}
