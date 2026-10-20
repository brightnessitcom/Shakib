package com.sandwabe.tecnol;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class ServiceNavHelper {

    public static void setupStatusBar(Activity activity) {
        if (activity == null) return;
        boolean isDark = ThemeHelper.isDarkMode(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(isDark ? Color.parseColor("#1A1A1A") : Color.parseColor("#F4F6F9"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = activity.getWindow().getDecorView();
            int flags = decor.getSystemUiVisibility();
            if (isDark) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // Light icons on dark status bar
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // Dark icons on light status bar
            }
            decor.setSystemUiVisibility(flags);
        }

        // Post theme application on decor view so it runs seamlessly after setContentView completes
        activity.getWindow().getDecorView().post(() -> applyTheme(activity));
    }

    public static void applyTheme(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        boolean isDark = ThemeHelper.isDarkMode(activity);

        int bgColor = isDark ? Color.parseColor("#161719") : Color.parseColor("#F4F6F9");
        int cardBgColor = isDark ? Color.parseColor("#26292B") : Color.parseColor("#FFFFFF");
        int iconColor = isDark ? Color.parseColor("#72D09D") : Color.parseColor("#00663D");
        int textColor = isDark ? Color.parseColor("#FFFFFF") : Color.parseColor("#1B1E21");
        int strokeColor = isDark ? Color.parseColor("#26292B") : Color.parseColor("#EAEFEA");
        int strokeWidth = isDark ? 0 : (int) (1 * activity.getResources().getDisplayMetrics().density);

        View root = activity.findViewById(R.id.rootLayout);
        if (root != null) root.setBackgroundColor(bgColor);

        View topBar = activity.findViewById(R.id.topBar);
        if (topBar != null) topBar.setBackgroundColor(bgColor);

        ImageView ivBack = activity.findViewById(R.id.ivBack);
        if (ivBack != null) ivBack.setColorFilter(iconColor);

        ImageView ivClose = activity.findViewById(R.id.ivClose);
        if (ivClose != null) ivClose.setColorFilter(iconColor);

        TextView tvTitle = activity.findViewById(R.id.tvTitle);
        if (tvTitle != null) tvTitle.setTextColor(textColor);

        TextView tvToolbarTitle = activity.findViewById(R.id.tvToolbarTitle);
        if (tvToolbarTitle != null) tvToolbarTitle.setTextColor(textColor);

        TextView tvSectionHeader = activity.findViewById(R.id.tvSectionHeader);
        if (tvSectionHeader != null) tvSectionHeader.setTextColor(textColor);

        TextView tvHeading = activity.findViewById(R.id.tvHeading);
        if (tvHeading != null) tvHeading.setTextColor(textColor);

        TextView tvDescription = activity.findViewById(R.id.tvDescription);
        if (tvDescription != null) tvDescription.setTextColor(isDark ? Color.parseColor("#8A9095") : Color.parseColor("#6C757D"));

    }

    public static void setupBottomNav(AppCompatActivity activity, BottomNavigationView bottomNav, int selectedTabId) {
        if (activity == null || bottomNav == null) return;

        // In public mode or when user is not logged in, hide bottom navigation bar completely
        boolean isPublic = activity.getIntent() != null && activity.getIntent().getBooleanExtra("IS_PUBLIC", false);
        String username = LoginActivity.getCurrentUsername(activity);
        User cached = ApiClient.getCachedUser(activity);
        boolean hasSession = (username != null && !username.trim().isEmpty()) && cached != null;

        if (isPublic || !hasSession) {
            bottomNav.setVisibility(View.GONE);
            return;
        }

        bottomNav.setSelectedItemId(selectedTabId);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("NAVIGATE_TO", id);
            activity.startActivity(intent);
            activity.finish();
            return true;
        });
    }

    public static void setupBack(Activity activity, int backViewId) {
        if (activity == null) return;
        View backView = activity.findViewById(backViewId);
        if (backView != null) {
            backView.setOnClickListener(v -> activity.finish());
        }
    }
}
