package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExpandedCardActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_expanded_card);

        String title = getIntent().getStringExtra("ITEM_TITLE");
        String subtitle = getIntent().getStringExtra("ITEM_SUBTITLE");
        int icon = getIntent().getIntExtra("ITEM_ICON", R.drawable.ic_services_outline);

        TextView tvExpandedTitle = findViewById(R.id.tvExpandedTitle);
        TextView tvExpandedMainTitle = findViewById(R.id.tvExpandedMainTitle);
        TextView tvExpandedSubtitle = findViewById(R.id.tvExpandedSubtitle);
        ImageView ivExpandedIcon = findViewById(R.id.ivExpandedIcon);

        if (title != null && !title.isEmpty()) {
            if (tvExpandedTitle != null) tvExpandedTitle.setText(title);
            if (tvExpandedMainTitle != null) tvExpandedMainTitle.setText(title);
        }

        if (subtitle != null && !subtitle.isEmpty() && tvExpandedSubtitle != null) {
            tvExpandedSubtitle.setText(subtitle);
        }

        if (ivExpandedIcon != null) {
            ivExpandedIcon.setImageResource(icon);
        }

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        findViewById(R.id.btnAction).setOnClickListener(v -> 
            Toast.makeText(this, "Record is up to date and active.", Toast.LENGTH_SHORT).show()
        );

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_my_services);
    }
}