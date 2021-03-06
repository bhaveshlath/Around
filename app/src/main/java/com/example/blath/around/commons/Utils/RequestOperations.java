package com.example.blath.around.commons.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.blath.around.R;
import com.example.blath.around.commons.Utils.app.AroundAppHandles;
import com.example.blath.around.commons.Utils.app.AroundUtils;
import com.example.blath.around.models.AroundLocation;
import com.example.blath.around.models.User;
import com.example.blath.around.models.UserPersonalInformation;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by blath on 7/17/17.
 */

public class RequestOperations {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PHONE_NUMBER_REGEX =
            Pattern.compile("^[0-9]{10}$", Pattern.CASE_INSENSITIVE);


    public static boolean validateEmail(String emailString) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailString);
        return matcher.find();
    }

    public static boolean validatePhoneNumber(String phoneNumberString) {
        Matcher matcher = VALID_PHONE_NUMBER_REGEX.matcher(phoneNumberString);
        return matcher.find();
    }

    public static boolean validateDateOfBirth(int year) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year >= currentYear || (currentYear - year) < 12 || (currentYear - year) > 115) {
            return false;
        }
        return true;
    }

    public static int isRegisterDataValid(String emailID, String phoneNumber, String dateOfBirth) {
        if (!validateEmail(emailID)) {
            return 1;
        } else if (!validateDateOfBirth(Integer.parseInt(dateOfBirth))) {
            return 2;
        } else if (!validatePhoneNumber(phoneNumber)) {
            return 3;
        }
        return 0;
    }


    public static User getUserObject(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AroundAppHandles.AROUND_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return new User(sharedPreferences.getString(User.KEY_USER_ID, ""), new UserPersonalInformation(sharedPreferences.getString(User.KEY_USER_FIRST_NAME, "John"),
                sharedPreferences.getString(User.KEY_USER_LAST_NAME, "Doe"),
                sharedPreferences.getString(User.KEY_USER_EMAIL, "abc@xyz.com"),
                sharedPreferences.getString(User.KEY_USER_DOB, "01 Jan, 2017"),
                sharedPreferences.getString(User.KEY_USER_GENDER, "male"),
                sharedPreferences.getString(User.KEY_USER_PHONE_NUMBER, "0000000000")),

                new AroundLocation(Double.parseDouble(sharedPreferences.getString(User.KEY_USER_LATITUDE, "72.12121")),
                        Double.parseDouble(sharedPreferences.getString(User.KEY_USER_LONGITUTDE, "-34.1212")),
                        sharedPreferences.getString(User.KEY_USER_LOCATION_ADDRESS, "Los Angeles,CA 90007, USA"),
                        sharedPreferences.getString(User.KEY_USER_LOCATION_POSTALCODE, "90007"),
                        sharedPreferences.getString(User.KEY_USER_LOCATION_COUNTRY, "USA")),
                sharedPreferences.getString(User.KEY_USER_PROFILE_STATUS, "Pending-Verification"),
                sharedPreferences.getString(User.KEY_USER_PROFILE_IMAGE, User.KEY_DEFAULT_PROFILE_ICON));
    }

    private static boolean verifyMinMaxAgeRange(String minAge, String maxAge) {
        int minValue = Integer.parseInt(minAge);
        int maxValue = Integer.parseInt(maxAge);

        if (minValue > maxValue) {
            return false;
        }
        if (minValue < 12 || minValue > 120) {
            return false;
        }
        if (maxValue < 12 || maxValue > 120) {
            return false;
        }

        return true;
    }

    public static String verifyPostDetails(Context context, String postTitleContent, String postSubTitleContent, String date, String time, String ageRangeMin, String ageRangeMax, String genderPreference, String description,
                                           AroundLocation userLocation, AroundUtils.AroundPostRequestType aroundPostRequestType) {

        switch (aroundPostRequestType) {
            case SPORTS:
                if (postTitleContent.isEmpty()) {
                    return context.getString(R.string.pick_sport_dialog_message);
                }
                break;
            case STUDY:
                if (postTitleContent.isEmpty()) {
                    return context.getString(R.string.pick_study_dialog_message);
                }
                break;
            case CONCERT:
                if (postTitleContent.isEmpty()) {
                    return context.getString(R.string.pick_concert_dialog_message);
                }
                break;
            case TRAVEL:
                if (postTitleContent.isEmpty()) {
                    return context.getString(R.string.pick_travel_source_dialog_message);
                } else if (postSubTitleContent.isEmpty()) {
                    return context.getString(R.string.pick_travel_destination_dialog_message);
                }
                break;
            case OTHER:
                if (postTitleContent.isEmpty()) {
                    return context.getString(R.string.pick_other_dialog_message);
                }
                break;
        }

        if (date.equals("")) {
            return context.getString(R.string.pick_date);
        } else if (time.equals("")) {
            return context.getString(R.string.pick_time);
        } else if (ageRangeMin.equals("")) {
            return context.getString(R.string.pick_min_age);
        } else if (ageRangeMax.equals("")) {
            return context.getString(R.string.pick_max_age);
        } else if (genderPreference.equals("")) {
            return context.getString(R.string.pick_gender_preference);
        } else if (description.equals("")) {
            return context.getString(R.string.pick_description);
        } else if (!
                verifyMinMaxAgeRange(ageRangeMin, ageRangeMax)) {
            return context.getString(R.string.age_range_failing_message);
        } else if (userLocation == null) {
            return context.getString(R.string.location_alert);
        } else {
            return context.getString(R.string.success);
        }
    }
}
