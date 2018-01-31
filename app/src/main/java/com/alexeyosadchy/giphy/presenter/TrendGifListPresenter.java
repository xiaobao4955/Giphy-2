package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.view.ITrendGifListActivity;

import javax.inject.Inject;

public class TrendGifListPresenter implements ITrendGifListPresenter {

    private ITrendGifListActivity mView;

    @Inject
    public TrendGifListPresenter() {

    }

    @Override
    public void onAttach(ITrendGifListActivity view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
    }
}
