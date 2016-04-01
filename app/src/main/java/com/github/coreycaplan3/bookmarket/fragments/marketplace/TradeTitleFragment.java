package com.github.coreycaplan3.bookmarket.fragments.marketplace;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.bookmarket.R;

/**
 * Created by Corey on 3/27/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class TradeTitleFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = TradeTitleFragment.class.getSimpleName();
    private View.OnClickListener mClickListener;

    public static TradeTitleFragment newInstance() {
        return new TradeTitleFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickListener = (View.OnClickListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade_title, container, false);
        view.findViewById(R.id.trade_title_find_card).setOnClickListener(this);
        view.findViewById(R.id.trade_title_post_card).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        mClickListener.onClick(v);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClickListener = null;
    }
}
