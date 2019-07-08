package com.islamiat.blackwallpapers.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.islamiat.blackwallpapers.helper.FlickrManager;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

public class HistoryPhoto extends SugarRecord implements Parcelable {

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

    public HistoryPhoto() {
    }


    protected HistoryPhoto(Parcel in) {
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

    public static final Creator<HistoryPhoto> CREATOR = new Creator<HistoryPhoto>() {
        @Override
        public HistoryPhoto createFromParcel(Parcel in) {
            return new HistoryPhoto(in);
        }

        @Override
        public HistoryPhoto[] newArray(int size) {
            return new HistoryPhoto[size];
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

    public static long saveHistoryFromPhoto(Photo photo) {
        // delete history photo if already exist
        long count = SugarRecord.count(HistoryPhoto.class, "photo_Id=?", new String[]{photo.photoId});
        if (count > 0) {
            SugarRecord.deleteAll(HistoryPhoto.class, "photo_Id=?", photo.photoId);
        }

        return photoToHistoryPhoto(photo).save();
    }

    public static HistoryPhoto photoToHistoryPhoto(Photo photo){
        HistoryPhoto hp = new HistoryPhoto();
        hp.photoId = photo.photoId;
        hp.title = photo.title;
        hp.isPublic = photo.isPublic;
        hp.ownerName = photo.ownerName;
        hp.license = photo.license;
        hp.views = photo.views;
        hp.tags = photo.tags;
        hp.thumb = photo.thumb;
        hp.pic_m = photo.pic_m;
        hp.pic_h = photo.pic_h;
        hp.pic_xl = photo.pic_xl;
        hp.size = photo.size;

        return hp;
    }

    public static List<Photo> getHistoryPhotos(int limit, int offset) {
        limit = (limit > 0) ? limit : FlickrManager.LIMIT;

        List<Photo> pList = new ArrayList<>();

        String sql = "SELECT * FROM History_Photo ORDER BY ID DESC LIMIT " + limit + " OFFSET " + offset;

        List<HistoryPhoto> HistoryPhotoList = SugarRecord.findWithQuery(HistoryPhoto.class, sql);
        //Log.e("TAG", "HistoryPhotoList:" + HistoryPhotoList.size());
        for (HistoryPhoto hp : HistoryPhotoList) {
            Photo photo = new Photo();
            photo.photoId = hp.photoId;
            photo.title = hp.title;
            photo.isPublic = hp.isPublic;
            photo.ownerName = hp.ownerName;
            photo.license = hp.license;
            photo.views = hp.views;
            photo.tags = hp.tags;
            photo.thumb = hp.thumb;
            photo.pic_m = hp.pic_m;
            photo.pic_h = hp.pic_h;
            photo.pic_xl = hp.pic_xl;
            photo.size = hp.size;

            pList.add(photo);
        }


        return pList;
    }
}
