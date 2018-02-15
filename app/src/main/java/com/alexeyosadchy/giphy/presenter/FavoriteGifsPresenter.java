package com.alexeyosadchy.giphy.presenter;

import android.net.Uri;

import com.alexeyosadchy.giphy.model.sharedpreferences.SharedPreferencesHelper;
import com.alexeyosadchy.giphy.model.storage.GifRetainHelper;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.screens.favorite.FavoriteGifListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public final class FavoriteGifsPresenter {

    private FavoriteGifListActivity mView;
    private final List<GifView> mGifViews;
    private final CompositeDisposable mDisposable;
    private final SharedPreferencesHelper preferences;
    private final GifRetainHelper mGifRetainHelper;

    @Inject
    public FavoriteGifsPresenter(CompositeDisposable disposable,
                                 SharedPreferencesHelper sharedPreferencesHelper,
                                 GifRetainHelper gifRetainHelper) {
        mDisposable = disposable;
        preferences = sharedPreferencesHelper;
        mGifRetainHelper = gifRetainHelper;
        mGifViews = new ArrayList<>();
    }

    public void onCreateView() {
        for (GifView gif : preferences.getAllFilePath()) {
            gif.setUri(Uri.fromFile(new File(gif.getLocalePath())).toString());
            mGifViews.add(gif);
        }
        mView.prepareView(mGifViews, 0);
    }

    public void onClickFavoriteButton(int position) {
        deleteGifFromStorage(() -> {
                    preferences.delete(mGifViews.get(position).getSharedPreferencesKey());
                    mGifViews.remove(position);
                    mView.updateList(position);
                },
                preferences.getFilePath(mGifViews.get(position).getSharedPreferencesKey()));
    }

    private void deleteGifFromStorage(Action onComplete, String path) {
        mDisposable.add(mGifRetainHelper.deleteGif(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete,throwable -> mView.showMessage(throwable.getLocalizedMessage())));
    }

    public void onAttach(FavoriteGifListActivity view) {
        mView = view;
    }

    public void onDetach() {
        mView = null;
        mDisposable.dispose();
    }
}
