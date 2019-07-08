package com.islamiat.blackwallpapers.admob;

import android.content.Context;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.ornach.andutils.android.ViewHelper;

public class BannerHelper {

    private final Context context;

    private BannerHelper(Context context){
        this.context = context;

        if (AdConfig.AD_ENABLED) {
            MobileAds.initialize(context, AdConfig.ADMOB_APP_ID);
        }
    }

    public static BannerHelper newInstance(Context context){
        return new BannerHelper(context);
    }

    public void loadBannerAdView(FrameLayout layout){

        if (!AdConfig.AD_ENABLED || !AdConfig.BANNER_AD_ENABLED) {
            return;
        }

        ViewHelper.smoothToShow(layout);

        AdView adView = new AdView(context);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(AdConfig.BANNER_AD_UNIT_ID);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView.loadAd(adRequest);

        layout.addView(adView);
    }


}
