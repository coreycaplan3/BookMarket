package com.github.coreycaplan3.bookmarket.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Corey on 3/31/2016.
 * Project: BookMarket
 * <p></p>
 * Purpose of Class:
 */
public abstract class AnimationAdapter<VH extends RecyclerView.ViewHolder> extends Adapter {

    private Context mContext;
    private int mLastLayoutPosition = 0;

    private static int sScreenWidth;

    private static final long ANIMATION_DURATION = 300L;

    public AnimationAdapter(Context context) {
        mContext = context;
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mLastLayoutPosition) {
            return;
        }
        mLastLayoutPosition = position;
        View view = holder.itemView;
        view.setTranslationX(getScreenWidth(view.getContext()));
        view.animate()
                .setDuration(ANIMATION_DURATION)
                .translationX(0)
                .start();
    }

    private static int getScreenWidth(Context context) {
        if (sScreenWidth == 0) {
            WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = window.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            sScreenWidth = size.x;
        }
        return sScreenWidth;
    }

    @Override
    public final void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        holder.itemView.clearAnimation();
        onAnimationViewHolderDetachedFromWindow((VH) holder);
    }

    abstract void onBindAnimationViewHolder(VH holder, int position);

    public void onAnimationViewHolderDetachedFromWindow(VH viewHolder) {

    }
}
