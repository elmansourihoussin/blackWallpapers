package com.islamiat.blackwallpapers.helper;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.islamiat.blackwallpapers.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class ActionHelper {

    public static void downloadFromBitmap(Context context, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }

        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getString(R.string.app_name);
        File file = new File(folderPath + "/img-" + new Date().getTime() + ".jpg");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        FileOutputStream output = null;

        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

        } catch (Exception e) {

        } finally {

            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }

            } catch (IOException e) {

            }

        }

        // if file save then show Toast massage
        if (file.exists()) {
            Toasty.success(context, "Wallpaper Download Successfully!\nYou can find in " + context.getString(R.string.app_name) + " folder.", Toast
                    .LENGTH_LONG).show();
        }

    }


    public static void shareImage(Context context, Bitmap bitmap){
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = generateImageUri(context, bitmap);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, "Share Image!"));
    }

    public static Uri generateImageUri(Context context, Bitmap bitmap) {

        Uri photoURI=null;

        File file = new File(getTempFilePath(context));
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        } catch (Exception e) {

        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException e) {
            }
        }

        if (file.exists()) {

            //return Uri.fromFile(file);
            //targetSdkVersion >= 24
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                photoURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".file.provider", file);
            }else{
                photoURI = Uri.fromFile(file);
            }

        }

        return photoURI;
    }

    public static String getTempFilePath(Context context) {
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + context.getString(R.string.app_name);

        File file = new File(folderPath + "/temp/igtp.jpg");
        //Log.e("TAG", file.toString());

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        return file.toString();
    }
}
