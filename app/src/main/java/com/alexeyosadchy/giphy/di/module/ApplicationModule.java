package com.alexeyosadchy.giphy.di.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.alexeyosadchy.giphy.App;
import com.alexeyosadchy.giphy.di.ApplicationContext;
import com.alexeyosadchy.giphy.model.api.ApiManager;
import com.alexeyosadchy.giphy.model.api.ApiProcessingManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class ApplicationModule {

    private static final String GIF_FILE_PREFERENCES_KEY = "com.alexeyosadchy.giphy.GIFS";

    private final Context mContext;

    public ApplicationModule(final App context) {
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
    ApiManager provideApiManager(final ApiProcessingManager apiProcessingManager) {
        return apiProcessingManager;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(@ApplicationContext final Context context) {
        return context.getSharedPreferences(GIF_FILE_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }
}
