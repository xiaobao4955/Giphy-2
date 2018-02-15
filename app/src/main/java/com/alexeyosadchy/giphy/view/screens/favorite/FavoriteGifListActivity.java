package com.alexeyosadchy.giphy.view.screens.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alexeyosadchy.giphy.R;
import com.alexeyosadchy.giphy.presenter.FavoriteGifsPresenter;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.base.BaseFavoriteButton;
import com.alexeyosadchy.giphy.view.base.BaseListGifActivity;
import com.alexeyosadchy.giphy.view.base.GifListAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

public final class FavoriteGifListActivity extends BaseListGifActivity {

    @Inject
    FavoriteGifsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_gif_list);

        setUnBinder(ButterKnife.bind(this));
        mActivityComponent.inject(this);
        presenter.onAttach(this);
        presenter.onCreateView();
        hideLoading();
        setTitle(R.string.option_menu_favorite);
    }

    @Override
    public void prepareView(List<GifView> gifs, int position) {
        super.prepareView(gifs, position);
        mGifListAdapter = new GifListAdapter(gifs, layoutManager, new BaseFavoriteButton() {
            @Override
            public void action(int position) {
                presenter.onClickFavoriteButton(position);
            }

            @Override
            public int getImageResources(int position) {
                return R.drawable.ic_delete;
            }
        });
        mRecyclerView.setAdapter(mGifListAdapter);
    }

    @Override
    public void updateList(int position) {
        mGifListAdapter.notifyItemRemoved(position);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
