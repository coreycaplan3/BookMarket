package com.github.coreycaplan3.bookmarket.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.application.BookApplication;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkFragment;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.SellingFormFragment;
import com.github.coreycaplan3.bookmarket.fragments.marketplace.TradingFormFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;
import com.github.coreycaplan3.bookmarket.utilities.Keys;
import com.github.coreycaplan3.bookmarket.utilities.UiUtility;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import static com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.*;
import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class FormActivity extends AppCompatActivity implements View.OnClickListener,
        PostNetworkCommunicator, GetNetworkCommunicator, ProgressDialog.OnCancelListener,
        ImageLoader.ImageListener {

    private static final String TAG = TitleActivity.class.getSimpleName();

    private boolean mIsSelling;
    private boolean mIsProgressShown;
    private UserProfile mUserProfile;
    @Nullable
    private TextBook mTextBookToEdit;
    private TextBook mTextBookBeingMade;

    private ProgressDialog mProgressDialog;

    private static final int RC_BARCODE_CAPTURE = 9001;

    private static final String BUNDLE_SELLING = TAG + "selling";
    private static final String BUNDLE_PROFILE = TAG + "profile";
    private static final String BUNDLE_BOOK = TAG + "book";
    private static final String BUNDLE_BOOK_MADE = TAG + "bookMade";
    private static final String BUNDLE_IS_PROGRESS_SHOWN = TAG + "isProgressShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)
                findViewById(R.id.form_floating_action_button);
        if (fab != null) {
            fab.setOnClickListener(this);
        }

        restoreInstance(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (mIsSelling) {
                if (mTextBookToEdit != null) {
                    getSupportActionBar().setTitle(R.string.edit_sell_listing);
                } else {
                    getSupportActionBar().setTitle(R.string.create_sell_listing);
                }
            } else {
                if (mTextBookToEdit != null) {
                    getSupportActionBar().setTitle(R.string.edit_trade_listing);
                } else {
                    getSupportActionBar().setTitle(R.string.create_trade_listing);
                }
            }
        }


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.retrieving_book_information));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setOnCancelListener(this);
        if (mIsProgressShown) {
            mProgressDialog.show();
        }
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());

            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mIsSelling = intent.getBooleanExtra(IntentExtra.ACTIVITY_SELLING, false);
            mTextBookToEdit = intent.getParcelableExtra(IntentExtra.BOOK);

            if (mIsSelling) {
                if (mTextBookToEdit == null) {
                    FragmentCreator.create(SellingFormFragment.newInstance(mUserProfile), SELL_FORM_FRAGMENT,
                            R.id.form_container, getSupportFragmentManager());
                } else {
                    String condition = TextBook.getCondition(mTextBookToEdit.getCondition(), this);
                    FragmentCreator.create(SellingFormFragment.newInstance(mUserProfile,
                            mTextBookToEdit.getTitle(), mTextBookToEdit.getAuthor(),
                            mTextBookToEdit.getIsbn(), mTextBookToEdit.getPicture(),
                            mTextBookToEdit.getPrice(), condition, mTextBookToEdit.getSellingId()),
                            SELL_FORM_FRAGMENT, R.id.form_container, getSupportFragmentManager());
                }
            } else {
                if (mTextBookToEdit == null) {
                    FragmentCreator.create(TradingFormFragment.newInstance(mUserProfile),
                            TRADE_FORM_FRAGMENT, R.id.form_container, getSupportFragmentManager());
                } else {
                    String condition = TextBook.getCondition(mTextBookToEdit.getCondition(), this);
                    FragmentCreator.create(TradingFormFragment.newInstance(mUserProfile,
                            mTextBookToEdit.getTitle(), mTextBookToEdit.getAuthor(),
                            mTextBookToEdit.getIsbn(), mTextBookToEdit.getPicture(),
                            condition, mTextBookToEdit.getTradingId()), TRADE_FORM_FRAGMENT,
                            R.id.form_container, getSupportFragmentManager());
                }
            }
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mIsSelling = savedInstanceState.getBoolean(BUNDLE_SELLING);
            mTextBookToEdit = savedInstanceState.getParcelable(BUNDLE_BOOK);
            mIsProgressShown = savedInstanceState.getBoolean(BUNDLE_IS_PROGRESS_SHOWN);
            mTextBookBeingMade = savedInstanceState.getParcelable(BUNDLE_BOOK_MADE);
        }
    }


    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(CameraActivity.BarcodeObject);
                    startBarcodeTask(barcode.rawValue);
                } else {
                    UiUtility.snackbar(findViewById(R.id.form_container), R.string.barcode_failure,
                            null);
                }
            }
        }
    }

    private void startBarcodeTask(String barcodeRawValue) {
        GetNetworkFragment fragment = (GetNetworkFragment) getSupportFragmentManager()
                .findFragmentByTag(FragmentKeys.GET_NETWORK_FRAGMENT);
        fragment.getTextbookFromIsbn(barcodeRawValue);

        mIsProgressShown = true;
        mProgressDialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.form_floating_action_button) {
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("Invalid!"));
        }
    }

    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {
        if (getConstraints.equals(GET_CONSTRAINT_BARCODE_AUTOCOMPLETE)) {
            TextBook textBook = result.getParcelable(getConstraints);
            if (textBook != null) {
                mTextBookBeingMade = textBook;
                BookApplication.getInstance().getImageLoader().get(textBook.getImageUrl(), this);
            } else {
                mIsProgressShown = false;
                mProgressDialog.dismiss();
                Log.e(TAG, "onGetNetworkTaskComplete: ", new NullPointerException(""));
            }
        }
    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {
        switch (postConstraints) {
            case PostNetworkConstants.CONSTRAINT_EDIT_SELL_BOOK:
                mTextBookToEdit = result.getParcelable(postConstraints);
                if (mIsSelling) {
                    SellingFormFragment fragment = (SellingFormFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.form_container);
                    fragment.onEditSuccessful();
                } else {
                    TradingFormFragment fragment = (TradingFormFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.form_container);
                    fragment.onEditSuccessful();
                }
                Intent intent = new Intent();
                intent.putExtra(IntentExtra.BOOK, mTextBookToEdit);
                setResult(RESULT_OK, intent);
                finish();
                return;
            case PostNetworkConstants.CONSTRAINT_POST_TRADE_BOOK: {
                TradingFormFragment fragment = (TradingFormFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.form_container);
                if (fragment != null) {
                    fragment.onPostSuccessful();
                }
                break;
            }
            case PostNetworkConstants.CONSTRAINT_POST_SELL_BOOK: {
                SellingFormFragment fragment = (SellingFormFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.form_container);
                fragment.onPostSuccessful();
                break;
            }
        }
        finish();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mIsProgressShown = false;
        GetNetworkFragment fragment = (GetNetworkFragment) getSupportFragmentManager()
                .findFragmentByTag(GET_CONSTRAINT_BARCODE_AUTOCOMPLETE);
        fragment.cancelTask(GET_CONSTRAINT_BARCODE_AUTOCOMPLETE);
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        mProgressDialog.dismiss();
        mIsProgressShown = false;
        if (mIsSelling) {
            SellingFormFragment fragment = (SellingFormFragment) getSupportFragmentManager()
                    .findFragmentByTag(FragmentKeys.SELL_FORM_FRAGMENT);
            String title = mTextBookBeingMade.getTitle();
            String author = mTextBookBeingMade.getAuthor();
            String isbn = mTextBookBeingMade.getIsbn();
            if (fragment != null) {
                fragment.onBarcodeRetrieved(new TextBook(title, author, isbn, response.getBitmap()));
            }
        } else {
            TradingFormFragment fragment = (TradingFormFragment) getSupportFragmentManager()
                    .findFragmentByTag(FragmentKeys.TRADE_FORM_FRAGMENT);
            String title = mTextBookBeingMade.getTitle();
            String author = mTextBookBeingMade.getAuthor();
            String isbn = mTextBookBeingMade.getIsbn();
            if (fragment != null) {
                fragment.onBarcodeRetrieved(new TextBook(title, author, isbn, response.getBitmap()));
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mProgressDialog.dismiss();
        mIsProgressShown = false;
        UiUtility.snackbar(findViewById(R.id.form_container), R.string.error_retrieving_book_info,
                null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putBoolean(BUNDLE_SELLING, mIsSelling);
        outState.putParcelable(BUNDLE_BOOK, mTextBookToEdit);
        outState.putBoolean(BUNDLE_IS_PROGRESS_SHOWN, mIsProgressShown);
        outState.putParcelable(BUNDLE_BOOK_MADE, mTextBookBeingMade);
    }

}
