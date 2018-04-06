package com.example.aliosama.dramatranslation.Configurations;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by aliosama on 1/30/2018.
 */

public class ourWebViewClient extends WebViewClient {

    String Url;
    public ourWebViewClient(String url) {
        this.Url = url;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.equals(Url)) {
            view.loadUrl(url);
        }
        return true;
    }
}
