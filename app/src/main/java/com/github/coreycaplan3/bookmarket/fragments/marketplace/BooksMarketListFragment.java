package com.github.coreycaplan3.bookmarket.fragments.marketplace;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.BookMarketListAdapter;
import com.github.coreycaplan3.bookmarket.adapters.BookMarketListAdapter.TextBookClickListener;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;

import java.util.ArrayList;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class BooksMarketListFragment extends Fragment implements OnRefreshListener {

    private static final String TAG = BooksMarketListFragment.class.getSimpleName();
    private boolean mIsBuyingBooks;
    private String mBookIsbn;
    private UserProfile mUserProfile;
    @Nullable
    private ArrayList<TextBook> mTextBookList;
    private TextBookClickListener mTextBookClickListener;

    private static final String BUNDLE_BUYING_BOOKS = "bundleBuyingBooks";
    private static final String BUNDLE_BOOKS_ISBN = "bundleBookIsbn";
    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_BOOKS = "bundleBooks";

    private SwipeRefreshLayout mRefreshLayout;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    public static BooksMarketListFragment newInstance(String bookIsbn,
                                                      @Nullable ArrayList<TextBook> textBookList,
                                                      UserProfile userProfile, boolean isBuyingBooks) {
        BooksMarketListFragment fragment = new BooksMarketListFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(BUNDLE_BOOKS, textBookList);
        arguments.putString(BUNDLE_BOOKS_ISBN, bookIsbn);
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putBoolean(BUNDLE_BUYING_BOOKS, isBuyingBooks);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mTextBookClickListener = (TextBookClickListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTextBookList = savedInstanceState.getParcelableArrayList(BUNDLE_BOOKS);
            mBookIsbn = savedInstanceState.getString(BUNDLE_BOOKS_ISBN);
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mIsBuyingBooks = savedInstanceState.getBoolean(BUNDLE_BUYING_BOOKS);
        } else if (getArguments() != null) {
            mBookIsbn = getArguments().getString(BUNDLE_BOOKS_ISBN);
            mTextBookList = getArguments().getParcelableArrayList(BUNDLE_BOOKS);
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mIsBuyingBooks = getArguments().getBoolean(BUNDLE_BUYING_BOOKS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_market_list, container, false);
        mRefreshLayout = (SwipeRefreshLayout) view
                .findViewById(R.id.fragment_book_market_place_refresh);
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_book_market_place_progress);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_book_market_place_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (mTextBookList == null) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            Adapter adapter = new BookMarketListAdapter(getContext(), mTextBookList,
                    mTextBookClickListener);
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onRefresh() {
        GetNetworkFragment fragment = (GetNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.GET_NETWORK_FRAGMENT);
        fragment.startGetBooksTask(mBookIsbn);
    }

    public void onBooksRefreshed(@Nullable ArrayList<TextBook> textBookList) {
        if (textBookList == null) {
            return;
        }
        mTextBookList = textBookList;
        mProgressBar.setVisibility(View.GONE);
        Adapter adapter = new BookMarketListAdapter(getContext(), mTextBookList,
                mTextBookClickListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_BOOKS_ISBN, mBookIsbn);
        outState.putParcelableArrayList(BUNDLE_BOOKS, mTextBookList);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putBoolean(BUNDLE_BUYING_BOOKS, mIsBuyingBooks);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTextBookClickListener = null;
    }

}
