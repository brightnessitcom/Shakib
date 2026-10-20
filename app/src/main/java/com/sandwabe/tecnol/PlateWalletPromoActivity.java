package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PlateWalletPromoActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_plate_wallet_promo);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
        }

        findViewById(R.id.btnCreatePlateWallet).setOnClickListener(v -> {
            Intent intent = new Intent(PlateWalletPromoActivity.this, PlateWalletReviewActivity.class);
            startActivity(intent);
        });
    }
}