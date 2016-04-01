package com.github.coreycaplan3.bookmarket.utilities;

import android.support.annotation.StringDef;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public interface FragmentKeys {

    String LOGIN_FRAGMENT = "Login";
    String TITLE_FRAGMENT = "Book Mart";
    String BOOKS_MARKET_LIST_FRAGMENT = "fragmentBooksMarketList";
    String BOOK_DETAILS_FRAGMENT = "fragmentBookDetails";
    String TRADE_TITLE_FRAGMENT = "Trade";
    String TRADE_FORM_FRAGMENT = "Trade a Book";
    String SELL_FORM_FRAGMENT = "Sell a Textbook";
    String REGISTER_FRAGMENT = "Register";
    String NOTIFICATIONS_FRAGMENT = "Notifications";
    String PROFILE_FRAGMENT = "My Profile";
    String NEW_SELL_LISTINGS_FRAGMENT = "Current Sell Listings";
    String OLD_SELL_LISTINGS_FRAGMENT = "Past Sell Listings";
    String NEW_TRADE_LISTINGS_FRAGMENT = "New Trade Listings";
    String OLD_TRADE_LISTINGS_FRAGMENT = "Old Sell Listings";
    String ADD_DESIRED_TRADE_FRAGMENT = "Add to Trades";

    String POST_NETWORK_FRAGMENT = "postNetworkFragment";
    String GET_NETWORK_FRAGMENT = "getNetworkFragment";

    @StringDef({LOGIN_FRAGMENT, TITLE_FRAGMENT, BOOKS_MARKET_LIST_FRAGMENT, BOOK_DETAILS_FRAGMENT,
            TRADE_TITLE_FRAGMENT, TRADE_FORM_FRAGMENT, SELL_FORM_FRAGMENT, REGISTER_FRAGMENT,
            NOTIFICATIONS_FRAGMENT, PROFILE_FRAGMENT, NEW_SELL_LISTINGS_FRAGMENT,
            OLD_SELL_LISTINGS_FRAGMENT, NEW_TRADE_LISTINGS_FRAGMENT, OLD_TRADE_LISTINGS_FRAGMENT,
            ADD_DESIRED_TRADE_FRAGMENT})
    @interface FragmentTag {
    }

}
