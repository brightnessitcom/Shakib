package com.sandwabe.tecnol;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class SessionManager implements Application.ActivityLifecycleCallbacks {

    public static final String KEY_LOGIN_TIMESTAMP = "login_timestamp";
    public static final long MAX_SESSION_DURATION_MS = 15 * 60 * 1000L; // 15 minutes max login duration

    private static SessionManager instance;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private WeakReference<Activity> currentActivityRef = new WeakReference<>(null);
    private int startedActivityCount = 0;

    private final Runnable appBackgroundRunnable = new Runnable() {
        @Override
        public void run() {
            if (startedActivityCount <= 0) {
                Context context = MyApp.getAppContext();
                if (context != null) {
                    SharedPreferences prefs = context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
                    boolean wasLoggedIn = prefs.getBoolean(LoginActivity.KEY_IS_LOGGED_IN, false)
                            && prefs.getBoolean(LoginActivity.KEY_IS_VERIFIED, false);
                    if (wasLoggedIn) {
                        clearSession(context);
                    }
                }
            }
        }
    };

    private final Runnable sessionTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            Activity activity = currentActivityRef.get();
            Context context = activity != null ? activity : MyApp.getAppContext();
            if (context == null) return;

            if (!isSessionValid(context)) {
                if (activity != null && !activity.isFinishing() && !isAuthScreen(activity)) {
                    performAutoLogout(activity);
                } else {
                    clearSession(context);
                }
            }
        }
    };

    public static synchronized void init(Application application) {
        if (instance == null) {
            instance = new SessionManager();
            application.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static void recordLoginTimestamp(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putLong(KEY_LOGIN_TIMESTAMP, System.currentTimeMillis()).apply();
    }

    public static boolean isSessionValid(Context context) {
        if (context == null) return false;
        SharedPreferences prefs = context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(LoginActivity.KEY_IS_LOGGED_IN, false);
        boolean isVerified = prefs.getBoolean(LoginActivity.KEY_IS_VERIFIED, false);

        if (!isLoggedIn || !isVerified) {
            return false;
        }

        long loginTime = prefs.getLong(KEY_LOGIN_TIMESTAMP, 0L);
        long now = System.currentTimeMillis();

        if (loginTime <= 0L) {
            // Initialize timestamp for already-logged-in session so the 10-minute timer starts now
            prefs.edit().putLong(KEY_LOGIN_TIMESTAMP, now).apply();
            return true;
        }

        long elapsed = now - loginTime;
        if (elapsed >= MAX_SESSION_DURATION_MS || elapsed < 0L) {
            clearSession(context);
            if (context instanceof SplashScreenActivity) {
                Toast.makeText(context, "Your session is expired now.", Toast.LENGTH_LONG).show();
            }
            return false;
        }

        return true;
    }

    public static long getRemainingSessionMs(Context context) {
        if (context == null) return 0L;
        SharedPreferences prefs = context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
        long loginTime = prefs.getLong(KEY_LOGIN_TIMESTAMP, 0L);
        if (loginTime <= 0L) return MAX_SESSION_DURATION_MS;
        long remaining = MAX_SESSION_DURATION_MS - (System.currentTimeMillis() - loginTime);
        return Math.max(0L, remaining);
    }

    public static void clearSession(Context context) {
        if (context == null) return;
        if (instance != null) {
            instance.handler.removeCallbacks(instance.sessionTimeoutRunnable);
        }
        SharedPreferences prefs = context.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putBoolean(LoginActivity.KEY_IS_LOGGED_IN, false)
                .putBoolean(LoginActivity.KEY_IS_VERIFIED, false)
                .remove(KEY_LOGIN_TIMESTAMP)
                .apply();
    }

    public static void performAutoLogout(Activity activity) {
        if (activity == null || activity.isFinishing()) return;
        clearSession(activity);
        Toast.makeText(activity, "Your session is expired now.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    private boolean isAuthScreen(Activity activity) {
        return activity instanceof SplashScreenActivity
                || activity instanceof PreLoginActivity
                || activity instanceof LoginActivity
                || activity instanceof SMSVerificationActivity
                || activity instanceof BiometricActivity
                || activity instanceof BlockingActivity;
    }

    private boolean isPublicInfoScreen(Activity activity) {
        return activity instanceof FaqsActivity
                || activity instanceof PrivacyPolicyActivity
                || activity instanceof DigitalDocumentsActivity
                || activity instanceof AuthenticationRequestsActivity
                || activity instanceof PublicServicesActivity
                || activity instanceof ManageDigitalIdentityMenuActivity
                || activity instanceof ActivateDigitalIdentityActivity
                || activity instanceof PauseDigitalIdentityActivity
                || activity instanceof AuthenticationServicesActivity
                || activity instanceof CreateTravelRequestActivity
                || activity instanceof AppointmentsActivity
                || activity instanceof VisitorDigitalDocumentActivity
                || activity instanceof ActivationSitesActivity;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        startedActivityCount++;
        handler.removeCallbacks(appBackgroundRunnable);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivityRef = new WeakReference<>(activity);
        handler.removeCallbacks(sessionTimeoutRunnable);

        if (isAuthScreen(activity)) {
            return;
        }

        SharedPreferences prefs = activity.getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
        boolean wasLoggedIn = prefs.getBoolean(LoginActivity.KEY_IS_LOGGED_IN, false)
                && prefs.getBoolean(LoginActivity.KEY_IS_VERIFIED, false);

        if (!wasLoggedIn || isPublicInfoScreen(activity)) {
            return;
        }

        if (!isSessionValid(activity)) {
            performAutoLogout(activity);
            return;
        }

        long remainingMs = getRemainingSessionMs(activity);
        handler.postDelayed(sessionTimeoutRunnable, remainingMs);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Activity current = currentActivityRef.get();
        if (current == activity) {
            handler.removeCallbacks(sessionTimeoutRunnable);
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        startedActivityCount--;
        if (startedActivityCount <= 0) {
            startedActivityCount = 0;
            if (!activity.isChangingConfigurations()) {
                handler.removeCallbacks(appBackgroundRunnable);
                if (activity.isFinishing()) {
                    appBackgroundRunnable.run();
                } else {
                    handler.postDelayed(appBackgroundRunnable, 500L);
                }
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Activity current = currentActivityRef.get();
        if (current == activity) {
            currentActivityRef.clear();
        }
    }
}
