package com.islamiat.blackwallpapers.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.helper.AppController;
import com.islamiat.blackwallpapers.helper.FlickrManager;
import com.islamiat.blackwallpapers.helper.Utils;
import com.islamiat.blackwallpapers.model.License;
import com.islamiat.blackwallpapers.model.Photo;
import com.islamiat.blackwallpapers.model.PhotoInfo;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ornach.andutils.android.ViewHelper;
import com.ornach.andutils.android.utils.HtmlCompat;
import com.ornach.andutils.java.StringUtils;

import org.json.JSONException;


public class PhotoInformationDialogFragment extends DialogFragment {


    private Photo photo;
    private PhotoInfo photoInfo;
    TextView tvTitle, tvDate, tvSize, tvUrl, tvViewCount, tvCopyright, tvDescription;
    View btnBack;

    public PhotoInformationDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_NoActionBar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_information_dialog, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        tvDate = view.findViewById(R.id.tv_date);
        tvSize = view.findViewById(R.id.tv_size);
        tvUrl = view.findViewById(R.id.tv_url);
        tvViewCount = view.findViewById(R.id.tv_view_count);
        tvCopyright = view.findViewById(R.id.tv_copyright);
        tvDescription = view.findViewById(R.id.tv_description);
        btnBack = view.findViewById(R.id.btn_back);
        return view;
    }

    public static PhotoInformationDialogFragment newInstance(Photo photo) {
        PhotoInformationDialogFragment f = new PhotoInformationDialogFragment();
        f.photo = photo;
        return f;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (photo != null) {
            flickrPhotoInfo(photo.photoId);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        tvUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoInfo !=null && !StringUtils.isEmpty(photoInfo.photoUrl)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(photoInfo.photoUrl));
                    startActivity(intent);
                }
            }
        });

    }

    private void flickrPhotoInfo(String photoId) {

        if (StringUtils.isEmpty(photoId)) {
            return;
        }

        String url = FlickrManager.getPhotoInfoUrl(photoId);
        //Log.e("TAG", "PhotoInfo url: "+url);
        ViewHelper.showView(getView().findViewById(R.id.layout_loader));
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("TAG", "PhotoInfo "+response);
                ViewHelper.smoothToHide(getView().findViewById(R.id.layout_loader));
                try {
                    photoInfo = PhotoInfo.jsonToPhotoInfoData(response);
                } catch (JSONException e) {
                    //Log.e("TAG", "Error", e);
                }

                if (getActivity() != null) {
                    updateLayout();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ViewHelper.smoothToHide(getView().findViewById(R.id.layout_loader));
            }
        });
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void updateLayout() {
        //updateOwnerInformation();
        updatePhotoInformation();
    }

    private void updatePhotoInformation() {
        if (photoInfo == null) {
            return;
        }

        ViewHelper.smoothToShow(getView().findViewById(R.id.layout_content));

        // date
        try {
            long date = Long.parseLong(photoInfo.datePosted);
            if (date > 0) {
                tvDate.setText(Utils.getDateFromUnix(date));
            } else {
                ViewHelper.hideView(getView().findViewById(R.id.layout_date));
            }
        } catch (Exception ignored) {
            ViewHelper.hideView(getView().findViewById(R.id.layout_date));
        }

        // photo size
        if (!StringUtils.isEmpty(photo.size)) {
            tvSize.setText(photo.size);
        } else {
            ViewHelper.hideView(getView().findViewById(R.id.layout_size));
        }

        // title
        String title = "Image Information";
        if (!StringUtils.isEmpty(photoInfo.title)) {
            title = photoInfo.title;
        }
        tvTitle.setText(title);

        if (!StringUtils.isEmpty(photoInfo.photoUrl)) {
            tvUrl.setText(photoInfo.photoUrl);
        } else {
            ViewHelper.hideView(getView().findViewById(R.id.layout_url));
        }


        // copyright
        if (photoInfo.viewCount>0) {
            tvViewCount.setText(photoInfo.viewCount+"");
        } else {
            ViewHelper.hideView(getView().findViewById(R.id.layout_view_count));
        }
        License license = License.getLicense(photoInfo.license);
        tvCopyright.setText(license.getName());


        // description
        if (!StringUtils.isEmpty(photoInfo.description)){
            String text = "<b>Description</b><br>"+ photoInfo.description;
            tvDescription.setText(HtmlCompat.fromHtml(text));
        }else{
            ViewHelper.hideView(getView().findViewById(R.id.layout_description));
        }


    }

}
