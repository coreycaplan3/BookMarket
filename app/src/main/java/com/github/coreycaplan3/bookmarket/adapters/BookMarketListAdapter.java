package com.github.coreycaplan3.bookmarket.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.coreycaplan3.bookmarket.R;
import com.github.coreycaplan3.bookmarket.functionality.TextBook;

import com.github.coreycaplan3.bookmarket.adapters.TextBookViewHolder.OnViewHolderClickListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class BookMarketListAdapter extends AnimationAdapter<TextBookViewHolder> implements
        OnViewHolderClickListener {

    public interface TextBookClickListener {

        void onTextBookClicked(TextBook textBook);
    }

    private ArrayList<TextBook> mTextBooks;
    private TextBookClickListener mClickListener;

    public BookMarketListAdapter(Context context, @NonNull ArrayList<TextBook> textBooks,
                                 @NonNull TextBookClickListener listener) {
        super(context);
        mTextBooks = textBooks;
        mClickListener = listener;
    }

    @Override
    public TextBookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_book_item, parent, false);
        return new TextBookViewHolder(view, this);
    }

    @Override
    public void onBindAnimationViewHolder(TextBookViewHolder holder, int position) {
        TextBook textBook = mTextBooks.get(position);
        holder.getTitleTextView().setText(textBook.getTitle());
        holder.getAuthorTextView().setText(textBook.getAuthor());
        holder.getConditionTextView().setText(textBook.getCondition());
        holder.getPriceTextView().setText(String.format(Locale.US, "$%.2f", textBook.getPrice()));
        holder.getBookImageView().setImageBitmap(textBook.getPicture());
    }

    @Override
    public int getItemCount() {
        return mTextBooks.size();
    }

    @Override
    public void onViewHolderClicked(int layoutPosition) {
        mClickListener.onTextBookClicked(mTextBooks.get(layoutPosition));
    }

}
