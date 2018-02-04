package com.alexeyosadchy.giphy.view;

import android.content.res.Configuration;
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
    private GifListAdapter mGifListAdapter;
    private ProgressBar mProgressBar;
    private SearchView searchItem;

    private boolean searchMode = false;
    private String searchQuery;

    @Inject
    ITrendGifListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_gif_list);
        mProgressBar = findViewById(R.id.progressBar);
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(((App) getApplication()).getApplicationComponent())
                .activityModule(new ActivityModule(this))
                .build();
        mActivityComponent.inject(this);

        presenter.onAttach(this);
        presenter.onCreateView();
        setTitle(R.string.search_mode_off);
    }

    @Override
    public void updateList(int position) {
        mGifListAdapter.notifyDataSetChanged();
    }

    @Override
    public void prepareView(List<GifView> gifs) {
        RecyclerView recyclerView = findViewById(R.id.rv_gifs);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mGifListAdapter = new GifListAdapter(gifs, mLinearLayoutManager);
        recyclerView.setAdapter(mGifListAdapter);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addOnScrollListener(new EndlessScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore() {
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
                searchQuery = query;
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
    public String getSearchQuery() {
        return searchQuery;
    }

    @Override
    public boolean isSearchModeActive() {
        return searchMode;
    }

    @Override
    public void onBackPressed() {
        presenter.onBackPressed();
    }

    @Override
    public void closeApplication() {
        super.onBackPressed();
    }

    @Override
    public void switchSearchMode() {
        if (!searchMode) {
            searchMode = true;
            setTitle(R.string.search_mode_on);
        } else {
            searchMode = false;
            setTitle(R.string.search_mode_off);
        }
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
                message, Snackbar.LENGTH_INDEFINITE).setAction(R.string.text_btn_snackbar, view -> callback.call());
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_color_inverse));
        snackbar.show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
}