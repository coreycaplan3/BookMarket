package com.github.coreycaplan3.bookmarket.fragments.network;

import android.support.annotation.StringDef;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: An annotation for distinguishing between which type of networking action the
 * {@link GetNetworkFragment} should perform.
 */

public interface GetNetworkConstants {

    @StringDef({GET_CONSTRAINT_SEARCH, GET_CONSTRAINT_GET_TRADING_BOOKS, GET_CONSTRAINT_GET_SELLING_BOOKS,
            GET_CONSTRAINT_GET_NEW_SELL_LISTINGS, GET_CONSTRAINT_GET_OLD_SELL_LISTINGS,
            GET_CONSTRAINT_GET_NEW_TRADE_LISTINGS, GET_CONSTRAINT_GET_OLD_TRADE_LISTINGS,
            GET_CONSTRAINT_GET_DESIRED_TRADING_BOOKS, GET_CONSTRAINT_BARCODE_AUTOCOMPLETE})
    @interface GetNetworkConstraints {
    }

    String GET_CONSTRAINT_SEARCH = "GetNetworkSearch";

    String GET_CONSTRAINT_GET_SELLING_BOOKS = "GetNetworkGetBooks";

    String GET_CONSTRAINT_GET_TRADING_BOOKS = "GetTradedBooks";

    String GET_CONSTRAINT_GET_NEW_SELL_LISTINGS = "GetNewSellListings";

    String GET_CONSTRAINT_GET_OLD_SELL_LISTINGS = "GetOldSellListings";

    String GET_CONSTRAINT_GET_NEW_TRADE_LISTINGS = "GetNewTradeListings";

    String GET_CONSTRAINT_GET_OLD_TRADE_LISTINGS = "GetOldTradeListings";

    String GET_CONSTRAINT_GET_DESIRED_TRADING_BOOKS = "GetDesiredTradingBooks";

    String GET_CONSTRAINT_BARCODE_AUTOCOMPLETE = "BarcodeAutocomplete";
}
