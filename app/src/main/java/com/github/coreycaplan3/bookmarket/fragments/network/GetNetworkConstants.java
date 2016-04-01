package com.github.coreycaplan3.bookmarket.fragments.network;

import android.support.annotation.StringDef;

/**
 * Created by Corey on 3/19/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: An annotation for distinguishing between which type of networking action the
 * {@link GetNetworkFragment} should perform.
 */

public interface GetNetworkConstants {

    @StringDef({GET_CONSTRAINT_SEARCH, GET_CONSTRAINT_GET_TRADED_BOOKS, GET_CONSTRAINT_GET_BOOKS})
    @interface GetNetworkConstraints {
    }

    String GET_CONSTRAINT_SEARCH = "GetNetworkSearch";
    String GET_CONSTRAINT_GET_BOOKS = "GetNetworkGetBooks";
    String GET_CONSTRAINT_GET_TRADED_BOOKS = "GetTradedBooks";

}
