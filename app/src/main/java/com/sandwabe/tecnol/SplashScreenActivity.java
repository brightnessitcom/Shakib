package com.sandwabe.tecnol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        ThemeHelper.applySystemTheme();
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    public void checkAndNavigate() {
        if (isFinishing()) return;
        
        try {
            Intent nextIntent;
            String currentUsername = LoginActivity.getCurrentUsername(this);
            boolean hasSavedUser = (currentUsername != null && !currentUsername.trim().isEmpty())
                    || (ApiClient.getCachedUser(this) != null);

            if (hasSavedUser && BiometricActivity.shouldUseBiometric(this)) {
                nextIntent = new Intent(this, BiometricActivity.class);
            } else if (SessionManager.isSessionValid(this)) {
                nextIntent = new Intent(this, MainActivity.class);
            } else {
                nextIntent = new Intent(this, PreLoginActivity.class);
            }
            
            startActivity(nextIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        } catch (Exception e) {
            Log.e("SplashScreen", "Navigation failed", e);
            startActivity(new Intent(this, PreLoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        ThemeHelper.applySystemTheme();
        super.onCreate(bundle);
        
        try {
            boolean isDark = ThemeHelper.isDarkMode(this);
            int bgColor = isDark ? Color.parseColor("#1A1A1A") : Color.parseColor("#F4F6F9");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(bgColor);
                window.setNavigationBarColor(bgColor);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getWindow().setDecorFitsSystemWindows(false);
                WindowInsetsController insetsController = getWindow().getInsetsController();
                if (insetsController != null) {
                    if (isDark) {
                        insetsController.setSystemBarsAppearance(0,
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS | WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
                    } else {
                        insetsController.setSystemBarsAppearance(
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS | WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS | WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS);
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = getWindow().getDecorView();
                int flags = decor.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (isDark) {
                    flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        flags &= ~View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    }
                } else {
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    }
                }
                decor.setSystemUiVisibility(flags);
            }

            setContentView(R.layout.activity_splash_screen);

            View root = findViewById(R.id.coordinator);
            if (root != null) {
                root.setBackgroundColor(bgColor);
            }
            
            LottieAnimationView animationView = findViewById(R.id.lottie_animation_view);
            if (animationView != null) {
                try {
                    animationView.setAnimation(R.raw.absher_loading);
                    animationView.playAnimation();
                } catch (Exception e) {
                    Log.e("SplashScreen", "Lottie failed", e);
                }
            }
            
            new Handler(Looper.getMainLooper()).postDelayed(this::checkAndNavigate, 3000L);
        } catch (Exception e) {
            Log.e("SplashScreen", "Init failed", e);
            checkAndNavigate();
        }
    }
}
