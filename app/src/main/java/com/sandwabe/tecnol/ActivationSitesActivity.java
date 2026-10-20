package com.sandwabe.tecnol;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivationSitesActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_CODE = 1001;
    private static final String TAG = "ActivationSites";
    private WebView mapWebView;
    private FusedLocationProviderClient fusedLocationClient;
    private double lastLat = 24.8949; // Default Tilagor, Sylhet
    private double lastLng = 91.8687;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_activation_sites);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_other);

        // Dismiss info banner
        View ivCloseBanner = findViewById(R.id.ivCloseBanner);
        View cardBanner = findViewById(R.id.cardBanner);
        if (ivCloseBanner != null && cardBanner != null) {
            ivCloseBanner.setOnClickListener(v -> cardBanner.setVisibility(View.GONE));
        }

        // Custom My Location FAB
        View btnMyLocation = findViewById(R.id.btnMyLocation);
        if (btnMyLocation != null) {
            btnMyLocation.setOnClickListener(v -> requestAndCenterLocation());
        }

        // Fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Setup Dark Map WebView
        mapWebView = findViewById(R.id.mapWebView);
        if (mapWebView != null) {
            mapWebView.setBackgroundColor(Color.parseColor("#202020"));
            WebSettings settings = mapWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            settings.setAllowFileAccess(true);
            settings.setAllowContentAccess(true);
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            mapWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onConsoleMessage(ConsoleMessage cm) {
                    Log.d(TAG, "WebView Console: " + cm.message() + " -- From line "
                            + cm.lineNumber() + " of " + cm.sourceId());
                    return true;
                }
            });

            mapWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    updateMapLocation(lastLat, lastLng);
                }
            });

            mapWebView.loadUrl("file:///android_asset/dark_map.html");
        }

        // Check & request location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        } else {
            requestAndCenterLocation();
        }
    }

    private void requestAndCenterLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) return;

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        applyLocation(location);
                    } else {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, lastLocation -> {
                                    if (lastLocation != null) {
                                        applyLocation(lastLocation);
                                    }
                                });
                    }
                });
    }

    private void applyLocation(Location location) {
        lastLat = location.getLatitude();
        lastLng = location.getLongitude();
        updateMapLocation(lastLat, lastLng);
    }

    private void updateMapLocation(double lat, double lng) {
        if (mapWebView != null) {
            mapWebView.post(() -> {
                String js = "if (typeof setLocation === 'function') { setLocation(" + lat + ", " + lng + "); }";
                mapWebView.evaluateJavascript(js, null);
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestAndCenterLocation();
            }
        }
    }
}