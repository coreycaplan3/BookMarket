package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.coreycaplan3.bookmarket.R;
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
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class FormActivity extends AppCompatActivity implements View.OnClickListener,
        PostNetworkCommunicator, GetNetworkCommunicator {

    private static final String TAG = TitleActivity.class.getSimpleName();

    private boolean mIsSelling;
    private UserProfile mUserProfile;
    @Nullable
    private TextBook mTextBookToEdit;

    private static final String BUNDLE_SELLING = TAG + "selling";
    private static final String BUNDLE_PROFILE = TAG + "profile";
    private static final String BUNDLE_BOOK = TAG + "book";

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
                            R.id.title_container, getSupportFragmentManager());
                } else {
                    String condition = TextBook.getCondition(mTextBookToEdit.getCondition(), this);
                    FragmentCreator.create(SellingFormFragment.newInstance(mUserProfile,
                            mTextBookToEdit.getTitle(), mTextBookToEdit.getAuthor(),
                            mTextBookToEdit.getIsbn(), mTextBookToEdit.getPicture(),
                            mTextBookToEdit.getPrice(), condition, mTextBookToEdit.getSellingId()),
                            SELL_FORM_FRAGMENT, R.id.title_container, getSupportFragmentManager());
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
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.form_floating_action_button) {
            //TODO implement barcode
        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("Invalid!"));
        }
    }


    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {

    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {
        switch (postConstraints) {
            case PostNetworkConstants.CONSTRAINT_EDIT_BOOK:
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putBoolean(BUNDLE_SELLING, mIsSelling);
        outState.putParcelable(BUNDLE_BOOK, mTextBookToEdit);
    }

}
