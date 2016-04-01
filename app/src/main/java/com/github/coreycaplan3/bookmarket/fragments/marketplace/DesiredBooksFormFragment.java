package com.github.coreycaplan3.bookmarket.fragments.marketplace;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FormValidation;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.UiUtility;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class DesiredBooksFormFragment extends Fragment implements View.OnClickListener, Dialog.OnCancelListener {

    private boolean mIsProgressShowing = false;
    private boolean mIsEditingBook = false;
    private String mTitle;
    private String mAuthor;
    private String mIsbn;
    private String mTradeId;

    private UserProfile mUserProfile;

    private TextInputLayout mTitleTextInputLayout;
    private TextInputLayout mAuthorTextInputLayout;
    private TextInputLayout mIsbnTextInputLayout;

    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private EditText mIsbnEditText;

    private ProgressDialog mProgressDialog;

    private static final String BUNDLE_IS_PROGRESS_SHOWING = "bundleProgressShowing";
    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_TITLE = "bundleTitle";
    private static final String BUNDLE_AUTHOR = "bundleAuthor";
    private static final String BUNDLE_ISBN = "bundleIsbn";
    private static final String BUNDLE_TRADE_ID = "bundleTradeId";
    private static final String BUNDLE_IS_EDITING_BOOK = "bundleBook";

    private static final int MINIMUM_LENGTH = 3;

    public static DesiredBooksFormFragment newInstance(UserProfile userProfile) {
        DesiredBooksFormFragment fragment = new DesiredBooksFormFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static DesiredBooksFormFragment newInstance(UserProfile userProfile, String title,
                                                       String author, String isbn, String tradeId) {
        DesiredBooksFormFragment fragment = new DesiredBooksFormFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putString(BUNDLE_TITLE, title);
        arguments.putString(BUNDLE_AUTHOR, author);
        arguments.putString(BUNDLE_ISBN, isbn);
        arguments.putString(BUNDLE_TRADE_ID, tradeId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_IS_PROGRESS_SHOWING);
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTitle = savedInstanceState.getString(BUNDLE_TITLE);
            mAuthor = savedInstanceState.getString(BUNDLE_AUTHOR);
            mIsbn = savedInstanceState.getString(BUNDLE_ISBN);
            mTradeId = savedInstanceState.getString(BUNDLE_TRADE_ID);
            mIsEditingBook = savedInstanceState.getBoolean(BUNDLE_IS_EDITING_BOOK);
        } else if (getArguments() != null) {
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mTitle = getArguments().getString(BUNDLE_TITLE);
            mAuthor = getArguments().getString(BUNDLE_AUTHOR);
            mIsbn = getArguments().getString(BUNDLE_ISBN);
            mTradeId = getArguments().getString(BUNDLE_TRADE_ID);
            mIsEditingBook = mIsbn != null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desired_form, container, false);

        mTitleTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_desired_form_title_text_layout);
        mAuthorTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_desired_form_author_text_layout);
        mIsbnTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_desired_form_isbn_text_layout);


        mTitleEditText = (EditText) view.findViewById(R.id.fragment_desired_form_title_edit_text);
        mAuthorEditText = (EditText) view.findViewById(R.id.fragment_desired_form_author_edit_text);
        mIsbnEditText = (EditText) view.findViewById(R.id.fragment_desired_form_isbn_edit_text);

        view.findViewById(R.id.fragment_desired_form_submit_button).setOnClickListener(this);
        setListeners();

        mTitleEditText.setText(mTitle == null ? "" : mTitle);
        mAuthorEditText.setText(mAuthor == null ? "" : mAuthor);
        mIsbnEditText.setText(mIsbn == null ? "" : mIsbn);

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setOnCancelListener(this);
        if (mIsEditingBook) {
            mProgressDialog.setMessage(getString(R.string.editing_my_trade));
        } else {
            mProgressDialog.setMessage(getString(R.string.adding_to_my_trades));
        }
        if (mIsProgressShowing) {
            mProgressDialog.show();
        }

        return view;
    }

    private void setListeners() {
        mTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTitle = s.toString();
            }
        });
        mAuthorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAuthor = s.toString();
            }
        });
        mIsbnEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIsbn = s.toString();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fragment_desired_form_submit_button) {
            if (isValid()) {
                PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                        .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
                if (mIsEditingBook) {
                    TextBook textBook = new TextBook(mTitle, mAuthor, mIsbn);
                    fragment.startEditDesiredTradeTask(textBook, mUserProfile);
                } else {
                    TextBook textBook = new TextBook(mTitle, mAuthor, mIsbn);
                    fragment.startPostDesiredTradeTask(textBook, mUserProfile);
                }

                mIsProgressShowing = true;
                mProgressDialog.show();
            }
        }
    }

    private boolean isValid() {
        mTitleTextInputLayout.setError(null);
        mAuthorTextInputLayout.setError(null);
        mIsbnTextInputLayout.setError(null);

        boolean isValid = true;
        if (FormValidation.isEmpty(mTitle)) {
            mTitleTextInputLayout.setError(getString(R.string.error_required));
            isValid = false;
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mTitle)) {
            mTitleTextInputLayout.setError(getString(R.string.error_too_short));
            isValid = false;
        }
        if (FormValidation.isEmpty(mAuthor)) {
            mAuthorTextInputLayout.setError(getString(R.string.error_required));
            isValid = false;
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mAuthor)) {
            mAuthorTextInputLayout.setError(getString(R.string.error_required));
            isValid = false;
        }

        if (FormValidation.isEmpty(mIsbn)) {
            mIsbnTextInputLayout.setError(getString(R.string.error_required));
            isValid = false;
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mIsbn)) {
            mIsbnTextInputLayout.setError(getString(R.string.error_required));
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mIsProgressShowing = false;
        PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        fragment.cancelTask(PostNetworkConstants.CONSTRAINT_POST_TRADE_BOOK);
    }

    public void onPostBookSuccessful() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        UiUtility.toast(getContext(), R.string.post_successful);
    }

    public void onEditBookSuccessful() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        UiUtility.toast(getContext(), R.string.edit_successful);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BUNDLE_IS_PROGRESS_SHOWING, mIsProgressShowing);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putString(BUNDLE_TITLE, mTitle);
        outState.putString(BUNDLE_AUTHOR, mAuthor);
        outState.putString(BUNDLE_ISBN, mIsbn);
        outState.putBoolean(BUNDLE_IS_EDITING_BOOK, mIsEditingBook);
    }

}
