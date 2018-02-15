package com.alexeyosadchy.giphy.view;

import android.support.v7.widget.RecyclerView;

import com.alexeyosadchy.giphy.utils.AdapterUtils;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal;
    private boolean loading = true;

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dx != 0 || dy != 0) {
            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = recyclerView.getAdapter().getItemCount();
            int firstVisibleItem = AdapterUtils.getCurrentRecyclerViewPosition(recyclerView);

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            int visibleThreshold = 3;
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onEndList();
                loading = true;
            }
        }
    }

    public abstract void onEndList();
}