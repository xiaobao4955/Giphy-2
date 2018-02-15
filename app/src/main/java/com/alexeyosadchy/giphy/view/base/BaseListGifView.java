package com.alexeyosadchy.giphy.view.base;

import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.screens.trends.TrendGifListActivity;

import java.util.List;

public interface BaseListGifView {

    void showLoading();

    void hideLoading();

    void prepareView(List<GifView> gifs, int position);

    void updateList();

    void updateList(int position);

    void closeApplication();

    void showError(String message, TrendGifListActivity.Callback callback);

    void showMessage(String message);
}
