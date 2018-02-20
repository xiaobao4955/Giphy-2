package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.storage.GifView;

import java.util.List;

import io.reactivex.Single;

public interface ApiManager {

    Single<List<GifView>> getTrendingGifs(int limits, int offset);

    Single<List<GifView>> search(String query, int limit, int offset);
}
