package com.github.coreycaplan3.bookmarket.fragments.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.activities.MyListingsActivity;
import com.github.coreycaplan3.bookmarket.adapters.DesiredTradesAdapter;
import com.github.coreycaplan3.bookmarket.dialogs.SignOutDialog;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.DesiredBooksFragment;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.DesiredBooksFormFragment;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ProfileFragment.class.getSimpleName();
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
        if (savedInstanceState != null) {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
        } else {
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        CardView notificationsCardView = (CardView) view
                .findViewById(R.id.fragment_profile_notifications_card);
        CardView bookListingsCardView = (CardView) view
                .findViewById(R.id.fragment_profile_book_sellings_card);
        CardView tradeListingsCardView = (CardView) view
                .findViewById(R.id.fragment_profile_book_trades_card);
        CardView addTradeCardView = (CardView) view
                .findViewById(R.id.fragment_profile_add_trade_card);
        CardView signOutCardView = (CardView) view
                .findViewById(R.id.fragment_profile_sign_out_card);

        notificationsCardView.setOnClickListener(this);
        bookListingsCardView.setOnClickListener(this);
        tradeListingsCardView.setOnClickListener(this);
        addTradeCardView.setOnClickListener(this);
        signOutCardView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fragment_profile_notifications_card) {
            NotificationsFragment fragment = NotificationsFragment.newInstance(mUserProfile);
            FragmentCreator.create(fragment, FragmentKeys.NOTIFICATIONS_FRAGMENT,
                    R.id.profile_container, getFragmentManager());
        } else if (id == R.id.fragment_profile_book_sellings_card) {
            Intent intent = new Intent(getContext(), MyListingsActivity.class);
            intent.putExtra(IntentExtra.PROFILE, mUserProfile);
            intent.putExtra(IntentExtra.ACTIVITY_SELLING, true);
            startActivity(intent);
        } else if (id == R.id.fragment_profile_book_trades_card) {
            Intent intent = new Intent(getContext(), MyListingsActivity.class);
            intent.putExtra(IntentExtra.PROFILE, mUserProfile);
            startActivity(intent);
        } else if (id == R.id.fragment_profile_desired_trade_card) {
            DesiredBooksFragment fragment = DesiredBooksFragment.newInstance(mUserProfile);
            FragmentCreator.create(fragment, FragmentKeys.VIEW_DESIRED_TRADES_FRAGMENT,
                    R.id.profile_container, getFragmentManager());
        } else if (id == R.id.fragment_profile_add_trade_card) {
            DesiredBooksFormFragment fragment = DesiredBooksFormFragment.newInstance(mUserProfile);
            FragmentCreator.create(fragment, FragmentKeys.ADD_DESIRED_TRADE_FRAGMENT,
                    R.id.profile_container, getFragmentManager());
        } else if (id == R.id.fragment_profile_sign_out_card) {
            SignOutDialog dialog = SignOutDialog.newInstance(R.string.sign_out_title,
                    R.string.sign_out_message);
            dialog.show(getFragmentManager(), SignOutDialog.DIALOG_TAG);
        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("Found: " + id));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
    }
}
