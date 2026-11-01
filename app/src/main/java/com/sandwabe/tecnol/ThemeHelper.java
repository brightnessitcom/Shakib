package com.sandwabe.tecnol;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    public static final String PREF_NAME = "UserAppPrefs";
    public static final String KEY_THEME = "app_theme_mode";

    public static final int THEME_SYSTEM = 0;
    public static final int THEME_LIGHT = 1;
    public static final int THEME_DARK = 2;

    public static void initTheme(Context context) {
        applySystemTheme();
    }

    public static void setThemeMode(Context context, int themeMode) {
        applySystemTheme();
    }

    public static int getThemeMode(Context context) {
        return THEME_SYSTEM;
    }

    public static void applySystemTheme() {
        int systemNightMode = Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (systemNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void applyTheme(int themeMode) {
        applySystemTheme();
    }

    public static boolean isDarkMode(Context context) {
        try {
            if (context != null) {
                int mode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mode == Configuration.UI_MODE_NIGHT_YES) return true;
                if (mode == Configuration.UI_MODE_NIGHT_NO) return false;
            }
            int systemNightMode = Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            return systemNightMode == Configuration.UI_MODE_NIGHT_YES;
        } catch (Exception e) {
            return false;
        }
    }
}

