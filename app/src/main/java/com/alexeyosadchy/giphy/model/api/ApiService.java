package com.alexeyosadchy.giphy.model.api;

import com.alexeyosadchy.giphy.model.api.response.Response;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/v1/gifs/trending")
    Observable<Response> getTrendingHighlights(@Query("api_key") String apiKey,
                                               @Query("limit") int limit,
                                               @Query("offset") int offset);

    @GET("/v1/gifs/search")
    Observable<Response> search(@Query("api_key") String apiKey,
                                @Query("q") String phrase,
                                @Query("limit") int limit,
                                @Query("offset") int offset);
}
