package com.alexeyosadchy.giphy.di.component;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexeyosadchy.giphy.di.ApplicationContext;
import com.alexeyosadchy.giphy.di.module.ApiModule;
import com.alexeyosadchy.giphy.di.module.ApplicationModule;
import com.alexeyosadchy.giphy.model.api.ApiManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context getContext();

    ApiManager getApiManager();

    SharedPreferences getSharedPreferences();
}
