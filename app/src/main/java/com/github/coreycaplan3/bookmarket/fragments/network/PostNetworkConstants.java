package com.github.coreycaplan3.bookmarket.fragments.network;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: An annotation for distinguishing between which type of networking action the
 * {@link PostNetworkFragment} should perform.
 */

public interface PostNetworkConstants {

    @StringDef({CONSTRAINT_LOGIN, CONSTRAINT_SILENT_LOGIN, CONSTRAINT_REGISTER,
            CONSTRAINT_EDIT_BOOK, CONSTRAINT_BUY_BOOK, CONSTRAINT_TRADE_BOOK,
            CONSTRAINT_POST_TRADE_LIST, CONSTRAINT_POST_TRADE_BOOK, CONSTRAINT_POST_SELL_BOOK})
    @Retention(RetentionPolicy.SOURCE)
    @interface PostNetworkConstraints {
    }

    /**
     * Constraint for performing a traditional login (involving email and password)
     */
    String CONSTRAINT_LOGIN = "postNetworkConstraintLogin";

    /**
     * Constraint for performing a silent login using the user's cached login token.
     */
    String CONSTRAINT_SILENT_LOGIN = "postNetworkConstraintSilentLogin";

    String CONSTRAINT_REGISTER = "postNetworkConstraintRegister";

    String CONSTRAINT_EDIT_BOOK = "postNetworkConstraintEditBook";

    String CONSTRAINT_BUY_BOOK = "postNetworkConstraintBuyBook";

    String CONSTRAINT_TRADE_BOOK = "postNetworkConstraintTradeBook";

    String CONSTRAINT_POST_TRADE_BOOK = "postNetworkConstraintPostTradeBook";

    String CONSTRAINT_POST_TRADE_LIST = "postNetworkConstraintPostTradeList";

    String CONSTRAINT_POST_SELL_BOOK = "postNetworkConstraintPostSellBook";
}
