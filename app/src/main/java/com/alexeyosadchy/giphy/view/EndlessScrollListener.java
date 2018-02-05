package com.alexeyosadchy.giphy.view;

import android.support.v7.widget.RecyclerView;

import com.alexeyosadchy.giphy.utils.AdapterUtils;

public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessScrollListener.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 3; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private RecyclerView.LayoutManager mLayoutManager;

    public EndlessScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dx != 0 || dy != 0) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = recyclerView.getAdapter().getItemCount();
            firstVisibleItem = AdapterUtils.getCurrentRecyclerViewPosition(recyclerView);

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onLoadMore();
                loading = true;
            }
        }
    }

    public abstract void onLoadMore();
}