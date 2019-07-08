package com.islamiat.blackwallpapers.helper;


import android.text.TextUtils;

public class FlickrManager {

    //Flickr API key
    private static final String FLICKR_API_KEY = "7224d67035c3308e62a26f698b9831d6";

    // flickr group id
    private static final String GROUP_ID = "14648044@N23";



    public static final int LIMIT = 50;

    private static final String BASE_URL = "https://api.flickr.com/services/rest/";
    private static final String METHOD_POOLS_GET_PHOTO = "?method=flickr.groups.pools.getPhotos";
    private static final String API_STRING = "&api_key=" + FLICKR_API_KEY;
    private static final String SORT_STRING = "&sort=";
    private static final String GROUP_STRING = "&group_id=" + GROUP_ID;
    private static final String TAG_STRING = "&tags=";
    private static final String EXTRA_PHOTO = "&extras=" + "license,tags,media,url_q,url_n,url_c,url_l,url_h,url_k,url_o";
    private static final String PER_PAGE_STRING = "&per_page=";
    private static final String PAGE_NUMBER_STRING = "&page=";
    private static final String FORMAT_STRING = "&format=json";
    private static final String CALLBACK = "&nojsoncallback=1";


    private static String getLimit(int limit) {
        if (limit > 0) {
            return PER_PAGE_STRING + limit;
        } else {
            return PER_PAGE_STRING + LIMIT;
        }
    }

    private static String getPageNumber(int i) {
        if (i > 1) {
            return PAGE_NUMBER_STRING + i;
        } else {
            return PAGE_NUMBER_STRING + 1;
        }
    }

    private static String getTagString(String tag) {
        if (tag != null && !tag.isEmpty()) {
            return TAG_STRING + tag;
        } else {
            return "";
        }
    }


    private static String getSortString(String key) {

        if (!TextUtils.isEmpty(key)) {
            return SORT_STRING + key;
        } else {
            return SORT_STRING + "relevance";

        }

    }

    public static String getPhotoUrl() {
        return getPhotoUrl(null, 0, 0);
    }

    public static String getPhotoUrl(String tag, int pageNo) {
        return getPhotoUrl(tag, 0, pageNo);
    }

    public static String getPhotoUrl(String tag, int limit, int pageNo) {
        String url = BASE_URL + METHOD_POOLS_GET_PHOTO + API_STRING + GROUP_STRING + getTagString(tag) + EXTRA_PHOTO + getLimit(limit) + getPageNumber(pageNo)
                + FORMAT_STRING + CALLBACK;
        return url;
    }

    public static String searchPhotoUrl(String query) {
        return searchPhotoUrl(query, null, 0, 0);
    }

    public static String searchPhotoUrl(String query, int pageNo) {
        return searchPhotoUrl(query, null, 0, pageNo);
    }

    public static String searchPhotoUrl(String query, String tag, int pageNo) {
        return searchPhotoUrl(query, tag, 0, pageNo);
    }

    public static String searchPhotoUrl(String query, String tag, int limit, int pageNo) {
        String QUERY_STRING = "";
        if (query != null && !query.isEmpty()) {
            QUERY_STRING = "&text=" + query;
        }
        String url = BASE_URL + "?method=flickr.photos.search" + API_STRING + QUERY_STRING + getSortString(null) + GROUP_STRING + getTagString
                (tag) + EXTRA_PHOTO + getLimit(limit) + getPageNumber(pageNo) + FORMAT_STRING + CALLBACK;
        return url;
    }

    public static String tagPhotoUrl(String tag, int pageNo) {
        return searchPhotoUrl(null, tag, 0, pageNo);
    }

    public static String tagPhotoUrl(String tag, int limit, int pageNo) {
        return searchPhotoUrl(null, tag, limit, pageNo);
    }


    public static String getPhotoInfoUrl(String photoId) {
        String url = null;
        if (!TextUtils.isEmpty(photoId)) {
            url = BASE_URL + "?method=flickr.photos.getInfo" + API_STRING + "&photo_id=" + photoId + FORMAT_STRING + CALLBACK;
        }
        return url;
    }


    public static String getUserPublicPhotos(String userId, int limit, int pageNo) {
        String url = BASE_URL + "?method=flickr.people.getPublicPhotos" + API_STRING + "&user_id=" + userId + EXTRA_PHOTO + getLimit(limit) + getPageNumber(pageNo) + FORMAT_STRING + CALLBACK;
        return url;
    }

}
