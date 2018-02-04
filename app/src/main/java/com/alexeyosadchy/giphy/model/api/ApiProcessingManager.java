package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.mapper.ResponseDataMapper;
import com.alexeyosadchy.giphy.model.api.response.Images;
import com.alexeyosadchy.giphy.model.api.response.Response;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiProcessingManager implements ApiManager {

    public static final String API_KEY = "BY5k25KOh0B1OUim9w7HqqYKOUNNRV2r";

    private ApiService mApiService;

    @Inject
    public ApiProcessingManager(ApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Observable<List<Images>> getTrendingGifs(int limit, int offset) {
        return mApiService.getTrendingHighlights(API_KEY, limit, offset)
                .map(response -> ResponseDataMapper.transform(response));
    }

    @Override
    public Observable<List<Images>> search(String phrase, int limit, int offset) {
        return mApiService.search(API_KEY, phrase, limit, offset)
                .map(response -> ResponseDataMapper.transform(response));
    }
}
