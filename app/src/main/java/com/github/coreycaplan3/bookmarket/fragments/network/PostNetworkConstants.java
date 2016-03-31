package com.github.coreycaplan3.bookmarket.fragments.network;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Corey on 3/19/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: An annotation for distinguishing between which type of networking action the
 * {@link PostNetworkFragment} should perform.
 */

public interface PostNetworkConstants {

    @StringDef({CONSTRAINT_LOGIN, CONSTRAINT_SILENT_LOGIN, CONSTRAINT_POST_BOOK,
            CONSTRAINT_EDIT_BOOK, CONSTRAINT_BUY_BOOK, CONSTRAINT_TRADE_BOOK,
            CONSTRAINT_POST_TRADE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    @interface PostNetworkConstraints {
    }

    /**
     * Constraint for performing a traditional login (involving email and password)
     */
    String CONSTRAINT_LOGIN = "getNetworkConstraintLogin";

    /**
     * Constraint for performing a silent login using the user's cached login token.
     */
    String CONSTRAINT_SILENT_LOGIN = "getNetworkConstraintSilentLogin";

    String CONSTRAINT_POST_BOOK = "getNetworkConstraintPostBook";
    String CONSTRAINT_EDIT_BOOK = "getNetworkConstraintPostBook";
    String CONSTRAINT_BUY_BOOK = "getNetworkConstraintPostBook";
    String CONSTRAINT_TRADE_BOOK = "getNetworkConstraintPostBook";
    String CONSTRAINT_POST_TRADE_LIST = "getNetworkConstraintPostBook";
}
