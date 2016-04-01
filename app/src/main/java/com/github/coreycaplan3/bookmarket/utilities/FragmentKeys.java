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

    String POST_NETWORK_FRAGMENT = "postNetworkFragment";
    String GET_NETWORK_FRAGMENT = "getNetworkFragment";

    @StringDef({LOGIN_FRAGMENT, TITLE_FRAGMENT, BOOKS_MARKET_LIST_FRAGMENT, BOOK_DETAILS_FRAGMENT,
            TRADE_TITLE_FRAGMENT, TRADE_FORM_FRAGMENT, SELL_FORM_FRAGMENT, REGISTER_FRAGMENT})
    @interface FragmentTag {
    }

}
