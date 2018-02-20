package com.alexeyosadchy.giphy.view.screens.trends;

import com.alexeyosadchy.giphy.view.base.BaseListGifView;


public interface ITrendGifListActivity extends BaseListGifView {

    void configurateAdapter();

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
