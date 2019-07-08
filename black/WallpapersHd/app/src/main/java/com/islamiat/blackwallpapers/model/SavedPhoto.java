package com.islamiat.blackwallpapers.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.islamiat.blackwallpapers.helper.FlickrManager;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class SavedPhoto extends SugarRecord implements Parcelable {

    public long id = 0;
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

    public SavedPhoto() {
    }


    protected SavedPhoto(Parcel in) {
        id = in.readLong();
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

    public static final Creator<SavedPhoto> CREATOR = new Creator<SavedPhoto>() {
        @Override
        public SavedPhoto createFromParcel(Parcel in) {
            return new SavedPhoto(in);
        }

        @Override
        public SavedPhoto[] newArray(int size) {
            return new SavedPhoto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
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

    public static SavedPhoto photoToSavedPhoto(Photo photo){
        SavedPhoto fp = new SavedPhoto();
        fp.photoId = photo.photoId;
        fp.title = photo.title;
        fp.isPublic = photo.isPublic;
        fp.ownerName = photo.ownerName;
        fp.license = photo.license;
        fp.views = photo.views;
        fp.tags = photo.tags;
        fp.thumb = photo.thumb;
        fp.pic_m = photo.pic_m;
        fp.pic_h = photo.pic_h;
        fp.pic_xl = photo.pic_xl;
        fp.size = photo.size;

        return fp;
    }

    public static List<Photo> getFavoritePhotos(int limit, int offset) {
        limit = (limit > 0) ? limit : FlickrManager.LIMIT;

        List<Photo> pList = new ArrayList<>();

        String sql = "SELECT * FROM Saved_Photo ORDER BY ID DESC LIMIT " + limit + " OFFSET " + offset;

        List<SavedPhoto> savedPhotoList = SugarRecord.findWithQuery(SavedPhoto.class, sql);
        //Log.e("TAG", "photoList:" + favPhotoList.size());
        for (SavedPhoto fp : savedPhotoList) {
            Photo photo = new Photo();
            photo.photoId = fp.photoId;
            photo.title = fp.title;
            photo.isPublic = fp.isPublic;
            photo.ownerName = fp.ownerName;
            photo.license = fp.license;
            photo.views = fp.views;
            photo.tags = fp.tags;
            photo.thumb = fp.thumb;
            photo.pic_m = fp.pic_m;
            photo.pic_h = fp.pic_h;
            photo.pic_xl = fp.pic_xl;
            photo.size = fp.size;

            pList.add(photo);
        }


        return pList;
    }
}
