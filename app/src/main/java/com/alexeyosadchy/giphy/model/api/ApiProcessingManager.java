package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.response.Response;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiProcessingManager implements ApiService {

    public static final String API_KEY = "BY5k25KOh0B1OUim9w7HqqYKOUNNRV2r";

    private ApiService mApiService;

    @Inject
    public ApiProcessingManager(ApiService apiService) {
        mApiService = apiService;
    }

    @Override
    public Observable<Response> getTrendingHighlights(String apiKey, int limit, int offset) {
        return mApiService.getTrendingHighlights(apiKey, limit, offset);
    }

    @Override
    public Observable<Response> search(String apiKey, String phrase) {
        return mApiService.search(apiKey, phrase);
    }
}
