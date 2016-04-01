package com.github.coreycaplan3.bookmarket.fragments.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.utilities.FormValidation;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;

/**
 * Created by Corey on 3/26/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class LoginFragment extends Fragment implements View.OnClickListener, OnCancelListener {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int MINIMUM_LENGTH = 3;

    private boolean mIsProgressShowing = false;
    private String mEmail;
    private String mPassword;

    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    private ProgressDialog mProgressDialog;

    private static final String BUNDLE_EMAIL = TAG + "email";
    private static final String BUNDLE_PASSWORD = TAG + "password";
    private static final String BUNDLE_PROGRESS_SHOWING = TAG + "progressShowing";

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mEmail = savedInstanceState.getString(BUNDLE_EMAIL);
            mPassword = savedInstanceState.getString(BUNDLE_PASSWORD);
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_PROGRESS_SHOWING);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_login_email_layout);
        mPasswordTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_login_password_layout);
        mEmailEditText = (EditText) view.findViewById(R.id.fragment_login_email_edit_text);
        mPasswordEditText = (EditText) view.findViewById(R.id.fragment_login_password_edit_text);
        view.findViewById(R.id.fragment_login_sign_in_card).setOnClickListener(this);
        view.findViewById(R.id.fragment_login_register_card).setOnClickListener(this);

        mEmailEditText.setText(mEmail == null ? "" : mEmail);
        mPasswordEditText.setText(mPassword == null ? "" : mPassword);

        setupListeners();

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.setMessage(getString(R.string.signing_in));
        if (mIsProgressShowing) {
            mProgressDialog.show();
        }

        return view;
    }

    private void setupListeners() {
        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mEmail = s.toString();
            }
        });
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPassword = s.toString();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fragment_login_sign_in_card) {
            if (isValid()) {
                PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                        .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
                fragment.startLoginTask(mEmail, mPassword);
                mProgressDialog.show();
                mIsProgressShowing = true;
            }
        } else if (id == R.id.fragment_login_register_card) {
            RegisterFragment fragment = RegisterFragment.newInstance();
            FragmentCreator.create(fragment, FragmentKeys.REGISTER_FRAGMENT, R.id.login_container,
                    getFragmentManager());
        } else {
            Log.e(TAG, "onClick: ", new IllegalArgumentException("Invalid Argument!"));
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        mEmailTextInputLayout.setError(null);
        mPasswordTextInputLayout.setError(null);

        if (FormValidation.isEmpty(mEmail)) {
            isValid = false;
            mEmailTextInputLayout.setError(getString(R.string.error_required));
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mEmail)) {
            isValid = false;
            mPasswordTextInputLayout.setError(getString(R.string.error_too_short));
        }

        if (FormValidation.isEmpty(mPassword)) {
            isValid = false;
            mPasswordTextInputLayout.setError(getString(R.string.error_required));
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mPassword)) {
            isValid = false;
            mPasswordTextInputLayout.setError(getString(R.string.error_too_short));
        }

        return isValid;
    }


    public void onSignInFailed() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        mEmailTextInputLayout.setError(getString(R.string.error_email_or_password));
    }

    public void onSignInSuccessful() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.e(TAG, "onCancel: ");
        mIsProgressShowing = false;
        PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        fragment.cancelTask(PostNetworkConstants.CONSTRAINT_LOGIN);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_EMAIL, mEmail);
        outState.putString(BUNDLE_PASSWORD, mPassword);
        outState.putBoolean(BUNDLE_PROGRESS_SHOWING, mIsProgressShowing);
    }

}
