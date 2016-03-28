package com.github.coreycaplan3.bookmarket.activities;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.github.coreycaplan3.bookmarket.functionality.TextBook;

import java.util.ArrayList;

/**
 * Created by Corey on 3/27/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class SearchFragment extends Fragment {

    public interface OnSearchCompleteListener {

        void onSearchComplete(ArrayList<TextBook> queryResults);
    }

    private static final String TAG = SearchFragment.class.getSimpleName();
    private OnSearchCompleteListener mListener;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSearchCompleteListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    public void startTask(String searchQuery) {
        new SearchTask(searchQuery)
                .execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private class SearchTask extends AsyncTask<Void, Void, ArrayList<TextBook>> {

        private final String mSearchQuery;

        private SearchTask(String searchQuery) {
            mSearchQuery = searchQuery;
        }

        @Override
        protected ArrayList<TextBook> doInBackground(Void... params) {
            //todo perform search
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TextBook> textBooks) {
            if (mListener != null) {
                mListener.onSearchComplete(textBooks);
            }
        }
    }

}
