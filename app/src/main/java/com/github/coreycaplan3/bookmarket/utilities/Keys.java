package com.github.coreycaplan3.bookmarket.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.github.coreycaplan3.bookmarket.functionality.UserProfile;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: Load and save information to {@link SharedPreferences}.
 */
@SuppressWarnings("unused")
public final class Keys {

    private static final String TAG = Keys.class.getSimpleName();

    private static final String KEY_SHARED_PREFERENCES = "sharedPreferencesStorage";
    private static final String PROFILE_NAME = "userFullName";
    private static final String PROFILE_EMAIL = "userEmail";
    private static final String PROFILE_CONNECTION_TOKEN = "userConnectionToken";
    private static final String PROFILE_USER_ID = "userId";
    private static final String PROFILE_UNIVERSITY = "userUniversity";

    private Keys() {
    }

    public static void saveUserInformation(Context context, UserProfile userProfile) {
        writeToPreferences(context, PROFILE_NAME, userProfile.getDisplayName());
        writeToPreferences(context, PROFILE_EMAIL, userProfile.getEmail());
        writeToPreferences(context, PROFILE_CONNECTION_TOKEN, userProfile.getConnectionToken());
        writeToPreferences(context, PROFILE_USER_ID, userProfile.getUserId());
        writeToPreferences(context, PROFILE_UNIVERSITY, userProfile.getUniversity());
    }

    @Nullable
    public static String getUserLoginToken(Context context) {
        return readPreferences(context, PROFILE_CONNECTION_TOKEN, null);
    }

    public static UserProfile getSavedLoginInformation(Context context) {
        String displayName = readPreferences(context, PROFILE_NAME, null);
        String email = readPreferences(context, PROFILE_EMAIL, null);
        String connectionToken = readPreferences(context, PROFILE_CONNECTION_TOKEN, null);
        String userId = readPreferences(context, PROFILE_USER_ID, null);
        String university = readPreferences(context, PROFILE_UNIVERSITY, null);
        return new UserProfile(displayName, email, connectionToken, userId, university);
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
