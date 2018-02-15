package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.response.Images;

import java.util.List;

import io.reactivex.Observable;

public interface ApiManager {

    Observable<List<Images>> getTrendingGifs(int limits, int offset);

    Observable<List<Images>> search(String query, int limit, int offset);
}
