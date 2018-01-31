package com.alexeyosadchy.giphy.di.component;

import com.alexeyosadchy.giphy.di.PerActivity;
import com.alexeyosadchy.giphy.di.module.ActivityModule;
import com.alexeyosadchy.giphy.view.TrendGifListActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(TrendGifListActivity activity);
}
