package com.alexeyosadchy.giphy.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.alexeyosadchy.giphy.di.ActivityContext;
import com.alexeyosadchy.giphy.di.ApplicationContext;
import com.alexeyosadchy.giphy.model.sharedpreferences.SharedPreferencesHelper;
import com.alexeyosadchy.giphy.model.storage.GifStorage;
import com.alexeyosadchy.giphy.presenter.FavoriteGifsPresenter;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;
import com.alexeyosadchy.giphy.presenter.TrendGifListPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public final class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(final AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable(){
        return new CompositeDisposable();
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    GifStorage proGifRetainHelper(@ApplicationContext final Context context){
        return new GifStorage(context);
    }

    @Provides
    ITrendGifListPresenter provideITrendGifListPresenter(final TrendGifListPresenter presenter) {
        return presenter;
    }

    @Provides
    FavoriteGifsPresenter provideFavoriteGifsPresenter(final CompositeDisposable compositeDisposable,
                                                       final SharedPreferencesHelper sharedPreferencesHelper,
                                                       final GifStorage gifStorage) {
        return new FavoriteGifsPresenter(compositeDisposable, sharedPreferencesHelper, gifStorage);
    }
}
