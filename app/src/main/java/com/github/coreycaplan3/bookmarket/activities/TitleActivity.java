package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.TitleFragment;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.TradeTitleFragment;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener,
        FragmentManager.OnBackStackChangedListener {

    private static final String TAG = TitleActivity.class.getSimpleName();

    private UserProfile mUserProfile;

    private static final String BUNDLE_PROFILE = TAG + "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        initiateFragment(savedInstanceState);

        if (getSupportActionBar() != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.title_container);
            if (fragment != null) {
                getSupportActionBar().setTitle(fragment.getTag());
            }
        }
    }

    private void initiateFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            FragmentCreator.create(TitleFragment.newInstance(), TITLE_FRAGMENT,
                    R.id.title_container, getSupportFragmentManager());
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getSupportActionBar() != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.title_container);
            if (fragment != null) {
                getSupportActionBar().setTitle(fragment.getTag());
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_buy_card) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra(IntentExtra.ACTIVITY_BUY, true);
            startActivity(intent);
        } else if (id == R.id.title_trade_card) {
            TradeTitleFragment fragment = TradeTitleFragment.newInstance();
            FragmentCreator.create(fragment, FragmentKeys.TRADE_TITLE_FRAGMENT,
                    R.id.title_container, getSupportFragmentManager());
        } else if (id == R.id.title_sell_card) {
            Intent intent = new Intent(getApplicationContext(), FormActivity.class);
            intent.putExtra(IntentExtra.PROFILE, mUserProfile);
            intent.putExtra(IntentExtra.ACTIVITY_SELLING, true);
            startActivity(intent);
        } else if (id == R.id.title_account_card) {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra(IntentExtra.PROFILE, mUserProfile);
            startActivity(intent);
        } else if (id == R.id.trade_title_post_card) {
            getSupportFragmentManager().popBackStack();
            Intent intent = new Intent(getApplicationContext(), FormActivity.class);
            intent.putExtra(IntentExtra.PROFILE, mUserProfile);
            startActivity(intent);
        } else if (id == R.id.trade_title_find_card) {
            getSupportFragmentManager().popBackStack();
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("INVALID ARG"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
    }
}