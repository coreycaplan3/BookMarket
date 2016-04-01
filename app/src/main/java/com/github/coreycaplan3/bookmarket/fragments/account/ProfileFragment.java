package com.github.coreycaplan3.bookmarket.fragments.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.coreycaplan3.bookmarket.functionality.UserProfile;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class ProfileFragment extends Fragment {

    private UserProfile mUserProfile;

    private static final String BUNDLE_PROFILE = "bundleProfile";

    public static ProfileFragment newInstance(UserProfile userProfile) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        fragment.setArguments(arguments);
        return fragment;
    }

}
