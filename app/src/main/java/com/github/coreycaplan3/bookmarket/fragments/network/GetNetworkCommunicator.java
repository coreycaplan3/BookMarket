package com.github.coreycaplan3.bookmarket.fragments.network;

import android.os.Bundle;

import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: A listener used for passing the results of the operations in the
 * {@link GetNetworkFragment} to the corresponding activity.
 */
public interface GetNetworkCommunicator {

    /**
     * This method is called after the corresponding task in {@link GetNetworkFragment} is
     * completed.
     *
     * @param result         A {@link Bundle} object containing the result of the network
     *                       operation with possible <b>null</b> values if the network operation
     *                       failed to execute. They key for the item being returned is the
     *                       {@link GetNetworkConstants} that was originally passed into the
     *                       {@link GetNetworkFragment} for execution.
     * @param getConstraints The original {@link GetNetworkConstants} that was passed into the
     *                       {@link GetNetworkFragment}.
     */
    void onGetNetworkTaskComplete(Bundle result, @GetNetworkConstraints String getConstraints);

}
