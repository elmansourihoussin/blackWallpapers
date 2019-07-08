package com.islamiat.blackwallpapers.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.activity.PhotoActivity;
import com.islamiat.blackwallpapers.adapter.GalleryAdapter;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class NewFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Photo> listPhoto = new ArrayList<>();

    private int pageNo = 1;
    private GalleryAdapter mAdapter;

    View layoutLoader, layoutError;
    TextView tvError;

    private GridLayoutManager mLayoutManager;
    private int column;

    public NewFragment() {

    }


    public static NewFragment newInstance() {
        return new NewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_new, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        layoutLoader = view.findViewById(R.id.layout_loader);
        layoutError =view.findViewById(R.id.layout_error);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new GalleryAdapter(getActivity(), listPhoto);

        column = getActivity().getResources().getInteger(R.integer.grid_column_number);
        mLayoutManager = new GridLayoutManager(getActivity(), column);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                getMorePhotos(page);
            }
        });

        final InterstitialHelper interstitialHelper = InterstitialHelper.newInstance((AppCompatActivity) getActivity());

        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                if (getActivity() == null) return;

                interstitialHelper.takeAction(new InterstitialHelper.ActionListener() {
                    @Override
                    public void onDone() {
                        ArrayList<Photo> list = (ArrayList<Photo>) mAdapter.getList();

                        Intent intent = new Intent(getActivity(), PhotoActivity.class);
                        intent.putParcelableArrayListExtra(Utils.EXTRA_LIST, list);
                        intent.putExtra(Utils.EXTRA_POSITION, position);
                        intent.putExtra(Utils.EXTRA_FLAG, Utils.FLAG_PHOTO_RECENT);
                        intent.putExtra(Utils.EXTRA_PAGE_NO, pageNo);
                        startActivity(intent);
                    }
                });

            }
        });

        //updateSpan();


    }

    /*public void updateSpan(){
        DisplayHelper.updateSpan(getActivity(),mLayoutManager,column);
    }*/

    @Override
    public void onResume() {
        super.onResume();

        if (mAdapter.getItemCount() <= 0) {
            getRecentPhotos();
        }

        DisplayHelper.updateSpan(getActivity(),mLayoutManager,column);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Log.e("TAG", " NewFragment onConfigurationChanged");
        DisplayHelper.updateSpan(newConfig.orientation, mLayoutManager,column);
    }

    private void getRecentPhotos() {
        String url = FlickrManager.getPhotoUrl();
        url = url.replace(" ", "%20");

        ViewHelper.smoothToShow(layoutLoader);

        StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ViewHelper.smoothToHide(layoutLoader);
                try {
                    List<FlickrPhoto> flickrPhotoList = FlickrPhoto.jsonToPhotoList(response);
                    listPhoto.addAll(Photo.flickrPhotoToPhoto(flickrPhotoList));
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ViewHelper.smoothToHide(layoutLoader);
                //tvError.setText("Network connection failed!");
                ViewHelper.smoothToShow(layoutError);
            }
        });

        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void getMorePhotos(int page) {
        pageNo = page + 1;

        if (pageNo >5){
            return;
        }

        String url = FlickrManager.getPhotoUrl(null, pageNo);
        url = url.replace(" ", "%20");
        //Log.e("TAG", "getMorePhotos: " + url);

        if (page < TempData.getInstance().pages) {
            StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        List<FlickrPhoto> flickrPhotoList = FlickrPhoto.jsonToPhotoList(response);
                        listPhoto.addAll(Photo.flickrPhotoToPhoto(flickrPhotoList));
                        mAdapter.notifyDataSetChanged();
                        //Log.e("TAG", "more size "+ mAdapter.getItemCount());

                    } catch (JSONException e) {
                        Log.e("TAG", "Error ", e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //show connection page if internet not connected
                    //ConnectionActivity.isOnline(getActivity());

                }
            });

            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }
}
