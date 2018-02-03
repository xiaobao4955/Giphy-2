package com.alexeyosadchy.giphy.presenter;

import com.alexeyosadchy.giphy.model.api.ApiService;
import com.alexeyosadchy.giphy.view.GifView;
import com.alexeyosadchy.giphy.view.ITrendGifListActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TrendGifListPresenter implements ITrendGifListPresenter {

    private ITrendGifListActivity mView;
    private ApiService mApiService;
    private List<GifView> mGifViews;

    @Inject
    public TrendGifListPresenter(ApiService apiService) {
        mApiService = apiService;
        mGifViews = new ArrayList<>();
    }

    @Override
    public void loadGifs() {
        getTrendingGifs(gifViews -> {mGifViews.addAll(gifViews); mView.updateList();});
    }

    @Override
    public void onCreateView() {
        getTrendingGifs(gifViews -> {mGifViews.addAll(gifViews);mView.prepareView(mGifViews);});
    }

    @Override
    public void onAttach(ITrendGifListActivity view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
    }

    @Override
    public void onSearchSubmit(String query){
        getFoundGifs(gifViews -> {mGifViews.clear();mGifViews.addAll(gifViews);mView.updateList();}, query);
    }

    private void getFoundGifs(Consumer<? super List<GifView>> onNext, String query){
        mApiService.search("BY5k25KOh0B1OUim9w7HqqYKOUNNRV2r", query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> Mapper.transform(response))
                .subscribe(onNext);
    }

    private void getTrendingGifs(Consumer<? super List<GifView>> onNext){
        mApiService.getTrendingHighlights("BY5k25KOh0B1OUim9w7HqqYKOUNNRV2r", 15, mGifViews.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> Mapper.transform(response))
                .subscribe(onNext);
    }
}
