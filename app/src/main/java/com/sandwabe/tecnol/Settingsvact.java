package com.sandwabe.tecnol;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Locale;
import java.util.Objects;

public class Settingsvact extends AppCompatActivity {
    public static final String KEY_BLUR_ENABLED = "blur_enabled";
    private static final String KEY_LANG = "AppLang";
    private static final String LANG_ARABIC = "ar";
    private static final String LANG_ENGLISH = "en";
    private static final String PREFS_NAME = "MyPrefs";
    private static final String APP_PREFS = "UserAppPrefs";
    
    public static final String KEY_CALENDAR_TYPE = "CalendarType";
    public static final String CALENDAR_GREGORIAN = "Gregorian";
    public static final String CALENDAR_HIJRI = "Hijri";

    private ShapeableImageView setting_profile_ic;
    private TextView tvUserId;
    private TextView tvUserName;
    private User user;
    private String userName = "";
    private SharedPreferences appPrefs;

    private void clearUserPreferences() {
        SharedPreferences.Editor edit = appPrefs.edit();
        edit.putBoolean(LoginActivity.KEY_IS_LOGGED_IN, false);
        edit.putBoolean(LoginActivity.KEY_IS_VERIFIED, false);
        edit.remove(SessionManager.KEY_LOGIN_TIMESTAMP);
        edit.putString(LoginActivity.KEY_USERNAME, null);
        edit.putString(LoginActivity.KEY_PASSWORD, null);
        edit.apply();
    }

    private void loadLocale() {
        setLocale(getSharedPreferences(PREFS_NAME, 0).getString(KEY_LANG, LANG_ENGLISH));
    }

    private void loadProfileImage(String str) {
        if (str == null || str.isEmpty() || str.equals("null")) {
            this.setting_profile_ic.setImageResource(R.drawable.absher);
            return;
        }
        try {
            String decryptedUrl = CustomCipher.decrypt(str, CustomCipher.getSecret(this), this);
            Glide.with(this).load(decryptedUrl).placeholder(R.drawable.placeholder).error(R.drawable.absher).into(this.setting_profile_ic);
        } catch (Exception e) {
            this.setting_profile_ic.setImageResource(R.drawable.absher);
        }
    }

    private void performLogout() {
        SessionManager.clearSession(this);
        ApiClient.clearCachedUser(this);
        clearUserPreferences();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void recreateActivity() {
        Intent intent = new Intent(this, Settingsvact.class);
        if (this.userName != null) intent.putExtra("USERNAME", this.userName);
        if (this.user != null) intent.putExtra("USER", this.user);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    private void saveLocale(String str) {
        LanguageHelper.setLanguage(this, str);
    }

    private void setLocale(String str) {
        saveLocale(str);
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("No", null)
                .show();
    }

    private void updateUI() {
        if (this.user != null) {
            String name = this.user.getIqama_holder_name();
            boolean isAr = LanguageHelper.isArabic(this);
            String arName = this.user.getIqama_holder_name_arabic();
            if (isAr && arName != null && !arName.isEmpty()) {
                this.tvUserName.setText(arName);
            } else {
                this.tvUserName.setText(name != null && !name.isEmpty() ? name.toUpperCase() : (this.userName != null ? this.userName.toUpperCase() : ""));
            }
            
            String id = this.user.getIqama_number_withid();
            if (id != null && id.toUpperCase().startsWith("ID:")) id = id.substring(3).trim();
            if (id != null && id.toUpperCase().startsWith("ID NO.:")) id = id.substring(7).trim();
            try {
                this.tvUserId.setText(id != null && !id.isEmpty() ? getString(R.string.iqama_number_withid, id) : getString(R.string.id_not_available));
            } catch (Exception e) {
                this.tvUserId.setText(id != null && !id.isEmpty() ? "ID No.: " + id : "ID: Not Available");
            }
            
            loadProfileImage(this.user.getProfile_image_url());
        } else {
            this.tvUserName.setText(this.userName != null ? this.userName.toUpperCase() : "");
            try {
                this.tvUserId.setText(getString(R.string.id_not_available));
            } catch (Exception e) {
                this.tvUserId.setText("ID: Not Available");
            }
            this.setting_profile_ic.setImageResource(R.drawable.absher);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ServiceNavHelper.setupStatusBar(this);
        loadLocale();
        setContentView(R.layout.layout_settingsvact);
        
        appPrefs = getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        this.userName = getIntent().getStringExtra("USERNAME");
        if (this.userName == null || this.userName.isEmpty()) {
            this.userName = appPrefs.getString(LoginActivity.KEY_USERNAME, "");
        }
        this.user = (User) getIntent().getSerializableExtra("USER");
        if (this.user == null) {
            this.user = ApiClient.getCachedUser(this);
        }
        
        this.setting_profile_ic = findViewById(R.id.setting_profile_ic);
        this.tvUserName = findViewById(R.id.tvUserName);
        this.tvUserId = findViewById(R.id.tvUserId);
        
        View backArrow = findViewById(R.id.backarrow);
        if (backArrow != null) backArrow.setOnClickListener(v -> finish());
        
        View logoutButton = findViewById(R.id.logoutButton);
        if (logoutButton != null) logoutButton.setOnClickListener(v -> showLogoutConfirmation());

        View tvChangeMobile = findViewById(R.id.tvChangeMobile);
        View tvNotificationSettings = findViewById(R.id.tvNotificationSettings);
        View tvManageDigitalIdentity = findViewById(R.id.tvManageDigitalIdentity);
        View tvSupportCenter = findViewById(R.id.tvSupportCenter);
        View tvAppServicesGuide = findViewById(R.id.tvAppServicesGuide);
        View tvAccessibilityGuide = findViewById(R.id.tvAccessibilityGuide);
        View tvLiveChat = findViewById(R.id.tvLiveChat);
        View tvFaqs = findViewById(R.id.tvFaqs);
        View tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);

        if (tvChangeMobile != null) tvChangeMobile.setOnClickListener(v -> startActivity(new Intent(this, ChangeMobileNumberActivity.class)));
        if (tvNotificationSettings != null) tvNotificationSettings.setOnClickListener(v -> startActivity(new Intent(this, NotificationManagementActivity.class)));
        if (tvManageDigitalIdentity != null) tvManageDigitalIdentity.setOnClickListener(v -> startActivity(new Intent(this, ManageDigitalIdentityMenuActivity.class)));
        if (tvSupportCenter != null) tvSupportCenter.setOnClickListener(v -> startActivity(new Intent(this, SupportCenterActivity.class)));
        if (tvAppServicesGuide != null) tvAppServicesGuide.setOnClickListener(v -> startActivity(new Intent(this, AppServicesGuideActivity.class)));
        if (tvAccessibilityGuide != null) tvAccessibilityGuide.setOnClickListener(v -> startActivity(new Intent(this, AccessibilityGuideActivity.class)));
        if (tvLiveChat != null) tvLiveChat.setOnClickListener(v -> startActivity(new Intent(this, AbsherAssistantActivity.class)));
        if (tvFaqs != null) tvFaqs.setOnClickListener(v -> startActivity(new Intent(this, FaqsActivity.class)));
        if (tvPrivacyPolicy != null) tvPrivacyPolicy.setOnClickListener(v -> startActivity(new Intent(this, PrivacyPolicyActivity.class)));

        // Biometrics Switch
        Switch switchBiometrics = findViewById(R.id.switch_blur); // ID in XML is switch_blur for biometrics card
        if (switchBiometrics != null) {
            switchBiometrics.setChecked(appPrefs.getBoolean(BiometricActivity.KEY_BIOMETRIC_ENABLED, true));
            switchBiometrics.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && !BiometricActivity.canAuthenticate(this)) {
                    Toast.makeText(this, "Please set up a fingerprint or screen lock on your device first", Toast.LENGTH_LONG).show();
                    switchBiometrics.setChecked(false);
                    return;
                }
                appPrefs.edit().putBoolean(BiometricActivity.KEY_BIOMETRIC_ENABLED, isChecked).apply();
                Toast.makeText(this, isChecked ? "Biometrics Enabled" : "Biometrics Disabled", Toast.LENGTH_SHORT).show();
            });
        }

        // Blur Images Switch
        Switch switchBlur = findViewById(R.id.switch_blur2);
        if (switchBlur != null) {
            switchBlur.setChecked(appPrefs.getBoolean(KEY_BLUR_ENABLED, false));
            switchBlur.setOnCheckedChangeListener((buttonView, isChecked) -> {
                appPrefs.edit().putBoolean(KEY_BLUR_ENABLED, isChecked).apply();
                Toast.makeText(this, isChecked ? "Image Blurring Enabled" : "Image Blurring Disabled", Toast.LENGTH_SHORT).show();
            });
        }

        // Language Radio Buttons
        RadioButton rbEnglish = findViewById(R.id.radio_english);
        RadioButton rbArabic = findViewById(R.id.radio_arabic);
        String currentLang = getSharedPreferences(PREFS_NAME, 0).getString(KEY_LANG, LANG_ENGLISH);
        
        if (rbArabic != null && rbEnglish != null) {
            if (Objects.equals(currentLang, LANG_ARABIC)) rbArabic.setChecked(true);
            else rbEnglish.setChecked(true);

            rbEnglish.setOnClickListener(v -> {
                if (!LANG_ENGLISH.equals(LanguageHelper.getLanguage(this))) {
                    setLocale(LANG_ENGLISH);
                    DynamicCardRenderer.clearCache();
                    recreateActivity();
                }
            });
            rbArabic.setOnClickListener(v -> {
                if (!LANG_ARABIC.equals(LanguageHelper.getLanguage(this))) {
                    setLocale(LANG_ARABIC);
                    DynamicCardRenderer.clearCache();
                    recreateActivity();
                }
            });
        }

        // Calendar Radio Buttons
        RadioGroup calendarGroup = findViewById(R.id.calendarRadioGroup);
        RadioButton rbGregorian = findViewById(R.id.radio_gregorian);
        RadioButton rbHijri = findViewById(R.id.radio_hijri);
        TextView tvCalendarSubtitle = findViewById(R.id.tvCalendarSubtitle);
        String currentCalendar = appPrefs.getString(KEY_CALENDAR_TYPE, CALENDAR_GREGORIAN);
        if (tvCalendarSubtitle != null) {
            String calLabel = CALENDAR_HIJRI.equals(currentCalendar) ? getString(R.string.settings_hijri) : getString(R.string.settings_gregorian);
            tvCalendarSubtitle.setText(getString(R.string.settings_calendar_subtitle, calLabel));
        }

        if (calendarGroup != null && rbGregorian != null && rbHijri != null) {
            if (Objects.equals(currentCalendar, CALENDAR_HIJRI)) rbHijri.setChecked(true);
            else rbGregorian.setChecked(true);

            calendarGroup.setOnCheckedChangeListener((group, checkedId) -> {
                String newType = (checkedId == R.id.radio_hijri) ? CALENDAR_HIJRI : CALENDAR_GREGORIAN;
                String oldType = appPrefs.getString(KEY_CALENDAR_TYPE, CALENDAR_GREGORIAN);
                
                if (!Objects.equals(newType, oldType)) {
                    appPrefs.edit().putString(KEY_CALENDAR_TYPE, newType).commit();
                    if (tvCalendarSubtitle != null) {
                        String calLabel = CALENDAR_HIJRI.equals(newType) ? getString(R.string.settings_hijri) : getString(R.string.settings_gregorian);
                        tvCalendarSubtitle.setText(getString(R.string.settings_calendar_subtitle, calLabel));
                    }
                    Toast.makeText(this, "Calendar set to " + newType, Toast.LENGTH_SHORT).show();
                    DynamicCardRenderer.clearCache();
                    recreateActivity();
                }
            });
        }


        updateUI();
        if (this.user == null && this.userName != null && !this.userName.isEmpty()) {
            ApiClient.fetchUser(this, this.userName, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User fetchedUser) {
                    Settingsvact.this.user = fetchedUser;
                    updateUI();
                }

                @Override
                public void onError(String message) {
                }
            });
        }
    }
}
