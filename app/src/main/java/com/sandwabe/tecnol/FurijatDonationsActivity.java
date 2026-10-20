package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FurijatDonationsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_furijat_donations);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        findViewById(R.id.btnMakeDonation).setOnClickListener(v ->
            Toast.makeText(this, "Donation service currently offline.", Toast.LENGTH_SHORT).show()
        );

        findViewById(R.id.btnSeeMostDeserving).setOnClickListener(v ->
            startActivity(new android.content.Intent(this, DeservingCasesActivity.class))
        );

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_other);
    }
}
