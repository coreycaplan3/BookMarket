package com.github.coreycaplan3.bookmarket.fragments.marketplace;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FormValidation;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;

/**
 * Created by Corey on 3/26/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class TradeFormFragment extends Fragment implements View.OnClickListener, Dialog.OnCancelListener {

    private String mTitle;
    private String mAuthor;
    private String mIsbn;
    private Bitmap mImage;

    private UserProfile mUserProfile;

    private TextInputLayout mTitleTextInputLayout;
    private TextInputLayout mAuthorTextInputLayout;
    private TextInputLayout mIsbnTextInputLayout;

    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private EditText mIsbnEditText;
    private ImageView mBookImageView;
    private TextView mAddImageTextView;
    private CardView mImageContainer;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_IMAGE = "bundleImage";
    private static final String BUNDLE_TITLE = "bundleTitle";
    private static final String BUNDLE_AUTHOR = "bundleAuthor";
    private static final String BUNDLE_ISBN = "bundleIsbn";

    private static final int MINIMUM_LENGTH = 3;

    public static TradeFormFragment newInstance(UserProfile userProfile) {
        TradeFormFragment fragment = new TradeFormFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static TradeFormFragment newInstance(UserProfile userProfile, String title,
                                                String author, String isbn, Bitmap image) {
        TradeFormFragment fragment = new TradeFormFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putString(BUNDLE_TITLE, title);
        arguments.putString(BUNDLE_AUTHOR, author);
        arguments.putString(BUNDLE_ISBN, isbn);
        arguments.putParcelable(BUNDLE_IMAGE, image);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTitle = getArguments().getString(BUNDLE_TITLE);
            mAuthor = getArguments().getString(BUNDLE_AUTHOR);
            mIsbn = getArguments().getString(BUNDLE_ISBN);
            mIsbn = getArguments().getString(BUNDLE_ISBN);
            mImage = getArguments().getParcelable(BUNDLE_IMAGE);
        } else if (getArguments() != null) {
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mTitle = getArguments().getString(BUNDLE_TITLE);
            mAuthor = getArguments().getString(BUNDLE_AUTHOR);
            mIsbn = getArguments().getString(BUNDLE_ISBN);
            mImage = getArguments().getParcelable(BUNDLE_IMAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade_form, container, false);

        mTitleTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_trade_form_title_text_layout);
        mAuthorTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_trade_form_author_text_layout);
        mIsbnTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_trade_form_isbn_text_layout);


        mTitleEditText = (EditText) view.findViewById(R.id.fragment_trade_form_title_edit_text);
        mAuthorEditText = (EditText) view.findViewById(R.id.fragment_trade_form_author_edit_text);
        mIsbnEditText = (EditText) view.findViewById(R.id.fragment_trade_form_isbn_edit_text);
        mAddImageTextView = (TextView) view
                .findViewById(R.id.fragment_trade_form_add_picture_text_view);
        mBookImageView = (ImageView) view.findViewById(R.id.fragment_trade_form_image_view);
        mImageContainer = (CardView) view.findViewById(R.id.fragment_trade_form_card_container);

        view.findViewById(R.id.fragment_trade_form_submit_button).setOnClickListener(this);
        setListeners();

        mTitleEditText.setText(mTitle == null ? "" : mTitle);
        mAuthorEditText.setText(mAuthor == null ? "" : mAuthor);
        mIsbnEditText.setText(mIsbn == null ? "" : mIsbn);
        if (mImage != null) {
            mBookImageView.setVisibility(View.VISIBLE);
            mBookImageView.setImageBitmap(mImage);
            mAddImageTextView.setVisibility(View.GONE);
        } else {
            mBookImageView.setVisibility(View.GONE);
            mAddImageTextView.setVisibility(View.VISIBLE);
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
        if (id == R.id.fragment_trade_form_submit_button) {
            if (isValid()) {
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage(getContext().getString(R.string.posting_book));
                dialog.setIndeterminate(true);
                dialog.setOnCancelListener(this);
                PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                        .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
                TextBook textBook = new TextBook(mTitle, mAuthor, mIsbn, mImage);
                fragment.startPostTradeBookTask(textBook, mUserProfile);
                dialog.show();
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        fragment.cancelTask(PostNetworkConstants.CONSTRAINT_POST_TRADE_BOOK);
    }

    private boolean isValid() {
        mTitleTextInputLayout.setError(null);
        mAuthorTextInputLayout.setError(null);
        mIsbnTextInputLayout.setError(null);
        int white = getContext().getResources().getColor(android.R.color.white);
        mImageContainer.setCardBackgroundColor(white);

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

        if (mImage == null) {
            int color = getContext().getResources().getColor(android.R.color.holo_red_light);
            mImageContainer.setCardBackgroundColor(color);
        }
        return isValid;
    }

    public void onBarcodeRetrieved(TextBook textBook) {
        mTitleEditText.setText(textBook.getTitle());
        mAuthorEditText.setText(textBook.getAuthor());
        mIsbnEditText.setText(textBook.getIsbn());
    }

    public void onImageTaken(Bitmap image) {
        mImage = image;
        int white = getContext().getResources().getColor(android.R.color.white);
        mImageContainer.setCardBackgroundColor(white);
        mAddImageTextView.setVisibility(View.GONE);
        mBookImageView.setImageBitmap(image);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putString(BUNDLE_TITLE, mTitle);
        outState.putString(BUNDLE_AUTHOR, mAuthor);
        outState.putString(BUNDLE_ISBN, mIsbn);
    }

}
