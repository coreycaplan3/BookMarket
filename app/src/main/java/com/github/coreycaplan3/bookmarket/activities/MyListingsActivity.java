package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.MyTextBookClickListener;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.account.MyNewSellListingsFragment;
import com.github.coreycaplan3.bookmarket.fragments.account.MyNewTradeListingsFragment;
import com.github.coreycaplan3.bookmarket.fragments.account.MyOldSellListingsFragment;
import com.github.coreycaplan3.bookmarket.fragments.account.MyOldTradeListingsFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.utilities.FragmentStatePagerAdapter;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;

import java.util.ArrayList;

import static com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.*;
import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class MyListingsActivity extends AppCompatActivity implements OnPageChangeListener,
        MyTextBookClickListener, GetNetworkCommunicator, PostNetworkCommunicator {

    private static final String TAG = MyListingsActivity.class.getSimpleName();

    private boolean mIsSelling;
    private UserProfile mUserProfile;
    private ArrayList<TextBook> mNewTextBooks;
    private ArrayList<TextBook> mOldTextBooks;

    private static final String BUNDLE_PROFILE = "bundleProfile";
    private static final String BUNDLE_NEW_BOOKS = "bundleNewBooks";
    private static final String BUNDLE_OLD_BOOKS = "bundleOldBooks";
    private static final String BUNDLE_IS_SELLING = "bundleIsSelling";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        restoreInstance(savedInstanceState);
        ViewPager viewPager = (ViewPager) findViewById(R.id.my_listings_pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        if (viewPager != null) {
            viewPager.addOnPageChangeListener(this);
            viewPager.setAdapter(pagerAdapter);
        } else {
            Log.e(TAG, "onCreate: ", new NullPointerException("ViewPager"));
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.my_listings_tab_layout);
        if (tabLayout != null) {
            if (mIsSelling) {
                tabLayout.addTab(tabLayout.newTab().setText(NEW_SELL_LISTINGS_FRAGMENT));
                tabLayout.addTab(tabLayout.newTab().setText(OLD_SELL_LISTINGS_FRAGMENT));
            } else {
                tabLayout.addTab(tabLayout.newTab().setText(NEW_TRADE_LISTINGS_FRAGMENT));
                tabLayout.addTab(tabLayout.newTab().setText(OLD_TRADE_LISTINGS_FRAGMENT));
            }
            tabLayout.setupWithViewPager(viewPager);
        } else {
            Log.e(TAG, "onCreate: ", new NullPointerException("TabLayout"));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            mUserProfile = intent.getParcelableExtra(IntentExtra.PROFILE);
            mNewTextBooks = intent.getParcelableArrayListExtra(IntentExtra.NEW_BOOKS);
            mOldTextBooks = intent.getParcelableArrayListExtra(IntentExtra.OLD_BOOKS);
            mIsSelling = intent.getBooleanExtra(IntentExtra.ACTIVITY_SELLING, false);

            FragmentCreator.createNetworks(getSupportFragmentManager());
        } else {
            mUserProfile = savedInstanceState.getParcelable(BUNDLE_PROFILE);
            mNewTextBooks = savedInstanceState.getParcelable(BUNDLE_NEW_BOOKS);
            mOldTextBooks = savedInstanceState.getParcelable(BUNDLE_OLD_BOOKS);
            mIsSelling = savedInstanceState.getBoolean(BUNDLE_IS_SELLING);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onTextBookClicked(TextBook textBook, boolean isNewTextBook) {
        Intent intent = new Intent(getApplicationContext(), MyBookDetailsActivity.class);
        intent.putExtra(IntentExtra.PROFILE, mUserProfile);
        intent.putExtra(IntentExtra.BOOK, textBook);
        intent.putExtra(IntentExtra.ACTIVITY_SELLING, mIsSelling);
        intent.putExtra(IntentExtra.ACTIVITY_NEW, isNewTextBook);
        startActivity(intent);
    }

    @Override
    public void onPageSelected(int position) {
        if (getSupportActionBar() == null) {
            Log.e(TAG, "onPageSelected: ", new NullPointerException("NULL!"));
            return;
        }
        if (mIsSelling) {
            if (position == 0) {
                getSupportActionBar().setTitle(NEW_SELL_LISTINGS_FRAGMENT);
            } else {
                getSupportActionBar().setTitle(OLD_SELL_LISTINGS_FRAGMENT);
            }
        } else {
            if (position == 0) {
                getSupportActionBar().setTitle(NEW_TRADE_LISTINGS_FRAGMENT);
            } else {
                getSupportActionBar().setTitle(OLD_TRADE_LISTINGS_FRAGMENT);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {
        switch (getConstraints) {
            case GET_CONSTRAINT_GET_NEW_SELL_LISTINGS:
                getNewSellListings(result, getConstraints);
                break;
            case GET_CONSTRAINT_GET_OLD_SELL_LISTINGS:
                getOldSellListings(result, getConstraints);
                break;
            case GET_CONSTRAINT_GET_NEW_TRADE_LISTINGS:
                getNewTradeListings(result, getConstraints);
                break;
            case GET_CONSTRAINT_GET_OLD_TRADE_LISTINGS:
                getOldTradeListings(result, getConstraints);
                break;
            default:
                Log.e(TAG, "onGetNetworkTaskComplete: ", new IllegalArgumentException("Found: " +
                        getConstraints));
                break;
        }
    }

    private void getNewSellListings(Bundle result, String getConstraints) {
        MyNewSellListingsFragment fragment = (MyNewSellListingsFragment)
                getSupportFragmentManager().findFragmentByTag(NEW_SELL_LISTINGS_FRAGMENT);
        ArrayList<TextBook> textBooks = result.getParcelableArrayList(getConstraints);
        fragment.onTextBooksRefreshed(textBooks);
    }

    private void getOldSellListings(Bundle result, String getConstraints) {
        MyOldSellListingsFragment fragment = (MyOldSellListingsFragment)
                getSupportFragmentManager().findFragmentByTag(OLD_SELL_LISTINGS_FRAGMENT);
        ArrayList<TextBook> textBooks = result.getParcelableArrayList(getConstraints);
        fragment.onTextBooksRefreshed(textBooks);
    }

    private void getNewTradeListings(Bundle result, String getConstraints) {
        MyNewTradeListingsFragment fragment = (MyNewTradeListingsFragment)
                getSupportFragmentManager().findFragmentByTag(NEW_TRADE_LISTINGS_FRAGMENT);
        ArrayList<TextBook> textBooks = result.getParcelableArrayList(getConstraints);
        fragment.onTextBooksRefreshed(textBooks);
    }

    private void getOldTradeListings(Bundle result, String getConstraints) {
        MyOldTradeListingsFragment fragment = (MyOldTradeListingsFragment)
                getSupportFragmentManager().findFragmentByTag(OLD_TRADE_LISTINGS_FRAGMENT);
        ArrayList<TextBook> textBooks = result.getParcelableArrayList(getConstraints);
        fragment.onTextBooksRefreshed(textBooks);
    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_PROFILE, mUserProfile);
        outState.putParcelableArrayList(BUNDLE_NEW_BOOKS, mNewTextBooks);
        outState.putParcelableArrayList(BUNDLE_OLD_BOOKS, mOldTextBooks);
        outState.putBoolean(BUNDLE_IS_SELLING, mIsSelling);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (mIsSelling) {
                if (position == 0) {
                    return MyNewSellListingsFragment.newInstance(mUserProfile, mNewTextBooks);
                } else {
                    return MyOldSellListingsFragment.newInstance(mUserProfile, mOldTextBooks);
                }
            } else {
                if (position == 0) {
                    return MyNewTradeListingsFragment.newInstance(mUserProfile, mNewTextBooks);
                } else {
                    return MyOldTradeListingsFragment.newInstance(mUserProfile, mOldTextBooks);
                }
            }
        }

        @Override
        public String getTag(int position) {
            if (mIsSelling) {
                if (position == 0) {
                    return NEW_SELL_LISTINGS_FRAGMENT;
                } else {
                    return OLD_SELL_LISTINGS_FRAGMENT;
                }
            } else {
                if (position == 0) {
                    return NEW_TRADE_LISTINGS_FRAGMENT;
                } else {
                    return OLD_TRADE_LISTINGS_FRAGMENT;
                }
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
