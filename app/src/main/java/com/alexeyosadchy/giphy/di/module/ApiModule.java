package com.alexeyosadchy.giphy.di.module;

import android.content.Context;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.di.ApplicationContext;
import com.alexeyosadchy.giphy.model.api.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public final class ApiModule {

    @Provides
    @Singleton
    Retrofit provideRestAdapter(@ApplicationContext final Context context) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(context.getString(R.string.api_url))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(final Retrofit restAdapter) {
        return restAdapter.create(ApiService.class);
    }
}
