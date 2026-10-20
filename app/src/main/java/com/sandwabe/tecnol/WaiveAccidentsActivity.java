package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WaiveAccidentsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_waive_accidents);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        findViewById(R.id.btnWaiveAccident).setOnClickListener(v ->
                Toast.makeText(this, "No eligible accident records found to waive or object.", Toast.LENGTH_SHORT).show()
        );

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
    }
}
