package com.ugurcanyildirim.trivagocasestudy.service;

import android.util.Log;

import com.ugurcanyildirim.trivagocasestudy.model.Movie;
import com.ugurcanyildirim.trivagocasestudy.model.SearchResult;
import com.ugurcanyildirim.trivagocasestudy.ui.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ugurc on 12.08.2016.
 */
public class ServiceManager {

    private final String TAG = ServiceManager.class.getSimpleName();

    private final String BASE_URL = "https://api.trakt.tv";
    private final String API_VERSION = "2";
    private final String API_KEY = "ad005b8c117cdeee58a1bdb7089ea31386cd489b21e14b19818c91511f12a086";
    private final String EXTENDED_INFO = "full,images";

    private Retrofit retrofit;
    private ServiceInterface serviceInterface;

    public ServiceManager(){

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceInterface = retrofit.create(ServiceInterface.class);

    }

    public void getPopularMovies(final MainActivity mainActivity, int page, int limit){

        Call<List<Movie>> call = serviceInterface.getPopularMovies(API_VERSION, API_KEY, EXTENDED_INFO, page, limit);
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {

                List<Movie> movies = response.body();
                if(movies != null) {
                    for (Movie movie : movies)
                        Log.d(TAG, movie.title + " " + movie.year);
                }

                mainActivity.loadMovies(movies, "");

            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                Log.e(TAG, t.toString());
                mainActivity.loadMovies(null, "");
            }
        });

    }

    public void searchMovies(final MainActivity mainActivity, int page, int limit, final String keyword){

        Call<List<SearchResult>> call = serviceInterface.searchMovies(API_VERSION, API_KEY, EXTENDED_INFO, page, limit, keyword);
        call.enqueue(new Callback<List<SearchResult>>() {
            @Override
            public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {

                List<Movie> movies = null;

                List<SearchResult> searchResults = response.body();
                if(searchResults != null) {
                    movies = new ArrayList<Movie>();
                    for (SearchResult result : searchResults)
                        movies.add(result.movie);
                }

                mainActivity.loadMovies(movies, keyword);

            }

            @Override
            public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                Log.e(TAG, t.toString());
                mainActivity.loadMovies(null, keyword);
            }
        });

    }

}
