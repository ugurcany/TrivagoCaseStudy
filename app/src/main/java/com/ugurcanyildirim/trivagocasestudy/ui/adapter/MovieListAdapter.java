package com.ugurcanyildirim.trivagocasestudy.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ugurcanyildirim.trivagocasestudy.R;
import com.ugurcanyildirim.trivagocasestudy.model.Movie;
import com.ugurcanyildirim.trivagocasestudy.ui.activity.MainActivity;
import com.ugurcanyildirim.trivagocasestudy.ui.custom.InfiniteListAdapter;

import java.util.ArrayList;

/**
 * Created by ugurc on 11.08.2016.
 */
public class MovieListAdapter<T> extends InfiniteListAdapter<T> {

    private MainActivity activity;
    private int itemLayoutRes;
    private ArrayList<T> itemList;

    public MovieListAdapter(MainActivity activity, int itemLayoutRes, ArrayList<T> itemList) {

        super(activity, itemLayoutRes, itemList);

        this.activity = activity;
        this.itemLayoutRes = itemLayoutRes;
        this.itemList = itemList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(itemLayoutRes, parent, false);

            holder = new ViewHolder();
            holder.moviePoster = (ImageView) convertView.findViewById(R.id.moviePoster);
            holder.movieTitle = (TextView) convertView.findViewById(R.id.movieTitle);
            holder.movieOverview = (TextView) convertView.findViewById(R.id.movieOverview);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Movie movie = (Movie) itemList.get(position);
        if (movie != null) {
            //POSTER
            Picasso.with(activity)
                    .load(movie.images.poster.thumb)
                    .placeholder(R.drawable.default_poster)
                    .error(R.drawable.default_poster)
                    .into(holder.moviePoster);

            //TITLE
            String title = movie.title + " (" + (!TextUtils.isEmpty(movie.year) ? movie.year : "-") + ")";
            holder.movieTitle.setText(title);

            //OVERVIEW
            String overview = !TextUtils.isEmpty(movie.overview) ? movie.overview.trim() : activity.getResources().getString(R.string.item_movie_nooverview);
            holder.movieOverview.setText(overview);
        }

        return convertView;
    }

    @Override
    public void onNewLoadRequired() {
        activity.loadNewItems();
    }

    @Override
    public void onRefresh() {
        activity.refreshList();
    }

    @Override
    public void onItemClick(int position) {
        activity.clickItem(position);
    }

    @Override
    public void onItemLongClick(int position) {
        activity.longClickItem(position);
    }


    static class ViewHolder {
        ImageView moviePoster;
        TextView movieTitle;
        TextView movieOverview;
    }

}