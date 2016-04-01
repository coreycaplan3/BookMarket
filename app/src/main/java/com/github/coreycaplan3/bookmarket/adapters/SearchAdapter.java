package com.github.coreycaplan3.bookmarket.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Corey on 4/1/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class SearchAdapter extends RecyclerView.Adapter {

    private ArrayList<TextBook> mBookList;

    public SearchAdapter(ArrayList<TextBook> bookList) {
        mBookList = bookList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SearchViewHolder) holder).mSearchTextView.setText(mBookList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mBookList == null ? 0 : mBookList.size();
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder {

        private TextView mSearchTextView;

        public SearchViewHolder(View itemView) {
            super(itemView);

            mSearchTextView = (TextView) itemView.findViewById(R.id.search_item_title_text_view);
        }
    }

}
