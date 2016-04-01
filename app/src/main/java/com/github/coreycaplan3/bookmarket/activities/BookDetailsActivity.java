package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.BookDetailsFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.GeneralUser;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

public class BookDetailsActivity extends AppCompatActivity implements GetNetworkCommunicator,
        PostNetworkCommunicator {

    private static final String TAG = BookDetailsActivity.class.getSimpleName();

    private static final String BUNDLE_BOOK = TAG + "bundleBook";
    private static final String BUNDLE_PROFILE = TAG + "bundleProfile";
    private static final String BUNDLE_SELLER = TAG + "bundleSeller";
    private static final String BUNDLE_IS_SELLING = TAG + "bundleIsSelling";

    private boolean mIsSelling;
    private UserProfile mUserProfile;
    private TextBook mTextBook;
    private GeneralUser mSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restoreInstance(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTextBook.getTitle());
        }
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mTextBook = intent.getParcelableExtra(IntentExtra.BOOK);
            mSeller = intent.getParcelableExtra(IntentExtra.SELLER);
            mIsSelling = intent.getBooleanExtra(IntentExtra.ACTIVITY_SELLING, false);
            BookDetailsFragment fragment = BookDetailsFragment.newInstance(mTextBook, mUserProfile,
                    mSeller, mIsSelling);
            FragmentCreator.create(fragment, FragmentKeys.BOOK_DETAILS_FRAGMENT,
                    R.id.book_details_container, getSupportFragmentManager());
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTextBook = savedInstanceState.getParcelable(BUNDLE_BOOK);
            mSeller = savedInstanceState.getParcelable(BUNDLE_SELLER);
            mIsSelling = savedInstanceState.getBoolean(BUNDLE_IS_SELLING);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {

    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {
        if (postConstraints.equals(PostNetworkConstants.CONSTRAINT_TRADE_BOOK_WITH_OTHER)) {
            BookDetailsFragment fragment = (BookDetailsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.book_details_container);
            if (fragment != null) {
                fragment.onNotificationComplete();
            }
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_BOOK, mTextBook);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelable(BUNDLE_SELLER, mSeller);
        outState.putBoolean(BUNDLE_IS_SELLING, mIsSelling);
    }
}
