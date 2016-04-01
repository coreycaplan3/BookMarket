package com.github.coreycaplan3.bookmarket.fragments.marketplace;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.activities.FormActivity;
import com.github.coreycaplan3.bookmarket.activities.MyBookDetailsActivity;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.GeneralUser;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;
import com.github.coreycaplan3.bookmarket.utilities.UiUtility;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class MyBookDetailsFragment extends Fragment implements View.OnClickListener {

    private boolean mIsProgressShowing = false;
    private boolean mIsSelling;
    private boolean mIsNewTextBook;
    private TextBook mTextBook;
    private UserProfile mUserProfile;

    private ImageView mBookImageView;
    private TextView mPriceTextView;
    private TextView mConditionTextView;
    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private TextView mIsbnTextView;
    private TextView mUniversityTextView;

    private static final String BUNDLE_BOOK = "bundleTextBook";
    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_IS_SELLING = "bundleIsSelling";
    private static final String BUNDLE_IS_PROGRESS_SHOWING = "bundleIsProgressShowing";
    private static final String BUNDLE_IS_NEW_TEXT_BOOK = "bundleIsNewTextBook";

    public static MyBookDetailsFragment newInstance(TextBook textBook, UserProfile userProfile,
                                                    boolean isSelling, boolean isNewTextBook) {
        MyBookDetailsFragment fragment = new MyBookDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_BOOK, textBook);
        arguments.putBoolean(BUNDLE_IS_SELLING, isSelling);
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putBoolean(BUNDLE_IS_NEW_TEXT_BOOK, isNewTextBook);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTextBook = savedInstanceState.getParcelable(BUNDLE_BOOK);
            mIsSelling = savedInstanceState.getBoolean(BUNDLE_IS_SELLING);
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_IS_PROGRESS_SHOWING);
            mIsNewTextBook = savedInstanceState.getBoolean(BUNDLE_IS_NEW_TEXT_BOOK);
        } else {
            mTextBook = getArguments().getParcelable(BUNDLE_BOOK);
            mIsSelling = getArguments().getBoolean(BUNDLE_IS_SELLING);
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mIsNewTextBook = getArguments().getBoolean(BUNDLE_IS_NEW_TEXT_BOOK);
        }
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_book_details, container, false);
        mBookImageView = (ImageView) view
                .findViewById(R.id.fragment_my_book_details_book_image_view);
        mPriceTextView = (TextView) view.findViewById(R.id.fragment_book_details_price_text_view);
        mConditionTextView = (TextView) view
                .findViewById(R.id.fragment_my_book_details_condition_text_view);
        mTitleTextView = (TextView) view
                .findViewById(R.id.fragment_my_book_details_title_text_view);
        mAuthorTextView = (TextView) view
                .findViewById(R.id.fragment_my_book_details_author_text_view);
        mIsbnTextView = (TextView) view
                .findViewById(R.id.fragment_my_book_details_isbn_text_view);
        mUniversityTextView = (TextView) view
                .findViewById(R.id.fragment_my_book_details_university_text_view);
        if (mIsNewTextBook) {
            view.findViewById(R.id.fragment_my_book_details_edit_button).setOnClickListener(this);
        } else {
            view.findViewById(R.id.fragment_my_book_details_edit_button).setVisibility(View.GONE);
        }

        mBookImageView.setImageBitmap(mTextBook.getPicture());
        if (mIsSelling) {
            mPriceTextView.setVisibility(View.VISIBLE);
            mPriceTextView.setText("");
        } else {
            mPriceTextView.setVisibility(View.GONE);
        }
        mConditionTextView.setText(TextBook.getCondition(mTextBook.getCondition(), getContext()));
        mTitleTextView.setText(mTextBook.getTitle());
        mAuthorTextView.setText(mTextBook.getAuthor());
        mIsbnTextView.setText(mTextBook.getIsbn());
        mUniversityTextView.setText(mUserProfile.getUniversity());

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_my_book_details_edit_button) {
            Intent intent;
            if (mIsSelling) {
                intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra(IntentExtra.ACTIVITY_SELLING, true);
                intent.putExtra(IntentExtra.PROFILE, mUserProfile);
                intent.putExtra(IntentExtra.BOOK, mTextBook);
                startActivityForResult(intent, MyBookDetailsActivity.REQUEST_CODE_EDIT_EVENT);
            } else {
                intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra(IntentExtra.PROFILE, mUserProfile);
                intent.putExtra(IntentExtra.BOOK, mTextBook);
                startActivityForResult(intent, MyBookDetailsActivity.REQUEST_CODE_EDIT_EVENT);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void onBookEdited(TextBook newTextBook) {
        mTextBook = newTextBook;
        mBookImageView.setImageBitmap(mTextBook.getPicture());
        mPriceTextView.setText("$" + mTextBook.getPrice());
        mConditionTextView.setText(TextBook.getCondition(mTextBook.getCondition(), getContext()));
        mTitleTextView.setText(mTextBook.getTitle());
        mAuthorTextView.setText(mTextBook.getAuthor());
        mIsbnTextView.setText(mTextBook.getIsbn());
        mUniversityTextView.setText(mUserProfile.getUniversity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_BOOK, mTextBook);
        outState.putBoolean(BUNDLE_IS_SELLING, mIsSelling);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putBoolean(BUNDLE_IS_PROGRESS_SHOWING, mIsProgressShowing);
        outState.putBoolean(BUNDLE_IS_NEW_TEXT_BOOK, mIsNewTextBook);
    }

}
