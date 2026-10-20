package com.sandwabe.tecnol;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;

public class LanguageHelper {
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_LANG = "AppLang";

    public static Context wrapContext(Context context) {
        String lang = getLanguage(context);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        int systemNightMode = Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (systemNightMode != Configuration.UI_MODE_NIGHT_UNDEFINED) {
            config.uiMode = (config.uiMode & ~Configuration.UI_MODE_NIGHT_MASK) | systemNightMode;
        }

        return context.createConfigurationContext(config);
    }

    public static String getLanguage(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_LANG, "en");
    }

    public static void setLanguage(Context context, String lang) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit().putString(KEY_LANG, lang).commit();
    }
    
    public static boolean isArabic(Context context) {
        return "ar".equals(getLanguage(context));
    }
}
