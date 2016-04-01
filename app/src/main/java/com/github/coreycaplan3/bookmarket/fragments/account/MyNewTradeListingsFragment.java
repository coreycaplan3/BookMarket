package com.github.coreycaplan3.bookmarket.fragments.account;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.MyListingsAdapter;
import com.github.coreycaplan3.bookmarket.adapters.MyTextBookClickListener;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;

import java.util.ArrayList;

/**
 * Created by Corey on 4/1/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class MyNewTradeListingsFragment extends Fragment implements OnRefreshListener {

    private static final String TAG = "NewTradeListings";
    private boolean mIsFragmentInitial;
    private UserProfile mUserProfile;
    private ArrayList<TextBook> mTextBooks;

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mRefreshLayout;
    private PercentRelativeLayout mNothingContainer;

    private MyTextBookClickListener mClickListener;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_BOOKS = "bundleBooks";
    private static final String BUNDLE_IS_FRAGMENT_INITIAL = "bundleIsFragmentInitial";

    public static MyNewTradeListingsFragment newInstance(UserProfile userProfile,
                                                        ArrayList<TextBook> textBooks) {
        MyNewTradeListingsFragment fragment = new MyNewTradeListingsFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putParcelableArrayList(BUNDLE_BOOKS, textBooks);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickListener = (MyTextBookClickListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTextBooks = savedInstanceState.getParcelable(BUNDLE_BOOKS);
            mIsFragmentInitial = savedInstanceState.getBoolean(BUNDLE_IS_FRAGMENT_INITIAL);
        } else {
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mTextBooks = getArguments().getParcelableArrayList(BUNDLE_BOOKS);
            mIsFragmentInitial = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_new_listings, container, false);
        mRefreshLayout = (SwipeRefreshLayout) view
                .findViewById(R.id.fragment_my_new_listings_refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
        mNothingContainer = (PercentRelativeLayout) view
                .findViewById(R.id.fragment_my_new_listings_nothing_container);
        mProgressBar = (ProgressBar) view
                .findViewById(R.id.fragment_my_new_listings_progress);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_my_new_listings_recycler);
        TextView nothingTextView = (TextView) view
                .findViewById(R.id.fragment_my_new_listings_nothing_text_view);
        nothingTextView.setText(R.string.nothing_new_trade_listings);

        if (mTextBooks == null && mIsFragmentInitial) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mNothingContainer.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            if (mTextBooks == null || mTextBooks.size() == 0) {
                mNothingContainer.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mNothingContainer.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.setAdapter(new MyListingsAdapter(mTextBooks, mClickListener, true,
                        true));
            }
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    public void onTextBooksRefreshed(ArrayList<TextBook> textBooks) {
        mIsFragmentInitial = false;
        if (textBooks == null || textBooks.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.GONE);
            mNothingContainer.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mNothingContainer.setVisibility(View.GONE);
            mRecyclerView.setAdapter(new MyListingsAdapter(textBooks, mClickListener, true, true));
        }
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        GetNetworkFragment fragment = (GetNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.GET_NETWORK_FRAGMENT);
        fragment.startGetNewTradeListingsTask(mUserProfile);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelableArrayList(BUNDLE_BOOKS, mTextBooks);
        outState.putBoolean(BUNDLE_IS_FRAGMENT_INITIAL, mIsFragmentInitial);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClickListener = null;
    }
}
