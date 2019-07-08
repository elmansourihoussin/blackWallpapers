package com.islamiat.blackwallpapers.activity;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.islamiat.blackwallpapers.R;
import com.ornach.magicicon.IconView;
import com.theartofdev.edmodo.cropper.CropImageView;

import es.dmoral.toasty.Toasty;

public class WallpaperActivity extends AppCompatActivity {

    CropImageView imvCropImage;
    TextView tvScrollable, tvFixed;
    IconView iconScrollable, iconFixed;

    String stringUri;
    Uri imgUri;

    int wallpaperType = 0;
    private AsyncTask<String, String, String> wallpaperAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        if (getIntent() != null && getIntent().hasExtra("URI")) {
            stringUri = getIntent().getStringExtra("URI");
            imgUri = Uri.parse(stringUri);
        }

        imvCropImage = (CropImageView) findViewById(R.id.cropImageView);

        iconScrollable = (IconView) findViewById(R.id.icon_scrollable);
        iconFixed = (IconView) findViewById(R.id.icon_fixed);
        tvScrollable = (TextView) findViewById(R.id.tv_scrollable);
        tvFixed = (TextView) findViewById(R.id.tv_fixed);

        if (imgUri == null) {
            return;
        }

        imvCropImage.setImageUriAsync(imgUri);

        updateButton(0);
        imvCropImage.setAspectRatio(6, 5);

    }

    public void updateButton(int id) {

        tvScrollable.setTextColor(Color.WHITE);
        tvFixed.setTextColor(Color.WHITE);

        iconScrollable.setIconColor(Color.WHITE);
        iconFixed.setIconColor(Color.WHITE);

        int activeColor = Color.parseColor("#34DB73");

        switch (id) {
            case R.id.btn_fixed:
                tvFixed.setTextColor(activeColor);
                iconFixed.setIconColor(activeColor);
                break;
            case R.id.btn_scrollable:
            default:
                tvScrollable.setTextColor(activeColor);
                iconScrollable.setIconColor(activeColor);
                break;
        }

    }

    public void onClickRationButton(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_scrollable:
                wallpaperType = 1;
                imvCropImage.setAspectRatio(6, 5);
                break;
            case R.id.btn_fixed:
                wallpaperType = 3;
                asFixedRatio();
                break;
        }

        updateButton(id);

    }

    private void asFixedRatio() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        imvCropImage.setAspectRatio(width, height);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_cancel) {
            finish();
        } else if (id == R.id.btn_wallpaper) {
            wallpaperAsyncTask = new WallpaperAsyncTask();
            wallpaperAsyncTask.execute();
        }
    }

    private class WallpaperAsyncTask extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(WallpaperActivity.this, "", "Setting wallpaper...", true, false);
            //ViewHelper.smoothToShow(WallpaperActivity.this, layoutLoader);
            //Log.e("TAG", "onPreExecute");
        }

        @Override
        protected String doInBackground(String... params) {

            synchronized (wallpaperAsyncTask) {
                try {
                    wallpaperAsyncTask.wait(500);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }

            WallpaperActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAsWallpaper();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("TAG", "onPostExecute");
            //setAsWallpaper();
            //ViewHelper.smoothToHide(WallpaperActivity.this, layoutLoader);

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void setAsWallpaper() {
        Bitmap bitmap = imvCropImage.getCroppedImage();

        if (bitmap == null) {
            return;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // get the height and width of screen
        int height = metrics.heightPixels;
        //int width = metrics.widthPixels << 1;  // best wallpaper width is twice screen width
        int width = metrics.widthPixels;

        bitmap = getScaleBitmap(bitmap, width, height);

        // for fixed/static wallpaper
        if (wallpaperType == 3) {
            width = metrics.widthPixels;
        } else {
            height = bitmap.getHeight();
            width = bitmap.getWidth();
        }

        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(this);
        myWallpaperManager.setWallpaperOffsetSteps(1, 1);
        myWallpaperManager.suggestDesiredDimensions(width, height);

        try {
            myWallpaperManager.setBitmap(bitmap);
            Toasty.success(this, "Wallpaper Set Successfully", Toast.LENGTH_LONG).show();
            finish();

        } catch (Exception e) {
            Toasty.error(this, "Wallpaper Set failed!").show();
        }
        //Log.e("TAG","End");

    }


    private Bitmap getScaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        float ratio = (float) height / maxHeight;
        height = maxHeight;
        width = (int) (width / ratio);
        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }
}
