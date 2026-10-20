package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PreLoginSettingsActivity extends AppCompatActivity {

    private String initialLang = "en";
    private String selectedLang = "en";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_pre_login_settings);

        TextView btnDone = findViewById(R.id.btnDone);
        if (btnDone != null) {
            boolean isDark = ThemeHelper.isDarkMode(this);
            int accentColor = isDark ? Color.parseColor("#72D09D") : Color.parseColor("#00663D");
            btnDone.setTextColor(accentColor);
            btnDone.setOnClickListener(v -> applyAndFinish());
        }

        initialLang = LanguageHelper.getLanguage(this);
        selectedLang = initialLang;

        RadioButton rbArabic = findViewById(R.id.radio_arabic);
        RadioButton rbEnglish = findViewById(R.id.radio_english);
        RadioGroup languageRadioGroup = findViewById(R.id.languageRadioGroup);

        if ("ar".equals(initialLang)) {
            if (rbArabic != null) rbArabic.setChecked(true);
        } else {
            if (rbEnglish != null) rbEnglish.setChecked(true);
        }

        if (rbArabic != null) {
            rbArabic.setOnClickListener(v -> selectedLang = "ar");
        }
        if (rbEnglish != null) {
            rbEnglish.setOnClickListener(v -> selectedLang = "en");
        }

        if (languageRadioGroup != null) {
            languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.radio_arabic) {
                    selectedLang = "ar";
                } else if (checkedId == R.id.radio_english) {
                    selectedLang = "en";
                }
            });
        }

    }

    private void applyAndFinish() {
        boolean langChanged = !selectedLang.equals(initialLang);
        if (langChanged) {
            LanguageHelper.setLanguage(this, selectedLang);
            Intent intent = new Intent(this, PreLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        applyAndFinish();
    }
}
