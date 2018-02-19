package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.response.Response;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/v1/gifs/trending")
    Single<Response> getTrendingHighlights(@Query("api_key") String apiKey,
                                           @Query("limit") int limit,
                                           @Query("offset") int offset);

    @GET("/v1/gifs/search")
    Single<Response> search(@Query("api_key") String apiKey,
                            @Query("q") String phrase,
                            @Query("limit") int limit,
                            @Query("offset") int offset);
}
