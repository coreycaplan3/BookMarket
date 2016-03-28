package com.github.coreycaplan3.bookmarket.utilities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.Snackbar.Callback;
import android.support.design.widget.Snackbar.Duration;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * Created by Corey on 3/26/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class UiUtility {

    public static void snackbar(View view, @StringRes int message, @Nullable Callback callback) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setCallback(callback)
                .show();
    }

    public static void snackbar(View view, @StringRes int message, @Nullable Callback callback,
                                @StringRes int actionMessage, OnClickListener clickListener) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(actionMessage, clickListener)
                .setCallback(callback)
                .show();
    }

    public static void snackbar(View view, @StringRes int message, @Duration int duration,
                                @Nullable Callback callback) {
        Snackbar.make(view, message, duration)
                .setCallback(callback)
                .show();
    }

    public static void toast(Context context, @StringRes int message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
