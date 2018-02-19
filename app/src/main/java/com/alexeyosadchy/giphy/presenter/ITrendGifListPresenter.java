package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.view.screens.trends.ITrendGifListActivity;

public interface ITrendGifListPresenter {

    void onClickMenuItemFavorite();

    void onAttach(ITrendGifListActivity view);

    void onDetach();

    void onBackPressed();

    void onCreateView();

    void onConfigurationChanged(int firstVisiblePosition);

    void loadGifs();

    void onClickFavoriteButton(int position);

    boolean onBindView(int position);

    void onSearchSubmit(String query);
}
