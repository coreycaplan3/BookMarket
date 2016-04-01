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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
public class TradingFormFragment extends Fragment implements View.OnClickListener,
        Dialog.OnCancelListener, Spinner.OnItemSelectedListener {

    private static final String TAG = TradingFormFragment.class.getSimpleName();

    private boolean mIsProgressShowing;
    private boolean mIsEditingBook;
    private String mTitle;
    private String mAuthor;
    private String mIsbn;
    private Bitmap mImage;
    private String mTradeId;
    private int mSpinnerPosition;

    private String[] mConditionData;

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

    private ProgressDialog mProgressDialog;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_IMAGE = "bundleImage";
    private static final String BUNDLE_TITLE = "bundleTitle";
    private static final String BUNDLE_AUTHOR = "bundleAuthor";
    private static final String BUNDLE_ISBN = "bundleIsbn";
    private static final String BUNDLE_CONDITION = "bundleCondition";
    private static final String BUNDLE_SPINNER_POSITION = "bundlePosition";
    private static final String BUNDLE_TRADE_ID = "bundleTradeId";
    private static final String BUNDLE_IS_PROGRESS_SHOWING = "bundleIsProgressShowing";
    private static final String BUNDLE_IS_EDITING_BOOK = "bundleIsEditingBook";

    private static final int MINIMUM_LENGTH = 3;

    public static TradingFormFragment newInstance(UserProfile userProfile) {
        TradingFormFragment fragment = new TradingFormFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static DesiredBooksFormFragment newInstance(UserProfile userProfile, String title,
                                                       String author, String isbn, Bitmap image,
                                                       String condition, String tradeId) {
        DesiredBooksFormFragment fragment = new DesiredBooksFormFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putString(BUNDLE_TITLE, title);
        arguments.putString(BUNDLE_AUTHOR, author);
        arguments.putString(BUNDLE_ISBN, isbn);
        arguments.putParcelable(BUNDLE_IMAGE, image);
        arguments.putString(BUNDLE_CONDITION, condition);
        arguments.putString(BUNDLE_TRADE_ID, tradeId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConditionData = getContext().getResources().getStringArray(R.array.book_conditions);
        if (savedInstanceState != null) {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTitle = savedInstanceState.getString(BUNDLE_TITLE);
            mAuthor = savedInstanceState.getString(BUNDLE_AUTHOR);
            mIsbn = savedInstanceState.getString(BUNDLE_ISBN);
            mIsbn = savedInstanceState.getString(BUNDLE_ISBN);
            mImage = savedInstanceState.getParcelable(BUNDLE_IMAGE);
            mSpinnerPosition = savedInstanceState.getInt(BUNDLE_SPINNER_POSITION);
            mTradeId = savedInstanceState.getString(BUNDLE_TRADE_ID);
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_IS_PROGRESS_SHOWING);
            mIsEditingBook = savedInstanceState.getBoolean(BUNDLE_IS_EDITING_BOOK);
        } else if (getArguments() != null) {
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mTitle = getArguments().getString(BUNDLE_TITLE);
            mAuthor = getArguments().getString(BUNDLE_AUTHOR);
            mIsbn = getArguments().getString(BUNDLE_ISBN);
            mImage = getArguments().getParcelable(BUNDLE_IMAGE);
            mTradeId = getArguments().getString(BUNDLE_TRADE_ID);
            mIsProgressShowing = false;

            String condition = getArguments().getString(BUNDLE_CONDITION);
            if (condition != null) {
                mIsEditingBook = true; //easiest way to check, since condition isn't null
                mSpinnerPosition = -1;
                for (int i = 0; i < mConditionData.length; i++) {
                    if (condition.equals(mConditionData[i])) {
                        mSpinnerPosition = i;
                        break;
                    }
                }
                if (mSpinnerPosition == -1) {
                    Log.e(TAG, "onCreate: ", new IllegalStateException("SpinnerPosition should" +
                            "not be -1!"));
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_book_form, container, false);

        mTitleTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.default_book_form_title_text_layout);
        mAuthorTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.default_book_form_author_text_layout);
        mIsbnTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.default_book_form_isbn_text_layout);
        Spinner conditionSpinner = (Spinner) view
                .findViewById(R.id.default_book_form_condition_spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mConditionData);
        conditionSpinner.setAdapter(adapter);

        mTitleEditText = (EditText) view.findViewById(R.id.default_book_form_title_edit_text);
        mAuthorEditText = (EditText) view.findViewById(R.id.default_book_form_author_edit_text);
        mIsbnEditText = (EditText) view.findViewById(R.id.default_book_form_isbn_edit_text);
        mAddImageTextView = (TextView) view
                .findViewById(R.id.default_book_form_add_picture_text_view);
        mBookImageView = (ImageView) view.findViewById(R.id.default_book_form_image_view);
        mImageContainer = (CardView) view.findViewById(R.id.default_book_form_image_container);
        view.findViewById(R.id.default_book_form_price_edit_text).setVisibility(View.GONE);
        view.findViewById(R.id.default_book_form_price_layout).setVisibility(View.GONE);

        Button button = (Button) view.findViewById(R.id.default_book_form_submit_button);
        if (mIsEditingBook) {
            button.setText(R.string.update);
        } else {
            button.setText(R.string.trade);
        }
        button.setOnClickListener(this);
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

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setOnCancelListener(this);
        if (mIsEditingBook) {
            mProgressDialog.setMessage(getString(R.string.updating_listing));
        } else {
            mProgressDialog.setMessage(getString(R.string.posting_book));
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
        if (id == R.id.default_book_form_submit_button) {
            if (isValid()) {
                PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                        .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
                @TextBook.Condition int condition = getCondition();
                TextBook textBook;
                if (mIsEditingBook) {
                    textBook = new TextBook(mTitle, mAuthor, mIsbn, condition, mImage, mTradeId);
                    fragment.startEditTradeBookTask(textBook, mUserProfile);
                } else {
                    textBook = new TextBook(mTitle, mAuthor, mIsbn, condition, mImage);
                    fragment.startPostTradeBookTask(textBook, mUserProfile);
                }

                mIsProgressShowing = true;
                mProgressDialog.show();
            }
        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("Invalid Id!"));
        }
    }

    @SuppressWarnings("deprecation")
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

//        if (mImage == null) {
//            int color = getContext().getResources().getColor(android.R.color.holo_red_light);
//            mImageContainer.setCardBackgroundColor(color);
//        }
        return isValid;
    }

    @TextBook.Condition
    private int getCondition() {
        switch (mSpinnerPosition) {
            case 0:
                return TextBook.CONDITION_NEW;
            case 1:
                return TextBook.CONDITION_LIKE_NEW;
            case 2:
                return TextBook.CONDITION_GOOD;
            case 3:
                return TextBook.CONDITION_BAD;
            default:
                Log.e(TAG, "getCondition: ", new IllegalArgumentException("Invalid position"));
        }
        return TextBook.CONDITION_NEW;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mIsProgressShowing = false;
        PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        fragment.cancelTask(PostNetworkConstants.CONSTRAINT_POST_SELL_BOOK);
    }

    public void onBarcodeRetrieved(TextBook textBook) {
        mTitleEditText.setText(textBook.getTitle());
        mAuthorEditText.setText(textBook.getAuthor());
        mIsbnEditText.setText(textBook.getIsbn());
        onImageTaken(textBook.getPicture());
    }

    @SuppressWarnings("deprecation")
    public void onImageTaken(Bitmap image) {
        mImage = image;
        int white = getContext().getResources().getColor(android.R.color.white);
        mImageContainer.setCardBackgroundColor(white);
        mAddImageTextView.setVisibility(View.GONE);
        mBookImageView.setImageBitmap(image);
        mBookImageView.setVisibility(View.VISIBLE);
    }

    public void onPostSuccessful() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        UiUtility.toast(getContext(), R.string.post_successful);
    }

    public void onEditSuccessful() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        UiUtility.toast(getContext(), R.string.edit_successful);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mSpinnerPosition != position) {
            mSpinnerPosition = position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putString(BUNDLE_TITLE, mTitle);
        outState.putString(BUNDLE_AUTHOR, mAuthor);
        outState.putString(BUNDLE_ISBN, mIsbn);
        outState.putInt(BUNDLE_SPINNER_POSITION, mSpinnerPosition);
        outState.putString(BUNDLE_TRADE_ID, mTradeId);
        outState.putBoolean(BUNDLE_IS_PROGRESS_SHOWING, mIsProgressShowing);
        outState.putBoolean(BUNDLE_IS_EDITING_BOOK, mIsEditingBook);
    }

}
