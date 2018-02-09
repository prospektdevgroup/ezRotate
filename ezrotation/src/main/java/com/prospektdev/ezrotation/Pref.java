package com.prospektdev.ezrotation;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

abstract class Pref {
    private static final String PREF_NAME = "ezrotation.preference";
    private static final String ACCELEROMETER_ROTATION = "accelerometerRotation";
    private static final String USER_ROTATION = "userRotation";

    final static int DEFAULT_VALUE = -1234567890;

    static void saveAccelerometerRotation(@NonNull Context context,
                                          int value) {
        save(context, ACCELEROMETER_ROTATION, value);
    }

    static int getAccelerometerRotation(@NonNull Context context) {
        return getPref(context).getInt(ACCELEROMETER_ROTATION, DEFAULT_VALUE);
    }

    static void saveUserRotation(@NonNull Context context,
                                 int value) {
        save(context, USER_ROTATION, value);
    }

    static int getUserRotation(@NonNull Context context) {
        return getPref(context).getInt(USER_ROTATION, DEFAULT_VALUE);
    }

    static void clear(@NonNull Context context) {
        getPref(context).edit()
                .clear()
                .apply();
    }

    private static void save(@NonNull Context context,
                             @NonNull String key,
                             int value) {
        getPref(context).edit()
                .putInt(key, value)
                .apply();
    }

    private static SharedPreferences getPref(@NonNull Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
