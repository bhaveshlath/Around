package com.example.blath.around.models;

import java.io.Serializable;

/**
 * Created by blath on 11/10/17.
 */

public enum PostStatus implements Serializable {
    ACTIVE,
    INACTIVE;

    public String getStatusString() {
        return this.name();
    }
}

