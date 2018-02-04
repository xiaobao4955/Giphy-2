package com.alexeyosadchy.giphy.view;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private ProgressBar mProgressBar;
    private SearchView searchItem;

    @Inject
    ITrendGifListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_gif_list);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_gifs);
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        mActivityComponent.inject(this);

        presenter.onAttach(this);
        presenter.onCreateView();
    }

    @Override
    public void updateList() {
        mGifListAdapter.notifyDataSetChanged();
    }

    @Override
    public void prepareView(List<GifView> gifs) {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGifListAdapter = new GifListAdapter(gifs, mLinearLayoutManager.getWidth());
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
        searchItem = (SearchView) menu.findItem(R.id.action_search).getActionView();
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

    @Override
    public void setTitle(String title) {
        setTitle(title);
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public CharSequence getSearchQuery() {
        return searchItem.getQuery();
    }

    @Override
    public boolean isSearchActive() {
        return !searchItem.isIconified();
    }

    @Override
    public void showError(String message, Callback callback) {
        if (message != null) {
            showSnackBar(message, callback);
        } else {
            showSnackBar(getString(R.string.error_some), callback);
        }
    }

    private void showSnackBar(String message, Callback callback) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_INDEFINITE).setAction("Retry", view -> callback.call());
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_color_inverse));
        snackbar.show();
    }
}