package com.ugurcanyildirim.trivagocasestudy.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ugurcanyildirim.trivagocasestudy.R;
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
            holder.title = (TextView) convertView.findViewById(R.id.movieTitle);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String text = (String) itemList.get(position);
        if (text != null) {
            holder.title.setText(text);
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
        TextView title;
    }

}