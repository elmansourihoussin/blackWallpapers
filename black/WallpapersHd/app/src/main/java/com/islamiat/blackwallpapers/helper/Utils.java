package com.islamiat.blackwallpapers.helper;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.model.FlickrPhoto;
import com.islamiat.blackwallpapers.model.HistoryPhoto;
import com.orm.SugarRecord;
import com.ornach.andutils.java.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {

    public static final String EXTRA_PHOTO = "EXTRA_PHOTO";
    public static final String EXTRA_PHOTO_INFO = "EXTRA_PHOTO_INFO";
    public static final String EXTRA_PHOTO_SIZE = "EXTRA_PHOTO_SIZE";
    public static final String EXTRA_LIST = "EXTRA_LIST";
    /*public static final String EXTRA_STRING_LIST = "EXTRA_STRING_LIST";*/
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    public static final String EXTRA_PAGE_NO = "EXTRA_PAGE_NO";
    public static final String EXTRA_TAG = "EXTRA_TAG";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_SEARCH_KEYWORD = "EXTRA_SEARCH_KEYWORD";
    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    public static final String EXTRA_FLAG = "EXTRA_FLAG";
    public static final String EXTRA_TEXT = "EXTRA_TEXT";

    public static final int FLAG_PHOTO_RECENT = 501;
    public static final int FLAG_PHOTO_POPULAR = 502;
    public static final int FLAG_PHOTO_SAVED = 503;
    public static final int FLAG_PHOTO_HISTORY = 504;
    public static final int FLAG_PHOTO_USER = 505;
    public static final int FLAG_PHOTO_SEARCH = 506;
    public static final int FLAG_PHOTO_TAG = 507;

    public static String getMediumPicUrlFromPhoto(FlickrPhoto photo) {
        String picUrl = "";

        if (!StringUtils.isEmpty(photo.url_c)) {
            picUrl = photo.url_c;
        } else if (!StringUtils.isEmpty(photo.url_n)) {
            picUrl = photo.url_n;
        }

        return picUrl;
    }

    public static String getLargePicUrlFromPhoto(FlickrPhoto photo) {
        String picUrl = "";

        if (!StringUtils.isEmpty(photo.url_l)) {
            picUrl = photo.url_l;
        } else if (!StringUtils.isEmpty(photo.url_c)) {
            picUrl = photo.url_c;
        } else if (!StringUtils.isEmpty(photo.url_n)) {
            picUrl = photo.url_n;
        }

        return picUrl;
    }

    public static String getMaxPicUrlFromPhoto(FlickrPhoto photo) {
        String picUrl = "";

        /*if (!StringUtils.isEmpty(photo.url_o)) {
            picUrl = photo.url_o;
        } else*/ if (!StringUtils.isEmpty(photo.url_k)) {
            picUrl = photo.url_k;
        } else if (!StringUtils.isEmpty(photo.url_h)) {
            picUrl = photo.url_h;
        } else if (!StringUtils.isEmpty(photo.url_l)) {
            picUrl = photo.url_l;
        } else if (!StringUtils.isEmpty(photo.url_c)) {
            picUrl = photo.url_c;
        } else {
            picUrl = photo.url_n;
        }

        return picUrl;
    }

    public static String getDateFromUnix(long timeStamp) {
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM, yyyy");
        String newDate = sdf.format(date);
        return newDate;
    }

    public static String getMaxLargePhotoSize(FlickrPhoto photo) {
        /*if (!StringUtils.isEmpty(photo.size_o)) {
            return photo.size_o;
        } else if (!StringUtils.isEmpty(photo.size_k)) {
            return photo.size_k;
        } else*/ if (!StringUtils.isEmpty(photo.size_h)) {
            return photo.size_h;
        } else if (!StringUtils.isEmpty(photo.size_l)) {
            return photo.size_l;
        } else if (!StringUtils.isEmpty(photo.size_c)) {
            return photo.size_c;
        } else if (!StringUtils.isEmpty(photo.size_z)) {
            return photo.size_z;
        } else if (!StringUtils.isEmpty(photo.size_n)) {
            return photo.size_n;
        }

        return "";
    }


    public static void goMoreApps(Context context) {
        Uri uriMarket = Uri.parse("market://dev?id=EL-Mansouri");
        Uri uriHttp = Uri.parse("http://play.google.com/store/apps/developer?id=EL-Mansouri");

        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, uriMarket));
            //Log.e("TAG", "uriMarket not resolved");
        } catch (ActivityNotFoundException e) {

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, uriHttp));
            } catch (Exception ex) {

            }
        }

    }

    public static void shareApps(Context context) {
        String appUrl = "http://play.google.com/store/apps/details?id=" + context.getPackageName();
        String appMarket = "market://details?id=" + context.getPackageName();

        String title = context.getString(R.string.app_name);
        //String msg = "Hey check out android app for HD Background at: " + appUrl;
        String msg = "HD Background. Hey check out android app for HD Background at: " + appUrl;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //intent.setData(Uri.parse(appUrl));
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, msg);

        try {
            //context.startActivity(intent);
            context.startActivity(Intent.createChooser(intent, "Share via"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " Sorry, Not able to Share!", Toast
                    .LENGTH_SHORT).show();
        }
    }

    public static void clearHistory(Context context) {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Clear History");
        d.setMessage("Are you want to clear history?");
        d.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SugarRecord.deleteAll(HistoryPhoto.class);
                dialog.dismiss();
            }
        });

        d.create().show();
    }
}
