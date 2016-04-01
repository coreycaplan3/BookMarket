package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
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
        OnItemClickListener, SearchView.OnQueryTextListener {

    private boolean mIsBuyingBook = false;
    private UserProfile mUserProfile;
    private ArrayList<TextBook> mBookData = new ArrayList<>();

    private ListView mListView;
    private SearchView mSearchView;
    private ProgressBar mProgressBar;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_BOOKS = "bundleBooks";
    private static final String BUNDLE_IS_BUYING = "bundleIsBuying";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mListView = (ListView) findViewById(R.id.activity_search_list);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        if (mSearchView != null) {
            mSearchView.setOnQueryTextListener(this);
        }
        mProgressBar = (ProgressBar) findViewById(R.id.search_progress_bar);

        restoreInstance(savedInstanceState);
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mIsBuyingBook = intent.getBooleanExtra(IntentExtra.ACTIVITY_BUY, false);
            if (mIsBuyingBook) {
                String hint = getString(R.string.search_for_book_buy);
                mSearchView.setQueryHint(hint);
            } else {
                String hint = getString(R.string.search_trade_for);
                mSearchView.setQueryHint(hint);
            }
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mBookData = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            mListView.setAdapter(new BookAdapter(mBookData));
            mIsBuyingBook = savedInstanceState.getBoolean(BUNDLE_IS_BUYING);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), BookMarketplaceActivity.class);
        intent.putExtra(IntentExtra.PROFILE, mUserProfile);
        intent.putExtra(IntentExtra.ISBN, mBookData.get(position).getIsbn());
        intent.putExtra(IntentExtra.ACTIVITY_BUY, mIsBuyingBook);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelableArrayList(BUNDLE_BOOKS, mBookData);
        outState.putBoolean(BUNDLE_IS_BUYING, mIsBuyingBook);
    }

    @Override
    public void onGetNetworkTaskComplete(Bundle result, @GetNetworkConstraints String getConstraints) {
        switch (getConstraints) {
            case GetNetworkConstants.GET_CONSTRAINT_SEARCH:
                onSearchComplete(result, getConstraints);
                break;
        }
    }

    private void onSearchComplete(Bundle result, @GetNetworkConstraints String getConstraints) {
        mBookData = result.getParcelableArrayList(getConstraints);
        BookAdapter adapter = new BookAdapter(mBookData);
        mListView.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        GetNetworkFragment fragment = (GetNetworkFragment) getSupportFragmentManager()
                .findFragmentByTag(FragmentKeys.GET_NETWORK_FRAGMENT);
        fragment.startSearchTask(query);
        mProgressBar.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private class BookAdapter implements ListAdapter {

        private final ArrayList<TextBook> mListData;

        private BookAdapter(ArrayList<TextBook> mListData) {
            this.mListData = mListData;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            if (mListData == null) {
                return 0;
            } else {
                return mListData.size();
            }
        }

        @Override
        public Object getItem(int position) {
            if (mListData == null) {
                return null;
            } else {
                return mListData.get(position);
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getApplicationContext());
            if (mListData == null) {
                textView.setText(R.string.no_results);
                return textView;
            } else {
                textView.setText(mListData.get(position).getTitle());
                return textView;
            }
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return mListData == null || mListData.size() == 0;
        }

    }

}
