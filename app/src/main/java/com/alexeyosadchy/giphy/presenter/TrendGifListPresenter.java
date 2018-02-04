package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.ApiManager;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.ITrendGifListActivity;
import com.alexeyosadchy.giphy.view.TrendGifListActivity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TrendGifListPresenter implements ITrendGifListPresenter {

    private static final int LIMIT_RECORDS = 12;

    private ITrendGifListActivity mView;
    private ApiManager mApiManager;
    private List<GifView> mGifViews;

    @Inject
    public TrendGifListPresenter(ApiManager apiManager) {
        mApiManager = apiManager;
        mGifViews = new ArrayList<>();
    }

    @Override
    public void loadGifs() {
        load();
    }

    @Override
    public void onCreateView() {
        mGifViews.clear();
        mView.prepareView(mGifViews);
        load();
    }

    private void load() {
        mView.showLoading();
        Consumer<List<GifView>> onNext = gifViews -> {
            mGifViews.addAll(gifViews);
            mView.updateList(0);
            mView.hideLoading();
        };
        Consumer<Throwable> onError = throwable -> errorHandling(throwable, this::loadGifs);
        if (mView.isSearchModeActive()) {
            getFoundGifs(onNext, onError, mView.getSearchQuery());
        } else {
            getTrendingGifs(onNext, onError);
        }
    }

    @Override
    public void onSearchSubmit(String query) {
        if (!mView.isSearchModeActive()) {
            mView.switchSearchMode();
        }
        onCreateView();
    }

    private void getFoundGifs(Consumer<? super List<GifView>> onNext, Consumer<? super Throwable> onError, String query) {
        mApiManager.search(query, LIMIT_RECORDS, mGifViews.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Mapper::transform)
                .subscribe(onNext, onError);
    }

    private void getTrendingGifs(Consumer<? super List<GifView>> onNext, Consumer<? super Throwable> onError) {
        mApiManager.getTrendingGifs(LIMIT_RECORDS, mGifViews.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Mapper::transform)
                .subscribe(onNext, onError);
    }

    @Override
    public void onAttach(ITrendGifListActivity view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
    }

    private void errorHandling(Throwable t, TrendGifListActivity.Callback callback) {
        mView.hideLoading();
        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            mView.showError("There is no internet connection", callback);
            t.printStackTrace();
        } else {
            mView.showError(t.getLocalizedMessage(), callback);
        }
    }

    @Override
    public void onBackPressed() {
        if (mView.isSearchModeActive()) {
            mView.switchSearchMode();
            onCreateView();
        } else {
            mView.closeApplication();
        }
    }
}
