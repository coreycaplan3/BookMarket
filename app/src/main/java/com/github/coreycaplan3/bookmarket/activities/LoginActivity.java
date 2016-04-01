package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.account.LoginFragment;
import com.github.coreycaplan3.bookmarket.fragments.account.RegisterFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;
import com.github.coreycaplan3.bookmarket.utilities.Keys;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class LoginActivity extends AppCompatActivity implements PostNetworkCommunicator,
        FragmentManager.OnBackStackChangedListener, GetNetworkCommunicator {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        setInitialFragment(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.login_container);
            if (fragment != null) {
                getSupportActionBar().setTitle(fragment.getTag());
            }
        }
    }

    private void setInitialFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());
            LoginFragment fragment = LoginFragment.newInstance();
            FragmentCreator.create(fragment, LOGIN_FRAGMENT, R.id.login_container,
                    getSupportFragmentManager());
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.login_container);
        if (fragment.getTag().equals(FragmentKeys.REGISTER_FRAGMENT)) {
            getSupportFragmentManager().popBackStackImmediate();
            return false;
        } else {
            finish();
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {
        UserProfile userProfile = null;
        switch (postConstraints) {
            case PostNetworkConstants.CONSTRAINT_REGISTER:
                userProfile = result.getParcelable(postConstraints);
                if (userProfile != null) {
                    Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
                    intent.putExtra(IntentExtra.PROFILE, userProfile);
                    startActivity(intent);
                    finish();
                } else {
                    RegisterFragment fragment = (RegisterFragment) getSupportFragmentManager()
                            .findFragmentByTag(FragmentKeys.REGISTER_FRAGMENT);
                    if (fragment != null) {
                        fragment.onRegistrationFailed();
                    } else {
                        Log.e(TAG, "onPostNetworkTaskComplete: ",
                                new NullPointerException("RegisterFragment was null!"));
                    }
                }
                break;
            case PostNetworkConstants.CONSTRAINT_LOGIN:
                userProfile = result.getParcelable(postConstraints);
                LoginFragment fragment = (LoginFragment) getSupportFragmentManager()
                        .findFragmentByTag(FragmentKeys.LOGIN_FRAGMENT);
                if (userProfile != null) {
                    if (fragment != null) {
                        fragment.onSignInSuccessful();
                    }
                    Keys.setUserLoginToken(getApplicationContext(),
                            userProfile.getConnectionToken());
                    Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
                    intent.putExtra(IntentExtra.PROFILE, userProfile);
                    startActivity(intent);
                    finish();
                } else {
                    if (fragment != null) {
                        fragment.onSignInFailed();
                    }
                }
                break;
            default:
                Log.e(TAG, "onPostNetworkTaskComplete: ",
                        new IllegalArgumentException("Found: " + postConstraints));
        }
    }

    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {

    }

    @Override
    public void onBackStackChanged() {
        if (getSupportActionBar() != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.login_container);
            getSupportActionBar().setTitle(fragment.getTag());
        }
    }

}
