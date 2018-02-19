package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.storage.GifStorage;
import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.view.screens.favorite.FavoriteGifListActivity;

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
        mGifViews = new ArrayList<>(mGifStorage.getAllSavedGifs());
    }

    public void onCreateView() {
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
