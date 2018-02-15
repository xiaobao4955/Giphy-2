package com.alexeyosadchy.giphy.view.screens.trends;

import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.base.BaseListGifView;

import java.util.List;


public interface ITrendGifListActivity extends BaseListGifView {

    void prepareView(List<GifView> gifs, int position);

    String getSearchQuery();

    boolean isSearchModeActive();

    void closeApplication();

    void switchSearchMode();

    void navigateToFavoriteGifsActivity();

    void endScroll();

    interface Callback {
        void call();
    }
}
