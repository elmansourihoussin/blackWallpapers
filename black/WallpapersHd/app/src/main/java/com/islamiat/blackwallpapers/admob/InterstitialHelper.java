package com.islamiat.blackwallpapers.admob;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Random;

public class InterstitialHelper {

    private Context context;
    private AppCompatActivity activity;

    private static final int MIN_TARGET = 5;
    private static final int MAX_TARGET = 10;

    private static final int DEFAULT_TARGET = MAX_TARGET;

    private static int action, target = MAX_TARGET;

    private ActionPref actionPref;

    private InterstitialAd mInterstitialAd;

    private InterstitialHelper(AppCompatActivity activity) {
        this.activity = activity;
        context = activity;
        actionPref = new ActionPref(context);
        target = actionPref.getTarget();
        action = actionPref.getAction();

        if (AdConfig.AD_ENABLED) {
            MobileAds.initialize(context, AdConfig.ADMOB_APP_ID);
        }
    }

    public static InterstitialHelper newInstance(AppCompatActivity activity) {
        return new InterstitialHelper(activity);
    }

    AdLoadingDialogFragment dialogFragment;
    private void loadAd(final ActionListener listener){

        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        dialogFragment = new AdLoadingDialogFragment();
        dialogFragment.show(ft, "dialog");


        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(AdConfig.INTERSTITIAL_AD_UNIT_ID);

        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (listener !=null){
                    listener.onDone();
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                //showAd();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    //Log.d("TAG", "The interstitial wasn't loaded yet.");
                    if (listener !=null){
                        listener.onDone();
                    }
                }

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (dialogFragment!=null)dialogFragment.dismiss();

                if (listener !=null){
                    listener.onDone();
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                resetTarget();
                if (dialogFragment!=null)dialogFragment.dismiss();

            }
        });

        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private void showAd(){
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            //Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }


    private void resetTarget() {
        //Log.e("TAG", "resetTarget");
        Random random = new Random();
        target = random.nextInt(MAX_TARGET - MIN_TARGET + 1) + MIN_TARGET;
        action = 0;

        actionPref.setTarget(target);
        actionPref.setAction(action);
    }

    public void takeAction(ActionListener listener) {

        if (!AdConfig.AD_ENABLED || !AdConfig.INTERSTITIAL_AD_ENABLED){
            if (listener!=null){
                listener.onDone();
            }
            return;
        }

        action++;
        //Log.e("TAG", "Action: "+action+"  Target: "+actionPref.getTarget());
        actionPref.setAction(action);

        if (actionPref.getAction()>=actionPref.getTarget()){
            loadAd(listener);

        }else{
            if (listener!=null){
                listener.onDone();
            }
        }

    }

    public interface ActionListener{
        void onDone();
    }


    private static class ActionPref {

        Context context;
        SharedPreferences pref;

        ActionPref(Context context) {
            this.context = context;
            pref = context.getSharedPreferences("Ad_pref", Context.MODE_PRIVATE);
        }

        void setTarget(int i) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("TARGET", i);
            editor.apply();
        }

        void setAction(int i) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("ACTION", i);
            editor.apply();
        }

        int getTarget() {
            return pref.getInt("TARGET", InterstitialHelper.DEFAULT_TARGET);
        }

        int getAction() {
            return pref.getInt("ACTION", 0);
        }


    }
}
