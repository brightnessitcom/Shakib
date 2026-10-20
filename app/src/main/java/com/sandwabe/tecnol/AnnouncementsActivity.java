package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AnnouncementsActivity extends AppCompatActivity {

    private View layoutLoading;
    private View layoutEmptyAnnouncements;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Status bar dynamically adapting to Light / Dark Mode
        boolean isDark = ThemeHelper.isDarkMode(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.announcement_bg));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            int flags = decor.getSystemUiVisibility();
            if (isDark) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(flags);
        }

        setContentView(R.layout.activity_announcements);

        View ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) {
            ivClose.setOnClickListener(v -> closeWithAnimation());
        }

        View ivMore = findViewById(R.id.ivMore);
        if (ivMore != null) {
            ivMore.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(this, v);
                popup.getMenu().add(0, 1, 0, "Mark all as read");
                popup.setOnMenuItemClickListener(item -> {
                    Toast.makeText(this, "All announcements marked as read", Toast.LENGTH_SHORT).show();
                    return true;
                });
                popup.show();
            });
        }

        layoutLoading = findViewById(R.id.layoutLoading);
        layoutEmptyAnnouncements = findViewById(R.id.layoutEmptyAnnouncements);

        // Initial Loading State (matching Absher announcement loading)
        showLoadingState();

        // Simulate brief authentic fetch delay, then show the empty state smoothly
        handler.postDelayed(() -> {
            if (isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())) {
                return;
            }
            showEmptyState();
        }, 600);
    }

    private void showLoadingState() {
        if (layoutLoading != null) layoutLoading.setVisibility(View.VISIBLE);
        if (layoutEmptyAnnouncements != null) layoutEmptyAnnouncements.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        if (layoutLoading != null && layoutLoading.getVisibility() == View.VISIBLE) {
            layoutLoading.animate().alpha(0f).setDuration(200).withEndAction(() -> {
                layoutLoading.setVisibility(View.GONE);
                layoutLoading.setAlpha(1f);
                if (layoutEmptyAnnouncements != null) {
                    layoutEmptyAnnouncements.setAlpha(0f);
                    layoutEmptyAnnouncements.setVisibility(View.VISIBLE);
                    layoutEmptyAnnouncements.animate().alpha(1f).setDuration(250).start();
                }
            }).start();
        } else {
            if (layoutEmptyAnnouncements != null) {
                layoutEmptyAnnouncements.setVisibility(View.VISIBLE);
            }
        }
    }

    private void closeWithAnimation() {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_right_smooth);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_right_smooth);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
