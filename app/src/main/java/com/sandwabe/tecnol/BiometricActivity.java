package com.sandwabe.tecnol;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class BiometricActivity extends AppCompatActivity {

    private static final String TAG = "BiometricAuth";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    public static final String KEY_BIOMETRIC_ENABLED = "biometric_enabled";
    private static final String KEY_LAST_BIOMETRIC = "last_biometric_time";
    private static final int MAX_RETRY = 3;
    private static final String PREFS_NAME = "UserAppPrefs";

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private boolean isAuthenticating = false;
    private int retryCount = 0;
    private TextView tvBiometricStatus;

    public class BiometricAuthCallback extends BiometricPrompt.AuthenticationCallback {
        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            isAuthenticating = false;
            Log.d(TAG, "onAuthenticationError: " + errorCode + " - " + errString);

            switch (errorCode) {
                case BiometricPrompt.ERROR_LOCKOUT:
                case BiometricPrompt.ERROR_LOCKOUT_PERMANENT:
                    Toast.makeText(BiometricActivity.this, "Too many failed attempts. Please use password.", Toast.LENGTH_SHORT).show();
                    goToLoginActivity();
                    break;
                case BiometricPrompt.ERROR_HW_NOT_PRESENT:
                case BiometricPrompt.ERROR_HW_UNAVAILABLE:
                case BiometricPrompt.ERROR_NO_BIOMETRICS:
                    goToMainActivity();
                    break;
                case BiometricPrompt.ERROR_USER_CANCELED:
                case BiometricPrompt.ERROR_NEGATIVE_BUTTON:
                    if (tvBiometricStatus != null) {
                        tvBiometricStatus.setText("Tap screen to verify identity");
                    }
                    break;
                default:
                    if (tvBiometricStatus != null) {
                        tvBiometricStatus.setText("Authentication paused. Tap to retry.");
                    }
                    break;
            }
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            isAuthenticating = false;
            retryCount = 0;
            Log.d(TAG, "onAuthenticationSucceeded");
            sharedPreferences.edit().putLong(KEY_LAST_BIOMETRIC, System.currentTimeMillis()).apply();
            goToMainActivity();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            isAuthenticating = false;
            retryCount++;
            Log.d(TAG, "onAuthenticationFailed: " + retryCount);
            if (retryCount >= MAX_RETRY) {
                Toast.makeText(BiometricActivity.this, "Too many failed attempts. Please use password.", Toast.LENGTH_SHORT).show();
                goToLoginActivity();
            } else {
                if (tvBiometricStatus != null) {
                    tvBiometricStatus.setText("Not recognized. Try again (" + retryCount + "/" + MAX_RETRY + ")");
                }
            }
        }
    }

    private final BiometricAuthCallback authenticationCallback = new BiometricAuthCallback();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_biometric);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        handler = new Handler(Looper.getMainLooper());
        executor = ContextCompat.getMainExecutor(this);
        tvBiometricStatus = findViewById(R.id.tvBiometricStatus);

        View rootLayout = findViewById(R.id.rootBiometricLayout);
        if (rootLayout != null) {
            rootLayout.setOnClickListener(v -> startBiometricAuth());
        }

        View btnUsePassword = findViewById(R.id.btnUsePassword);
        if (btnUsePassword != null) {
            btnUsePassword.setOnClickListener(v -> goToLoginActivity());
        }

        if (!canAuthenticate(this)) {
            Log.d(TAG, "Device cannot authenticate, proceeding to MainActivity");
            goToMainActivity();
            return;
        }

        setupBiometricAuth();
        handler.postDelayed(this::startBiometricAuth, 300L);
    }

    private void setupBiometricAuth() {
        biometricPrompt = new BiometricPrompt(this, executor, authenticationCallback);

        BiometricManager bm = BiometricManager.from(this);
        int bioRes = bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
        boolean hasBiometrics = (bioRes == BiometricManager.BIOMETRIC_SUCCESS);

        BiometricPrompt.PromptInfo.Builder builder = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Verify Your Identity")
                .setSubtitle(hasBiometrics ? "Use your fingerprint or device PIN to continue" : "Enter your device PIN to continue");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (hasBiometrics) {
                builder.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
            } else {
                builder.setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL);
            }
        } else {
            builder.setDeviceCredentialAllowed(true);
        }
        promptInfo = builder.build();
    }

    private void startBiometricAuth() {
        if (isFinishing() || isAuthenticating || biometricPrompt == null || promptInfo == null) return;
        isAuthenticating = true;
        if (tvBiometricStatus != null) {
            tvBiometricStatus.setText("Touch the fingerprint sensor or enter PIN");
        }
        try {
            biometricPrompt.authenticate(promptInfo);
        } catch (Exception e) {
            isAuthenticating = false;
            Log.e(TAG, "Biometric authenticate exception", e);
            goToMainActivity();
        }
    }

    private void goToMainActivity() {
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .putBoolean(LoginActivity.KEY_IS_LOGGED_IN, true)
                    .putBoolean(LoginActivity.KEY_IS_VERIFIED, true)
                    .putLong(SessionManager.KEY_LOGIN_TIMESTAMP, System.currentTimeMillis())
                    .apply();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("biometric_failed", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Prevent bypassing biometric; minimize the app to home screen
        moveTaskToBack(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (biometricPrompt != null) {
            try {
                biometricPrompt.cancelAuthentication();
            } catch (Exception ignored) {}
        }
        isAuthenticating = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public static boolean canAuthenticate(Context context) {
        try {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            boolean isDeviceSecure = (km != null && km.isDeviceSecure());
            BiometricManager bm = BiometricManager.from(context);
            int bioResWeak = bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK);
            int bioResStrong = bm.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG);
            boolean hasBiometrics = (bioResWeak == BiometricManager.BIOMETRIC_SUCCESS || bioResStrong == BiometricManager.BIOMETRIC_SUCCESS);

            Log.d(TAG, "canAuthenticate: isDeviceSecure=" + isDeviceSecure + ", hasBiometrics=" + hasBiometrics);
            return isDeviceSecure || hasBiometrics;
        } catch (Exception e) {
            Log.e(TAG, "canAuthenticate check error", e);
            return false;
        }
    }

    public static void setBiometricEnabled(Context context, boolean enabled) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_BIOMETRIC_ENABLED, enabled)
                .apply();
    }

    public static boolean shouldUseBiometric(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isBioEnabled = prefs.getBoolean(KEY_BIOMETRIC_ENABLED, true);
        boolean canAuth = canAuthenticate(context);
        Log.d(TAG, "shouldUseBiometric: isBioEnabled=" + isBioEnabled + ", canAuth=" + canAuth);
        return isBioEnabled && canAuth;
    }
}
