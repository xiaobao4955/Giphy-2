package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.view.screens.trends.ITrendGifListActivity;

public interface ITrendGifListPresenter {

    void onClickMenuItemFavorite();

    void onAttach(ITrendGifListActivity view);

    void onDetach();

    void onBackPressed();

    void onCreateView();

    void loadGifs();

    void onClickFavoriteButton(GifView gif, int position);

    boolean onBindView(GifView gif);

    void onSearchSubmit(String query);
}
