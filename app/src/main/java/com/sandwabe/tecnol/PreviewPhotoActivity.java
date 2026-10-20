package com.sandwabe.tecnol;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class PreviewPhotoActivity extends AppCompatActivity {

    private static final String TAG = "PreviewPhotoActivity";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupThemeAndStatusBar();
        setContentView(R.layout.activity_preview_photo);

        applyDynamicTheming();

        ImageView ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) {
            ivClose.setOnClickListener(v -> finish());
        }

        TouchImageView touchImageView = findViewById(R.id.touchImageView);

        String rawUrl = getIntent().getStringExtra("IMAGE_URL");
        if (rawUrl == null || rawUrl.trim().isEmpty() || "null".equalsIgnoreCase(rawUrl.trim())) {
            User cached = ApiClient.getCachedUser(this);
            if (cached != null) {
                rawUrl = cached.getProfile_image_url();
            }
        }

        String url = rawUrl;
        if (url != null && !url.trim().isEmpty() && !"null".equalsIgnoreCase(url.trim())) {
            url = url.trim();
            if (!url.startsWith("http") && !url.startsWith("/")) {
                try {
                    url = CustomCipher.decrypt(url, CustomCipher.getSecret(this), this);
                } catch (Exception e) {
                    Log.e(TAG, "Decryption failed for URL: " + url, e);
                }
            }
        }

        if (touchImageView != null) {
            if (url != null && !url.trim().isEmpty() && !"null".equalsIgnoreCase(url.trim())) {
                Glide.with(this)
                        .load(url.trim())
                        .placeholder(R.drawable.absher)
                        .error(R.drawable.absher)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(1800)
                        .into(touchImageView);
            } else {
                touchImageView.setImageResource(R.drawable.absher);
            }
        }
    }

    private void setupThemeAndStatusBar() {
        boolean isDark = ThemeHelper.isDarkMode(this);
        int statusBarColor = isDark ? Color.parseColor("#161719") : Color.parseColor("#FFFFFF");

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusBarColor);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = window.getDecorView();
            int flags = decor.getSystemUiVisibility();
            if (isDark) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(flags);
        }
    }

    private void applyDynamicTheming() {
        boolean isDark = ThemeHelper.isDarkMode(this);
        int bgColor = isDark ? Color.parseColor("#161719") : Color.parseColor("#FFFFFF");
        int textColor = isDark ? Color.parseColor("#FFFFFF") : Color.parseColor("#1B1E21");
        int iconColor = isDark ? Color.parseColor("#72D09D") : Color.parseColor("#00663D");

        View root = findViewById(R.id.rootLayout);
        if (root != null) root.setBackgroundColor(bgColor);

        View topBar = findViewById(R.id.topBar);
        if (topBar != null) topBar.setBackgroundColor(bgColor);

        View photoContainer = findViewById(R.id.photoContainer);
        if (photoContainer != null) photoContainer.setBackgroundColor(bgColor);

        ImageView ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) ivClose.setColorFilter(iconColor);

        TextView tvTitle = findViewById(R.id.tvTitle);
        if (tvTitle != null) tvTitle.setTextColor(textColor);
    }
}
