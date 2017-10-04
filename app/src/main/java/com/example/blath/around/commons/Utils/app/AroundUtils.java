package com.example.blath.around.commons.Utils.app;

/**
 * Created by blath on 9/28/17.
 */

public class AroundUtils {
    public enum AroundPostRequestType {
        SPORTS,
        STUDY,
        CONCERT,
        TRAVEL,
        OTHER
    }

    public static AroundPostRequestType sAroundPostRequestType = AroundPostRequestType.SPORTS;

    public static AroundPostRequestType getAroundPostRequestType() {
        return sAroundPostRequestType;
    }

    public static void setAroundPostRequestType(AroundPostRequestType aroundPostRequestType) {
        sAroundPostRequestType = aroundPostRequestType;
    }
}
