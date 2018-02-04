package com.alexeyosadchy.giphy.di.module;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.alexeyosadchy.giphy.di.ActivityContext;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;
import com.alexeyosadchy.giphy.presenter.TrendGifListPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(@ActivityContext Context context){
        return new LinearLayoutManager(context);
    }

    @Provides
    ITrendGifListPresenter provideITrendGifListPresenter(TrendGifListPresenter presenter) {
        return presenter;
    }
}
