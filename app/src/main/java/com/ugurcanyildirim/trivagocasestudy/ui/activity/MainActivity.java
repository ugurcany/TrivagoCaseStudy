package com.ugurcanyildirim.trivagocasestudy.ui.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.ugurcanyildirim.trivagocasestudy.R;
import com.ugurcanyildirim.trivagocasestudy.ui.adapter.MovieListAdapter;
import com.ugurcanyildirim.trivagocasestudy.ui.custom.InfiniteListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ugurc on 11.08.2016.
 */
public class MainActivity extends AppCompatActivity {

    private final int ITEM_COUNT_TO_LOAD = 25;
    private final int ITEM_COUNT_LIMIT = 200;
    private final int TIME_TO_LOAD = 1500; //in ms

    private ArrayList<String> movieList;
    private MovieListAdapter adapter;
    private View loadingView;

    private int itemOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                Log.d(MainActivity.class.getSimpleName(), "Search: " + newQuery);

            }
        });

        movieList = new ArrayList<String>();
        adapter = new MovieListAdapter(this, R.layout.item_movie, movieList);
        loadingView = getLayoutInflater().inflate(R.layout.item_loading, null);

        movieListView.init(adapter, loadingView);

        refreshList();

    }

    //LOAD NEW ITEMS
    public void loadNewItems() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                movieListView.startLoading();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(TIME_TO_LOAD);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                if(itemOffset >= ITEM_COUNT_LIMIT) {
                    movieListView.hasMore(false);
                }
                else {
                    //ADD NEW ITEMS TO LIST
                    for (int i = itemOffset; i < itemOffset + ITEM_COUNT_TO_LOAD; i++) {
                        String item = "Item #" + i;
                        movieListView.addNewItem(item);
                    }
                    itemOffset += ITEM_COUNT_TO_LOAD;
                    Log.d("InfiniteListView", "Current item count = " + itemOffset);

                    movieListView.hasMore(true);
                }

                movieListView.stopLoading();
            }
        }.execute();
    }

    //DO THIS ON SWIPE-REFRESH
    public void refreshList() {
        itemOffset = 0;
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


    @BindView(R.id.searchView)
    FloatingSearchView searchView;

    @BindView(R.id.movieListView)
    InfiniteListView movieListView;

}
