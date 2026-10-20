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

public class PrivacyPolicyActivity extends AppCompatActivity {

    private WebView webViewPrivacyPolicy;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_privacy_policy);

        View ivBack = findViewById(R.id.ivBack);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        webViewPrivacyPolicy = findViewById(R.id.webViewPrivacyPolicy);
        if (webViewPrivacyPolicy != null) {
            boolean isDark = ThemeHelper.isDarkMode(this);
            webViewPrivacyPolicy.setBackgroundColor(ContextCompat.getColor(this, R.color.app_background));
            WebSettings settings = webViewPrivacyPolicy.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);
            webViewPrivacyPolicy.setVerticalScrollBarEnabled(true);
            webViewPrivacyPolicy.setHorizontalScrollBarEnabled(false);
            webViewPrivacyPolicy.setWebViewClient(new WebViewClient() {
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
            webViewPrivacyPolicy.loadUrl("file:///android_asset/absher_privacy_policy.html" + (isDark ? "?theme=dark" : "?theme=light"));
        }
    }

    @Override
    public void onBackPressed() {
        if (webViewPrivacyPolicy != null && webViewPrivacyPolicy.canGoBack()) {
            webViewPrivacyPolicy.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
