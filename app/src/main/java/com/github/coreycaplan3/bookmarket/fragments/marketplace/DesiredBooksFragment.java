package com.github.coreycaplan3.bookmarket.fragments.marketplace;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.DesiredTradesAdapter;
import com.github.coreycaplan3.bookmarket.adapters.DesiredTradesAdapter.OnDesiredTradeClickListener;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.UiUtility;

import java.util.ArrayList;

/**
 * Created by Corey on 4/1/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class DesiredBooksFragment extends Fragment implements OnDesiredTradeClickListener,
        DialogInterface.OnCancelListener {

    private static final String TAG = DesiredBooksFragment.class.getSimpleName();

    private boolean mIsProgressShowing;
    private UserProfile mUserProfile;
    private ArrayList<TextBook> mTextBooks;
    private boolean mIsDialogShowing;

    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private PercentRelativeLayout mNothingContainer;

    private Adapter mAdapter;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_BOOKS = "bundleBooks";
    private static final String BUNDLE_IS_PROGRESS_SHOWING = "bundleIsProgressShowing";
    private static final String BUNDLE_IS_DIALOG_SHOWING = "bundleIsDialogShowing";

    public static DesiredBooksFragment newInstance(UserProfile userProfile) {
        DesiredBooksFragment fragment = new DesiredBooksFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_IS_PROGRESS_SHOWING);
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTextBooks = savedInstanceState.getParcelable(BUNDLE_BOOKS);
            mIsDialogShowing = savedInstanceState.getBoolean(BUNDLE_IS_DIALOG_SHOWING);
        } else {
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mTextBooks = getArguments().getParcelable(BUNDLE_BOOKS);
            mIsProgressShowing = mTextBooks == null;
            mIsDialogShowing = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desired_trades, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_desired_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mProgressBar = (ProgressBar) view.findViewById(R.id.fragment_desired_progress);
        mNothingContainer = (PercentRelativeLayout) view
                .findViewById(R.id.fragment_desired_trades_nothing_container);

        if (mTextBooks == null && mIsProgressShowing) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else if (mTextBooks == null || mTextBooks.size() == 0) {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            mNothingContainer.setVisibility(View.VISIBLE);
        } else if (mTextBooks.size() > 0) {
            mProgressBar.setVisibility(View.GONE);
            mNothingContainer.setVisibility(View.GONE);

            mAdapter = new DesiredTradesAdapter(mTextBooks, this, getContext());
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(mAdapter);
        }

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.setMessage(getString(R.string.deleting_desired_trade));
        if (mIsProgressShowing) {
            mProgressDialog.show();
        }

        return view;
    }

    public void onDesiredTextbooksLoaded(ArrayList<TextBook> desiredTextBooks) {
        mIsProgressShowing = false;
        mProgressBar.setVisibility(View.GONE);
        if (desiredTextBooks == null || desiredTextBooks.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            mNothingContainer.setVisibility(View.VISIBLE);
        } else {
            mNothingContainer.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new DesiredTradesAdapter(mTextBooks, this, getContext());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDesiredTradeClicked(TextBook textBook, boolean shouldDelete, boolean shouldEdit) {
        if (shouldDelete) {
            PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                    .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
            fragment.startDeleteDesiredTradeTask(textBook, mUserProfile);
        } else if (shouldEdit) {
            DesiredBooksFormFragment fragment = DesiredBooksFormFragment.newInstance(mUserProfile,
                    textBook.getTitle(), textBook.getAuthor(), textBook.getIsbn(),
                    textBook.getTradingId());
            FragmentCreator.create(fragment, FragmentKeys.ADD_DESIRED_TRADE_FRAGMENT,
                    R.id.profile_container, getFragmentManager());
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void onDeleteSuccessful(TextBook textBook) {
        for (int i = 0; i < mTextBooks.size(); i++) {
            if (textBook.getTradingId().equals(mTextBooks.get(i).getTradingId())) {
                mTextBooks.remove(i);
                mAdapter = new DesiredTradesAdapter(mTextBooks, this, getContext());
                mRecyclerView.swapAdapter(mAdapter, true);
                mProgressDialog.dismiss();
                mIsDialogShowing = false;
                break;
            }
        }
    }

    public void onDeleteFailure() {
        mProgressDialog.dismiss();
        mIsDialogShowing = false;
        UiUtility.toast(getContext(), R.string.error_deleting_book);
    }

    @SuppressWarnings("ConstantConditions")
    public void onEditSuccessful(TextBook textBook) {
        for (int i = 0; i < mTextBooks.size(); i++) {
            if (textBook.getTradingId().equals(mTextBooks.get(i).getTradingId())) {
                mTextBooks.remove(i);
                mTextBooks.add(i, textBook);
                mAdapter = new DesiredTradesAdapter(mTextBooks, this, getContext());
                mRecyclerView.swapAdapter(mAdapter, true);
                break;
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mIsDialogShowing = false;
        PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        if (fragment != null) {
            fragment.cancelTask(PostNetworkConstants.CONSTRAINT_DELETE_DESIRED_TRADE_BOOK);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_IS_PROGRESS_SHOWING, mIsProgressShowing);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelableArrayList(BUNDLE_BOOKS, mTextBooks);
        outState.putBoolean(BUNDLE_IS_DIALOG_SHOWING, mIsDialogShowing);
    }

}
