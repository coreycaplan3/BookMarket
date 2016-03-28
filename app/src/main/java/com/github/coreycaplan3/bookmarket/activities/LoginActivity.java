package com.github.coreycaplan3.bookmarket.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.account.FragmentLogin;

import static com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.*;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setInitialFragment(savedInstanceState);
    }

    private void setInitialFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());
            FragmentLogin fragment = FragmentLogin.newInstance();
            FragmentCreator.create(fragment, LOGIN_FRAGMENT, R.id.login_container,
                    getSupportFragmentManager());
        } else {
            //todo restore anything
        }
    }

}
