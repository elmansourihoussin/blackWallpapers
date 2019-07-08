package com.islamiat.blackwallpapers.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.ornach.andutils.java.JsonValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoInfo implements Parcelable {
	public String id ="";
	public String secret ="";
	public String server ="";
	public String farm ="";
	public String datePosted ="";
	public int license=0;
	public String originalFormat ="";
	public String title ="";
	public String description ="";
	public boolean isPublic=false;
	public String dateTaken ="";
	public int viewCount =0;
	public int canDownload=0;
	public int canShare=0;
	public int commentCount=0;
	public String photoUrl ="";

	public Owner owner;

    public PhotoInfo(){

    }

	protected PhotoInfo(Parcel in) {
		id = in.readString();
		secret = in.readString();
		server = in.readString();
		farm = in.readString();
		datePosted = in.readString();
		license = in.readInt();
		originalFormat = in.readString();
		title = in.readString();
		description = in.readString();
		isPublic = in.readByte() != 0;
		dateTaken = in.readString();
		viewCount = in.readInt();
		canDownload = in.readInt();
		canShare = in.readInt();
		commentCount = in.readInt();
		photoUrl = in.readString();
		owner = in.readParcelable(Owner.class.getClassLoader());
	}

	public static final Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
		@Override
		public PhotoInfo createFromParcel(Parcel in) {
			return new PhotoInfo(in);
		}

		@Override
		public PhotoInfo[] newArray(int size) {
			return new PhotoInfo[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeString(id);
		dest.writeString(secret);
		dest.writeString(server);
		dest.writeString(farm);
		dest.writeString(datePosted);
		dest.writeInt(license);
		dest.writeString(originalFormat);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeByte((byte) (isPublic ? 1 : 0));
		dest.writeString(dateTaken);
		dest.writeInt(viewCount);
		dest.writeInt(canDownload);
		dest.writeInt(canShare);
		dest.writeInt(commentCount);
		dest.writeString(photoUrl);
		dest.writeParcelable(owner, flags);
	}

	public static PhotoInfo jsonToPhotoInfoData(String jsonString) throws JSONException {
		JsonValidator jv = new JsonValidator();

		JSONObject json = new JSONObject(jsonString);

		PhotoInfo photoInfo = new PhotoInfo();

		if (json.has("photo")) {
			JSONObject jsonPhoto = json.getJSONObject("photo");
			//Log.e("TAG", photo.toString());
			photoInfo.id = jv.getString(jsonPhoto, "id");
			photoInfo.secret = jv.getString(jsonPhoto, "secret");
			photoInfo.server = jv.getString(jsonPhoto, "server");
			photoInfo.farm = jv.getString(jsonPhoto, "farm");
			photoInfo.license = jv.getInt(jsonPhoto, "license");
			photoInfo.originalFormat = jv.getString(jsonPhoto, "originalformat");

			JSONObject jt = jsonPhoto.getJSONObject("title");
			photoInfo.title = jv.getString(jt, "_content");

			JSONObject jd = jsonPhoto.getJSONObject("description");
			photoInfo.description = jv.getString(jd, "_content");

			JSONObject jvi = jsonPhoto.getJSONObject("visibility");
			photoInfo.isPublic = jv.getBoolean(jvi, "ispublic");

			JSONObject jdt = jsonPhoto.getJSONObject("dates");
			photoInfo.datePosted = jv.getString(jdt, "posted");
			photoInfo.dateTaken = jv.getString(jdt, "taken");

			photoInfo.viewCount = jv.getInt(jsonPhoto, "views");

			JSONObject jUsage = jsonPhoto.getJSONObject("usage");
			photoInfo.canDownload = jv.getInt(jUsage, "candownload");
			photoInfo.canShare = jv.getInt(jUsage, "canshare");
			//Log.e("TAG", photoInfo.canShare +"  "+photoInfo.id);

			JSONObject jcmt = jsonPhoto.getJSONObject("comments");
			photoInfo.commentCount = jv.getInt(jcmt, "_content");

			// get photo url
			if (jsonPhoto.has("urls")){
				JSONObject urls = jsonPhoto.getJSONObject("urls");
				if (urls.has("url")){
					JSONArray url = urls.getJSONArray("url");
					if (url.length()>0){
						JSONObject urlObj = url.getJSONObject(0);
						if (urlObj.has("_content")){
							photoInfo.photoUrl = jv.getString(urlObj,"_content");
						}
					}
				}

			}


			// get Owner information
			Owner owner = new Owner();
			JSONObject jsonOwner = jsonPhoto.getJSONObject("owner");
			owner.nsid = jv.getString(jsonOwner, "nsid");
			owner.username = jv.getString(jsonOwner, "username");
			owner.realname = jv.getString(jsonOwner, "realname");
			owner.location = jv.getString(jsonOwner, "location");
			owner.iconserver = jv.getString(jsonOwner, "iconserver");
			owner.iconfarm = jv.getString(jsonOwner, "iconfarm");
			owner.path_alias = jv.getString(jsonOwner, "path_alias");

			photoInfo.owner = owner;

		}

		return photoInfo;

	}
}
