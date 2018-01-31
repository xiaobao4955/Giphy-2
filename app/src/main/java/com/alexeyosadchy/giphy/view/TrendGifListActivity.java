package com.alexeyosadchy.giphy.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alexeyosadchy.giphy.App;
import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.di.component.ActivityComponent;
import com.alexeyosadchy.giphy.di.component.DaggerActivityComponent;
import com.alexeyosadchy.giphy.di.module.ActivityModule;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;

import javax.inject.Inject;

public class TrendGifListActivity extends AppCompatActivity implements ITrendGifListActivity {

    private ActivityComponent mActivityComponent;

    @Inject
    ITrendGifListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_gif_list);

        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        mActivityComponent.inject(this);

        presenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
