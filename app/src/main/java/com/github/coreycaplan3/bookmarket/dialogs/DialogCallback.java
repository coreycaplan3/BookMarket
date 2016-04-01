package com.github.coreycaplan3.bookmarket.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;

/**
 * Created by Corey on 4/1/2016.
 * Project: MeetUp
 * <p></p>
 * Purpose of Class: To provide a uniform way through which dialogs may send information back to the
 * caller.
 */
public interface DialogCallback {

    /**
     * @param dialogTag The tag used to create the dialog.
     * @param dialog    A reference to the dialog attached to this interface.
     * @param which     One of {@link Dialog#BUTTON_POSITIVE} or {@link Dialog#BUTTON_NEGATIVE}.
     */
    void onDialogClick(String dialogTag, DialogInterface dialog, int which);
}
