package com.islamiat.blackwallpapers.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Owner implements Parcelable {
	public String nsid ="";
	public String username ="";
	public String realname ="";
	public String location ="";
	public String iconserver ="";
	public String iconfarm ="";
	public String path_alias ="";

	public Owner() {
	}

	protected Owner(Parcel in) {
		nsid = in.readString();
		username = in.readString();
		realname = in.readString();
		location = in.readString();
		iconserver = in.readString();
		iconfarm = in.readString();
		path_alias = in.readString();
	}

	public static final Creator<Owner> CREATOR = new Creator<Owner>() {
		@Override
		public Owner createFromParcel(Parcel in) {
			return new Owner(in);
		}

		@Override
		public Owner[] newArray(int size) {
			return new Owner[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nsid);
		dest.writeString(username);
		dest.writeString(realname);
		dest.writeString(location);
		dest.writeString(iconserver);
		dest.writeString(iconfarm);
		dest.writeString(path_alias);
	}
}
