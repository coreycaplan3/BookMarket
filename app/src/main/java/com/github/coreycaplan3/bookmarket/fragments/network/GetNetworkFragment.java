package com.github.coreycaplan3.bookmarket.fragments.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.database.DatabaseApi;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;

import static com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.*;

/**
 * Created by Corey on 3/19/2016.
 * Project: MeetUp
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

        private String mQuery;

        private final String TAG = getClass().getSimpleName();

        private NetworkTask() {
            STABLE_ID = stableIdCounter++;
        }

        private void performSearch(String query) {
            mNetworkConstraint = GET_CONSTRAINT_SEARCH;
            mQuery = query;
        }


        @Override
        protected Bundle doInBackground(Void... params) {
            Bundle bundle = new Bundle();
            //TODO implement logic for each api call
            DatabaseApi databaseApi = new DatabaseApi();
            switch (mNetworkConstraint) {
                case GET_CONSTRAINT_SEARCH:
                    bundle = getSearchResults(databaseApi);
                    break;
                default:
                    Log.e(TAG, "doInBackground: ", new IllegalArgumentException("Invalid network " +
                            "constraint: " + mNetworkConstraint));
            }
            return bundle;
        }

        private Bundle getSearchResults(DatabaseApi databaseApi) {
            //TODO implement me
            return null;
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
