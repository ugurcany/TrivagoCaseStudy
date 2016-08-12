package com.ugurcanyildirim.trivagocasestudy.ui.activity;

import android.app.Application;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.ugurcanyildirim.trivagocasestudy.BaseApplication;
import com.ugurcanyildirim.trivagocasestudy.R;
import com.ugurcanyildirim.trivagocasestudy.model.Movie;
import com.ugurcanyildirim.trivagocasestudy.ui.adapter.MovieListAdapter;
import com.ugurcanyildirim.trivagocasestudy.ui.custom.InfiniteListView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ugurc on 11.08.2016.
 */
public class MainActivity extends AppCompatActivity {

    private final int ITEM_COUNT = 20;
    private int page = 1;

    private ArrayList<Movie> movieList;
    private MovieListAdapter adapter;

    private boolean isSearchActive = false;
    private String keyword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if(newQuery.isEmpty()){
                    isSearchActive = false;
                    keyword = "";

                    resultsTitle.setText(getResources().getString(R.string.results_popularmovies));
                }
                else{
                    isSearchActive = true;
                    keyword = newQuery;

                    resultsTitle.setText(getResources().getString(R.string.results_searchmovies));
                }

                Log.d(MainActivity.class.getSimpleName(), "Search: " + newQuery);

                refreshList();

            }
        });

        movieList = new ArrayList<Movie>();
        adapter = new MovieListAdapter<Movie>(this, R.layout.item_movie, movieList);

        movieListView.init(adapter, R.layout.item_loading);

        refreshList();

    }

    //LOAD NEW ITEMS
    public void loadNewItems() {
        movieListView.startLoading();
        if(isSearchActive){
            BaseApplication.getService().searchMovies(this, page, ITEM_COUNT, keyword);
        }
        else{
            BaseApplication.getService().getPopularMovies(this, page, ITEM_COUNT);
        }
    }

    //DO THIS ON SWIPE-REFRESH
    public void refreshList() {
        page = 1;
        movieListView.clearList();
        loadNewItems();
    }

    //DO THIS ON ITEM CLICK
    public void clickItem(int position) {
        //LogUtil.d("item clicked: " + position);
    }

    //DO THIS ON ITEM LONG-CLICK
    public void longClickItem(int position) {
        //LogUtil.d("item long clicked: " + position);
    }

    //CALLED FROM SERVICE
    public void loadMovies(List<Movie> movies, String keyword){
        if(this.keyword.equals(keyword)) { //OLD RESULTS
            if (movies != null) {
                if (movies.size() < ITEM_COUNT) {
                    movieListView.hasMore(false);
                } else {
                    //ADD NEW ITEMS TO LIST
                    for (Movie movie : movies) {
                        movieListView.addNewItem(movie);
                    }
                    page++;

                    movieListView.hasMore(true);
                }
            }
            movieListView.stopLoading();
        }
    }


    @BindView(R.id.searchView)
    FloatingSearchView searchView;

    @BindView(R.id.resultsTitle)
    TextView resultsTitle;

    @BindView(R.id.movieListView)
    InfiniteListView movieListView;

}
