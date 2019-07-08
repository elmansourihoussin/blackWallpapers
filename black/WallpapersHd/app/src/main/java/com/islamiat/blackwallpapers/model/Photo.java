package com.islamiat.blackwallpapers.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.islamiat.blackwallpapers.helper.Utils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Photo implements Parcelable {

    //public long id = 0;
    public String photoId = "";
    public String title = "";
    public boolean isPublic = false;
    public String ownerName = "";
    //public String dateAdded = "";
    public int license = 0;
    public String views = "";
    public String tags = "";
    public String thumb = "";
    public String pic_m="";
    public String pic_h="";
    public String pic_xl="";
    public String size="";

    public Photo() {
    }


    protected Photo(Parcel in) {
        //id = in.readLong();
        photoId = in.readString();
        title = in.readString();
        isPublic = in.readByte() != 0;
        ownerName = in.readString();
        license = in.readInt();
        views = in.readString();
        tags = in.readString();
        thumb = in.readString();
        pic_m = in.readString();
        pic_h = in.readString();
        pic_xl = in.readString();
        size = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public static List<Photo> flickrPhotoToPhoto(List<FlickrPhoto> flickrPhotos)throws JSONException {
        List<Photo> list = new ArrayList<>();

        for (FlickrPhoto fPhoto : flickrPhotos){
            Photo photo = new Photo();
            photo.photoId= String.valueOf(fPhoto.id);
            photo.title= fPhoto.title;
            photo.isPublic= fPhoto.isPublic;
            photo.ownerName= fPhoto.ownerName;
            //photo.dateAdded= fPhoto.dateAdded;
            photo.license= fPhoto.license;
            photo.views= fPhoto.views;
            photo.tags= fPhoto.tags;
            photo.thumb= fPhoto.thumb;
            photo.pic_m= Utils.getMediumPicUrlFromPhoto(fPhoto);
            photo.pic_h= Utils.getLargePicUrlFromPhoto(fPhoto);
            photo.pic_xl= Utils.getMaxPicUrlFromPhoto(fPhoto);
            photo.size = Utils.getMaxLargePhotoSize(fPhoto);
            //Log.e("TAG", "photo.size "+ photo.size);

            list.add(photo);
        }

        return list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoId);
        dest.writeString(title);
        dest.writeByte((byte) (isPublic ? 1 : 0));
        dest.writeString(ownerName);
        dest.writeInt(license);
        dest.writeString(views);
        dest.writeString(tags);
        dest.writeString(thumb);
        dest.writeString(pic_m);
        dest.writeString(pic_h);
        dest.writeString(pic_xl);
        dest.writeString(size);
    }
}
