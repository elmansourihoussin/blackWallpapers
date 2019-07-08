package com.islamiat.blackwallpapers.fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.activity.PhotoActivity;
import com.islamiat.blackwallpapers.adapter.GalleryAdapter;
import com.islamiat.blackwallpapers.admob.InterstitialHelper;
import com.islamiat.blackwallpapers.helper.DisplayHelper;
import com.islamiat.blackwallpapers.helper.EndlessRecyclerViewScrollListener;
import com.islamiat.blackwallpapers.helper.FlickrManager;
import com.islamiat.blackwallpapers.helper.Utils;
import com.islamiat.blackwallpapers.model.HistoryPhoto;
import com.islamiat.blackwallpapers.model.Photo;
import com.ornach.andutils.android.ViewHelper;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private View layoutError;
    private List<Photo> photoList = new ArrayList<>();
    private GalleryAdapter mAdapter;

    int limit = FlickrManager.LIMIT, offset = 0;
    private int pageNo = 1;

    private GridLayoutManager mLayoutManager;
    private int column;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutError = view.findViewById(R.id.layout_error);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new GalleryAdapter(getActivity(), photoList);

        column = getResources().getInteger(R.integer.grid_column_number);
        mLayoutManager = new GridLayoutManager(getActivity(), column);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                //Log.e("TAG", page + "");
                getMorePhotos(page);
            }
        });

        final InterstitialHelper interstitialHelper = InterstitialHelper.newInstance((AppCompatActivity) getActivity());
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                interstitialHelper.takeAction(new InterstitialHelper.ActionListener() {
                    @Override
                    public void onDone() {
                        ArrayList<Photo> list = (ArrayList<Photo>) mAdapter.getList();
                        Intent intent = new Intent(getActivity(), PhotoActivity.class);
                        intent.putParcelableArrayListExtra(Utils.EXTRA_LIST, list);
                        intent.putExtra(Utils.EXTRA_POSITION, position);
                        intent.putExtra(Utils.EXTRA_FLAG, Utils.FLAG_PHOTO_HISTORY);
                        intent.putExtra(Utils.EXTRA_PAGE_NO, pageNo);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        showItems();

        DisplayHelper.updateSpan(getActivity(),mLayoutManager,column);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Log.e("TAG", "HistoryFragment onConfigurationChanged");
        DisplayHelper.updateSpan(newConfig.orientation, mLayoutManager,column);
    }

    private void showItems() {
        photoList.clear();
        photoList = HistoryPhoto.getHistoryPhotos(0, 0);

        if (mAdapter ==null){
            mAdapter = new GalleryAdapter(getActivity(), photoList);
        }

        if (photoList.size() > 0) {
            mAdapter.newItems(photoList);
            ViewHelper.smoothToHide(layoutError);
        } else {
            ViewHelper.smoothToShow(layoutError);
        }
    }

    private void getMorePhotos(int page) {
        this.pageNo = page;
        this.offset = limit * page;
        mAdapter.addItems(HistoryPhoto.getHistoryPhotos(limit, offset));
    }

}
