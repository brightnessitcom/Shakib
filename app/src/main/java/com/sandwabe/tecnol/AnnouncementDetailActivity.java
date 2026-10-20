package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AnnouncementDetailActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Status bar
        boolean isDark = ThemeHelper.isDarkMode(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(isDark ? Color.parseColor("#1A1A1A") : Color.parseColor("#FFFFFF"));
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

        setContentView(R.layout.activity_announcement_detail);

        View ivBack = findViewById(R.id.ivBack);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> closeWithAnimation());
        }

        TextView tvTitle = findViewById(R.id.tvAnnouncementTitle);
        TextView tvBody = findViewById(R.id.tvAnnouncementBody);
        TextView tvSub = findViewById(R.id.tvAnnouncementSub);
        TextView tvLink = findViewById(R.id.tvAnnouncementLink);
        View layoutLink = findViewById(R.id.layoutLink);
        TextView tvDate = findViewById(R.id.tvAnnouncementDate);

        // Populate from Intent extras if present
        String extraTitle = getIntent().getStringExtra("title");
        String extraBody = getIntent().getStringExtra("body");
        String extraSub = getIntent().getStringExtra("sub");
        String extraLink = getIntent().getStringExtra("link");
        String extraDate = getIntent().getStringExtra("date");

        if (extraTitle != null && tvTitle != null) {
            tvTitle.setText(extraTitle);
        }
        if (extraBody != null && tvBody != null) {
            tvBody.setText(extraBody);
        }
        if (tvSub != null) {
            if (extraSub != null && !extraSub.isEmpty()) {
                tvSub.setText(extraSub);
                tvSub.setVisibility(View.VISIBLE);
            } else if (extraSub != null && extraSub.isEmpty()) {
                tvSub.setVisibility(View.GONE);
            }
        }
        if (extraLink != null && !extraLink.isEmpty()) {
            if (tvLink != null) tvLink.setText(extraLink);
            if (layoutLink != null) {
                layoutLink.setVisibility(View.VISIBLE);
                layoutLink.setOnClickListener(v -> openUrl(extraLink));
            }
        } else if (extraLink != null && extraLink.isEmpty() && layoutLink != null) {
            layoutLink.setVisibility(View.GONE);
        } else if (layoutLink != null && tvLink != null) {
            // Default link click
            String currentUrl = tvLink.getText().toString();
            layoutLink.setOnClickListener(v -> openUrl(currentUrl));
        }

        if (extraDate != null && tvDate != null) {
            tvDate.setText(extraDate);
        }
    }

    private void openUrl(String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception ignored) {
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
}
