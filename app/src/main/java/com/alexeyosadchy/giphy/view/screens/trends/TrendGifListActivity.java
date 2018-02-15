package com.alexeyosadchy.giphy.view.screens.trends;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;
import com.alexeyosadchy.giphy.utils.AdapterUtils;
import com.alexeyosadchy.giphy.view.EndlessScrollListener;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.base.BaseFavoriteButton;
import com.alexeyosadchy.giphy.view.base.BaseListGifActivity;
import com.alexeyosadchy.giphy.view.base.GifListAdapter;
import com.alexeyosadchy.giphy.view.screens.favorite.FavoriteGifListActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

public final class TrendGifListActivity extends BaseListGifActivity implements ITrendGifListActivity {

    private boolean searchMode = false;
    private String searchQuery;

    @Inject
    ITrendGifListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_gif_list);
        setUnBinder(ButterKnife.bind(this));

        mActivityComponent.inject(this);
        presenter.onAttach(this);
        presenter.onCreateView();
        setTitle(R.string.search_mode_off);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trend_gif_list_activity, menu);
        MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        favoriteItem.setOnMenuItemClickListener(menuItem -> {
            presenter.onClickMenuItemFavorite();
            return false;
        });
        SearchView searchItem = (SearchView) menu.findItem(R.id.action_search).getActionView();
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
    public void prepareView(List<GifView> gifs, int position) {
        super.prepareView(gifs, position);
        BaseFavoriteButton button = new BaseFavoriteButton() {
            @Override
            public void action(int position) {
                presenter.onClickFavoriteButton(position);
            }

            @Override
            public int getImageResources(int position) {
                if (presenter.onBindView(position)) {
                    return R.drawable.ic_favorite;
                } else {
                    return R.drawable.ic_favorite_border;
                }
            }
        };
        mGifListAdapter = new GifListAdapter(gifs, layoutManager, button);
        mRecyclerView.setAdapter(mGifListAdapter);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onEndList() {
                endScroll();
            }
        });
    }

    @Override
    public void navigateToFavoriteGifsActivity() {
        Intent intent = new Intent(TrendGifListActivity.this, FavoriteGifListActivity.class);
        startActivity(intent);
    }

    @Override
    public void endScroll() {
        presenter.loadGifs();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        presenter.onConfigurationChanged(AdapterUtils.getCurrentRecyclerViewPosition(mRecyclerView));
    }

    @Override
    protected void onResume() {
        mGifListAdapter.notifyDataSetChanged();
        super.onResume();
    }
}