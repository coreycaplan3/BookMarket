package com.github.coreycaplan3.bookmarket.fragments.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.database.DatabaseApi;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.functionality.GeneralUser;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.Keys;

import static com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.*;

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
public class PostNetworkFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private final ArrayMap<Long, NetworkTask> mRunningTasks = new ArrayMap<>();
    private long stableIdCounter = 0;

    private PostNetworkCommunicator mPostNetworkCommunicator;

    public static PostNetworkFragment newInstance() {
        return new PostNetworkFragment();
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
            mPostNetworkCommunicator = (PostNetworkCommunicator) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    public void startLoginTask(String email, String password) {
        NetworkTask task = new NetworkTask();
        task.performLoginTask(email, password);
        startTask(task);
    }

    public void startSilentLoginTask(String connectionToken) {
        NetworkTask task = new NetworkTask();
        task.performSilentLoginTask(connectionToken);
        startTask(task);
    }

    public void startRegisterTask(String name, String email, String password, String university) {
        NetworkTask task = new NetworkTask();
        task.performRegisterTask(name, email, password, university);
        startTask(task);
    }

    public void startEditBookTask(TextBook textBook, UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performEditBookTask(textBook, userProfile);
        startTask(task);
    }

    public void startBuyBookWithUserTask(TextBook textBook, UserProfile userProfile,
                                         GeneralUser seller) {
        NetworkTask task = new NetworkTask();
        task.performBuyBookTaskWithUser(textBook, userProfile, seller);
        startTask(task);
    }

    public void startTradeBookWithUserTask(TextBook textBook, UserProfile userProfile,
                                           GeneralUser seller) {
        NetworkTask task = new NetworkTask();
        task.performTradeBookWithUserTask(textBook, userProfile, seller);
        startTask(task);
    }

    public void startPostTradeBookTask(TextBook textBook, UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performPostTradeBookTask(textBook, userProfile);
        startTask(task);
    }

    public void startPostSellBookTask(TextBook textBook, UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performPostSellBookTask(textBook, userProfile);
        startTask(task);
    }

    public void startPostDesiredTradeTask(TextBook textBook, UserProfile userProfile) {
        NetworkTask task = new NetworkTask();
        task.performPostDesiredTradeTask(textBook, userProfile);
        startTask(task);
    }

    /**
     * Used for accessing Blue Mix's data for posting pictures and getting information
     */
    public void startCameraApiTask() {
        NetworkTask task = new NetworkTask();
        startTask(task);
    }

    private void startTask(NetworkTask task) {
        mRunningTasks.put(task.STABLE_ID, task);
        task.execute();
    }

    private void onTaskComplete(long stableId) {
        mRunningTasks.remove(stableId);
    }

    public void cancelTask(@PostNetworkConstraints String tag) {
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
        mPostNetworkCommunicator = null;
    }

    private class NetworkTask extends AsyncTask<Void, Void, Bundle> {

        private final long STABLE_ID;
        @PostNetworkConstraints
        private String mNetworkConstraint;

        private String mName;
        private String mEmail;
        private String mPassword;
        private String mUniversity;
        private String mConnectionToken;
        private GeneralUser mSeller;
        private TextBook mTextBook;
        private UserProfile mUserProfile;

        private final String TAG = getClass().getSimpleName();

        private NetworkTask() {
            STABLE_ID = stableIdCounter++;
        }

        private void performLoginTask(String email, String password) {
            mNetworkConstraint = CONSTRAINT_LOGIN;
            mEmail = email;
            mPassword = password;
        }

        private void performSilentLoginTask(String connectionToken) {
            mNetworkConstraint = CONSTRAINT_SILENT_LOGIN;
            mConnectionToken = connectionToken;
        }

        private void performRegisterTask(String name, String email, String password,
                                         String university) {
            mNetworkConstraint = CONSTRAINT_REGISTER;
            mName = name;
            mEmail = email;
            mPassword = password;
            mUniversity = university;
        }

        private void performEditBookTask(TextBook textBook, UserProfile userProfile) {
            mNetworkConstraint = CONSTRAINT_EDIT_BOOK;
            mTextBook = textBook;
            mUserProfile = userProfile;
        }

        private void performBuyBookTaskWithUser(TextBook textBook, UserProfile userProfile,
                                                GeneralUser seller) {
            mNetworkConstraint = CONSTRAINT_BUY_BOOK;
            mTextBook = textBook;
            mUserProfile = userProfile;
            mSeller = seller;
        }

        private void performTradeBookWithUserTask(TextBook textBook, UserProfile userProfile,
                                                  GeneralUser seller) {
            mNetworkConstraint = CONSTRAINT_TRADE_BOOK;
            mTextBook = textBook;
            mUserProfile = userProfile;
            mSeller = seller;
        }

        private void performPostTradeBookTask(TextBook textBook, UserProfile userProfile) {
            mNetworkConstraint = CONSTRAINT_POST_TRADE_BOOK;
            mTextBook = textBook;
            mUserProfile = userProfile;
        }

        private void performPostSellBookTask(TextBook textBook, UserProfile userProfile) {
            mNetworkConstraint = CONSTRAINT_POST_SELL_BOOK;
            mTextBook = textBook;
            mUserProfile = userProfile;
        }

        private void performPostDesiredTradeTask(TextBook textBookList, UserProfile userProfile) {
            mNetworkConstraint = CONSTRAINT_POST_DESIRED_TRADE;
            mTextBook = textBookList;
            mUserProfile = userProfile;
        }

        @Override
        protected Bundle doInBackground(Void... params) {
            Bundle bundle = new Bundle();
            DatabaseApi databaseApi = new DatabaseApi();
            switch (mNetworkConstraint) {
                case CONSTRAINT_LOGIN:
                    startLogin(databaseApi, bundle);
                    break;
                case CONSTRAINT_SILENT_LOGIN:
                    startSilentLogin(databaseApi, bundle);
                    break;
                case CONSTRAINT_REGISTER:
                    startRegistration(databaseApi, bundle);
                    break;
                case CONSTRAINT_POST_SELL_BOOK:
                    startPostSellBook(databaseApi, bundle);
                    break;
                case CONSTRAINT_BUY_BOOK:
                    startBuyBookWithUser(databaseApi, bundle);
                    break;
                case CONSTRAINT_EDIT_BOOK:
                    startEditBook(databaseApi, bundle);
                    break;
                case CONSTRAINT_TRADE_BOOK:
                    startTradeBookWithUser(databaseApi, bundle);
                    break;
                case CONSTRAINT_POST_TRADE_BOOK:
                    startPostTradeBook(databaseApi, bundle);
                    break;
                case CONSTRAINT_POST_DESIRED_TRADE:
                    startPostDesiredTradeBook(databaseApi, bundle);
                    break;
                default:
                    Log.e(TAG, "doInBackground: ", new IllegalArgumentException("Invalid network " +
                            "constraint: " + mNetworkConstraint));
            }
            return bundle;
        }

        private void startLogin(DatabaseApi databaseApi, Bundle bundle) {
            UserProfile userProfile = null;
            try {
                userProfile = databaseApi.logIn(mEmail, mPassword);
            } catch (Exception e) {
                Log.e(TAG, "startLogin: ", e);
            }
            bundle.putParcelable(mNetworkConstraint, userProfile);
        }

        private void startSilentLogin(DatabaseApi databaseApi, Bundle bundle) {
            boolean isValid;
            UserProfile userProfile = null;
            if(mConnectionToken != null) {
                Log.e(TAG, "startSilentLogin: " + "Starting cached sign in...");
                try {
                    isValid = databaseApi.isValidToken(mConnectionToken);
                    if (isValid) {
                        userProfile = Keys.getSavedLoginInformation(getContext());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "startSilentLogin: ", e);
                }
            }
            bundle.putParcelable(mNetworkConstraint, userProfile);
        }

        private void startRegistration(DatabaseApi databaseApi, Bundle bundle) {
            UserProfile userProfile = null;
            try {
                String userId = databaseApi.register(mName, mEmail, mPassword, mUniversity);
                userProfile = new UserProfile(mName, mEmail, mConnectionToken, userId, mUniversity);
            } catch (Exception e) {
                Log.e(TAG, "startRegistration: ", e);
            }
            bundle.putParcelable(mNetworkConstraint, userProfile);
        }

        private void startPostSellBook(DatabaseApi databaseApi, Bundle bundle) {
//            try {
//                databaseApi.postSellListing(mTextBook.getIsbn(), )
//            }
        }

        private void startPostTradeBook(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void startPostDesiredTradeBook(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void startBuyBookWithUser(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void startEditBook(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void startTradeBookWithUser(DatabaseApi databaseApi, Bundle bundle) {

        }

        private void startSellBook(DatabaseApi databaseApi, Bundle bundle) {

        }

        @Override
        protected void onCancelled(Bundle bundle) {
            if (mPostNetworkCommunicator != null) {
                bundle = new Bundle();
                mPostNetworkCommunicator.onPostNetworkTaskComplete(bundle, mNetworkConstraint);
                onTaskComplete(STABLE_ID);
            } else {
                Thread thread = new Thread(new WaitForActivityThread(STABLE_ID, bundle,
                        mNetworkConstraint));
                thread.start();
            }
        }

        @Override
        protected void onPostExecute(Bundle bundle) {
            if (mPostNetworkCommunicator != null) {
                mPostNetworkCommunicator.onPostNetworkTaskComplete(bundle, mNetworkConstraint);
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
        @PostNetworkConstraints
        private String mNetworkConstraints;

        private WaitForActivityThread(long stableId, Bundle bundle,
                                      @PostNetworkConstraints String networkConstraints) {
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
                if (getActivity() != null && mPostNetworkCommunicator != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPostNetworkCommunicator.onPostNetworkTaskComplete(mBundle,
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
