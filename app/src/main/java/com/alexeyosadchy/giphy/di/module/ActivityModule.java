package com.alexeyosadchy.giphy.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.alexeyosadchy.giphy.di.ActivityContext;
import com.alexeyosadchy.giphy.di.ApplicationContext;
import com.alexeyosadchy.giphy.model.sharedpreferences.SharedPreferencesHelper;
import com.alexeyosadchy.giphy.model.storage.GifRetainHelper;
import com.alexeyosadchy.giphy.presenter.FavoriteGifsPresenter;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;
import com.alexeyosadchy.giphy.presenter.TrendGifListPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public final class ActivityModule {

    private final AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity mActivity) {
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
    GifRetainHelper proGifRetainHelper(@ApplicationContext Context context){
        return new GifRetainHelper(context);
    }

    @Provides
    ITrendGifListPresenter provideITrendGifListPresenter(TrendGifListPresenter presenter) {
        return presenter;
    }

    @Provides
    FavoriteGifsPresenter provideFavoriteGifsPresenter(CompositeDisposable compositeDisposable,
                                                       SharedPreferencesHelper sharedPreferencesHelper,
                                                       GifRetainHelper gifRetainHelper) {
        return new FavoriteGifsPresenter(compositeDisposable, sharedPreferencesHelper, gifRetainHelper);
    }
}
