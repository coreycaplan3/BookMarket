package com.github.coreycaplan3.bookmarket.adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.coreycaplan3.bookmarket.R;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public class TextBookViewHolder extends ViewHolder implements View.OnClickListener {

    public interface OnViewHolderClickListener {

        void onViewHolderClicked(int layoutPosition);
    }

    private CardView mBackgroundCardView;
    private TextView mTitleTextView;
    private TextView mAuthorTextView;
    private TextView mConditionTextView;
    private TextView mPriceTextView;
    private ImageView mBookImageView;

    @Nullable
    private OnViewHolderClickListener mClickListener;

    public TextBookViewHolder(View itemView, @Nullable OnViewHolderClickListener listener) {
        super(itemView);
        mClickListener = listener;
        mBackgroundCardView = (CardView) itemView.findViewById(R.id.recycler_book_background_card);
        mTitleTextView = (TextView) itemView.findViewById(R.id.recycler_book_title_text_view);
        mAuthorTextView = (TextView) itemView.findViewById(R.id.recycler_book_author_text_view);
        mConditionTextView = (TextView) itemView.findViewById(R.id.recycler_book_condition_text_view);
        mPriceTextView = (TextView) itemView.findViewById(R.id.recycler_book_price_text_view);
        mBookImageView = (ImageView) itemView.findViewById(R.id.recycler_book_image_view);

        mBackgroundCardView.setOnClickListener(this);
    }

    public CardView getBackgroundCardView() {
        return mBackgroundCardView;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public TextView getAuthorTextView() {
        return mAuthorTextView;
    }

    public TextView getConditionTextView() {
        return mConditionTextView;
    }

    public TextView getPriceTextView() {
        return mPriceTextView;
    }

    public ImageView getBookImageView() {
        return mBookImageView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.recycler_book_background_card && mClickListener != null) {
            mClickListener.onViewHolderClicked(getLayoutPosition());
        }
    }
}
