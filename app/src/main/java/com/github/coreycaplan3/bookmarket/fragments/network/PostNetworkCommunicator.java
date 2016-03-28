package com.github.coreycaplan3.bookmarket.fragments.network;

import android.os.Bundle;

import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;

/**
 * Created by Corey on 1/14/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: A listener used for passing the results of the operations in the
 * {@link GetNetworkFragment} to the corresponding activity.
 */
public interface PostNetworkCommunicator {

    /**
     * This method is called after the corresponding task in {@link GetNetworkFragment} is
     *
     * @param result          A {@link Bundle} object containing the result of the network
     *                        operation with possible <b>null</b> values if the network operation
     *                        failed to execute. They key for the item being returned is the
     *                        {@link PostNetworkConstants} that was originally passed into the
     *                        {@link PostNetworkFragment} for execution.
     * @param postConstraints The original {@link PostNetworkConstants} that was passed into the
     *                        {@link PostNetworkFragment}.
     */
    void onPostNetworkTaskComplete(Bundle result, @PostNetworkConstraints String postConstraints);

}
