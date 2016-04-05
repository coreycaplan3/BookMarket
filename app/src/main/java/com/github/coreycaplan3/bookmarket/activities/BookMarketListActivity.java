package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.TextBookClickListener;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkFragment;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.BookMarketListFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import java.util.ArrayList;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class BookMarketListActivity extends AppCompatActivity implements TextBookClickListener,
        GetNetworkCommunicator, PostNetworkCommunicator {

    private static final String TAG = BookMarketListActivity.class.getSimpleName();
    private boolean mIsBuyingBooks;
    private String mBookIsbn;
    private UserProfile mUserProfile;
    private ArrayList<TextBook> mTextBookList;

    private static final String BUNDLE_ISBN = "bundleIsbn";
    private static final String BUNDLE_BUYING_BOOKS = "bundleBuyingBooks";
    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_BOOKS = "bundleBooks";

    private static final int REQUEST_TRADE_OR_SELL = 0x000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_marketplace);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restoreState(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mIsBuyingBooks = intent.getBooleanExtra(IntentExtra.ACTIVITY_BUY, false);
            mBookIsbn = intent.getStringExtra(IntentExtra.ISBN);

            FragmentCreator.createNetworks(getSupportFragmentManager());
            BookMarketListFragment fragment = BookMarketListFragment.newInstance(mBookIsbn,
                    mTextBookList, mUserProfile, mIsBuyingBooks);
            FragmentCreator.create(fragment, BOOKS_MARKET_LIST_FRAGMENT,
                    R.id.book_marketplace_container, getSupportFragmentManager());
        } else {
            mBookIsbn = savedInstanceState.getString(BUNDLE_ISBN);
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTextBookList = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            mIsBuyingBooks = savedInstanceState.getBoolean(BUNDLE_BUYING_BOOKS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBooks();
    }

    private void getBooks() {
        if (mBookIsbn.equals("test")) {
            GetNetworkFragment fragment = (GetNetworkFragment) getSupportFragmentManager()
                    .findFragmentByTag(GET_NETWORK_FRAGMENT);
            fragment.getTextbookFromIsbn("9780077238469");
            fragment.getTextbookFromIsbn("9789380386355");
            fragment.getTextbookFromIsbn("9781861002204");
            fragment.getTextbookFromIsbn("9788445074879");
            fragment.getTextbookFromIsbn("9781405091046");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TRADE_OR_SELL && resultCode == RESULT_OK) {
            finish();
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
    public void onTextBookClicked(TextBook textBook) {
        Intent intent = new Intent(getApplicationContext(), BookDetailsActivity.class);
        intent.putExtra(IntentExtra.BOOK, textBook);
        intent.putExtra(IntentExtra.ACTIVITY_BUY, mIsBuyingBooks);
        intent.putExtra(IntentExtra.PROFILE, mUserProfile);
        startActivityForResult(intent, REQUEST_TRADE_OR_SELL);
    }


    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {
        if (getConstraints.equals(GetNetworkConstants.GET_CONSTRAINT_GET_SELLING_BOOKS)) {
            mTextBookList = result.getParcelableArrayList(getConstraints);
            BookMarketListFragment fragment = (BookMarketListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.book_marketplace_container);
            fragment.onBooksRefreshed(mTextBookList);
        } else if(getConstraints.equals(GetNetworkConstants.GET_CONSTRAINT_BARCODE_AUTOCOMPLETE)) {
            TextBook textBook = result.getParcelable(getConstraints);
            if(textBook == null) {
                Log.e(TAG, "onGetNetworkTaskComplete: " + "null book!");
                return;
            }
            double price = Math.random()* 25 + 100;
            @TextBook.Condition
            int random = (int) (Math.random() * 4);
            textBook = new TextBook(textBook.getTitle(), textBook.getAuthor(),
                    textBook.getIsbn(), price, random, textBook.getPicture());
            BookMarketListFragment fragment = (BookMarketListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.book_marketplace_container);
            fragment.onBooksRefreshed(textBook);
        }
    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {

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
