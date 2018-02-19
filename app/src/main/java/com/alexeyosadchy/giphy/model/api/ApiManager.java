package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.response.Datum;

import java.util.List;

import io.reactivex.Observable;

public interface ApiManager {

    Observable<List<Datum>> getTrendingGifs(int limits, int offset);

    Observable<List<Datum>> search(String query, int limit, int offset);
}
