package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.view.ITrendGifListActivity;

public interface ITrendGifListPresenter {

    void onAttach(ITrendGifListActivity view);
    void onDetach();

    void onBackPressed();

    void onCreateView();

    void onConfigurationChanged(int firstVisiblePosition);

    void onLongClickItem(int position);

    void loadGifs();

    void onSearchSubmit(String query);
}
