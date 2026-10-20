package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BirthCertificatesServicesActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_birth_certificates_services);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_other);

        View cardViewBirthCerts = findViewById(R.id.cardViewBirthCerts);
        if (cardViewBirthCerts != null) {
            cardViewBirthCerts.setOnClickListener(v ->
                    startActivity(new Intent(this, ViewBirthCertificateActivity.class))
            );
        }

        View cardUpdateBirthCert = findViewById(R.id.cardUpdateBirthCert);
        if (cardUpdateBirthCert != null) {
            cardUpdateBirthCert.setOnClickListener(v ->
                    startActivity(new Intent(this, UpdateBirthCertificateActivity.class))
            );
        }

        View cardReissueBirthCert = findViewById(R.id.cardReissueBirthCert);
        if (cardReissueBirthCert != null) {
            cardReissueBirthCert.setOnClickListener(v ->
                    startActivity(new Intent(this, ViewBirthCertificateActivity.class))
            );
        }

        View btnViewRequests = findViewById(R.id.btnViewRequests);
        if (btnViewRequests != null) {
            btnViewRequests.setOnClickListener(v ->
                    startActivity(new Intent(this, DeliveryRequestsActivity.class))
            );
        }
    }
}
