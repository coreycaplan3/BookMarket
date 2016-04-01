package com.github.coreycaplan3.bookmarket.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.FragmentCreator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkConstants.GetNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkCommunicator;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkConstants.PostNetworkConstraints;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.functionality.UserProfile;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.IntentExtra;
import com.github.coreycaplan3.bookmarket.utilities.Keys;

public class SplashActivity extends AppCompatActivity implements PostNetworkCommunicator,
        GetNetworkCommunicator {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        restoreInstance(savedInstanceState);
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            FragmentCreator.createNetworks(getSupportFragmentManager());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PostNetworkFragment fragment = (PostNetworkFragment) getSupportFragmentManager()
                .findFragmentByTag(FragmentKeys.POST_NETWORK_FRAGMENT);
        String connectionToken = Keys.getUserLoginToken(getApplicationContext());
        fragment.startSilentLoginTask(connectionToken);
    }

    @Override
    public void onPostNetworkTaskComplete(Bundle result,
                                          @PostNetworkConstraints String postConstraints) {
        if (postConstraints.equals(PostNetworkConstants.CONSTRAINT_SILENT_LOGIN)) {
            UserProfile userProfile = result.getParcelable(postConstraints);
            if (userProfile == null) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), TitleActivity.class);
                intent.putExtra(IntentExtra.PROFILE, userProfile);
                startActivity(intent);
                finish();
            }
        } else {
            Log.e(TAG, "onPostNetworkTaskComplete: ", new IllegalArgumentException("Found: " +
                    postConstraints));
        }
    }

    @Override
    public void onGetNetworkTaskComplete(Bundle result,
                                         @GetNetworkConstraints String getConstraints) {

    }
}
