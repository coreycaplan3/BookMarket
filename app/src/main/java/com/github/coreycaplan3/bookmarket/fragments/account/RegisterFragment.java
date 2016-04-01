package com.github.coreycaplan3.bookmarket.fragments.account;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.utilities.FormValidation;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.UiUtility;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class RegisterFragment extends Fragment implements View.OnClickListener,
        ProgressDialog.OnCancelListener {

    private static final String TAG = RegisterFragment.class.getSimpleName();
    private static final int MINIMUM_LENGTH = 3;

    private boolean mIsProgressShowing = false;
    @Nullable
    private String mName;
    @Nullable
    private String mEmail;
    @Nullable
    private String mPassword;
    @Nullable
    private String mConfirmPassword;
    @Nullable
    private String mUniversity;

    private TextInputLayout mNameTextInputLayout;
    private TextInputLayout mEmailTextInputLayout;
    private TextInputLayout mPasswordTextInputLayout;
    private TextInputLayout mConfirmPasswordTextInputLayout;
    private TextInputLayout mUniversityTextInputLayout;

    private EditText mNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private EditText mUniversityEditText;

    private ProgressDialog mProgressDialog;

    private static final String BUNDLE_NAME = TAG + "name";
    private static final String BUNDLE_EMAIL = TAG + "email";
    private static final String BUNDLE_PASSWORD = TAG + "password";
    private static final String BUNDLE_CONFIRM_PASSWORD = TAG + "confirmPassword";
    private static final String BUNDLE_UNIVERSITY = TAG + "university";
    private static final String BUNDLE_PROGRESS_SHOWING = TAG + "progressShowing";

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mIsProgressShowing = savedInstanceState.getBoolean(BUNDLE_PROGRESS_SHOWING);
            mName = savedInstanceState.getString(BUNDLE_NAME);
            mEmail = savedInstanceState.getString(BUNDLE_EMAIL);
            mPassword = savedInstanceState.getString(BUNDLE_PASSWORD);
            mConfirmPassword = savedInstanceState.getString(BUNDLE_CONFIRM_PASSWORD);
            mUniversity = savedInstanceState.getString(BUNDLE_UNIVERSITY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mNameTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_register_name_layout);
        mEmailTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_register_email_layout);
        mPasswordTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_register_password_layout);
        mConfirmPasswordTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_register_confirm_password_layout);
        mUniversityTextInputLayout = (TextInputLayout) view
                .findViewById(R.id.fragment_register_university_layout);

        mNameEditText = (EditText) view.findViewById(R.id.fragment_register_name_edit_text);
        mEmailEditText = (EditText) view.findViewById(R.id.fragment_register_email_edit_text);
        mPasswordEditText = (EditText) view.findViewById(R.id.fragment_register_password_edit_text);
        mConfirmPasswordEditText = (EditText) view.findViewById(R.id.fragment_register_confirm_password_edit_text);
        mUniversityEditText = (EditText) view.findViewById(R.id.fragment_register_university_edit_text);
        view.findViewById(R.id.fragment_register_submit_button).setOnClickListener(this);

        setupListeners();

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setOnCancelListener(this);
        mProgressDialog.setMessage(getString(R.string.registering));
        if (mIsProgressShowing) {
            mProgressDialog.show();
        }

        return view;
    }

    private void setupListeners() {
        mNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
            }
        });
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
        mConfirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mConfirmPassword = s.toString();
            }
        });
        mUniversityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUniversity = s.toString();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fragment_register_submit_button) {
            if (isValid()) {
                PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                        .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
                fragment.startRegisterTask(mName, mEmail, mPassword, mUniversity);
                mIsProgressShowing = true;
                mProgressDialog.show();
            }
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        mNameTextInputLayout.setError(null);
        mEmailTextInputLayout.setError(null);
        mPasswordTextInputLayout.setError(null);
        mConfirmPasswordTextInputLayout.setError(null);
        mUniversityTextInputLayout.setError(null);

        if (FormValidation.isEmpty(mName)) {
            isValid = false;
            mNameTextInputLayout.setError(getString(R.string.error_required));
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mName)) {
            isValid = false;
            mNameTextInputLayout.setError(getString(R.string.error_too_short));
        }

        if (FormValidation.isEmpty(mEmail)) {
            isValid = false;
            mEmailTextInputLayout.setError(getString(R.string.error_required));
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mEmail)) {
            isValid = false;
            mEmailTextInputLayout.setError(getString(R.string.error_too_short));
        }

        if (FormValidation.isEmpty(mPassword)) {
            isValid = false;
            mPasswordTextInputLayout.setError(getString(R.string.error_required));
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mPassword)) {
            isValid = false;
            mPasswordTextInputLayout.setError(getString(R.string.error_too_short));
        }

        if (FormValidation.isEmpty(mConfirmPassword)) {
            isValid = false;
            mConfirmPasswordTextInputLayout.setError(getString(R.string.error_required));
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mConfirmPassword)) {
            isValid = false;
            mConfirmPasswordTextInputLayout.setError(getString(R.string.error_too_short));
        }

        if (mPassword != null && mConfirmPassword != null && !mPassword.equals(mConfirmPassword)) {
            isValid = false;
            mPasswordTextInputLayout.setError(getString(R.string.error_passwords_match));
        }

        if (FormValidation.isEmpty(mUniversity)) {
            isValid = false;
            mUniversityTextInputLayout.setError(getString(R.string.error_required));
        } else if (FormValidation.isTooShort(MINIMUM_LENGTH, mUniversity)) {
            isValid = false;
            mUniversityTextInputLayout.setError(getString(R.string.error_too_short));
        }

        return isValid;
    }

    public void onRegistrationFailed() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        mEmailTextInputLayout.setError(getString(R.string.error_email_in_use));
    }

    public void onRegistrationSuccessful() {
        mIsProgressShowing = false;
        mProgressDialog.dismiss();
        UiUtility.toast(getContext(), R.string.registration_successful);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mIsProgressShowing = false;
        PostNetworkFragment fragment = (PostNetworkFragment) getFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        fragment.cancelTask(PostNetworkConstants.CONSTRAINT_REGISTER);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_NAME, mName);
        outState.putString(BUNDLE_EMAIL, mEmail);
        outState.putString(BUNDLE_PASSWORD, mPassword);
        outState.putString(BUNDLE_CONFIRM_PASSWORD, mConfirmPassword);
        outState.putString(BUNDLE_UNIVERSITY, mUniversity);
        outState.putBoolean(BUNDLE_PROGRESS_SHOWING, mIsProgressShowing);
    }

}
