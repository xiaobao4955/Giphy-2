package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.storage.GifStorage;
import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.view.screens.favorite.FavoriteGifListActivity;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

public final class FavoriteGifsPresenter {

    private FavoriteGifListActivity mView;
    private final CompositeDisposable mDisposable;
    private final GifStorage mGifStorage;

    @Inject
    public FavoriteGifsPresenter(final CompositeDisposable disposable,
                                 final GifStorage gifStorage) {
        mDisposable = disposable;
        mGifStorage = gifStorage;
    }

    public void onCreateView() {
        mView.configurateAdapter();
        mView.addItemsToList(mGifStorage.getAllSavedGifs());
    }

    public void onClickFavoriteButton(final GifView gif, int position) {
        deleteGifFromStorage(() -> mView.removeItem(position), gif);
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
