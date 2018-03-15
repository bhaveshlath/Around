package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 2/1/18.
 */

public class Location implements Serializable {
    private String type;
    private Double[] coordinates;

    public Location(Double[] coordinates) {
        this.type = "Point";
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }
}
