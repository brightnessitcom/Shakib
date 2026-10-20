package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class ManageDigitalIdentityMenuActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_manage_digital_identity_menu);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        View cardIssueIdentity = findViewById(R.id.cardIssueIdentity);
        View cardChangeMobile = findViewById(R.id.cardChangeMobile);
        View cardActivateIdentity = findViewById(R.id.cardActivateIdentity);
        View cardPauseIdentity = findViewById(R.id.cardPauseIdentity);

        if (cardIssueIdentity != null) {
            cardIssueIdentity.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.absher.sa/wps/portal/individuals/static/registration"));
                startActivity(browserIntent);
            });
        }

        if (cardChangeMobile != null) {
            cardChangeMobile.setOnClickListener(v -> {
                Intent intent = new Intent(this, ChangeMobileNumberActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }

        if (cardActivateIdentity != null) {
            cardActivateIdentity.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.absher.sa/wps/portal/individuals/static/registration"));
                startActivity(browserIntent);
            });
        }

        if (cardPauseIdentity != null) {
            cardPauseIdentity.setOnClickListener(v -> {
                Intent intent = new Intent(this, PauseDigitalIdentityActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
    }
}