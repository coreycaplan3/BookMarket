package com.github.coreycaplan3.bookmarket.utilities;

import android.os.Build;

/**
 * Created by Corey on 2/7/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: To easily check the various versions of the user's device.
 */
public final class VersionUtility {

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
