package com.github.coreycaplan3.bookmarket.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;

/**
 * Created by Corey on 4/1/2016.
 * Project: Book Mart
 * <p></p>
 * Purpose of Class: To provide the users with a confirmation that allows them to choose whether or
 * not they should sign out from the app.
 */
public class SignOutDialog extends DialogFragment implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();
    @StringRes
    private int mTitle;
    @StringRes
    private int mMessage;
    private DialogCallback mListener;
    private static final String BUNDLE_TITLE = "dialogTitle";
    private static final String BUNDLE_MESSAGE = "dialogMessage";

    /**
     * Sets the message to be displayed by this sign out dialog.
     *
     * @param title   The title of this dialog that will be displayed to the user upon signing out.
     * @param message The message to be displayed to the user upon signing out.
     */
    public static SignOutDialog newInstance(@StringRes int title, @StringRes int message) {
        SignOutDialog signOutDialog = new SignOutDialog();
        Bundle arguments = new Bundle();
        arguments.putInt(BUNDLE_TITLE, title);
        arguments.putInt(BUNDLE_MESSAGE, message);
        signOutDialog.setArguments(arguments);
        return signOutDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogCallback) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getInt(BUNDLE_TITLE);
            mMessage = savedInstanceState.getInt(BUNDLE_MESSAGE);
        } else if (getArguments() != null) {
            mTitle = getArguments().getInt(BUNDLE_TITLE);
            mMessage = getArguments().getInt(BUNDLE_MESSAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mDialogView = inflater.inflate(R.layout.dialog_sign_out, container, false);
        ((TextView) mDialogView.findViewById(R.id.dialog_sign_out_message)).setText(mMessage);
        Button positiveButton = (Button) mDialogView.findViewById(R.id.dialog_sign_out_button_positive);
        Button negativeButton = (Button) mDialogView.findViewById(R.id.dialog_sign_out_button_negative);
        positiveButton.setOnClickListener(this);
        negativeButton.setOnClickListener(this);
        return mDialogView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle(mTitle);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_sign_out_button_positive:
                if (mListener != null) {
                    mListener.onDialogClick(getTag(), getDialog(), Dialog.BUTTON_POSITIVE);
                }
                dismiss();
                break;
            case R.id.dialog_sign_out_button_negative:
                if (mListener != null) {
                    mListener.onDialogClick(getTag(), getDialog(), Dialog.BUTTON_NEGATIVE);
                }
                dismiss();
                break;
            default:
                Log.e(TAG, "onDialogClick: ", new IllegalAccessError("Error: Invalid View passed!"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_TITLE, mTitle);
        outState.putInt(BUNDLE_MESSAGE, mMessage);
    }
}
