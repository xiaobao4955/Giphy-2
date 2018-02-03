package com.alexeyosadchy.giphy;

import android.app.Application;

import com.alexeyosadchy.giphy.di.component.ApplicationComponent;
import com.alexeyosadchy.giphy.di.component.DaggerApplicationComponent;
import com.alexeyosadchy.giphy.di.module.ApplicationModule;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Application class.
 */

public class App extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        Fresco.initialize(this);
        Fresco.getImagePipeline().clearCaches();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
