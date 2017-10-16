package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 8/7/17.
 */

public class AgeRange implements Serializable{
    private int minAge;
    private int maxAge;

    public AgeRange(int minAge, int maxAge){
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
