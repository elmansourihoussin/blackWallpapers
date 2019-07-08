package com.islamiat.blackwallpapers.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.islamiat.blackwallpapers.helper.TempData;
import com.ornach.andutils.java.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FlickrPhoto implements Parcelable {
    public long id = 0;
    public String owner = "";
    public String secret = "";
    public String server = "";
    public String farm = "";
    public String title = "";
    public boolean isPublic = false;
    public String ownerName = "";
    public String dateAdded = "";
    public int license = 0;
    public String description = "";
    public String dateTaken = "";
    public String views = "";
    public String tags = "";
    public String placeId = "";
    public String media = "";
    public String thumb = "";
    public String size_n = "";    // size Small 320 width="320" height="240
    public String url_n = "";    // Small 320 width="320" height="240
    public String size_z = "";    // size Medium 640 width="640" height="480"
    public String url_z = "";    // Medium 640 width="640" height="480"
    public String size_c = "";    // size Medium 800 width="800" height="600"
    public String url_c = "";    // Medium 800 width="800" height="600"
    public String size_l = "";    // size Large width="1024" height="768"
    public String url_l = "";    // Large width="1024" height="768"
    public String size_h = "";    // size Large 1600 width="1600" height="958"
    public String url_h = "";    // Large 1600 width="1600" height="958"
    public String size_k = "";    // size Large 2048 width="2048" height="1226"
    public String url_k = "";    // Large 2048 width="2048" height="1226"
    public String size_o = "";    // size Original" width="2400" height="1800"
    public String url_o = "";    // Original" width="2400" height="1800"

    public FlickrPhoto() {

    }

    protected FlickrPhoto(Parcel in) {
        id = in.readLong();
        owner = in.readString();
        secret = in.readString();
        server = in.readString();
        farm = in.readString();
        title = in.readString();
        isPublic = in.readByte() != 0;
        ownerName = in.readString();
        dateAdded = in.readString();
        license = in.readInt();
        description = in.readString();
        dateTaken = in.readString();
        views = in.readString();
        tags = in.readString();
        placeId = in.readString();
        media = in.readString();
        thumb = in.readString();
        size_n = in.readString();
        url_n = in.readString();
        size_z = in.readString();
        url_z = in.readString();
        size_c = in.readString();
        url_c = in.readString();
        size_l = in.readString();
        url_l = in.readString();
        size_h = in.readString();
        url_h = in.readString();
        size_k = in.readString();
        url_k = in.readString();
        size_o = in.readString();
        url_o = in.readString();
    }

    public static final Creator<FlickrPhoto> CREATOR = new Creator<FlickrPhoto>() {
        @Override
        public FlickrPhoto createFromParcel(Parcel in) {
            return new FlickrPhoto(in);
        }

        @Override
        public FlickrPhoto[] newArray(int size) {
            return new FlickrPhoto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(owner);
        dest.writeString(secret);
        dest.writeString(server);
        dest.writeString(farm);
        dest.writeString(title);
        dest.writeByte((byte) (isPublic ? 1 : 0));
        dest.writeString(ownerName);
        dest.writeString(dateAdded);
        dest.writeInt(license);
        dest.writeString(description);
        dest.writeString(dateTaken);
        dest.writeString(views);
        dest.writeString(tags);
        dest.writeString(placeId);
        dest.writeString(media);
        dest.writeString(thumb);
        dest.writeString(size_n);
        dest.writeString(url_n);
        dest.writeString(size_z);
        dest.writeString(url_z);
        dest.writeString(size_c);
        dest.writeString(url_c);
        dest.writeString(size_l);
        dest.writeString(url_l);
        dest.writeString(size_h);
        dest.writeString(url_h);
        dest.writeString(size_k);
        dest.writeString(url_k);
        dest.writeString(size_o);
        dest.writeString(url_o);
    }


    public static List<FlickrPhoto> jsonToPhotoList(String jsonString) throws JSONException {
        List<FlickrPhoto> list = new ArrayList<>();


        JSONObject json = new JSONObject(jsonString);
        JSONObject jPhotos = json.getJSONObject("photos");

        TempData.getInstance().page = JsonUtils.getInt(jPhotos, "page");
        TempData.getInstance().pages = JsonUtils.getInt(jPhotos, "pages");
        TempData.getInstance().perPage = JsonUtils.getInt(jPhotos, "perpage");
        int total = JsonUtils.getInt(jPhotos, "total");
        TempData.getInstance().total = total;
        if (total > 0) {
            JSONArray photoArray = jPhotos.getJSONArray("photo");
            list.addAll(parsePhotoJson(photoArray.toString()));
        }
        return list;
    }

    public static List<FlickrPhoto> parsePhotoJson(String jsonString) throws JSONException {
        List<FlickrPhoto> list = new ArrayList<>();

        JSONArray photoArray = new JSONArray(jsonString);

        for (int i = 0; i < photoArray.length(); i++) {
            JSONObject j = photoArray.getJSONObject(i);

            FlickrPhoto photo = new FlickrPhoto();
            photo.id = JsonUtils.getLong(j, "id");
            photo.owner = JsonUtils.getString(j, "owner");
            photo.secret = JsonUtils.getString(j, "secret");
            photo.server = JsonUtils.getString(j, "server");
            photo.farm = JsonUtils.getString(j, "farm");
            photo.title = JsonUtils.getString(j, "title");
            photo.isPublic = JsonUtils.getBoolean(j, "ispublic");
            photo.ownerName = JsonUtils.getString(j, "ownername");

				/*long dateAdded = JsonUtils.getLong(j, "dateadded");
                photo.dateAdded = getDateFromUnix(dateAdded); */

            photo.license = JsonUtils.getInt(j, "license");

				/* JSONObject description = j.getJSONObject("description");
                photo.description = JsonUtils.getString(description, "_content"); */

				/* String datetaken = JsonUtils.getString(j, "datetaken");
                photo.dateTaken = getDateFromString(datetaken); */

            //photo.views = JsonUtils.getString(j, "views");
            photo.tags = JsonUtils.getString(j, "tags");
            //photo.placeId = JsonUtils.getString(j, "place_id");
            photo.media = JsonUtils.getString(j, "media");
            photo.thumb = JsonUtils.getString(j, "url_n");

            photo.url_n = JsonUtils.getString(j, "url_n");

            if (j.has("width_n") && j.has("height_n"))
                photo.size_n = JsonUtils.getString(j, "width_n") + " * " + JsonUtils.getString(j, "height_n");

            photo.url_z = JsonUtils.getString(j, "url_z");

            if (j.has("width_z") && j.has("height_z"))
                photo.size_z = JsonUtils.getString(j, "width_z") + " * " + JsonUtils.getString(j, "height_z");

            photo.url_c = JsonUtils.getString(j, "url_c");

            if (j.has("width_c") && j.has("height_c"))
                photo.size_c = JsonUtils.getString(j, "width_c") + " * " + JsonUtils.getString(j, "height_c");

            photo.url_l = JsonUtils.getString(j, "url_l");

            if (j.has("width_l") && j.has("height_l"))
                photo.size_l = JsonUtils.getString(j, "width_l") + " * " + JsonUtils.getString(j, "height_l");

            photo.url_h = JsonUtils.getString(j, "url_h");

            if (j.has("width_h") && j.has("height_h"))
                photo.size_h = JsonUtils.getString(j, "width_h") + " * " + JsonUtils.getString(j, "height_h");

            photo.url_k = JsonUtils.getString(j, "url_k");

            if (j.has("width_k") && j.has("height_k"))
                photo.size_k = JsonUtils.getString(j, "width_k") + " * " + JsonUtils.getString(j, "height_k");

            photo.url_o = JsonUtils.getString(j, "url_o");

            if (j.has("width_o") && j.has("height_o"))
                photo.size_o = JsonUtils.getString(j, "width_o") + " * " + JsonUtils.getString(j, "height_o");

            if (photo.media.equalsIgnoreCase("photo")) {
                list.add(photo);

            }


        }


        return list;
    }
}
