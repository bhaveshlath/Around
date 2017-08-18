package com.example.blath.around.models;

/**
 * Created by blath on 8/7/17.
 */

public class AgeRange {
    private int mMinAge;
    private int mMaxAge;

    public AgeRange(int minAge, int maxAge){
        mMinAge = minAge;
        mMaxAge = maxAge;
    }

    public int getMinAge() {
        return mMinAge;
    }

    public void setMinAge(int minAge) {
        mMinAge = minAge;
    }

    public int getMaxAge() {
        return mMaxAge;
    }

    public void setMaxAge(int maxAge) {
        mMaxAge = maxAge;
    }
}
