package com.github.coreycaplan3.bookmarket.fragments.marketplace;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.GeneralUser;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.UiUtility;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class BookDetailsFragment extends Fragment implements View.OnClickListener,
        Dialog.OnCancelListener {

    private boolean mIsProgressShowing = false;
    private boolean mIsSelling;
    private TextBook mTextBook;
    private UserProfile mUserProfile;
    private GeneralUser mSeller;

    private ProgressDialog mProgressDialog;

    private static final String BUNDLE_BOOK = "bundleTextBook";
    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_SELLER = "bundleSeller";
    private static final String BUNDLE_IS_SELLING = "bundleIsSelling";
    private static final String BUNDLE_IS_PROGRESS_SHOWING = "bundleIsProgressShowing";

    public static BookDetailsFragment newInstance(TextBook textBook, UserProfile userProfile,
                                                  GeneralUser seller, boolean isSelling) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_BOOK, textBook);
        arguments.putBoolean(BUNDLE_IS_SELLING, isSelling);
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putParcelable(BUNDLE_SELLER, seller);
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
            mSeller = savedInstanceState.getParcelable(BUNDLE_SELLER);
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_IS_PROGRESS_SHOWING);
        } else {
            mTextBook = getArguments().getParcelable(BUNDLE_BOOK);
            mIsSelling = getArguments().getBoolean(BUNDLE_IS_SELLING);
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mSeller = getArguments().getParcelable(BUNDLE_SELLER);
        }
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_details, container, false);
        ImageView bookImageView = (ImageView) view
                .findViewById(R.id.fragment_book_details_book_image_view);
        TextView priceTextView = (TextView) view.findViewById(R.id.fragment_book_details_price_text_view);
        TextView conditionTextView = (TextView) view
                .findViewById(R.id.fragment_book_details_condition_text_view);
        TextView titleTextView = (TextView) view
                .findViewById(R.id.fragment_book_details_title_text_view);
        TextView authorTextView = (TextView) view
                .findViewById(R.id.fragment_book_details_author_text_view);
        TextView isbnTextView = (TextView) view
                .findViewById(R.id.fragment_book_details_isbn_text_view);
        TextView universityTextView = (TextView) view
                .findViewById(R.id.fragment_book_details_university_text_view);
        Button button = (Button) view.findViewById(R.id.fragment_book_details_submit_button);
        button.setOnClickListener(this);

        bookImageView.setImageBitmap(mTextBook.getPicture());
        if (mIsSelling) {
            priceTextView.setVisibility(View.VISIBLE);
            priceTextView.setText("");
            button.setText(R.string.sell);
        } else {
            priceTextView.setVisibility(View.GONE);
            button.setText(R.string.trade);
        }
        conditionTextView.setText(getCondition());
        titleTextView.setText(mTextBook.getTitle());
        authorTextView.setText(mTextBook.getAuthor());
        isbnTextView.setText(mTextBook.getIsbn());
        universityTextView.setText(mSeller.getUniversity());

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setOnCancelListener(this);
        if (mIsSelling) {
            mProgressDialog.setMessage(getString(R.string.notify_seller));
        } else {
            mProgressDialog.setMessage(getString(R.string.notify_trader));
        }
        if (mIsProgressShowing) {
            mProgressDialog.show();
        }
        return view;
    }

    private String getCondition() {
        String condition = getString(R.string.string_new);
        switch (mTextBook.getCondition()) {
            case TextBook.CONDITION_NEW:
                condition = getString(R.string.string_new);
                break;
            case TextBook.CONDITION_LIKE_NEW:
                condition = getString(R.string.like_new);
                break;
            case TextBook.CONDITION_GOOD:
                condition = getString(R.string.good);
                break;
            case TextBook.CONDITION_BAD:
                condition = getString(R.string.bad);
                break;
        }
        return condition;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_book_details_submit_button) {
            mIsProgressShowing = true;
            mProgressDialog.show();
            PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                    .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
            if (mIsSelling) {
                fragment.startTradeBookTask(mTextBook, mUserProfile, mSeller);
            } else {
                fragment.startBuyBookTask(mTextBook, mUserProfile, mSeller);
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mIsProgressShowing = false;
    }

    public void onNotificationComplete() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        if (mIsSelling) {
            UiUtility.snackbar(getView(), R.string.seller_notified, null);
        } else {
            UiUtility.snackbar(getView(), R.string.trader_notified, null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_BOOK, mTextBook);
        outState.putBoolean(BUNDLE_IS_SELLING, mIsSelling);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelable(BUNDLE_SELLER, mSeller);
        outState.putBoolean(BUNDLE_IS_PROGRESS_SHOWING, mIsProgressShowing);
    }

}
