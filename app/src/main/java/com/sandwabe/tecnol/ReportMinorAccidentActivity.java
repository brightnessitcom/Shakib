package com.sandwabe.tecnol;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ReportMinorAccidentActivity extends AppCompatActivity {

    private TextView tvTabReceived;
    private TextView tvTabSent;
    private View indicatorReceived;
    private View indicatorSent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_report_minor_accident);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        View tabReceived = findViewById(R.id.tabReceived);
        View tabSent = findViewById(R.id.tabSent);
        tvTabReceived = findViewById(R.id.tvTabReceived);
        tvTabSent = findViewById(R.id.tvTabSent);
        indicatorReceived = findViewById(R.id.indicatorReceived);
        indicatorSent = findViewById(R.id.indicatorSent);

        if (tabReceived != null) {
            tabReceived.setOnClickListener(v -> selectTab(true));
        }
        if (tabSent != null) {
            tabSent.setOnClickListener(v -> selectTab(false));
        }

        // Action button
        MaterialButton btnReportMinorAccident = findViewById(R.id.btnReportMinorAccident);
        if (btnReportMinorAccident != null) {
            btnReportMinorAccident.setOnClickListener(v ->
                Toast.makeText(this, "Report Minor Accident service initiated", Toast.LENGTH_SHORT).show()
            );
        }

        // Bottom Navigation Bar
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
    }

    private void selectTab(boolean isReceived) {
        if (tvTabReceived != null && tvTabSent != null && indicatorReceived != null && indicatorSent != null) {
            int activeColor = androidx.core.content.ContextCompat.getColor(this, R.color.icon_tint_primary);
            int inactiveColor = androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary);
            if (isReceived) {
                tvTabReceived.setTextColor(activeColor);
                indicatorReceived.setVisibility(View.VISIBLE);

                tvTabSent.setTextColor(inactiveColor);
                indicatorSent.setVisibility(View.INVISIBLE);
            } else {
                tvTabReceived.setTextColor(inactiveColor);
                indicatorReceived.setVisibility(View.INVISIBLE);

                tvTabSent.setTextColor(activeColor);
                indicatorSent.setVisibility(View.VISIBLE);
            }
        }
    }
}
