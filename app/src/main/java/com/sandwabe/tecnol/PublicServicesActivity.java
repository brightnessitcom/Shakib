package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

/**
 * Dedicated Public Services Screen (Accessible without login from Pre-Login "See All")
 * Features a 2-column grid in our dark theme and light theme with no bottom navigation bar.
 */
public class PublicServicesActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_public_services);

        applyTheme();

        View ivBack = findViewById(R.id.ivBack);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        View cardManageIdentity = findViewById(R.id.cardManageIdentity);
        View cardAuthServices = findViewById(R.id.cardAuthServices);
        View cardAbsherTravel = findViewById(R.id.cardAbsherTravel);
        View cardViewDocuments = findViewById(R.id.cardViewDocuments);
        View cardCivilAppointment = findViewById(R.id.cardCivilAppointment);
        View cardVisitorDoc = findViewById(R.id.cardVisitorDoc);
        View cardPassportAppt = findViewById(R.id.cardPassportAppt);
        View cardActivationSites = findViewById(R.id.cardActivationSites);
        View cardAuthenticator = findViewById(R.id.cardAuthenticator);

        if (cardManageIdentity != null) {
            cardManageIdentity.setOnClickListener(v -> {
                Intent intent = new Intent(this, ManageDigitalIdentityMenuActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
        if (cardAuthServices != null) {
            cardAuthServices.setOnClickListener(v -> {
                Intent intent = new Intent(this, AuthenticationServicesActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
        if (cardAbsherTravel != null) {
            cardAbsherTravel.setOnClickListener(v -> {
                Intent intent = new Intent(this, CreateTravelRequestActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
        if (cardViewDocuments != null) {
            cardViewDocuments.setOnClickListener(v -> {
                String lastUser = LoginActivity.getCurrentUsername(this);
                User cached = ApiClient.getCachedUser(this);
                if ((lastUser == null || lastUser.trim().isEmpty()) && cached == null) {
                    AbsherDialogHelper.show(
                        this,
                        "Can't Access Digital Documents",
                        "You can't view your Digital Documents without enabling biometric authentication on the device. Please log in and enable it on the device to enable offline access to the Digital Cards"
                    );
                } else {
                    Intent intent = new Intent(this, DigitalDocumentsActivity.class);
                    intent.putExtra("IS_PUBLIC", true);
                    startActivity(intent);
                }
            });
        }
        if (cardCivilAppointment != null) {
            cardCivilAppointment.setOnClickListener(v -> {
                Intent intent = new Intent(this, AppointmentsActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                intent.putExtra("SERVICE_TYPE", "CIVIL_AFFAIRS");
                startActivity(intent);
            });
        }
        if (cardVisitorDoc != null) {
            cardVisitorDoc.setOnClickListener(v -> {
                Intent intent = new Intent(this, VisitorDigitalDocumentActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
        if (cardPassportAppt != null) {
            cardPassportAppt.setOnClickListener(v -> {
                Intent intent = new Intent(this, AppointmentsActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                intent.putExtra("SERVICE_TYPE", "PASSPORT");
                startActivity(intent);
            });
        }
        if (cardActivationSites != null) {
            cardActivationSites.setOnClickListener(v -> {
                Intent intent = new Intent(this, ActivationSitesActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
        if (cardAuthenticator != null) {
            cardAuthenticator.setOnClickListener(v -> {
                Intent intent = new Intent(this, AbsherAuthenticatorActivity.class);
                intent.putExtra("IS_PUBLIC", true);
                startActivity(intent);
            });
        }
    }

    private void applyTheme() {
        boolean isDark = ThemeHelper.isDarkMode(this);

        int bgColor = isDark ? Color.parseColor("#161719") : Color.parseColor("#F4F6F9");
        int cardBgColor = isDark ? Color.parseColor("#26292B") : Color.parseColor("#FFFFFF");
        int iconColor = isDark ? Color.parseColor("#72D09D") : Color.parseColor("#00663D");
        int textColor = isDark ? Color.parseColor("#FFFFFF") : Color.parseColor("#1B1E21");
        int strokeColor = isDark ? Color.parseColor("#26292B") : Color.parseColor("#EAEFEA");
        int strokeWidth = isDark ? 0 : (int) (1 * getResources().getDisplayMetrics().density);

        View root = findViewById(R.id.rootLayout);
        if (root != null) root.setBackgroundColor(bgColor);

        View topBar = findViewById(R.id.topBar);
        if (topBar != null) topBar.setBackgroundColor(bgColor);

        ImageView ivBack = findViewById(R.id.ivBack);
        if (ivBack != null) ivBack.setColorFilter(iconColor);

        TextView tvTitle = findViewById(R.id.tvTitle);
        if (tvTitle != null) tvTitle.setTextColor(textColor);

        MaterialCardView[] cards = new MaterialCardView[]{
            findViewById(R.id.cardManageIdentity),
            findViewById(R.id.cardAuthServices),
            findViewById(R.id.cardAbsherTravel),
            findViewById(R.id.cardViewDocuments),
            findViewById(R.id.cardCivilAppointment),
            findViewById(R.id.cardVisitorDoc),
            findViewById(R.id.cardPassportAppt),
            findViewById(R.id.cardActivationSites),
            findViewById(R.id.cardAuthenticator)
        };

        for (MaterialCardView card : cards) {
            if (card != null) {
                card.setCardBackgroundColor(cardBgColor);
                card.setStrokeColor(strokeColor);
                card.setStrokeWidth(strokeWidth);

                if (card.getChildCount() > 0 && card.getChildAt(0) instanceof ViewGroup) {
                    ViewGroup layout = (ViewGroup) card.getChildAt(0);
                    for (int i = 0; i < layout.getChildCount(); i++) {
                        View child = layout.getChildAt(i);
                        if (child instanceof ImageView) {
                            ((ImageView) child).setColorFilter(iconColor);
                        } else if (child instanceof TextView) {
                            ((TextView) child).setTextColor(textColor);
                        }
                    }
                }
            }
        }
    }
}