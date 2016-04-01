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

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class TradingFormFragment extends Fragment implements View.OnClickListener,
        Dialog.OnCancelListener, Spinner.OnItemSelectedListener {

    private static final String TAG = TradingFormFragment.class.getSimpleName();
    private String mPrice;
    private String mTitle;
    private String mAuthor;
    private String mIsbn;
    private Bitmap mImage;
    private int mSpinnerPosition;

    private String[] mConditionData;

    private UserProfile mUserProfile;

    private TextInputLayout mPriceTextInputLayout;
    private TextInputLayout mTitleTextInputLayout;
    private TextInputLayout mAuthorTextInputLayout;
    private TextInputLayout mIsbnTextInputLayout;

    private EditText mTitleEditText;
    private EditText mAuthorEditText;
    private EditText mIsbnEditText;
    private EditText mPriceEditText;
    private ImageView mBookImageView;
    private TextView mAddImageTextView;
    private CardView mImageContainer;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_IMAGE = "bundleImage";
    private static final String BUNDLE_TITLE = "bundleTitle";
    private static final String BUNDLE_AUTHOR = "bundleAuthor";
    private static final String BUNDLE_ISBN = "bundleIsbn";
    private static final String BUNDLE_PRICE = "bundlePrice";
    private static final String BUNDLE_CONDITION = "bundleCondition";
    private static final String BUNDLE_SPINNER_POSITION = "bundlePosition";

    private static final int MINIMUM_LENGTH = 3;

    public static TradingFormFragment newInstance(UserProfile userProfile) {
        TradingFormFragment fragment = new TradingFormFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static DesiredBooksForm newInstance(UserProfile userProfile, String title,
                                               String author, String isbn, Bitmap image,
                                               double price, String condition) {
        DesiredBooksForm fragment = new DesiredBooksForm();
        Bundle arguments = new Bundle();
        arguments.putParcelable(BUNDLE_PROFILE, userProfile);
        arguments.putString(BUNDLE_TITLE, title);
        arguments.putString(BUNDLE_AUTHOR, author);
        arguments.putString(BUNDLE_ISBN, isbn);
        arguments.putParcelable(BUNDLE_IMAGE, image);
        arguments.putString(BUNDLE_PRICE, price + "");
        arguments.putString(BUNDLE_CONDITION, condition);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mTitle = savedInstanceState.getString(BUNDLE_TITLE);
            mAuthor = savedInstanceState.getString(BUNDLE_AUTHOR);
            mIsbn = savedInstanceState.getString(BUNDLE_ISBN);
            mIsbn = savedInstanceState.getString(BUNDLE_ISBN);
            mImage = savedInstanceState.getParcelable(BUNDLE_IMAGE);
            mPrice = savedInstanceState.getString(BUNDLE_PRICE);
            mSpinnerPosition = savedInstanceState.getInt(BUNDLE_SPINNER_POSITION);
        } else if (getArguments() != null) {
            mUserProfile = getArguments().getParcelable(BUNDLE_PROFILE);
            mTitle = getArguments().getString(BUNDLE_TITLE);
            mAuthor = getArguments().getString(BUNDLE_AUTHOR);
            mIsbn = getArguments().getString(BUNDLE_ISBN);
            mImage = getArguments().getParcelable(BUNDLE_IMAGE);
            mPrice = getArguments().getString(BUNDLE_PRICE);

            String condition = getArguments().getString(BUNDLE_CONDITION);
            String conditionNew = getResources().getString(R.string.string_new);
            String conditionLikeNew = getResources().getString(R.string.string_new);
            String conditionGood = getResources().getString(R.string.good);
            String conditionBad = getResources().getString(R.string.bad);
            if (condition != null) {
                if (condition.equals(conditionNew)) {
                    mSpinnerPosition = 0;
                } else if (condition.equals(conditionLikeNew)) {
                    mSpinnerPosition = 1;
                } else if (condition.equals(conditionGood)) {
                    mSpinnerPosition = 2;
                } else if (condition.equals(conditionBad)) {
                    mSpinnerPosition = 3;
                } else {
                    Log.e(TAG, "onCreate: ", new IllegalArgumentException("Invalid Condition!"));
                }
            } else {
                Log.e(TAG, "onCreate: ", new NullPointerException("Condition was null!"));
            }

            mSpinnerPosition = getArguments().getInt(BUNDLE_SPINNER_POSITION, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desired_form, container, false);

        mTitleTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.default_book_form_title_text_layout);
        mAuthorTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.default_book_form_author_text_layout);
        mIsbnTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.default_book_form_isbn_text_layout);
        Spinner conditionSpinner = (Spinner) view
                .findViewById(R.id.default_book_form_condition_spinner);

        mConditionData = getContext().getResources().getStringArray(R.array.book_conditions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, mConditionData);
        conditionSpinner.setAdapter(adapter);

        mPriceTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.default_book_form_price_text_layout);

        mTitleEditText = (EditText) view.findViewById(R.id.default_book_form_title_edit_text);
        mAuthorEditText = (EditText) view.findViewById(R.id.default_book_form_author_edit_text);
        mIsbnEditText = (EditText) view.findViewById(R.id.default_book_form_isbn_edit_text);
        mAddImageTextView = (TextView) view
                .findViewById(R.id.default_book_form_add_picture_text_view);
        mBookImageView = (ImageView) view.findViewById(R.id.default_book_form_image_view);
        mImageContainer = (CardView) view.findViewById(R.id.default_book_form_image_container);

        Button button = (Button) view.findViewById(R.id.default_book_form_submit_button);
        button.setText(R.string.trade);
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
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage(getContext().getString(R.string.posting_book));
                dialog.setIndeterminate(true);
                dialog.setOnCancelListener(this);
                PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                        .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
                @TextBook.Condition int condition = getCondition();
                TextBook textBook = new TextBook(mTitle, mAuthor, mIsbn, Double.parseDouble(mPrice),
                        condition, mImage);
                fragment.startPostSellBookTask(textBook, mUserProfile);
                dialog.show();
            }
        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("Invalid Id!"));
        }
    }

    private boolean isValid() {
        mTitleTextInputLayout.setError(null);
        mAuthorTextInputLayout.setError(null);
        mIsbnTextInputLayout.setError(null);
        int white = getContext().getResources().getColor(android.R.color.white);
        mImageContainer.setCardBackgroundColor(white);
        mPriceTextInputLayout.setError(null);

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

        if (FormValidation.isEmpty(mPrice)) {
            mPriceTextInputLayout.setError(getString(R.string.error_required));
            isValid = false;
        }

        if (mImage == null) {
            int color = getContext().getResources().getColor(android.R.color.holo_red_light);
            mImageContainer.setCardBackgroundColor(color);
        }
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
        PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        fragment.cancelTask(PostNetworkConstants.CONSTRAINT_POST_SELL_BOOK);
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
        outState.putString(BUNDLE_PRICE, mPrice);
        outState.putInt(BUNDLE_SPINNER_POSITION, mSpinnerPosition);
    }

}
