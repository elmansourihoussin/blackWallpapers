package com.islamiat.blackwallpapers.model;

import java.util.ArrayList;
import java.util.List;

public class License {

	int id=0;
	String name ="";
	String url ="";

	public License() {
	}

	public License(int id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private static List<License> getLicenseList() {
		List<License> list = new ArrayList<>();
		list.add(new License(0, "All Rights Reserved", ""));
		list.add(new License(1, "Attribution-NonCommercial-ShareAlike License", "http://creativecommons.org/licenses/by-nc-sa/2.0/"));
		list.add(new License(2, "Attribution-NonCommercial License", "http://creativecommons.org/licenses/by-nc/2.0/"));
		list.add(new License(3, "Attribution-NonCommercial-NoDerivs License", "http://creativecommons.org/licenses/by-nc-nd/2.0/"));
		list.add(new License(4, "Attribution License", "http://creativecommons.org/licenses/by/2.0/"));
		list.add(new License(5, "Attribution-ShareAlike License", "http://creativecommons.org/licenses/by-sa/2.0/"));
		list.add(new License(6, "Attribution-NoDerivs License", "http://creativecommons.org/licenses/by-nd/2.0/"));
		list.add(new License(7, "No known copyright restrictions", "http://flickr.com/commons/usage/"));
		list.add(new License(8, "United States Government Work", "http://www.usa.gov/copyright.shtml"));
		list.add(new License(9, "Public Domain Dedication (CC0)", "https://creativecommons.org/publicdomain/zero/1.0/"));
		list.add(new License(10, "Public Domain Mark", "https://creativecommons.org/publicdomain/mark/1.0/"));

		return list;
	}

	public static License getLicense(int id){
		List<License> licenses = getLicenseList();
		License license;
		try {
			license =licenses.get(id);
		}catch (Exception e){
			license = new License(0, "Unknown","");
		}
		return license;
	}
}
