package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DigitalDocumentsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String lastUser = LoginActivity.getCurrentUsername(this);
        User cached = ApiClient.getCachedUser(this);
        if ((lastUser == null || lastUser.trim().isEmpty()) && cached == null) {
            startActivity(new android.content.Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_digital_documents);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new IdViewFragment())
                    .commit();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_home);
    }
}