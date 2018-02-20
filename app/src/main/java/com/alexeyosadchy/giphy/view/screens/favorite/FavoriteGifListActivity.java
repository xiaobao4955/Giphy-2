package com.alexeyosadchy.giphy.view.screens.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.model.storage.GifView;
import com.alexeyosadchy.giphy.presenter.FavoriteGifsPresenter;
import com.alexeyosadchy.giphy.view.base.BaseFavoriteButton;
import com.alexeyosadchy.giphy.view.base.BaseListGifActivity;
import com.alexeyosadchy.giphy.view.base.GifListAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;

public final class FavoriteGifListActivity extends BaseListGifActivity {

    @Inject
    FavoriteGifsPresenter presenter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent.inject(this);
        presenter.onAttach(this);
        presenter.onCreateView();
        hideLoading();
        setTitle(R.string.option_menu_favorite);
    }

    @Override
    public void configurateAdapter() {
        super.configurateAdapter();
        mGifListAdapter = new GifListAdapter(deleteGifButton);
        mRecyclerView.setAdapter(mGifListAdapter);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private final BaseFavoriteButton deleteGifButton = new BaseFavoriteButton() {
        @Override
        public void action(final GifView gif, final int position) {
            presenter.onClickFavoriteButton(gif, position);
        }

        @Override
        public int getImageResources(final GifView gif) {
            return R.drawable.ic_delete;
        }
    };
}
