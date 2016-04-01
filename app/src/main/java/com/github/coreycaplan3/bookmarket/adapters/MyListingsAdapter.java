package com.github.coreycaplan3.bookmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.adapters.TextBookViewHolder.OnViewHolderClickListener;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class MyListingsAdapter extends Adapter<TextBookViewHolder> implements
        OnViewHolderClickListener {

    private static final String TAG = MyListingsAdapter.class.getSimpleName();
    private boolean mIsSelling;
    private boolean mIsNewTextBook;
    private ArrayList<TextBook> mTextBooks;
    private MyTextBookClickListener mClickListener;

    public MyListingsAdapter(@NonNull ArrayList<TextBook> textBooks, MyTextBookClickListener listener,
                             boolean isSelling, boolean isNewTextBook) {
        mTextBooks = textBooks;
        mClickListener = listener;
        mIsSelling = isSelling;
        mIsNewTextBook = isNewTextBook;
    }

    @Override
    public TextBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_book_item, parent, false);
        return new TextBookViewHolder(view, this, mIsSelling);
    }

    @Override
    public void onBindViewHolder(TextBookViewHolder holder, int position) {
        TextBook textBook = mTextBooks.get(position);
        holder.getTitleTextView().setText(textBook.getTitle());
        holder.getAuthorTextView().setText(textBook.getAuthor());
        holder.getConditionTextView().setText(textBook.getCondition());
        if (mIsSelling) {
            holder.getPriceTextView().setText(String.format(Locale.US, "$%.2f",
                    textBook.getPrice()));
        }
        holder.getBookImageView().setImageBitmap(textBook.getPicture());
    }

    @Override
    public int getItemCount() {
        return mTextBooks.size();
    }

    @Override
    public void onViewHolderClicked(int layoutPosition) {
        if (mClickListener != null) {
            mClickListener.onTextBookClicked(mTextBooks.get(layoutPosition), mIsNewTextBook);
        } else {
            Log.e(TAG, "onViewHolderClicked: ", new NullPointerException("ClickListener cannot be" +
                    " null!"));
        }
    }

}
