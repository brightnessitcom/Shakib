package com.sandwabe.tecnol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class FaqsActivity extends AppCompatActivity {

    private WebView webViewFaqs;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_faqs);

        View ivBack = findViewById(R.id.ivBack);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        webViewFaqs = findViewById(R.id.webViewFaqs);
        if (webViewFaqs != null) {
            boolean isDark = ThemeHelper.isDarkMode(this);
            webViewFaqs.setBackgroundColor(ContextCompat.getColor(this, R.color.app_background));
            WebSettings settings = webViewFaqs.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            webViewFaqs.setVerticalScrollBarEnabled(true);
            webViewFaqs.setHorizontalScrollBarEnabled(false);
            webViewFaqs.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (isDark) {
                        view.evaluateJavascript(
                                "document.documentElement.classList.add('dark-mode'); document.body.classList.add('dark-mode');",
                                null
                        );
                    }
                }
            });
            webViewFaqs.loadUrl("file:///android_asset/absher_faqs.html" + (isDark ? "?theme=dark" : "?theme=light"));
        }
    }

    @Override
    public void onBackPressed() {
        if (webViewFaqs != null && webViewFaqs.canGoBack()) {
            webViewFaqs.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
