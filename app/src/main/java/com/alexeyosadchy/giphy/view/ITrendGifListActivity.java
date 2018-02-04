package com.alexeyosadchy.giphy.view;

import java.util.List;


public interface ITrendGifListActivity {

    void updateList();

    void prepareView(List<GifView> gifs);

    void setTitle(String title);

    void showLoading();

    void hideLoading();

    boolean isNetworkConnected();

    CharSequence getSearchQuery();

    boolean isSearchActive();

    void showError(String message, TrendGifListActivity.Callback callback);

    interface Callback {
        void call();
    }
}
