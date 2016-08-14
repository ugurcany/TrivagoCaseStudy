package com.ugurcanyildirim.trivagocasestudy.ui.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.ugurcanyildirim.trivagocasestudy.BaseApplication;
import com.ugurcanyildirim.trivagocasestudy.R;
import com.ugurcanyildirim.trivagocasestudy.model.Movie;
import com.ugurcanyildirim.trivagocasestudy.ui.adapter.MovieListAdapter;
import com.ugurcanyildirim.trivagocasestudy.ui.custom.InfiniteListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ugurc on 11.08.2016.
 */
public class MainActivity extends AppCompatActivity {

    private final int ITEM_COUNT = 10;
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

                //IF EMPTY, GET POPULAR MOVIES
                if(newQuery.isEmpty()){
                    isSearchActive = false;
                    keyword = "";

                    resultsTitle.setText(getResources().getString(R.string.results_popularmovies));
                }
                else{ //SEARCH
                    isSearchActive = true;
                    keyword = newQuery;

                    resultsTitle.setText(getResources().getString(R.string.results_searchmovies));
                }

                Log.d(MainActivity.class.getSimpleName(), "Search: " + newQuery);

                refreshList();

            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            }

            @Override
            public void onSearchAction(String currentQuery) {
                if(!currentQuery.equals(keyword) && !currentQuery.isEmpty()){
                    isSearchActive = true;
                    keyword = currentQuery;

                    resultsTitle.setText(getResources().getString(R.string.results_searchmovies));

                    refreshList();
                }
            }
        });

        movieList = new ArrayList<Movie>();
        adapter = new MovieListAdapter<Movie>(this, R.layout.item_movie, movieList);

        movieListView.init(adapter, R.layout.item_loading);

        //FIRST-TIME LOAD
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

    //DO THIS ON SWIPE-REFRESH & FIRST-TIME SEARCH
    public void refreshList() {
        page = 1;
        movieListView.clearList();
        loadNewItems();
    }

    //DO THIS ON ITEM CLICK
    public void clickItem(int position) {
    }

    //DO THIS ON ITEM LONG-CLICK
    public void longClickItem(int position) {
    }

    //TO BE CALLED FROM SERVICE MANAGER
    public void loadMovies(List<Movie> movies, String keyword){
        if(this.keyword.equals(keyword)) { //IF NOT OLD RESULTS
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

            if(movieList.isEmpty()){
                resultsTitle.setText(getResources().getString(R.string.results_nomovie));
            }
            movieListView.stopLoading();
        }
    }


    @OnClick(R.id.buttonInfo)
    void onButtonInfoClick(){
        MaterialStyledDialog dialog = new MaterialStyledDialog(this)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setTitle(R.string.app_name)
                .setDescription(R.string.app_info)
                .setHeaderColor(R.color.primary)
                .setPositive("OK", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        dialog.show();
    }

    @BindView(R.id.searchView)
    FloatingSearchView searchView;

    @BindView(R.id.resultsTitle)
    TextView resultsTitle;

    @BindView(R.id.movieListView)
    InfiniteListView movieListView;

}
