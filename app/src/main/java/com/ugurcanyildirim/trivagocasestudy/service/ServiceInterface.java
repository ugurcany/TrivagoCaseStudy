package com.ugurcanyildirim.trivagocasestudy.service;

import com.ugurcanyildirim.trivagocasestudy.model.Movie;
import com.ugurcanyildirim.trivagocasestudy.model.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by ugurc on 12.08.2016.
 */
public interface ServiceInterface {

    @GET("/movies/popular")
    Call<List<Movie>> getPopularMovies(
            @Header("trakt-api-version") String apiVersion,
            @Header("trakt-api-key") String apiKey,
            @Query("page") int page,
            @Query("limit") int limit);

    @GET("/search/movie")
    Call<List<SearchResult>> searchMovies(
            @Header("trakt-api-version") String apiVersion,
            @Header("trakt-api-key") String apiKey,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("query") String keyword);

}
