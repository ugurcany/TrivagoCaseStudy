package com.ugurcanyildirim.trivagocasestudy.ui.custom;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.ugurcanyildirim.trivagocasestudy.R;

import java.util.List;

/**
 * Created by ugurc on 11.08.2016.
 */
public class InfiniteListView<T> extends FrameLayout {

    private Context context;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

    private boolean loading = false;
    private View loadingView;
    private int loadingViewLayout;

    private boolean hasMore = false;

    private InfiniteListAdapter infiniteListAdapter;

    public InfiniteListView(Context context) {
        super(context);
        this.init(context, null);
    }

    public InfiniteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public InfiniteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        View view = inflate(context, R.layout.custom_infinitelistview, this);

        this.context = context;

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                infiniteListAdapter.onRefresh();

            }
        });

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setFooterDividersEnabled(false);
    }

    public void init(InfiniteListAdapter<T> infiniteListAdapter, int loadingViewLayout){

        this.infiniteListAdapter = infiniteListAdapter;
        listView.setAdapter(infiniteListAdapter);

        this.loadingViewLayout = loadingViewLayout;

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!hasMore)
                    return;

                int lastVisibleItem = visibleItemCount + firstVisibleItem;
                if (lastVisibleItem >= totalItemCount && !loading) {

                    InfiniteListView.this.infiniteListAdapter.onNewLoadRequired();

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                InfiniteListView.this.infiniteListAdapter.onItemClick(position);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                InfiniteListView.this.infiniteListAdapter.onItemLongClick(position);

                return true;
            }
        });

    }

    public void addNewItem(T newItem){
        infiniteListAdapter.addNewItem(newItem);
    }

    public void addAll(List<T> newItems){
        infiniteListAdapter.addAll(newItems);
    }

    public void clearList(){
        hasMore = false;
        infiniteListAdapter.clearList();
    }

    public void startLoading(){
        //IF FOOTER ALREADY EXISTS, REMOVE IT
        if(listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(loadingView);
        }

        loading = true;
        this.loadingView = LayoutInflater.from(context).inflate(loadingViewLayout, null, false);

        if(!swipeRefreshLayout.isRefreshing() && listView.getFooterViewsCount() == 0) {
            listView.addFooterView(loadingView, null, false);
        }
    }

    public void stopLoading(){
        if(/*!swipeRefreshLayout.isRefreshing() &&*/ listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(loadingView);
        }
        swipeRefreshLayout.setRefreshing(false);
        loading = false;
    }

    public void hasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

}