package com.github.coreycaplan3.bookmarket.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.dialogs.DialogCallback;
import com.github.coreycaplan3.bookmarket.dialogs.SignOutDialog;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.DesiredBooksFragment;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.account.ProfileFragment;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.DesiredBooksFormFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;
import com.github.coreycaplan3.bookmarket.utilities.Keys;
import com.github.coreycaplan3.bookmarket.utilities.UiUtility;

import java.util.ArrayList;

import static com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.*;
import static com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.*;
import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class ProfileActivity extends AppCompatActivity implements DialogCallback,
        GetNetworkCommunicator, PostNetworkCommunicator {

    private UserProfile mUserProfile;

    private static final String BUNDLE_PROFILE = "bundleProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        restoreInstance(savedInstanceState);
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            FragmentCreator.createNetworks(getSupportFragmentManager());

            ProfileFragment fragment = ProfileFragment.newInstance(mUserProfile);
            FragmentCreator.create(fragment, PROFILE_FRAGMENT, R.id.profile_container,
                    getSupportFragmentManager());
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
        }
    }

    @Override
    public void onDialogClick(String dialogTag, DialogInterface dialog, int which) {
        if (dialogTag.equals(SignOutDialog.DIALOG_TAG) && which == Dialog.BUTTON_POSITIVE) {
            Keys.saveUserInformation(this, null);
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            UiUtility.toast(getApplicationContext(), R.string.sign_out_successful);
            finish();
        }
    }

    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {
        if(getConstraints.equals(GET_CONSTRAINT_GET_DESIRED_TRADING_BOOKS)) {
            DesiredBooksFragment fragment = (DesiredBooksFragment) getSupportFragmentManager()
                    .findFragmentByTag(FragmentKeys.VIEW_DESIRED_TRADES_FRAGMENT);
            ArrayList<TextBook> bookList = result.getParcelableArrayList(getConstraints);
            if(fragment != null) {
                fragment.onDesiredTextbooksLoaded(bookList);
            }
        }
    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {
        if (postConstraints.equals(CONSTRAINT_POST_DESIRED_TRADE)) {
            DesiredBooksFormFragment fragment = (DesiredBooksFormFragment)
                    getSupportFragmentManager().findFragmentByTag(ADD_DESIRED_TRADE_FRAGMENT);
            if (fragment != null) {
                fragment.onPostBookSuccessful();
                getFragmentManager().popBackStack();
            }
        } else if (postConstraints.equals(CONSTRAINT_EDIT_DESIRED_TRADE)) {
            DesiredBooksFormFragment fragment = (DesiredBooksFormFragment)
                    getSupportFragmentManager().findFragmentByTag(ADD_DESIRED_TRADE_FRAGMENT);
            if (fragment != null) {
                fragment.onEditBookSuccessful();
                getFragmentManager().popBackStack();
            }
        } else if (postConstraints.equals(CONSTRAINT_DELETE_DESIRED_TRADE_BOOK)) {
            DesiredBooksFragment fragment = (DesiredBooksFragment) getSupportFragmentManager()
                    .findFragmentByTag(VIEW_DESIRED_TRADES_FRAGMENT);
            TextBook textBook = result.getParcelable(postConstraints);
            if (fragment != null && textBook != null) {
                fragment.onDeleteSuccessful(textBook);
            } else if (fragment != null) {
                fragment.onDeleteFailure();
            }
        } else if (postConstraints.equals(CONSTRAINT_EDIT_DESIRED_TRADE)) {
            DesiredBooksFormFragment fragment = (DesiredBooksFormFragment)
                    getSupportFragmentManager().findFragmentByTag(VIEW_DESIRED_TRADES_FRAGMENT);
            TextBook textBook = result.getParcelable(postConstraints);
            if (fragment != null && textBook != null) {
                fragment.onEditBookSuccessful();
                getFragmentManager().popBackStack();
                DesiredBooksFragment desiredBooksFragment = (DesiredBooksFragment)
                        getSupportFragmentManager().findFragmentByTag(VIEW_DESIRED_TRADES_FRAGMENT);
                desiredBooksFragment.onEditSuccessful(textBook);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
    }
}
