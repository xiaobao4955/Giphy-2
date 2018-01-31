package com.alexeyosadchy.giphy.di.module;

import android.content.Context;

import com.alexeyosadchy.giphy.App;
import com.alexeyosadchy.giphy.di.ApplicationContext;

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
}
