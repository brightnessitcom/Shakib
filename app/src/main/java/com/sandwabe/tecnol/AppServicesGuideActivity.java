package com.sandwabe.tecnol;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppServicesGuideActivity extends AppCompatActivity {

    private static class GuideItem {
        final String category;
        final String title;
        final String[] tags;

        GuideItem(String category, String title, String... tags) {
            this.category = category;
            this.title = title;
            this.tags = tags;
        }
    }

    private static final GuideItem[] GUIDE_ITEMS = new GuideItem[]{
            new GuideItem("PASSPORTS", "Update Residence Passport Information", "Citizen", "Resident"),
            new GuideItem("PASSPORTS", "Saudi Passport Renewal For Family Member", "Citizen"),
            new GuideItem("PASSPORTS", "Public Query Of Worker Arrivals", "Citizen", "Resident"),
            new GuideItem("PASSPORTS", "Query Border Number", "Citizen", "Resident", "Visitor"),
            new GuideItem("PASSPORTS", "Family Visa Service", "Resident"),
            new GuideItem("CIVIL AFFAIRS", "Validate Citizen Data Document", "Citizen"),
            new GuideItem("PUBLIC SECURITY", "Report Los/Stolen Plates", "Citizen", "Resident"),
            new GuideItem("PUBLIC SECURITY", "Driving License Info Report", "Citizen", "Resident"),
            new GuideItem("PASSPORTS", "Public Query Health Insurance Validity (For Residents)", "Resident"),
            new GuideItem("PASSPORTS", "Query Hajj Eligibility", "Citizen", "Resident"),
            new GuideItem("PASSPORTS", "Tawasul", "Citizen", "Resident", "Visitor"),
            new GuideItem("CIVIL AFFAIRS", "Request Replacement For Damaged Id", "Citizen"),
            new GuideItem("PASSPORTS", "Muqeem Print", "Citizen", "Resident")
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_app_services_guide);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_worker);

        LinearLayout container = findViewById(R.id.llServicesGuideContainer);
        if (container != null) {
            populateGuideItems(container);
        }
    }

    private void populateGuideItems(LinearLayout container) {
        for (int i = 0; i < GUIDE_ITEMS.length; i++) {
            GuideItem item = GUIDE_ITEMS[i];

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            row.setPadding(dp(16), dp(18), dp(16), dp(18));
            TypedValue outValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            row.setBackgroundResource(outValue.resourceId);
            row.setClickable(true);
            row.setFocusable(true);

            // Category label
            TextView tvCategory = new TextView(this);
            tvCategory.setText(item.category);
            tvCategory.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.icon_tint_primary));
            tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.5f);
            tvCategory.setTypeface(null, Typeface.BOLD);
            tvCategory.setLetterSpacing(0.03f);
            row.addView(tvCategory);

            // Service Title
            TextView tvTitle = new TextView(this);
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            titleParams.topMargin = dp(6);
            tvTitle.setLayoutParams(titleParams);
            tvTitle.setText(item.title);
            tvTitle.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_primary));
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.5f);
            tvTitle.setTypeface(null, Typeface.BOLD);
            row.addView(tvTitle);

            // Tags / Pills Row
            LinearLayout tagsRow = new LinearLayout(this);
            LinearLayout.LayoutParams tagsRowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            tagsRowParams.topMargin = dp(12);
            tagsRow.setLayoutParams(tagsRowParams);
            tagsRow.setOrientation(LinearLayout.HORIZONTAL);
            tagsRow.setGravity(Gravity.CENTER_VERTICAL);

            for (String tag : item.tags) {
                TextView tvTag = new TextView(this);
                LinearLayout.LayoutParams tagParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                tagParams.setMarginEnd(dp(10));
                tvTag.setLayoutParams(tagParams);
                tvTag.setBackgroundResource(R.drawable.bg_service_guide_pill);
                tvTag.setPadding(dp(14), dp(6), dp(14), dp(6));
                tvTag.setText(tag);
                tvTag.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_primary));
                tvTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12.5f);
                tvTag.setTypeface(null, Typeface.BOLD);
                tagsRow.addView(tvTag);
            }
            row.addView(tagsRow);

            container.addView(row);

            if (i < GUIDE_ITEMS.length - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams divParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp(1)
                );
                divider.setLayoutParams(divParams);
                divider.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.divider_color));
                container.addView(divider);
            }
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
