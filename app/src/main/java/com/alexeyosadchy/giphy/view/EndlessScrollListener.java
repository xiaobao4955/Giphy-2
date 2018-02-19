package com.alexeyosadchy.giphy.view;

import android.support.v7.widget.RecyclerView;

import com.alexeyosadchy.giphy.utils.AdapterUtils;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private static final int VISIBLE_THRESHOLD = 3;

    private int previousTotal;
    private boolean loading = true;

    @Override
    public final void onScrolled(final RecyclerView recyclerView, final int dx, final int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dx != 0 || dy != 0) {
            final int visibleItemCount = recyclerView.getChildCount();
            final int totalItemCount = recyclerView.getAdapter().getItemCount();
            final int firstVisibleItem = AdapterUtils.getCurrentRecyclerViewPosition(recyclerView);

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
                onEndList();
                loading = true;
            }
        }
    }

    public abstract void onEndList();
}