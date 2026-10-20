package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationServicesActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_authentication_services);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        View cardAuthRequests = findViewById(R.id.cardAuthRequests);
        if (cardAuthRequests != null) {
            cardAuthRequests.setOnClickListener(v -> {
                Intent intent = new Intent(this, AuthenticationRequestsActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
    }
}
