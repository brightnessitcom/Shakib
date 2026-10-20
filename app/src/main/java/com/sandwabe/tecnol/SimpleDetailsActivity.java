package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SimpleDetailsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_simple_details);

        String title = getIntent().getStringExtra("TITLE");
        int icon = getIntent().getIntExtra("ICON", R.drawable.ic_public_card);
        String heading = getIntent().getStringExtra("HEADING");
        String description = getIntent().getStringExtra("DESCRIPTION");

        TextView tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ImageView ivCenterIcon = findViewById(R.id.ivCenterIcon);
        TextView tvHeading = findViewById(R.id.tvHeading);
        TextView tvDescription = findViewById(R.id.tvDescription);
        View llSearchBar = findViewById(R.id.llSearchBar);
        View tvSectionHeader = findViewById(R.id.tvSectionHeader);
        View llVehicleServicesSection = findViewById(R.id.llVehicleServicesSection);

        if (tvToolbarTitle != null) tvToolbarTitle.setText(title);
        if (tvHeading != null) {
            tvHeading.setText(heading);
            tvHeading.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 22);
            tvHeading.setTypeface(null, android.graphics.Typeface.BOLD);
        }
        if (tvDescription != null) tvDescription.setText(description);

        boolean isMyVehicles = title != null && (title.toLowerCase().contains("vehicle")
                || title.toLowerCase().contains("مركبات")
                || title.toLowerCase().contains("গাড়ী"));

        boolean isWeapons = title != null && (title.toLowerCase().contains("weapon")
                || title.toLowerCase().contains("أسلحة")
                || title.toLowerCase().contains("অস্ত্র"));
        if (isWeapons) {
            icon = R.drawable.ic_weapon_outline;
        }

        if (!isMyVehicles) {
            // Non-vehicle screens: show icon passed via intent, hide vehicle-specific views
            if (ivCenterIcon != null) {
                ivCenterIcon.setImageResource(icon);
                if (icon == R.drawable.ic_weapon_outline || icon == R.drawable.ic_weapon_real || icon == R.drawable.ic_accident_real
                        || icon == R.drawable.ic_accident_detail || icon == R.drawable.ic_residdent_id) {
                    ivCenterIcon.setImageTintList(null);
                    ivCenterIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
            if (llSearchBar != null) llSearchBar.setVisibility(View.GONE);
            if (tvSectionHeader != null) tvSectionHeader.setVisibility(View.GONE);
            if (llVehicleServicesSection != null) llVehicleServicesSection.setVisibility(View.GONE);
            View llEmptyState = findViewById(R.id.llEmptyState);
            if (llEmptyState != null) {
                android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) llEmptyState.getLayoutParams();
                lp.height = 0;
                lp.weight = 1f;
                llEmptyState.setLayoutParams(lp);
            }
        } else {
            // My Vehicles screen: use the dedicated car outline icon
            View llEmptyState = findViewById(R.id.llEmptyState);
            if (llEmptyState != null) {
                android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) llEmptyState.getLayoutParams();
                lp.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
                lp.weight = 0f;
                llEmptyState.setLayoutParams(lp);
            }
            if (ivCenterIcon != null) {
                ivCenterIcon.setImageResource(R.drawable.ic_no_vehicles_car);
                ivCenterIcon.setImageTintList(
                        android.content.res.ColorStateList.valueOf(
                                android.graphics.Color.parseColor("#8A9095")));
            }
            if (llSearchBar != null) {
                llSearchBar.setVisibility(View.VISIBLE);
                llSearchBar.setOnClickListener(v ->
                        startActivity(new Intent(this, SearchActivity.class)));
            }
            if (tvSectionHeader != null) tvSectionHeader.setVisibility(View.VISIBLE);
            if (llVehicleServicesSection != null) llVehicleServicesSection.setVisibility(View.VISIBLE);
        }

        // ── Vehicle Service card click listeners ──────────────────────────────

        View cardPlateDigitalWallet = findViewById(R.id.cardPlateDigitalWallet);
        if (cardPlateDigitalWallet != null) {
            cardPlateDigitalWallet.setOnClickListener(v ->
                    startActivity(new Intent(this, PlateWalletPromoActivity.class)));
        }

        View cardTrafficViolations = findViewById(R.id.cardTrafficViolations);
        if (cardTrafficViolations != null) {
            cardTrafficViolations.setOnClickListener(v ->
                    startActivity(new Intent(this, TrafficViolationsActivity.class)));
        }

        View cardWaiveAccident = findViewById(R.id.cardWaiveAccident);
        if (cardWaiveAccident != null) {
            cardWaiveAccident.setOnClickListener(v ->
                    startActivity(new Intent(this, WaiveAccidentsActivity.class)));
        }

        View cardManageOwnership = findViewById(R.id.cardManageOwnership);
        if (cardManageOwnership != null) {
            cardManageOwnership.setOnClickListener(v ->
                    startActivity(new Intent(this, VehicleOwnershipActivity.class)));
        }

        View cardRequestAccidentReport = findViewById(R.id.cardRequestAccidentReport);
        if (cardRequestAccidentReport != null) {
            cardRequestAccidentReport.setOnClickListener(v ->
                    startActivity(new Intent(this, RequestAccidentReportActivity.class)));
        }

        // ── Back button ───────────────────────────────────────────────────────
        ServiceNavHelper.setupBack(this, R.id.ivBack);

        // ── Bottom Nav ────────────────────────────────────────────────────────
        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
            ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
        }
    }
}