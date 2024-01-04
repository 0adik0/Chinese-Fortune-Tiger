package com.fortunetiger.chi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IfraActivity extends AppCompatActivity {

    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ifra);

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        myWebView = findViewById(R.id.webview);
        configureWebView(url);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView(String url) {
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadWithOverviewMode(true);
        myWebView.getSettings().setUseWideViewPort(true);
        myWebView.getSettings().setSupportZoom(true);
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.getSettings().setDisplayZoomControls(false);

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                extractUrlFromPage();
            }
        });

        myWebView.loadUrl(url);
    }

    private void extractUrlFromPage() {
        myWebView.evaluateJavascript(
                "(function() { return document.body.innerText.match(/https?:\\/\\/[\\w\\.-]+/)[0]; })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        // Log the raw JavaScript result
                        Log.d("WebView", "Raw JavaScript result: " + value);

                        // Remove quotes and null check
                        String extractedUrl = (value != null && !value.equals("null")) ? value.replace("\"", "") : "";
                        Log.d("WebView", "Extracted URL: " + extractedUrl);

                        // Load the extracted URL if it is valid
                        if (URLUtil.isValidUrl(extractedUrl)) {
                            myWebView.loadUrl(extractedUrl);
                        } else {
                            Log.d("WebView", "The extracted URL is not valid.");
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}