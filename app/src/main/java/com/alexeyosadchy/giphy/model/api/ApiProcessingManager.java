package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.response.Datum;
import com.alexeyosadchy.giphy.model.api.response.FixedHeight;
import com.alexeyosadchy.giphy.model.api.response.Response;
import com.alexeyosadchy.giphy.model.storage.GifView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public final class ApiProcessingManager implements ApiManager {

    private static final String API_KEY = "BY5k25KOh0B1OUim9w7HqqYKOUNNRV2r";

    private final ApiService mApiService;

    @Inject
    ApiProcessingManager(final ApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Single<List<GifView>> getTrendingGifs(final int limit, final int offset) {
        return startRequest(mApiService.getTrendingHighlights(API_KEY, limit, offset));
    }

    @Override
    public Single<List<GifView>> search(final String phrase, final int limit, final int offset) {
        return startRequest(mApiService.search(API_KEY, phrase, limit, offset));
    }

    private Single<List<GifView>> startRequest(final Single<Response> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(this::transformResponseToGifViews);
    }

    private List<GifView> transformResponseToGifViews(final Response response) {
        final List<GifView> gifs = new ArrayList<>();
        for (final Datum datum : response.getData()) {
            final FixedHeight fixedHeight = datum.getImages().getFixedHeight();
            gifs.add(new GifView(fixedHeight.getUrl(), datum.getId(),
                    Integer.valueOf(fixedHeight.getWidth()),
                    Integer.valueOf(fixedHeight.getHeight())));
        }
        return gifs;
    }
}
