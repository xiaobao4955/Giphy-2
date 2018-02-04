package com.alexeyosadchy.giphy.di.module;

import android.content.Context;

import com.alexeyosadchy.giphy.App;
import com.alexeyosadchy.giphy.di.ApplicationContext;
import com.alexeyosadchy.giphy.model.api.ApiManager;
import com.alexeyosadchy.giphy.model.api.ApiProcessingManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Context mContext;

    public ApplicationModule(App context) {
        this.mContext = context;
    }

    @Singleton
    @Provides
    @ApplicationContext
    Context provideAppContext() {
        return mContext;
    }

    @Singleton
    @Provides
    ApiManager provideApiManager(ApiProcessingManager apiProcessingManager) {
        return apiProcessingManager;
    }
}
