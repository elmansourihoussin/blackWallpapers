package com.islamiat.blackwallpapers.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.islamiat.blackwallpapers.R;
import com.islamiat.blackwallpapers.helper.Utils;
import com.ornach.andutils.android.ViewHelper;
import com.ornach.andutils.android.utils.AlertDialog;

public class AboutActivity extends AppCompatActivity {

    private TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvVersion = (TextView) findViewById(R.id.tv_version);


        updateVersionName();
    }

    private void updateVersionName() {
        String version = getVersionName();

        if (!TextUtils.isEmpty(version)) {
            tvVersion.setText("v " + version);
        } else {
            ViewHelper.hideView(tvVersion);
        }
    }

    private String getVersionName() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
        }
        return info.versionName;
    }


    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_more_app) {

            Utils.goMoreApps(this);

        } else if (id == R.id.btn_os_license) {

            //AlertDialog.show(this, "","Add you own license url here");
            /*String privacyUrl = "http://example.com/license.html";
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("URL", privacyUrl);
            startActivity(intent);*/
            startActivity(new Intent(this, OSLicenseActivity.class));

        } else if (id == R.id.btn_privacy_policy) {

            AlertDialog.show(this, "","Add you own privacy url here");
            /*String privacyUrl = "http://example.com/privacy.html";
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("URL", privacyUrl);
            startActivity(intent);*/

        }
    }
}
