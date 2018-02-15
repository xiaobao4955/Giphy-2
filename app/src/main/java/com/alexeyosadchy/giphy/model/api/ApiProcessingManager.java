package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.mapper.ResponseDataMapper;
import com.alexeyosadchy.giphy.model.api.response.Images;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public final class ApiProcessingManager implements ApiManager {

    private static final String API_KEY = "BY5k25KOh0B1OUim9w7HqqYKOUNNRV2r";

    private final ApiService mApiService;

    @Inject
    ApiProcessingManager(ApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Observable<List<Images>> getTrendingGifs(int limit, int offset) {
        return mApiService.getTrendingHighlights(API_KEY, limit, offset)
                .map(ResponseDataMapper::transform);
    }

    @Override
    public Observable<List<Images>> search(String phrase, int limit, int offset) {
        return mApiService.search(API_KEY, phrase, limit, offset)
                .map(ResponseDataMapper::transform);
    }
}
