package com.sandwabe.tecnol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
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
        super.onCreate(bundle);
        
        try {
            setContentView(R.layout.activity_splash_screen);
            
            // Modern full screen splash
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                getWindow().setDecorFitsSystemWindows(false);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
