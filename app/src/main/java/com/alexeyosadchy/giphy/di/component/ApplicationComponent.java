package com.alexeyosadchy.giphy.di.component;

import com.alexeyosadchy.giphy.di.module.ApiModule;
import com.alexeyosadchy.giphy.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {
}
