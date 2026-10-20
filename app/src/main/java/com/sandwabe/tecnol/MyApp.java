package com.sandwabe.tecnol;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

public class MyApp extends Application {
    private static Context appContext;
    private static Activity currentActivity;

    public static Context getAppContext() {
        return appContext;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    @Override
    protected void attachBaseContext(Context base) {
        ThemeHelper.initTheme(base);
        super.attachBaseContext(LanguageHelper.wrapContext(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        SessionManager.init(this);
        setupAppShortcuts();
        
        ThemeHelper.initTheme(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}
            @Override
            public void onActivityStarted(@NonNull Activity activity) {}
            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                currentActivity = activity;
            }
            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                if (currentActivity == activity) {
                    currentActivity = null;
                }
            }
            @Override
            public void onActivityStopped(@NonNull Activity activity) {}
            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}
            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                if (currentActivity == activity) {
                    currentActivity = null;
                }
            }
        });
        
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Log.e("CRASH", "Uncaught exception", throwable);
            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(appContext, "App Crash: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            });
            // Keep it alive long enough to show toast? No, but logging is key.
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ThemeHelper.applySystemTheme();
        if (currentActivity != null && !(currentActivity instanceof MainActivity) && !currentActivity.isFinishing() && !currentActivity.isDestroyed()) {
            final Activity act = currentActivity;
            act.runOnUiThread(() -> {
                if (!act.isFinishing() && !act.isDestroyed()) {
                    act.recreate();
                }
            });
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            SessionManager.clearSession(this);
        }
    }

    private void setupAppShortcuts() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            try {
                android.content.pm.ShortcutManager shortcutManager = getSystemService(android.content.pm.ShortcutManager.class);
                if (shortcutManager != null) {
                    android.content.Intent docIntent = new android.content.Intent(this, DigitalDocumentsActivity.class);
                    docIntent.setAction(android.content.Intent.ACTION_VIEW);
                    docIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    android.content.pm.ShortcutInfo docShortcut = new android.content.pm.ShortcutInfo.Builder(this, "digital_documents")
                            .setShortLabel(getString(R.string.shortcut_digital_documents))
                            .setLongLabel(getString(R.string.shortcut_digital_documents))
                            .setIcon(android.graphics.drawable.Icon.createWithResource(this, R.drawable.ic_public_qr_scanner))
                            .setIntent(docIntent)
                            .setRank(1)
                            .build();

                    android.content.Intent authIntent = new android.content.Intent(this, AbsherAuthenticatorActivity.class);
                    authIntent.setAction(android.content.Intent.ACTION_VIEW);
                    authIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    android.content.pm.ShortcutInfo authShortcut = new android.content.pm.ShortcutInfo.Builder(this, "absher_authenticator")
                            .setShortLabel(getString(R.string.shortcut_absher_authenticator))
                            .setLongLabel(getString(R.string.shortcut_absher_authenticator))
                            .setIcon(android.graphics.drawable.Icon.createWithResource(this, R.drawable.ic_shortcut_lock))
                            .setIntent(authIntent)
                            .setRank(2)
                            .build();

                    shortcutManager.setDynamicShortcuts(java.util.Arrays.asList(docShortcut, authShortcut));
                }
            } catch (Exception e) {
                Log.e("MyApp", "Failed to setup app shortcuts", e);
            }
        }
    }
}
