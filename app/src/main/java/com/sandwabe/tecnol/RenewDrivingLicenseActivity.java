package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class RenewDrivingLicenseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_pending_delivery_requests);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        // Action button to proceed with license renewal
        MaterialButton btnRenewDrivingLicense = findViewById(R.id.btnRenewDrivingLicense);
        if (btnRenewDrivingLicense != null) {
            btnRenewDrivingLicense.setOnClickListener(v -> {
                Intent intent = new Intent(this, RenewDrivingLicenseFormActivity.class);
                startActivity(intent);
            });
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
    }
}
