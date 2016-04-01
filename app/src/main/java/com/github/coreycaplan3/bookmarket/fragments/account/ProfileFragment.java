package com.github.coreycaplan3.bookmarket.fragments.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.bookmarket.R;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        CardView notificationsCardView = (CardView) view
                .findViewById(R.id.fragment_profile_notifications_card);
        CardView bookListingsCardView = (CardView) view
                .findViewById(R.id.fragment_profile_book_listings_card);
        CardView tradeListingsCardView = (CardView) view
                .findViewById(R.id.fragment_profile_trades_card);
        CardView signOutCardView = (CardView) view
                .findViewById(R.id.fragment_profile_notifications_card);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
