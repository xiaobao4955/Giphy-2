package com.alexeyosadchy.giphy.view.base;

import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.view.screens.trends.TrendGifListActivity;

import java.util.List;

public interface BaseListGifView {

    void showLoading();

    void hideLoading();

    void configurateAdapter();

    void addItemsToList(List<GifView> gifs);

    void removeItem(int position);

    void clearList();

    int getSizeList();

    void updateList(int position);

    void closeApplication();

    void showError(String message, TrendGifListActivity.Callback callback);

    void showMessage(String message);
}
