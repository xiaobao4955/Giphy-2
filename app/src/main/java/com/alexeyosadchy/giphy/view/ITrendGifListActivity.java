package com.alexeyosadchy.giphy.view;

import java.util.List;


public interface ITrendGifListActivity {

    void updateList(int position);

    void prepareView(List<GifView> gifs);

    void showLoading();

    void hideLoading();

    boolean isNetworkConnected();

    String getSearchQuery();

    boolean isSearchModeActive();

    void onBackPressed();

    void closeApplication();

    void switchSearchMode();

    void showError(String message, TrendGifListActivity.Callback callback);

    interface Callback {
        void call();
    }
}
