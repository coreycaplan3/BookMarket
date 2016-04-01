package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.MyBookDetailsFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

public class MyBookDetailsActivity extends AppCompatActivity {

    private static final String TAG = MyBookDetailsActivity.class.getSimpleName();

    private static final String BUNDLE_BOOK = TAG + "bundleBook";
    private static final String BUNDLE_PROFILE = TAG + "bundleProfile";
    private static final String BUNDLE_IS_SELLING = TAG + "bundleIsSelling";
    private static final String BUNDLE_IS_NEW_TEXT_BOOK = TAG + "bundleIsNewTextBook";

    private boolean mIsSelling;
    private boolean mIsNewTextBook;
    private UserProfile mUserProfile;
    private TextBook mTextBook;

    public static final int REQUEST_CODE_EDIT_EVENT = 0x000001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book_details);
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
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mTextBook = intent.getParcelableExtra(IntentExtra.BOOK);
            mIsSelling = intent.getBooleanExtra(IntentExtra.ACTIVITY_SELLING, false);
            mIsNewTextBook = intent.getBooleanExtra(IntentExtra.ACTIVITY_NEW, false);
            MyBookDetailsFragment fragment = MyBookDetailsFragment
                    .newInstance(mTextBook, mUserProfile, mIsSelling, mIsNewTextBook);
            FragmentCreator.create(fragment, FragmentKeys.BOOK_DETAILS_FRAGMENT,
                    R.id.book_details_container, getSupportFragmentManager());
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTextBook = savedInstanceState.getParcelable(BUNDLE_BOOK);
            mIsSelling = savedInstanceState.getBoolean(BUNDLE_IS_SELLING);
            mIsNewTextBook = savedInstanceState.getBoolean(BUNDLE_IS_NEW_TEXT_BOOK);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_EVENT && resultCode == RESULT_OK) {
            mTextBook = data.getParcelableExtra(IntentExtra.BOOK);
            MyBookDetailsFragment fragment = (MyBookDetailsFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.my_book_details_container);
            fragment.onBookEdited(mTextBook);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_BOOK, mTextBook);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putBoolean(BUNDLE_IS_SELLING, mIsSelling);
        outState.putBoolean(BUNDLE_IS_NEW_TEXT_BOOK, mIsNewTextBook);
    }
}
