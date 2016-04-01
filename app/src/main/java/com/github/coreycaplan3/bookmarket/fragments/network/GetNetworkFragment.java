package com.github.coreycaplan3.bookmarket.fragments.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.database.DatabaseApi;
import com.github.coreycaplan3.bookmarket.database.IsbnApi;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;

import java.util.ArrayList;

import static com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.*;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: To provide a worker fragment that is responsible for handling all POST network
 * requests. This fragment retains its instance whenever the corresponding activity is destroyed.
 * <p></p>
 * This fragment should <b>only</b> be instantiated when an activity is created, since it does
 * <b>not</b> persist throughout activities.
 */
public class GetNetworkFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private final ArrayMap<Long, NetworkTask> mRunningTasks = new ArrayMap<>();
    private long stableIdCounter = 0;

    private GetNetworkCommunicator mGetNetworkCommunicator;

    public static GetNetworkFragment newInstance() {
        return new GetNetworkFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mGetNetworkCommunicator = (GetNetworkCommunicator) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    public void startSearchTask(String query) {
        NetworkTask task = new NetworkTask();
        task.performSearch(query);
        startTask(task);
    }

    public void startGetBooksTask(String isbn) {
        NetworkTask task = new NetworkTask();
        task.performGetBooks(isbn);
        startTask(task);
    }

    /**
     * Gets a user's books for which he/she is trading
     *
     * @param userId The user id of the person whose books need to be found
     */
    public void startGetTradedBooksTask(String userId) {
        NetworkTask task = new NetworkTask();
        task.performGetTradedBooks(userId);
        startTask(task);
    }

    public void startGetNewSellListingsTask(UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performGetNewSellListings(userProfile);
        startTask(task);
    }

    public void startGetOldSellListingsTask(UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performGetOldSellListings(userProfile);
        startTask(task);
    }

    public void startGetNewTradeListingsTask(UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performGetNewTradeListings(userProfile);
        startTask(task);
    }

    public void startGetOldTradeListingsTask(UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performGetOldTradeListings(userProfile);
        startTask(task);
    }

    public void getTextbookFromIsbn(String isbn) {
        NetworkTask task = new NetworkTask();
        task.performGetTextbookFromIsbn(isbn);
        startTask(task);
    }

    private void startTask(NetworkTask task) {
        mRunningTasks.put(task.STABLE_ID, task);
        task.execute();
    }

    private void onTaskComplete(long stableId) {
        mRunningTasks.remove(stableId);
    }

    public void cancelTask(@GetNetworkConstraints String tag) {
        for (int i = 0; i < mRunningTasks.size(); i++) {
            NetworkTask task = mRunningTasks.valueAt(i);
            if (task != null && task.mNetworkConstraint.equals(tag)) {
                task.cancel(true);
            }
        }
    }

    public void cancelTasks() {
        for (int i = 0; i < mRunningTasks.size(); i++) {
            NetworkTask task = mRunningTasks.valueAt(i);
            if (task != null) {
                task.cancel(true);
            }
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGetNetworkCommunicator = null;
    }

    private class NetworkTask extends AsyncTask<Void, Void, Bundle> {

        private final long STABLE_ID;
        @GetNetworkConstraints
        private String mNetworkConstraint;

        private UserProfile mUserProfile;
        private String mQuery;
        private String mIsbn;
        private String mUserId;

        private final String TAG = getClass().getSimpleName();

        private NetworkTask() {
            STABLE_ID = stableIdCounter++;
        }

        private void performSearch(String query) {
            mNetworkConstraint = GET_CONSTRAINT_SEARCH;
            mQuery = query;
        }

        private void performGetBooks(String isbn) {
            mNetworkConstraint = GET_CONSTRAINT_GET_SELLING_BOOKS;
            mIsbn = isbn;
        }

        private void performGetTradedBooks(String userId) {
            mNetworkConstraint = GET_CONSTRAINT_GET_TRADING_BOOKS;
            mUserId = userId;
        }

        private void performGetNewSellListings(UserProfile userProfile) {
            mNetworkConstraint = GET_CONSTRAINT_GET_NEW_SELL_LISTINGS;
            mUserProfile = userProfile;
        }

        private void performGetOldSellListings(UserProfile userProfile) {
            mNetworkConstraint = GET_CONSTRAINT_GET_OLD_SELL_LISTINGS;
            mUserProfile = userProfile;
        }

        private void performGetNewTradeListings(UserProfile userProfile) {
            mNetworkConstraint = GET_CONSTRAINT_GET_NEW_TRADE_LISTINGS;
            mUserProfile = userProfile;
        }

        private void performGetOldTradeListings(UserProfile userProfile) {
            mNetworkConstraint = GET_CONSTRAINT_GET_OLD_TRADE_LISTINGS;
            mUserProfile = userProfile;
        }

        private void performGetTextbookFromIsbn(String isbn) {
            mNetworkConstraint = GET_CONSTRAINT_BARCODE_AUTOCOMPLETE;
            mIsbn = isbn;
        }

        @Override
        protected Bundle doInBackground(Void... params) {
            Bundle bundle = new Bundle();
            DatabaseApi databaseApi = new DatabaseApi();
            switch (mNetworkConstraint) {
                case GET_CONSTRAINT_SEARCH:
                    getSearchResults(databaseApi, bundle);
                    break;
                case GET_CONSTRAINT_GET_SELLING_BOOKS:
                    getSellingResults(databaseApi, bundle);
                    break;
                case GET_CONSTRAINT_GET_TRADING_BOOKS:
                    getTradingBooks(databaseApi, bundle);
                    break;
                case GET_CONSTRAINT_GET_NEW_SELL_LISTINGS:
                    getNewSellListings(databaseApi, bundle);
                    break;
                case GET_CONSTRAINT_GET_OLD_SELL_LISTINGS:
                    getOldSellListings(databaseApi, bundle);
                    break;
                case GET_CONSTRAINT_GET_NEW_TRADE_LISTINGS:
                    getNewTradeListings(databaseApi, bundle);
                    break;
                case GET_CONSTRAINT_GET_OLD_TRADE_LISTINGS:
                    getOldTradeListings(databaseApi, bundle);
                    break;
                case GET_CONSTRAINT_BARCODE_AUTOCOMPLETE:
                    getBookInformationFromIsbn(bundle);
                    break;
                default:
                    Log.e(TAG, "doInBackground: ", new IllegalArgumentException("Invalid network " +
                            "constraint: " + mNetworkConstraint));
            }
            return bundle;
        }

        private void getSearchResults(DatabaseApi databaseApi, Bundle bundle) {
            ArrayList<TextBook> mBooks = null;
            try {
                mBooks = databaseApi.textbookLookup(mQuery);
            } catch (Exception e) {
                Log.e(TAG, "getSearchResults: ", e);
            }
            bundle.putParcelableArrayList(mNetworkConstraint, mBooks);
        }

        private void getSellingResults(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void getTradingBooks(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void getNewSellListings(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void getOldSellListings(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void getNewTradeListings(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void getOldTradeListings(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void getBookInformationFromIsbn(Bundle bundle) {
            IsbnApi isbnApi = new IsbnApi();
            TextBook textBook = null;
            try {
                textBook = isbnApi.getTextBookFromIsbn(mIsbn);
            } catch (Exception e) {
                Log.e(TAG, "getBookInformationFromIsbn: ", e);
            }
            bundle.putParcelable(mNetworkConstraint, textBook);
        }

        @Override
        protected void onCancelled(Bundle bundle) {
            if (mGetNetworkCommunicator != null) {
                mGetNetworkCommunicator.onGetNetworkTaskComplete(bundle, mNetworkConstraint);
                onTaskComplete(STABLE_ID);
            } else {
                Thread thread = new Thread(new WaitForActivityThread(STABLE_ID, bundle,
                        mNetworkConstraint));
                thread.start();
            }
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            if (mGetNetworkCommunicator != null) {
                mGetNetworkCommunicator.onGetNetworkTaskComplete(bundle, mNetworkConstraint);
                onTaskComplete(STABLE_ID);
            } else {
                Thread thread = new Thread(new WaitForActivityThread(STABLE_ID, bundle,
                        mNetworkConstraint));
                thread.start();
            }
        }

    }

    private class WaitForActivityThread implements Runnable {

        private final long STABLE_ID;

        private Bundle mBundle;
        @GetNetworkConstraints
        private String mNetworkConstraints;

        private WaitForActivityThread(long stableId, Bundle bundle,
                                      @GetNetworkConstraints String networkConstraints) {
            mBundle = bundle;
            mNetworkConstraints = networkConstraints;
            STABLE_ID = stableId;
        }

        @Override
        public void run() {
            final long timeToWait = 5000L;
            final long intervalToCheck = 200L;
            long currentTime = 0L;
            while (currentTime < timeToWait) {
                if (getActivity() != null && mGetNetworkCommunicator != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGetNetworkCommunicator.onGetNetworkTaskComplete(mBundle,
                                    mNetworkConstraints);
                        }
                    });
                    break;
                }
                currentTime += intervalToCheck;
            }
            onTaskComplete(STABLE_ID);
        }

    }

}
