package com.islamiat.blackwallpapers.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.fragment.PhotoFragment;
import com.islamiat.blackwallpapers.fragment.PhotoInformationDialogFragment;
import com.islamiat.blackwallpapers.helper.ActionHelper;
import com.islamiat.blackwallpapers.helper.AppController;
import com.islamiat.blackwallpapers.helper.FlickrManager;
import com.islamiat.blackwallpapers.helper.GlideManager;
import com.islamiat.blackwallpapers.helper.ParallaxTransformer;
import com.islamiat.blackwallpapers.helper.TempData;
import com.islamiat.blackwallpapers.helper.Utils;
import com.islamiat.blackwallpapers.model.FlickrPhoto;
import com.islamiat.blackwallpapers.model.HistoryPhoto;
import com.islamiat.blackwallpapers.model.Photo;
import com.islamiat.blackwallpapers.model.SavedPhoto;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.orm.SugarRecord;
import com.ornach.andutils.android.ViewHelper;
import com.ornach.bitpermission.BitPermission;
import com.ornach.bitpermission.PermissionListener;
import com.ornach.magicicon.IconButton;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PhotoActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private View layoutLoader;
    private SectionsPagerAdapter mSectionsPagerAdapter = null;
    IconButton btnFav;

    private List<Photo> listPhoto;
    public int position = 0;
    private int pageNo = 1;
    private String tag, keyword, userId;
    private int flag;

    // load more when viewpager change
    private int pageLimit = 5;
    boolean isLoadMore = true;

    private final long REQUEST_TIME_OUT = 5 * 1000;
    private static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent intent = getIntent();
        if (intent != null) {
            listPhoto = intent.getParcelableArrayListExtra(Utils.EXTRA_LIST);
            position = intent.getIntExtra(Utils.EXTRA_POSITION, 0);
            pageNo = intent.getIntExtra(Utils.EXTRA_PAGE_NO, 0);
            tag = intent.getStringExtra(Utils.EXTRA_TAG);
            userId = intent.getStringExtra(Utils.EXTRA_USER_ID);
            keyword = intent.getStringExtra(Utils.EXTRA_SEARCH_KEYWORD);
            flag = intent.getIntExtra(Utils.EXTRA_FLAG, Utils.FLAG_PHOTO_RECENT);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        layoutLoader = findViewById(R.id.layout_loader);
        btnFav = findViewById(R.id.btn_favorite);

        mViewPager.setOffscreenPageLimit(3);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new ParallaxTransformer());
        mViewPager.setCurrentItem(position);
        mSectionsPagerAdapter.notifyDataSetChanged();


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int i) {
                position = i;

                int targetPoint = listPhoto.size() - pageLimit;
                if (position > targetPoint) {
                    if (isLoadMore && position == targetPoint + 1) {
                        updatePhotos();
                    }
                }

                updateFavButton();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        updateFavButton();

        //Log.e("TAG", "listPhoto "+listPhoto.size());
    }



    boolean isPhotoSaved = false;
    private void updateFavButton() {
        Photo photo = listPhoto.get(position);
        long count = SugarRecord.count(SavedPhoto.class, "photo_Id=?", new String[]{photo.photoId});
        if (count > 0) {
            isPhotoSaved = true;
            btnFav.setIconColor(Color.parseColor("#FFEA00"));
        } else {
            isPhotoSaved = false;
            btnFav.setIconColor(Color.parseColor("#DDDDDD"));
        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_info:
                showInformationDialog();
                break;
            case R.id.btn_download:
                onDownload();
                break;
            case R.id.btn_wallpaper:
                onWallpaper();
                break;
            case R.id.btn_share:
                onShare();
                break;
            case R.id.btn_favorite:
                onFavorite();
                break;

        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           Photo  photo = listPhoto.get(position);
            //PhotoActivity.this.position = position;
            //Log.e("TAG", " photo == null "+ (photo == null));
            return PhotoFragment.newInstance(photo);
        }

        @Override
        public int getCount() {
            //int size = listPhoto.size() > 0 ? listPhoto.size() : mViewPagerSize;
            return listPhoto.size();
        }

    }


    private void updatePhotos() {
        pageNo++;
        String url = "";

        // for Favorite (database),
        // get photos from database and return method
        if (flag == Utils.FLAG_PHOTO_SAVED) {
            updatePhotosFromFavorite();
            return;
        } else if (flag == Utils.FLAG_PHOTO_HISTORY) {
            updatePhotosFromHistory();
            return;
        }


        // if it is not favorite then get photos from flickr
        switch (flag) {
            case Utils.FLAG_PHOTO_RECENT:
                url = FlickrManager.getPhotoUrl(null, pageNo);
                break;

            case Utils.FLAG_PHOTO_SEARCH:
                url = FlickrManager.searchPhotoUrl(keyword, "", pageNo);
                break;

            case Utils.FLAG_PHOTO_TAG:
                url = FlickrManager.searchPhotoUrl("", tag, pageNo);
                break;
            default:
                break;

        }

        updatePhotosFromServer(url);

    }

    private void updatePhotosFromFavorite() {
        int offset = FlickrManager.LIMIT * this.pageNo;

        if (offset < SugarRecord.count(SavedPhoto.class)) {

            List<Photo> list = SavedPhoto.getFavoritePhotos(FlickrManager.LIMIT, offset);

            if (list.size() > 0) {
                listPhoto.addAll(list);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.invalidate();
                isLoadMore = true;
            } else {
                isLoadMore = false;
            }

        }
    }

    private void updatePhotosFromHistory() {
        int offset = FlickrManager.LIMIT * this.pageNo;

        if (offset < SugarRecord.count(HistoryPhoto.class)) {
            List<Photo> list = HistoryPhoto.getHistoryPhotos(FlickrManager.LIMIT, offset);

            if (list.size() > 0) {
                listPhoto.addAll(list);
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.invalidate();
                isLoadMore = true;
            } else {
                isLoadMore = false;
            }

        }
    }

    private void updatePhotosFromServer(String url) {
        url = url.replace(" ", "%20");
        //Log.e("TAG", "updatePhotosFromServer "+url);
        if (pageNo < TempData.getInstance().pages) {
            StringRequest jsonReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        List<FlickrPhoto> flickrPhotoList = FlickrPhoto.jsonToPhotoList(response);
                        if (flickrPhotoList.size() > 0) {
                            List<Photo> list = Photo.flickrPhotoToPhoto(flickrPhotoList);
                            listPhoto.addAll(list);
                            mSectionsPagerAdapter.notifyDataSetChanged();
                            mViewPager.invalidate();
                            isLoadMore = true;
                        } else {
                            isLoadMore = false;
                        }
                    } catch (JSONException e) {
                        //e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            AppController.getInstance().addToRequestQueue(jsonReq);
        } else {
            isLoadMore = false;
        }
    }


    private void showInformationDialog() {
        Photo photo = listPhoto.get(position);
        if (photo != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            PhotoInformationDialogFragment newFragment = PhotoInformationDialogFragment.newInstance(photo);
            newFragment.show(ft, "dialog_photo_information");
        }
    }


    private void permissionRequest(PermissionListener listener) {
        BitPermission bitPermission = BitPermission.with(this)
                .setPermissionListener(listener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .build();
        bitPermission.request();
    }

    private void onDownload() {
        final Photo photo = listPhoto.get(position);
        HistoryPhoto.saveHistoryFromPhoto(photo);

        permissionRequest(new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                ViewHelper.showView(layoutLoader);
                GlideManager.loadBitmap(PhotoActivity.this, photo.pic_h, new GlideManager.GlideListener() {
                    @Override
                    public void onError(Exception e) {
                        ViewHelper.hideView(layoutLoader);
                        Toasty.error(PhotoActivity.this, "Image is not downloaded!").show();
                    }

                    @Override
                    public void onReady(Bitmap bitmap) {
                        ViewHelper.hideView(layoutLoader);
                        ActionHelper.downloadFromBitmap(PhotoActivity.this, bitmap);
                    }

                    @Override
                    public void onStart() {

                    }
                });
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }

        });
    }

    private void onWallpaper() {
        final Photo photo = listPhoto.get(position);
        HistoryPhoto.saveHistoryFromPhoto(photo);

        permissionRequest(new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                ViewHelper.showView(layoutLoader);
                GlideManager.loadBitmap(PhotoActivity.this, photo.pic_h, new GlideManager.GlideListener() {
                    @Override
                    public void onError(Exception e) {
                        ViewHelper.hideView(layoutLoader);
                        Toasty.error(PhotoActivity.this, "Image is not downloaded!").show();
                    }

                    @Override
                    public void onReady(Bitmap bitmap) {
                        ViewHelper.hideView(layoutLoader);

                        Uri uri = ActionHelper.generateImageUri(PhotoActivity.this, bitmap);
                        Intent intent = new Intent(PhotoActivity.this, WallpaperActivity.class);
                        intent.putExtra("URI", uri.toString());
                        startActivity(intent);
                    }

                    @Override
                    public void onStart() {

                    }
                });

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }

        });
    }


    private void onShare() {
        final Photo photo = listPhoto.get(position);
        HistoryPhoto.saveHistoryFromPhoto(photo);

        permissionRequest(new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                ViewHelper.showView(layoutLoader);
                GlideManager.loadBitmap(PhotoActivity.this, photo.pic_h, new GlideManager.GlideListener() {
                    @Override
                    public void onError(Exception e) {
                        ViewHelper.hideView(layoutLoader);
                        Toasty.error(PhotoActivity.this, "Image is not downloaded!").show();
                    }

                    @Override
                    public void onReady(Bitmap bitmap) {
                        ViewHelper.hideView(layoutLoader);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        Uri uri = ActionHelper.generateImageUri(PhotoActivity.this, bitmap);
                        intent.setDataAndType(uri, "image/*");
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(intent, "Share Image!"));
                    }

                    @Override
                    public void onStart() {

                    }
                });
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }

        });
    }

    private void onFavorite() {
        Photo photo = listPhoto.get(position);
        if (isPhotoSaved) {
            long id = SugarRecord.deleteAll(SavedPhoto.class, "photo_Id=?", photo.photoId);
        } else {
            SavedPhoto savedPhoto = SavedPhoto.photoToSavedPhoto(photo);
            long id = savedPhoto.save();
        }

        updateFavButton();
    }
}
