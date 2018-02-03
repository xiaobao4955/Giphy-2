package com.alexeyosadchy.giphy.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.alexeyosadchy.giphy.App;
import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.di.component.ActivityComponent;
import com.alexeyosadchy.giphy.di.component.DaggerActivityComponent;
import com.alexeyosadchy.giphy.di.module.ActivityModule;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;
import com.alexeyosadchy.giphy.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

public class TrendGifListActivity extends AppCompatActivity implements ITrendGifListActivity {

    private ActivityComponent mActivityComponent;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GifListAdapter mGifListAdapter;

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
        presenter.onCreateView();
    }

    @Override
    public void updateList(){
        mGifListAdapter.notifyDataSetChanged();
    }

    @Override
    public void prepareView(List<GifView> gifs) {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_gifs);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mGifListAdapter = new GifListAdapter(gifs);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mGifListAdapter);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                presenter.loadGifs();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trend_gif_list_activity, menu);
        SearchView searchItem = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                presenter.onSearchSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }
}