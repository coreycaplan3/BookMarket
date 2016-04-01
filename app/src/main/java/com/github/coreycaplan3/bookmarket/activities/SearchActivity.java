package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.SearchAdapter;
import com.github.coreycaplan3.bookmarket.adapters.TextBookViewHolder;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import java.util.ArrayList;

/**
 * Created by Corey on 3/27/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class SearchActivity extends AppCompatActivity implements GetNetworkCommunicator,
        OnItemClickListener, SearchView.OnQueryTextListener, PostNetworkCommunicator,
        TextBookViewHolder.OnViewHolderClickListener {

    private boolean mIsProgressShowing;
    private boolean mIsBuyingBook = false;
    private UserProfile mUserProfile;
    private ArrayList<TextBook> mBookData = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_BOOKS = "bundleBooks";
    private static final String BUNDLE_IS_BUYING = "bundleIsBuying";
    private static final String BUNDLE_IS_PROGRESS_SHOWING = "bundleIsProgressShowing";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_search_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSearchView = (SearchView) findViewById(R.id.search_view);
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(this);
        }
        mProgressBar = (ProgressBar) findViewById(R.id.search_progress_bar);

        restoreInstance(savedInstanceState);
        if (mIsProgressShowing) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mIsBuyingBook = intent.getBooleanExtra(IntentExtra.ACTIVITY_BUY, false);
            String hint = getString(R.string.search_for_book_hint);
            mSearchView.setQueryHint(hint);
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mBookData = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            mRecyclerView.setAdapter(new SearchAdapter(mBookData));
            mIsBuyingBook = savedInstanceState.getBoolean(BUNDLE_IS_BUYING);
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_IS_PROGRESS_SHOWING);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSearchView.requestFocus();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), BookMarketListActivity.class);
        intent.putExtra(IntentExtra.PROFILE, mUserProfile);
        intent.putExtra(IntentExtra.ISBN, mBookData.get(position).getIsbn());
        intent.putExtra(IntentExtra.ACTIVITY_BUY, mIsBuyingBook);
        startActivity(intent);
        finish();
    }

    @Override
    public void onGetNetworkTaskComplete(Bundle result, @GetNetworkConstraints String getConstraints) {
        switch (getConstraints) {
            case GetNetworkConstants.GET_CONSTRAINT_SEARCH:
                onSearchComplete(result, getConstraints);
                break;
        }
    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {

    }

    private void onSearchComplete(Bundle result, @GetNetworkConstraints String getConstraints) {
        mBookData = result.getParcelableArrayList(getConstraints);
        if (mBookData == null) {
            Log.e("onSearchComplete: ", "NULL!!!");
        }
        mRecyclerView.setAdapter(new SearchAdapter(mBookData));
        mProgressBar.setVisibility(View.GONE);
        mIsProgressShowing = false;
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        GetNetworkFragment fragment = (GetNetworkFragment) getSupportFragmentManager()
                .findFragmentByTag(FragmentKeys.GET_NETWORK_FRAGMENT);
        fragment.startSearchTask(query);
        mProgressBar.setVisibility(View.VISIBLE);
        mIsProgressShowing = true;
        mRecyclerView.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelableArrayList(BUNDLE_BOOKS, mBookData);
        outState.putBoolean(BUNDLE_IS_BUYING, mIsBuyingBook);
        outState.putBoolean(BUNDLE_IS_PROGRESS_SHOWING, mIsProgressShowing);
    }

    @Override
    public void onViewHolderClicked(int layoutPosition) {
        if (mIsBuyingBook) {

        } else {

        }
    }
}
