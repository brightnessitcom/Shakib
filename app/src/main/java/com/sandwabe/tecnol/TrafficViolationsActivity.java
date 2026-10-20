package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrafficViolationsActivity extends AppCompatActivity {

    private TextView tvTabUnpaid;
    private TextView tvTabPaid;
    private View indicatorUnpaid;
    private View indicatorPaid;
    private TextView tvEmptySubtitle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_traffic_violations);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        tvTabUnpaid = findViewById(R.id.tvTabUnpaid);
        tvTabPaid = findViewById(R.id.tvTabPaid);
        indicatorUnpaid = findViewById(R.id.indicatorUnpaid);
        indicatorPaid = findViewById(R.id.indicatorPaid);
        tvEmptySubtitle = findViewById(R.id.tvEmptySubtitle);

        findViewById(R.id.tabUnpaid).setOnClickListener(v -> selectTab(true));
        findViewById(R.id.tabPaid).setOnClickListener(v -> selectTab(false));

        View ivSearch = findViewById(R.id.ivSearch);
        if (ivSearch != null) {
            ivSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        }

        findViewById(R.id.btnViewResolvedDisputes).setOnClickListener(v ->
                Toast.makeText(this, "No resolved disputes found.", Toast.LENGTH_SHORT).show()
        );

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
    }

    private void selectTab(boolean unpaidSelected) {
        int activeColor = androidx.core.content.ContextCompat.getColor(this, R.color.icon_tint_primary);
        int inactiveColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);
        if (unpaidSelected) {
            tvTabUnpaid.setTextColor(activeColor);
            tvTabPaid.setTextColor(inactiveColor);
            indicatorUnpaid.setVisibility(View.VISIBLE);
            indicatorPaid.setVisibility(View.INVISIBLE);
            if (tvEmptySubtitle != null) {
                tvEmptySubtitle.setText("Once you have unpaid traffic violations, they will display here.");
            }
        } else {
            tvTabUnpaid.setTextColor(inactiveColor);
            tvTabPaid.setTextColor(activeColor);
            indicatorUnpaid.setVisibility(View.INVISIBLE);
            indicatorPaid.setVisibility(View.VISIBLE);
            if (tvEmptySubtitle != null) {
                tvEmptySubtitle.setText("Once you have paid traffic violations, they will display here.");
            }
        }
    }
}
