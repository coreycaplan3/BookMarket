package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.TitleFragment;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TitleActivity.class.getSimpleName();

    private boolean mIsBuying = false;
    private boolean mIsTrading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        initiateFragment(savedInstanceState);
    }

    private void initiateFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.create(TitleFragment.newInstance(), TITLE_FRAGMENT,
                    R.id.title_container, getSupportFragmentManager());
        } else {

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_buy_card) {
            mIsBuying = true;
            mIsTrading = false;
            onSearchRequested();
        } else if (id == R.id.title_trade_card) {
            mIsBuying = false;
            mIsTrading = true;
            onSearchRequested();
        } else if (id == R.id.title_sell_card) {

        } else if (id == R.id.title_account_card) {

        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("INVALID ARG"));
        }
    }

}
