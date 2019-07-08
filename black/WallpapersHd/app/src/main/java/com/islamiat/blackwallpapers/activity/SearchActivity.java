package com.islamiat.blackwallpapers.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.adapter.GalleryAdapter;
import com.islamiat.blackwallpapers.admob.BannerHelper;
import com.islamiat.blackwallpapers.admob.InterstitialHelper;
import com.islamiat.blackwallpapers.helper.AppController;
import com.islamiat.blackwallpapers.helper.DisplayHelper;
import com.islamiat.blackwallpapers.helper.EndlessRecyclerViewScrollListener;
import com.islamiat.blackwallpapers.helper.FlickrManager;
import com.islamiat.blackwallpapers.helper.TempData;
import com.islamiat.blackwallpapers.helper.Utils;
import com.islamiat.blackwallpapers.model.FlickrPhoto;
import com.islamiat.blackwallpapers.model.Photo;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ornach.andutils.android.ViewHelper;
import com.ornach.andutils.android.utils.Connectivity;

import org.apache.commons.lang3.text.WordUtils;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private View layoutLoader;
    private List<Photo> listPhoto = new ArrayList<>();
    private View layoutError;

    private int pageNo = 1;
    private String query = "";
    private String tagName = "";


    private GalleryAdapter mAdapter;

    private GridLayoutManager mLayoutManager;
    private int column;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (getIntent() != null) {
            if (getIntent().hasExtra(Utils.EXTRA_TEXT)) {
                query = getIntent().getStringExtra(Utils.EXTRA_TEXT);
                toolbar.setTitle(WordUtils.capitalize(query));
            }
        }

        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutLoader = findViewById(R.id.layout_loader);
        layoutError = findViewById(R.id.layout_error);

        mAdapter = new GalleryAdapter(this, listPhoto);

        column = getResources().getInteger(R.integer.grid_column_number);
        mLayoutManager = new GridLayoutManager(this, column);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //Log.e("TAG", "getSpanSize "+ mAdapter.getItemViewType(position));
                switch (mAdapter.getItemViewType(position)) {

                    case GalleryAdapter.AD_VIEW_TYPE:
                        return column;
                    case GalleryAdapter.ITEM_VIEW_TYPE:
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                flickrMoreSearchPhotos(page);
            }
        });

        final InterstitialHelper interstitialHelper = InterstitialHelper.newInstance(this);

        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                interstitialHelper.takeAction(new InterstitialHelper.ActionListener() {
                    @Override
                    public void onDone() {
                        ArrayList<Photo> list = (ArrayList<Photo>) mAdapter.getList();
                        Intent intent = new Intent(SearchActivity.this, PhotoActivity.class);
                        intent.putParcelableArrayListExtra(Utils.EXTRA_LIST, list);
                        intent.putExtra(Utils.EXTRA_POSITION, position);
                        intent.putExtra(Utils.EXTRA_FLAG, Utils.FLAG_PHOTO_SEARCH);
                        intent.putExtra(Utils.EXTRA_PAGE_NO, pageNo);
                        intent.putExtra(Utils.EXTRA_TAG, query);
                        startActivity(intent);
                    }
                });

            }
        });

        // load admob banner ad
        BannerHelper.newInstance(this).loadBannerAdView((FrameLayout) findViewById(R.id.layout_ad));

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdapter.getItemCount() <= 0) {
            flickrSearchPhotos();
        }

        DisplayHelper.updateSpan(this,mLayoutManager,column);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        DisplayHelper.updateSpan(this,mLayoutManager,column);
    }


    private void flickrSearchPhotos() {

        if (!Connectivity.isConnected(this)) {
            ViewHelper.smoothToShow(layoutError);
            return;
        }

        String url = FlickrManager.searchPhotoUrl(query, pageNo);
        url = url.replace(" ", "%20");
        //Log.e("TAG", url);

        ViewHelper.smoothToShow(layoutLoader);

        StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ViewHelper.smoothToHide(layoutLoader);
                try {
                    List<FlickrPhoto> flickrPhotoList = FlickrPhoto.jsonToPhotoList(response);
                    listPhoto.addAll(Photo.flickrPhotoToPhoto(flickrPhotoList));
                    mAdapter.notifyDataSetChanged();
                    //Log.e("TAG", "Size "+mAdapter.getItemCount());
                } catch (JSONException e) {
                    //e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ViewHelper.smoothToHide(layoutLoader);
                if (!Connectivity.isConnected(SearchActivity.this)) {
                    ViewHelper.smoothToShow(layoutError);
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void flickrMoreSearchPhotos(int page) {
        pageNo = page + 1;

        if (pageNo >5){
            return;
        }

        String url = FlickrManager.searchPhotoUrl(query, pageNo);
        url = url.replace(" ", "%20");
        // Log.e("TAG", url);

        if (page < TempData.getInstance().pages) {
            StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        List<FlickrPhoto> flickrPhotoList = FlickrPhoto.jsonToPhotoList(response);
                        listPhoto.addAll(Photo.flickrPhotoToPhoto(flickrPhotoList));
                        mAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        //e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            AppController.getInstance().addToRequestQueue(jsonReq);
        }


    }

}
