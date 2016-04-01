package com.github.coreycaplan3.bookmarket.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;

import java.util.ArrayList;

/**
 * Created by Corey on 4/1/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class DesiredTradesAdapter extends AnimationAdapter {

    public interface OnDesiredTradeClickListener {

        void onDesiredTradeClicked(TextBook textBook, boolean shouldDelete, boolean shouldEdit);
    }

    private static final String TAG = DesiredTradesAdapter.class.getSimpleName();

    private ArrayList<TextBook> mBookList;
    private OnDesiredTradeClickListener mListener;

    public DesiredTradesAdapter(ArrayList<TextBook> bookList, OnDesiredTradeClickListener listener,
                                Context context) {
        super(context);
        mBookList = bookList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_desired_item, parent, false);
        return new DesiredTradesViewHolder(view);
    }

    @Override
    public void onBindAnimationViewHolder(ViewHolder holder, int position) {
        DesiredTradesViewHolder viewHolder = (DesiredTradesViewHolder) holder;
        TextBook textBook = mBookList.get(position);
        viewHolder.mTitleTextView.setText(textBook.getTitle());
        viewHolder.mAuthorTextView.setText(textBook.getAuthor());
        viewHolder.mIsbnTextView.setText(textBook.getIsbn());
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }

    public void onDesiredTradeDeleteSuccessful(int position) {
        mBookList.remove(position);
        notifyItemRemoved(position);
    }

    public void onDesiredTradeEditSuccessful(int position, TextBook textBook) {
        mBookList.remove(position);
        mBookList.add(position, textBook);
        notifyItemChanged(position);
    }

    private class DesiredTradesViewHolder extends ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mAuthorTextView;
        private TextView mIsbnTextView;

        public DesiredTradesViewHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView
                    .findViewById(R.id.recycler_desired_title_text_view);
            mAuthorTextView = (TextView) itemView
                    .findViewById(R.id.recycler_desired_author_text_view);
            mIsbnTextView = (TextView) itemView
                    .findViewById(R.id.recycler_desired_isbn_text_view);

            itemView.findViewById(R.id.recycler_desired_edit_image_view).setOnClickListener(this);
            itemView.findViewById(R.id.recycler_desired_delete_image_view).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (mListener == null) {
                Log.e(TAG, "onClick: ", new NullPointerException("Listener was null!"));
                return;
            }
            if (id == R.id.recycler_desired_edit_image_view) {
                mListener.onDesiredTradeClicked(mBookList.get(getLayoutPosition()), false, true);
            } else if (id == R.id.recycler_desired_delete_image_view) {
                mListener.onDesiredTradeClicked(mBookList.get(getLayoutPosition()), true, false);
            }
        }
    }

}
