package com.sandwabe.tecnol;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class NafathActivity extends AppCompatActivity {

    private static final String NAFATH_LIVE_URL = "https://www.iam.gov.sa/authservice/userauthservice";

    private WebView webView;
    private ProgressBar pbWebLoading;
    private ProgressBar pbCenterLoading;
    private TextView tvTimerPill;
    private CountDownTimer timer;
    private Dialog timeoutDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupStatusBar();
        setContentView(R.layout.activity_nafath);

        initViews();
        setupTimer();
        setupWebView();
    }

    private void setupStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            boolean isNight = currentNightMode == Configuration.UI_MODE_NIGHT_YES;
            int statusBarColor = ContextCompat.getColor(this, isNight ? R.color.colorNafathBg : R.color.colorNafathBg);
            window.setStatusBarColor(statusBarColor);
            View decor = window.getDecorView();
            int flags = decor.getSystemUiVisibility();
            if (isNight) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(flags);
        }
    }

    private void initViews() {
        ImageView ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) {
            ivClose.setOnClickListener(v -> finishWithAnim());
        }

        tvTimerPill = findViewById(R.id.tvTimerPill);
        pbWebLoading = findViewById(R.id.pbWebLoading);
        pbCenterLoading = findViewById(R.id.pbCenterLoading);
        webView = findViewById(R.id.webViewNafath);
    }

    private void setupTimer() {
        if (tvTimerPill == null) return;
        // 3 minutes countdown (180,000 ms) matching reference app
        timer = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long totalSeconds = millisUntilFinished / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                tvTimerPill.setText(timeFormatted);
            }

            @Override
            public void onFinish() {
                tvTimerPill.setText("00:00");
                showTimeoutDialog();
            }
        }.start();
    }

    private void showTimeoutDialog() {
        if (isFinishing() || isDestroyed()) return;
        try {
            if (timeoutDialog != null && timeoutDialog.isShowing()) {
                return;
            }
            timeoutDialog = new Dialog(this);
            timeoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            timeoutDialog.setContentView(R.layout.dialog_nafath_timeout);
            timeoutDialog.setCancelable(false);
            timeoutDialog.setCanceledOnTouchOutside(false);
            if (timeoutDialog.getWindow() != null) {
                timeoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timeoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            View btnOk = timeoutDialog.findViewById(R.id.btnDialogOk);
            if (btnOk != null) {
                btnOk.setOnClickListener(v -> {
                    timeoutDialog.dismiss();
                    finishWithAnim();
                });
            }
            timeoutDialog.show();
        } catch (Exception e) {
            // fallback finish if dialog cannot be displayed
            finishWithAnim();
        }
    }

    private void setupWebView() {
        if (webView == null) return;

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // Set modern mobile browser User-Agent so government portal doesn't restrict WebView
        String userAgent = settings.getUserAgentString();
        if (userAgent != null) {
            settings.setUserAgentString(userAgent.replace("; wv", ""));
        }

        // Dark mode background matching
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNight = currentNightMode == Configuration.UI_MODE_NIGHT_YES;
        webView.setBackgroundColor(ContextCompat.getColor(this, isNight ? R.color.colorNafathBg : R.color.colorNafathBg));

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (pbWebLoading != null) {
                    if (newProgress < 100) {
                        pbWebLoading.setVisibility(View.VISIBLE);
                        pbWebLoading.setProgress(newProgress);
                    } else {
                        pbWebLoading.setVisibility(View.GONE);
                    }
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (pbCenterLoading != null) {
                    pbCenterLoading.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pbCenterLoading != null) {
                    pbCenterLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (pbCenterLoading != null) {
                    pbCenterLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // Ensure SSL errors or certificates do not block portal display
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        // Load the official live Nafath SSO portal URL
        String lang = LanguageHelper.getLanguage(this);
        String targetUrl = NAFATH_LIVE_URL + "?lang=" + ("ar".equals(lang) ? "ar" : "en");
        webView.loadUrl(targetUrl);
    }

    private void finishWithAnim() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            finishWithAnim();
        }
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        if (timeoutDialog != null && timeoutDialog.isShowing()) {
            timeoutDialog.dismiss();
        }
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
        }
        super.onDestroy();
    }
}
