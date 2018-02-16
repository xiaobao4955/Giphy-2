package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.ApiManager;
import com.alexeyosadchy.giphy.model.sharedpreferences.SharedPreferencesHelper;
import com.alexeyosadchy.giphy.model.storage.GifStorage;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.screens.trends.ITrendGifListActivity;
import com.alexeyosadchy.giphy.view.screens.trends.TrendGifListActivity;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public final class TrendGifListPresenter implements ITrendGifListPresenter {

    private static final int LIMIT_RECORDS = 10;

    private ITrendGifListActivity mView;
    private final ApiManager mApiManager;
    private final List<GifView> mGifViews;
    private final CompositeDisposable mDisposable;
    private final GifStorage mGifStorage;
    private final SharedPreferencesHelper preferences;

    @Inject
    TrendGifListPresenter(final ApiManager apiManager,
                          final CompositeDisposable disposable,
                          final GifStorage gifStorage,
                          final SharedPreferencesHelper sharedPreferencesHelper) {
        mApiManager = apiManager;
        mDisposable = disposable;
        mGifStorage = gifStorage;
        preferences = sharedPreferencesHelper;
        mGifViews = new ArrayList<>();
    }

    @Override
    public void onConfigurationChanged(final int firstVisiblePosition) {
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
        final Consumer<List<GifView>> onNext = gifViews -> {
            mGifViews.addAll(gifViews);
            mView.updateList();
            mView.hideLoading();
        };
        final Consumer<Throwable> onError = throwable -> errorHandling(throwable, this::loadGifs);
        if (mView.isSearchModeActive()) {
            getFoundGifs(onNext, onError, mView.getSearchQuery());
        } else {
            getTrendingGifs(onNext, onError);
        }
    }

    @Override
    public void onClickFavoriteButton(final int position) {
        final String key = mGifViews.get(position).getUri();
        if (preferences.hasContainKey(key)) {
            deleteGifFromStorage(() -> {
                        preferences.delete(key);
                        mView.updateList(position);
                    },
                    preferences.getFilePath(key));
        } else {
            saveGifToLocalStorage(path -> {
                        mGifViews.get(position).setLocalePath(path);
                        preferences.put(mGifViews.get(position));
                        mView.updateList(position);
                    },
                    key);
        }
    }

    @Override
    public boolean onBindView(final int position) {
        return preferences.hasContainKey(mGifViews.get(position).getUri());
    }

    @Override
    public void onSearchSubmit(final String query) {
        if (!mView.isSearchModeActive()) {
            mView.switchSearchMode();
        }
        onCreateView();
    }

    @Override
    public void onClickMenuItemFavorite() {
        mView.navigateToFavoriteGifsActivity();
    }

    private void deleteGifFromStorage(final Action onComplete, final String path) {
        mDisposable.add(mGifStorage.deleteGif(path)
                .subscribe(onComplete, throwable -> mView.showMessage(throwable.getLocalizedMessage())));
    }

    private void saveGifToLocalStorage(final Consumer<? super String> onNext, final String uri) {
        mDisposable.add(mGifStorage.saveGif(uri)
                .subscribe(onNext, throwable -> mView.showMessage(throwable.getLocalizedMessage())));
    }

    private void getFoundGifs(final Consumer<? super List<GifView>> onNext, final Consumer<? super Throwable> onError, final String query) {
        mDisposable.add(mApiManager.search(query, LIMIT_RECORDS, mGifViews.size())
                .map(Mapper::transform)
                .subscribe(onNext, onError));
    }

    private void getTrendingGifs(final Consumer<? super List<GifView>> onNext, final Consumer<? super Throwable> onError) {
        mDisposable.add(mApiManager.getTrendingGifs(LIMIT_RECORDS, mGifViews.size())
                .map(Mapper::transform)
                .subscribe(onNext, onError));
    }

    @Override
    public void onAttach(final ITrendGifListActivity view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
        mDisposable.dispose();
    }

    private void errorHandling(final Throwable t, final TrendGifListActivity.Callback callback) {
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
