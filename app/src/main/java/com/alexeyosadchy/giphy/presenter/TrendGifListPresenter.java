package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.ApiManager;
import com.alexeyosadchy.giphy.model.sharedpreferences.SharedPreferencesHelper;
import com.alexeyosadchy.giphy.model.storage.GifRetainHelper;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.screens.trends.ITrendGifListActivity;
import com.alexeyosadchy.giphy.view.screens.trends.TrendGifListActivity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public final class TrendGifListPresenter implements ITrendGifListPresenter {

    private static final int LIMIT_RECORDS = 10;

    private ITrendGifListActivity mView;
    private final ApiManager mApiManager;
    private final List<GifView> mGifViews;
    private final CompositeDisposable mDisposable;
    private final GifRetainHelper mGifRetainHelper;
    private final SharedPreferencesHelper preferences;

    @Inject
    TrendGifListPresenter(ApiManager apiManager,
                          CompositeDisposable disposable,
                          GifRetainHelper gifRetainHelper,
                          SharedPreferencesHelper sharedPreferencesHelper) {
        mApiManager = apiManager;
        mDisposable = disposable;
        mGifRetainHelper = gifRetainHelper;
        preferences = sharedPreferencesHelper;
        mGifViews = new ArrayList<>();
    }

    @Override
    public void onConfigurationChanged(int firstVisiblePosition) {
        mView.prepareView(mGifViews, firstVisiblePosition);
    }

    @Override
    public void loadGifs() {
        load();
    }

    @Override
    public void onCreateView() {
        mGifViews.clear();
        mView.prepareView(mGifViews, 0);
        load();
    }

    private void load() {
        mView.showLoading();
        Consumer<List<GifView>> onNext = gifViews -> {
            mGifViews.addAll(gifViews);
            mView.updateList();
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
    public void onClickFavoriteButton(int position) {
        if (preferences.hasContainKey(mGifViews.get(position).getUri())) {

            deleteGifFromStorage(() -> {
                        preferences.delete(mGifViews.get(position).getUri());
                        mView.updateList(position);
                    },
                    preferences.getFilePath(mGifViews.get(position).getUri()));
        } else {
            saveGifToLocalStorage(path -> {
                        mGifViews.get(position).setLocalePath(path);
                        preferences.put(mGifViews.get(position));
                        mView.updateList(position);
                    },
                    mGifViews.get(position).getUri());
        }
    }

    @Override
    public boolean onBindView(int position) {
        return preferences.hasContainKey(mGifViews.get(position).getUri());
    }

    @Override
    public void onSearchSubmit(String query) {
        if (!mView.isSearchModeActive()) {
            mView.switchSearchMode();
        }
        onCreateView();
    }

    @Override
    public void onClickMenuItemFavorite() {
        mView.navigateToFavoriteGifsActivity();
    }

    private void deleteGifFromStorage(Action onComplete, String path) {
        mDisposable.add(mGifRetainHelper.deleteGif(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete, throwable -> mView.showMessage(throwable.getLocalizedMessage())));
    }

    private void saveGifToLocalStorage(Consumer<? super String> onNext, String uri) {
        mDisposable.add(mGifRetainHelper.saveGif(uri)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> mView.showMessage(throwable.getLocalizedMessage())));
    }

    private void getFoundGifs(Consumer<? super List<GifView>> onNext, Consumer<? super Throwable> onError, String query) {
        mDisposable.add(mApiManager.search(query, LIMIT_RECORDS, mGifViews.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Mapper::transform)
                .subscribe(onNext, onError));
    }

    private void getTrendingGifs(Consumer<? super List<GifView>> onNext, Consumer<? super Throwable> onError) {
        mDisposable.add(mApiManager.getTrendingGifs(LIMIT_RECORDS, mGifViews.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(Mapper::transform)
                .subscribe(onNext, onError));
    }

    @Override
    public void onAttach(ITrendGifListActivity view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
        mDisposable.dispose();
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
