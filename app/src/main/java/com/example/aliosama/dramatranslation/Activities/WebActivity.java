package com.example.aliosama.dramatranslation.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.aliosama.dramatranslation.Configurations.ourWebViewClient;
import com.example.aliosama.dramatranslation.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class WebActivity extends AppCompatActivity  {
    WebView webView;
    private InterstitialAd mInterstitialAd;
    String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        try {

                //            Ads Implementation
                mInterstitialAd = new InterstitialAd(this);
                mInterstitialAd.setAdUnitId(getString(R.string.webview_interstitial_unit_id));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        // Call displayInterstitial() function
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                            Log.d("TAG", "The interstitial loaded now.");
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }

                    @Override
                    public void onAdClosed() {

                    }
                });

            Intent intent = getIntent();
            Url = intent.getExtras().getString("url");

            webView = (WebView) findViewById(R.id.webView);

            webView.setWebChromeClient(new WebChromeClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
            webView.getSettings().setPluginState(WebSettings.PluginState.ON);
            webView.loadUrl(Url);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPause() {
        webView.onPause();
        webView.pauseTimers();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.resumeTimers();
        webView.onResume();
    }


    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

}
