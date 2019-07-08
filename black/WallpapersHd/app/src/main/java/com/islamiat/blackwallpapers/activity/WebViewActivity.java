package com.islamiat.blackwallpapers.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.islamiat.blackwallpapers.R;
import com.ornach.andutils.android.ViewHelper;
import com.ornach.andutils.android.utils.Connectivity;
import com.ornach.andutils.java.StringUtils;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private FrameLayout mContainer;
    TextView tvError;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** request for no statusBar **/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view);

        if (getIntent() != null && getIntent().hasExtra("URL")) {
            url = getIntent().getStringExtra("URL");
        }


        mContainer = (FrameLayout) findViewById(R.id.container);
        tvError = (TextView) findViewById(R.id.tv_error);

        /** create and add  webview into FrameLayout **/
        webView = new WebView(getApplicationContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        mContainer.addView(webView);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(false);
        webSettings.setSupportZoom(false);
        webView.setBackgroundColor(Color.TRANSPARENT);

        if (Connectivity.isConnected(this) && !StringUtils.isEmpty(url)) {
            webView.loadUrl(url);
            ViewHelper.showView(mContainer);
            ViewHelper.hideView(tvError);
        } else {
            ViewHelper.showView(tvError);
            ViewHelper.hideView(mContainer);

            if (!Connectivity.isConnected(this))
                tvError.setText("No internet connection");

            if (StringUtils.isEmpty(url))
                tvError.setText("Invalid request");

        }
    }
}
