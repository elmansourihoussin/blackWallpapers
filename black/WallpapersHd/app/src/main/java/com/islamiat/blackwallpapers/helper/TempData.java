package com.islamiat.blackwallpapers.helper;


public class TempData {

	private static TempData tempData;

	public int page=0;
	public int pages=0;
	public int perPage=0;
	public int total=0;


	//public ArrayList<Photo> INTENT_EXTRA_LIST;
	//private final Map<String, Object> extras = new HashMap<>();

	public static TempData getInstance(){
		if (tempData == null){
			tempData = new TempData();
		}

		return tempData;
	}
}
