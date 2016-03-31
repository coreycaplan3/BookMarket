package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.TitleFragment;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TitleActivity.class.getSimpleName();

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
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra(IntentExtra.ACTIVITY_BUY, true);
            startActivity(intent);
        } else if (id == R.id.title_trade_card) {

        } else if (id == R.id.title_sell_card) {

        } else if (id == R.id.title_account_card) {

        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("INVALID ARG"));
        }
    }

}
