package com.github.coreycaplan3.bookmarket.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.bookmarket.R;

/**
 * Created by Corey on 3/26/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class TitleFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = TitleFragment.class.getSimpleName();

    private View.OnClickListener mClickListener;

    public static TitleFragment newInstance() {
        return new TitleFragment();
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
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        view.findViewById(R.id.title_buy_card).setOnClickListener(this);
        view.findViewById(R.id.title_trade_card).setOnClickListener(this);
        view.findViewById(R.id.title_account_card).setOnClickListener(this);
        view.findViewById(R.id.title_sell_card).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onClick(v);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mClickListener = null;
    }
}
