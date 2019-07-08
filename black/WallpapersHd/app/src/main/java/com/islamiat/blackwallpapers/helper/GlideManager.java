package com.islamiat.blackwallpapers.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class GlideManager {

    public static void clear(ImageView imageView) {
        Glide.clear(imageView);
    }

    public static void loadImage(Context context, String picUrl, ImageView imageView) {

        if (context == null) {
            return;
        }

        clear(imageView); // clear image

        Glide
                .with(context)
                .load(picUrl)
                //.thumbnail(0.2f)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .crossFade(500)
                .into(imageView);
    }


    public static void loadBitmap(final Activity activity, String picUrl, final GlideListener listener) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Glide
                .with(activity)
                .load(picUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (listener != null) {
                            listener.onError(e);
                        }

                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {

                        if (listener != null) {
                            listener.onReady(resource);
                        }

                        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        if (listener != null) {
                            listener.onStart();
                        }
                    }
                });
    }


    public interface GlideListener {
        void onError(Exception e);

        void onReady(Bitmap bitmap);

        void onStart();
    }
}
