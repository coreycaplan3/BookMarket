package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.BookMarketListAdapter;
import com.github.coreycaplan3.bookmarket.adapters.BookMarketListAdapter.TextBookClickListener;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.BooksMarketListFragment;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import java.util.ArrayList;

public class BookMarketplaceActivity extends AppCompatActivity implements TextBookClickListener {

    private boolean mIsBuyingBooks;
    private String mBookIsbn;
    private UserProfile mUserProfile;
    private ArrayList<TextBook> mTextBookList;

    private static final String BUNDLE_ISBN = "bundleIsbn";
    private static final String BUNDLE_BUYING_BOOKS = "bundleBuyingBooks";
    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_BOOKS = "bundleBooks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_marketplace);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        restoreState(savedInstanceState);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mIsBuyingBooks = intent.getBooleanExtra(IntentExtra.ACTIVITY_BUY, false);
            mBookIsbn = intent.getStringExtra(IntentExtra.ISBN);

            FragmentCreator.createNetworks(getSupportFragmentManager());
            BooksMarketListFragment fragment = BooksMarketListFragment.newInstance("", mTextBookList,
                    mUserProfile, mIsBuyingBooks);
            FragmentCreator.create(fragment, FragmentKeys.BOOKS_MARKET_LIST_FRAGMENT,
                    R.id.book_marketplace_container, getSupportFragmentManager());
        } else {
            mBookIsbn = savedInstanceState.getString(BUNDLE_ISBN);
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTextBookList = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            mIsBuyingBooks = savedInstanceState.getBoolean(BUNDLE_BUYING_BOOKS);
        }
    }

    @Override
    public void onTextBookClicked(TextBook textBook) {
        Intent intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
        intent.putExtra(IntentExtra.BOOK, textBook);
        intent.putExtra(IntentExtra.ACTIVITY_BUY, mIsBuyingBooks);
        intent.putExtra(IntentExtra.PROFILE, mUserProfile);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_ISBN, mBookIsbn);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelableArrayList(BUNDLE_BOOKS, mTextBookList);
        outState.putBoolean(BUNDLE_BUYING_BOOKS, mIsBuyingBooks);
    }

}
