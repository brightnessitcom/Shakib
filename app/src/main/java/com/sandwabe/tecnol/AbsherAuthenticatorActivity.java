package com.sandwabe.tecnol;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AbsherAuthenticatorActivity extends AppCompatActivity {

    private TextView tvTabAuthenticator;
    private TextView tvTabLinkedAccounts;
    private View indicatorAuthenticator;
    private View indicatorLinkedAccounts;
    private View layoutAuthEmpty;
    private View layoutLinkedEmpty;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_absher_authenticator);

        ImageView ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) {
            ivClose.setOnClickListener(v -> finish());
        }

        tvTabAuthenticator = findViewById(R.id.tvTabAuthenticator);
        tvTabLinkedAccounts = findViewById(R.id.tvTabLinkedAccounts);
        indicatorAuthenticator = findViewById(R.id.indicatorAuthenticator);
        indicatorLinkedAccounts = findViewById(R.id.indicatorLinkedAccounts);
        layoutAuthEmpty = findViewById(R.id.layoutAuthEmpty);
        layoutLinkedEmpty = findViewById(R.id.layoutLinkedEmpty);

        View tabAuthenticator = findViewById(R.id.tabAuthenticator);
        View tabLinkedAccounts = findViewById(R.id.tabLinkedAccounts);

        if (tabAuthenticator != null) {
            tabAuthenticator.setOnClickListener(v -> selectTab(true));
        }

        if (tabLinkedAccounts != null) {
            tabLinkedAccounts.setOnClickListener(v -> selectTab(false));
        }

        selectTab(true);
    }

    private void selectTab(boolean isAuthenticator) {
        boolean isDark = ThemeHelper.isDarkMode(this);
        int activeColor = isDark ? Color.parseColor("#72D09D") : Color.parseColor("#00663D");
        int inactiveColor = isDark ? Color.parseColor("#9CA2A7") : Color.parseColor("#6C757D");

        if (isAuthenticator) {
            if (tvTabAuthenticator != null) {
                tvTabAuthenticator.setTextColor(activeColor);
                tvTabAuthenticator.setTypeface(null, Typeface.BOLD);
            }
            if (indicatorAuthenticator != null) {
                indicatorAuthenticator.setBackgroundColor(activeColor);
            }

            if (tvTabLinkedAccounts != null) {
                tvTabLinkedAccounts.setTextColor(inactiveColor);
                tvTabLinkedAccounts.setTypeface(null, Typeface.NORMAL);
            }
            if (indicatorLinkedAccounts != null) {
                indicatorLinkedAccounts.setBackgroundColor(Color.TRANSPARENT);
            }

            if (layoutAuthEmpty != null) layoutAuthEmpty.setVisibility(View.VISIBLE);
            if (layoutLinkedEmpty != null) layoutLinkedEmpty.setVisibility(View.GONE);
        } else {
            if (tvTabLinkedAccounts != null) {
                tvTabLinkedAccounts.setTextColor(activeColor);
                tvTabLinkedAccounts.setTypeface(null, Typeface.BOLD);
            }
            if (indicatorLinkedAccounts != null) {
                indicatorLinkedAccounts.setBackgroundColor(activeColor);
            }

            if (tvTabAuthenticator != null) {
                tvTabAuthenticator.setTextColor(inactiveColor);
                tvTabAuthenticator.setTypeface(null, Typeface.NORMAL);
            }
            if (indicatorAuthenticator != null) {
                indicatorAuthenticator.setBackgroundColor(Color.TRANSPARENT);
            }

            if (layoutAuthEmpty != null) layoutAuthEmpty.setVisibility(View.GONE);
            if (layoutLinkedEmpty != null) layoutLinkedEmpty.setVisibility(View.VISIBLE);
        }
    }
}
