package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.SellingFormFragment;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.TradingFormFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class FormActivity extends AppCompatActivity implements View.OnClickListener,
        PostNetworkCommunicator, GetNetworkCommunicator {

    private static final String TAG = TitleActivity.class.getSimpleName();

    private boolean mIsSelling;
    private UserProfile mUserProfile;

    private static final String BUNDLE_SELLING = TAG + "selling";
    private static final String BUNDLE_PROFILE = TAG + "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)
                findViewById(R.id.form_floating_action_button);
        if (fab != null) {
            fab.setOnClickListener(this);
        }

        initiateFragment(savedInstanceState);
    }

    private void initiateFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());

            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mIsSelling = intent.getBooleanExtra(IntentExtra.ACTIVITY_SELLING, false);
            if (mIsSelling) {
                FragmentCreator.create(SellingFormFragment.newInstance(mUserProfile), SELL_FORM_FRAGMENT,
                        R.id.title_container, getSupportFragmentManager());
            } else {
                FragmentCreator.create(TradingFormFragment.newInstance(mUserProfile),
                        TRADE_FORM_FRAGMENT, R.id.form_container, getSupportFragmentManager());
            }
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mIsSelling = savedInstanceState.getBoolean(BUNDLE_SELLING);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.form_floating_action_button) {
            //TODO implement barcode
        }
    }


    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {

    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putBoolean(BUNDLE_SELLING, mIsSelling);
    }

}
