package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class QabulRequestsActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_qabul_requests);

        // Back button
        ServiceNavHelper.setupBack(this, R.id.ivBack);

        // Bottom nav with Other tab highlighted
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_other);

        // ViewPager2 + TabLayout
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new QabulPagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Pending");
            } else {
                tab.setText("Accepted");
            }
        }).attach();
    }

    // Pager adapter
    private static class QabulPagerAdapter extends FragmentStateAdapter {

        QabulPagerAdapter(@NonNull FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new QabulPendingFragment();
            } else {
                return new QabulAcceptedFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
