package com.alexeyosadchy.giphy.view.screens.trends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.presenter.ITrendGifListPresenter;
import com.alexeyosadchy.giphy.view.EndlessScrollListener;
import com.alexeyosadchy.giphy.view.base.BaseFavoriteButton;
import com.alexeyosadchy.giphy.view.base.BaseListGifActivity;
import com.alexeyosadchy.giphy.view.base.GifListAdapter;
import com.alexeyosadchy.giphy.view.screens.favorite.FavoriteGifListActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public final class TrendGifListActivity extends BaseListGifActivity implements ITrendGifListActivity {

    private boolean searchMode = false;
    private String searchQuery;

    @Inject
    ITrendGifListPresenter presenter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_gif_list);
        setUnBinder(ButterKnife.bind(this));

        mActivityComponent.inject(this);
        presenter.onAttach(this);
        presenter.onCreateView();
        setTitle(R.string.search_mode_off);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trend_gif_list_activity, menu);
        final MenuItem favoriteItem = menu.findItem(R.id.action_favorite);
        favoriteItem.setOnMenuItemClickListener(menuItem -> {
            presenter.onClickMenuItemFavorite();
            return false;
        });
        final SearchView searchItem = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                searchQuery = query;
                presenter.onSearchSubmit(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
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
    public void configurationAdapter() {
        super.configurationAdapter();
        mGifListAdapter = new GifListAdapter(likeButtonHandler);
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
        final Intent intent = new Intent(TrendGifListActivity.this, FavoriteGifListActivity.class);
        startActivity(intent);
    }

    @Override
    public void endScroll() {
        presenter.loadGifs();
    }

    @Override
    protected void onResume() {
        mGifListAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private final BaseFavoriteButton likeButtonHandler = new BaseFavoriteButton() {
        @Override
        public void action(final GifView gif, final int position) {
            presenter.onClickFavoriteButton(gif, position);
        }

        @Override
        public int getImageResources(final GifView gif) {
            if (presenter.onBindView(gif)) {
                return R.drawable.ic_favorite;
            } else {
                return R.drawable.ic_favorite_border;
            }
        }
    };
}