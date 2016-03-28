package com.github.coreycaplan3.bookmarket.utilities;

import android.support.annotation.StringDef;

/**
 * Created by Corey on 3/26/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public final class FragmentKeys {

    private static final String TAG = FragmentKeys.class.getSimpleName();

    public static final String LOGIN_FRAGMENT = "fragmentLogin";
    public static final String TITLE_FRAGMENT = "fragmentTitle";

    public static final String POST_NETWORK_FRAGMENT = TAG + "postNetworkFragment";
    public static final String GET_NETWORK_FRAGMENT = TAG + "getNetworkFragment";

    @StringDef({LOGIN_FRAGMENT, TITLE_FRAGMENT})
    public @interface FragmentTag {
    }

    private FragmentKeys() {
    }

}
