package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AuthorizationsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_authorizations);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        findViewById(R.id.btnIssueAuthorization).setOnClickListener(v ->
            Toast.makeText(this, "Authorization service currently unavailable.", Toast.LENGTH_SHORT).show()
        );

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_other);
    }
}
