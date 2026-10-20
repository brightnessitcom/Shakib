package com.sandwabe.tecnol;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class VehicleOwnershipActivity extends AppCompatActivity {

    private TextView tvTabBuying;
    private TextView tvTabSelling;
    private View indicatorBuying;
    private View indicatorSelling;
    private TextView tvEmptyTitle;
    private TextView tvEmptySubtitle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_vehicle_ownership);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        tvTabBuying = findViewById(R.id.tvTabBuying);
        tvTabSelling = findViewById(R.id.tvTabSelling);
        indicatorBuying = findViewById(R.id.indicatorBuying);
        indicatorSelling = findViewById(R.id.indicatorSelling);
        tvEmptyTitle = findViewById(R.id.tvEmptyTitle);
        tvEmptySubtitle = findViewById(R.id.tvEmptySubtitle);

        findViewById(R.id.tabBuying).setOnClickListener(v -> selectTab(true));
        findViewById(R.id.tabSelling).setOnClickListener(v -> selectTab(false));

        findViewById(R.id.btnOwnershipHistory).setOnClickListener(v ->
                Toast.makeText(this, "No ownership requests in history.", Toast.LENGTH_SHORT).show()
        );

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
    }

    private void selectTab(boolean buyingSelected) {
        int activeColor = androidx.core.content.ContextCompat.getColor(this, R.color.icon_tint_primary);
        int inactiveColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);
        if (buyingSelected) {
            tvTabBuying.setTextColor(activeColor);
            tvTabSelling.setTextColor(inactiveColor);
            indicatorBuying.setVisibility(View.VISIBLE);
            indicatorSelling.setVisibility(View.INVISIBLE);
            if (tvEmptyTitle != null) tvEmptyTitle.setText("No Buying Requests");
            if (tvEmptySubtitle != null) {
                tvEmptySubtitle.setText("Once you have buying requests they will display here.");
            }
        } else {
            tvTabBuying.setTextColor(inactiveColor);
            tvTabSelling.setTextColor(activeColor);
            indicatorBuying.setVisibility(View.INVISIBLE);
            indicatorSelling.setVisibility(View.VISIBLE);
            if (tvEmptyTitle != null) tvEmptyTitle.setText("No Selling Requests");
            if (tvEmptySubtitle != null) {
                tvEmptySubtitle.setText("Once you have selling requests they will display here.");
            }
        }
    }
}
