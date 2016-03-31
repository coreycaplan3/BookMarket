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
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import java.util.ArrayList;

/**
 * Created by Corey on 3/27/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class SearchActivity extends AppCompatActivity implements GetNetworkCommunicator,
        OnItemClickListener {

    private ArrayList<TextBook> mBookData = new ArrayList<>();
    private boolean mIsBuyingBook = false;
    private boolean mIsTradingBook = false;

    private ListView mListView;
    private SearchView mSearchView;

    private static final String BUNDLE_BOOKS = "bundleBooks";
    private static final String BUNDLE_IS_TRADING = "bundleIsTrading";
    private static final String BUNDLE_IS_BUYING = "bundleIsBuying";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mListView = (ListView) findViewById(R.id.activity_search_list);
//        mSearchView = findViewById(R.)
        Intent intent = getIntent();
        if (savedInstanceState == null) {
            mIsBuyingBook = intent.getBooleanExtra(IntentExtra.ACTIVITY_BUY, false);
            mIsTradingBook = intent.getBooleanExtra(IntentExtra.ACTIVITY_TRADE, false);
            if (mIsBuyingBook) {
                String hint = getString(R.string.search_for_book_buy);
                mSearchView.setQueryHint(hint);
            } else if(mIsTradingBook) {
                String hint = getString(R.string.search_trade_for);
                mSearchView.setQueryHint(hint);
            }
        } else {
            mBookData = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            mListView.setAdapter(new BookAdapter(mBookData));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), BookMarketplaceActivity.class);
        intent.putExtra(IntentExtra.BOOK, mBookData.get(position));
        intent.putExtra(IntentExtra.ACTIVITY_BUY, mIsBuyingBook);
        intent.putExtra(IntentExtra.ACTIVITY_TRADE, mIsTradingBook);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_BOOKS, mBookData);
        outState.putBoolean(BUNDLE_IS_BUYING, mIsBuyingBook);
        outState.putBoolean(BUNDLE_IS_TRADING, mIsTradingBook);
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
                textView.setText(mListData.get(position).getName());
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
