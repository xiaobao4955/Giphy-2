package com.alexeyosadchy.giphy.di.component;

import com.alexeyosadchy.giphy.di.PerActivity;
import com.alexeyosadchy.giphy.di.module.ActivityModule;
import com.alexeyosadchy.giphy.view.screens.trends.TrendGifListActivity;
import com.alexeyosadchy.giphy.view.screens.favorite.FavoriteGifListActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(TrendGifListActivity activity);

    void inject(FavoriteGifListActivity activity);
}
