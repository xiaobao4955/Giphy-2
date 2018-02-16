package com.alexeyosadchy.giphy.presenter;

import android.net.Uri;

import com.alexeyosadchy.giphy.model.storage.GifStorage;
import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.view.screens.favorite.FavoriteGifListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

public final class FavoriteGifsPresenter {

    private FavoriteGifListActivity mView;
    private final List<GifView> mGifViews;
    private final CompositeDisposable mDisposable;
    private final GifStorage mGifStorage;

    @Inject
    public FavoriteGifsPresenter(final CompositeDisposable disposable,
                                 final GifStorage gifStorage) {
        mDisposable = disposable;
        mGifStorage = gifStorage;
        mGifViews = new ArrayList<>();
    }

    public void onCreateView() {
        for (final GifView gif : mGifStorage.getAllSavedGifs()) {
            gif.setUri(Uri.fromFile(new File(gif.getLocalePath())).toString());
            mGifViews.add(gif);
        }
        mView.prepareView(mGifViews, 0);
    }

    public void onClickFavoriteButton(final int position) {
        final GifView gif = mGifViews.get(position);
        deleteGifFromStorage(() -> {
            mGifViews.remove(position);
            mView.updateList(position);
        }, gif);
    }

    private void deleteGifFromStorage(final Action onComplete, final GifView gif) {
        mDisposable.add(mGifStorage.deleteGif(gif)
                .subscribe(onComplete, throwable -> mView.showMessage(throwable.getLocalizedMessage())));
    }

    public void onAttach(final FavoriteGifListActivity view) {
        mView = view;
    }

    public void onDetach() {
        mView = null;
        mDisposable.dispose();
    }
}
