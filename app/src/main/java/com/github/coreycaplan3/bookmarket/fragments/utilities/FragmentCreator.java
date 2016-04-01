package com.github.coreycaplan3.bookmarket.fragments.utilities;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.fragments.network.GetNetworkFragment;
import com.github.coreycaplan3.bookmarket.fragments.network.PostNetworkFragment;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys;
import com.github.coreycaplan3.bookmarket.utilities.FragmentKeys.FragmentTag;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class: To streamline fragment creation in a uniform way and reduce code bloat whenever
 * possible.
 */
public final class FragmentCreator {

    private static final int ENTER_ANIMATION = R.anim.right_to_center;
    private static final int EXIT_ANIMATION = R.anim.center_to_left;
    private static final int POP_ENTER_ANIMATION = R.anim.left_to_center;
    private static final int POP_EXIT_ANIMATION = R.anim.center_to_right;

    private FragmentCreator() {
    }

    public static int create(Fragment fragment, @FragmentTag String tag, @IdRes int containerId,
                             FragmentManager fragmentManager) {
        return fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(tag)
                .setCustomAnimations(ENTER_ANIMATION, EXIT_ANIMATION, POP_ENTER_ANIMATION,
                        POP_EXIT_ANIMATION)
                .commit();
    }

    public static int createNetworks(FragmentManager fragmentManager) {
        PostNetworkFragment postNetworkFragment = PostNetworkFragment.newInstance();
        GetNetworkFragment getNetworkFragment = GetNetworkFragment.newInstance();
        return fragmentManager.beginTransaction()
                .add(postNetworkFragment, FragmentKeys.POST_NETWORK_FRAGMENT)
                .add(getNetworkFragment, FragmentKeys.GET_NETWORK_FRAGMENT)
                .commit();
    }

}
