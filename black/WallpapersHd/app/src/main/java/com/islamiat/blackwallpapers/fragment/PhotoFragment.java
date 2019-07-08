package com.islamiat.blackwallpapers.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.model.Photo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ornach.andutils.android.ViewHelper;
import com.ornach.andutils.java.StringUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFragment extends Fragment {

    private Photo photo;
    private ImageView imageView;

    private View layoutLoader;

    public PhotoFragment() {
        // Required empty public constructor
    }

    public static PhotoFragment newInstance(Photo photo) {
        PhotoFragment fragment = new PhotoFragment();
        fragment.photo = photo;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        imageView = (ImageView) view.findViewById(R.id.image_view);
        layoutLoader = view.findViewById(R.id.layout_loader);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadImage();
    }

    private void loadImage() {
        if (photo == null || StringUtils.isEmpty(photo.pic_h)) {
            return;
        }

        String picUrl = photo.pic_h;
        ViewHelper.smoothToShow(layoutLoader);
        // load full pic
        Glide
                .with(this)
                .load(picUrl)
                //.thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .crossFade(500)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        ViewHelper.smoothToHide(layoutLoader);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        ViewHelper.smoothToHide(layoutLoader);
                        return false;
                    }
                })
                .into(imageView);


    }
}
