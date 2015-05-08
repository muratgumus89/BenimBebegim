package com.example.murat.benimbebegim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.murat.benimbebegim.adapters.EarlierLayoutAdapter;
import com.example.murat.benimbebegim.stickHeader.StickyListHeadersListView;


/**
 * Created by Murat on 9.3.2015.
 */
public class ActivityEarlier extends Fragment  implements
        AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener{

    private EarlierLayoutAdapter mAdapter;
    private boolean fadeHeader = true;

    private StickyListHeadersListView stickyList;
    private SwipeRefreshLayout refreshLayout;



    public static final String TAG = ActivityEarlier.class.getSimpleName();

    public static ActivityEarlier newInstance() {
        return new ActivityEarlier();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_earlier, container,false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mAdapter = new EarlierLayoutAdapter(getActivity()); //.getBaseContext()

        stickyList = (StickyListHeadersListView) view.findViewById(R.id.list);
        stickyList.setOnItemClickListener(this);
        stickyList.setOnHeaderClickListener(this);
        stickyList.setOnStickyHeaderChangedListener(this);
        stickyList.setOnStickyHeaderOffsetChangedListener(this);
//        stickyList.addHeaderView(getLayoutInflater().inflate(R.layout.earlier_layout_header, null));
//        stickyList.addFooterView(getLayoutInflater().inflate(R.layout.earlier_footer, null));
        stickyList.setEmptyView(view.findViewById(R.id.empty));
        stickyList.setDrawingListUnderStickyHeader(true);
        stickyList.setAreHeadersSticky(true);
        stickyList.setAdapter(mAdapter);

        stickyList.setStickyHeaderTopOffset(-20);
        return view;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
        if (fadeHeader && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            header.setAlpha(1 - (offset / (float) header.getMeasuredHeight()));
        }
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
        Log.i("Earlier : ", "Earlier Layout Sticky listview item clicked " + headerId + " " + currentlySticky);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Earlier : ", "Earlier Layout Sticky listview item clicked " + position);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
        header.setAlpha(1);
    }

}
