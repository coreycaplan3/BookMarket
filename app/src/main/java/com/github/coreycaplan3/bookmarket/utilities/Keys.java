package com.github.coreycaplan3.bookmarket.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by Corey on 3/26/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: Load and save information to {@link SharedPreferences}.
 */
@SuppressWarnings("unused")
public final class Keys {

    private static final String TAG = Keys.class.getSimpleName();

    private static final String KEY_SHARED_PREFERENCES = "sharedPreferencesStorage";
    private static final String LOGIN_STATUS = "userLoginStatus";

    private Keys() {
    }

    public static void setUserLoginToken(Context context, String loginToken) {
        writeToPreferences(context, LOGIN_STATUS, loginToken);
    }

    private static void writeToPreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static void writeToPreferences(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static void writeToPreferences(Context context, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    private static void writeToPreferences(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static void writeToPreferences(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    @Nullable
    public static String getUserLoginToken(Context context) {
        return readPreferences(context, LOGIN_STATUS, null);
    }

    @Nullable
    private static String readPreferences(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    private static int readPreferences(Context context, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    private static float readPreferences(Context context, String key, float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    private static long readPreferences(Context context, String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    private static boolean readPreferences(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

}
