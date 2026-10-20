package com.sandwabe.tecnol;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import java.util.List;

public class OtherFragment extends Fragment {

    private LinearLayout llPreferredContainer;
    private LinearLayout llOtherContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other, container, false);

        // Header elements for dynamic scroll transition
        View leftIcons = view.findViewById(R.id.leftIcons);
        View tvStickyHeaderTitle = view.findViewById(R.id.tvStickyHeaderTitle);

        // Scroll view setup: reset to top and toggle sticky header title on scroll
        NestedScrollView scrollView = view.findViewById(R.id.otherScrollView);
        if (scrollView != null) {
            scrollView.post(() -> scrollView.scrollTo(0, 0));
            scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                boolean isScrolled = scrollY > 80;
                if (leftIcons != null) {
                    leftIcons.setVisibility(isScrolled ? View.GONE : View.VISIBLE);
                }
                if (tvStickyHeaderTitle != null) {
                    tvStickyHeaderTitle.setVisibility(isScrolled ? View.VISIBLE : View.GONE);
                }
            });
        }

        // Notifications icon
        View notificationsIcon = view.findViewById(R.id.notificationsIcon);
        if (notificationsIcon != null) {
            notificationsIcon.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), InboxActivity.class))
            );
        }

        // Settings icon
        View settingsIcon = view.findViewById(R.id.settingsIcon);
        if (settingsIcon != null) {
            settingsIcon.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), Settingsvact.class))
            );
        }

        // Search bar
        View searchBarContainer = view.findViewById(R.id.searchBarContainer);
        if (searchBarContainer != null) {
            searchBarContainer.setOnClickListener(v ->
                startActivity(new Intent(getContext(), SearchActivity.class))
            );
        }

        // Personalise Banner Dismiss
        View cardPersonaliseBanner = view.findViewById(R.id.cardPersonaliseBanner);
        View btnDismissBanner = view.findViewById(R.id.btnDismissBanner);
        if (btnDismissBanner != null && cardPersonaliseBanner != null) {
            btnDismissBanner.setOnClickListener(v -> cardPersonaliseBanner.setVisibility(View.GONE));
        }

        // Personalise Space Actions (banner link & bottom button)
        View tvBannerPersonaliseSpace = view.findViewById(R.id.tvBannerPersonaliseSpace);
        if (tvBannerPersonaliseSpace != null) {
            tvBannerPersonaliseSpace.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), PersonaliseSpaceActivity.class))
            );
        }

        View btnPersonaliseSpace = view.findViewById(R.id.btnPersonaliseSpace);
        if (btnPersonaliseSpace != null) {
            btnPersonaliseSpace.setOnClickListener(v -> 
                startActivity(new Intent(getContext(), PersonaliseSpaceActivity.class))
            );
        }

        llPreferredContainer = view.findViewById(R.id.llPreferredContainer);
        llOtherContainer = view.findViewById(R.id.llOtherContainer);

        populateAllServices();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateAllServices();
    }

    private void populateAllServices() {
        if (getContext() == null || llPreferredContainer == null || llOtherContainer == null) return;

        List<PersonaliseSpaceManager.ServiceItem> preferred = PersonaliseSpaceManager.getPreferredServices(getContext());
        List<PersonaliseSpaceManager.ServiceItem> other = PersonaliseSpaceManager.getOtherServices(getContext());

        populateSection(llPreferredContainer, preferred);
        populateSection(llOtherContainer, other);
    }

    private void populateSection(LinearLayout container, List<PersonaliseSpaceManager.ServiceItem> items) {
        container.removeAllViews();
        if (getContext() == null) return;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        int margin8 = dpToPx(8);
        int margin5 = dpToPx(5);
        int height116 = dpToPx(116);

        for (int i = 0; i < items.size(); i += 2) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setBaselineAligned(false);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(margin8, 0, margin8, 0);
            row.setLayoutParams(rowParams);

            // Left Item
            PersonaliseSpaceManager.ServiceItem itemLeft = items.get(i);
            View viewLeft = inflateServiceView(inflater, row, itemLeft, height116, margin5);
            row.addView(viewLeft);

            // Right Item or invisible balance placeholder
            if (i + 1 < items.size()) {
                PersonaliseSpaceManager.ServiceItem itemRight = items.get(i + 1);
                View viewRight = inflateServiceView(inflater, row, itemRight, height116, margin5);
                row.addView(viewRight);
            } else {
                View placeholder = new View(getContext());
                LinearLayout.LayoutParams pParams = new LinearLayout.LayoutParams(0, height116, 1.0f);
                pParams.setMargins(margin5, margin5, margin5, margin5);
                placeholder.setLayoutParams(pParams);
                placeholder.setVisibility(View.INVISIBLE);
                row.addView(placeholder);
            }

            container.addView(row);
        }
    }

    private View inflateServiceView(LayoutInflater inflater, ViewGroup parent, PersonaliseSpaceManager.ServiceItem item, int height, int margin) {
        View view = inflater.inflate(R.layout.item_other_service, parent, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, height, 1.0f);
        params.setMargins(margin, margin, margin, margin);
        view.setLayoutParams(params);

        ImageView icon = view.findViewById(R.id.icon);
        TextView tv = view.findViewById(R.id.title);
        View badgeContainer = view.findViewById(R.id.badgeContainer);
        ImageView badge = view.findViewById(R.id.badge);

        if (icon != null) {
            icon.setImageResource(item.iconRes);
            if ("NONE".equals(item.tintColor)) {
                icon.setImageTintList(null);
            } else {
                icon.setImageTintList(androidx.core.content.ContextCompat.getColorStateList(getContext(), R.color.icon_tint_primary));
            }
        }
        if (tv != null) {
            tv.setText(item.getOtherGridTitle(getContext()));
        }
        if (badgeContainer != null) {
            badgeContainer.setVisibility(item.showBadge ? View.VISIBLE : View.GONE);
        } else if (badge != null) {
            badge.setVisibility(item.showBadge ? View.VISIBLE : View.GONE);
        }

        view.setOnClickListener(v -> {
            if (item.targetActivity != null) {
                startActivity(new Intent(getContext(), item.targetActivity));
            } else if (PersonaliseSpaceManager.ID_GOV_FEES.equals(item.id)) {
                openCategorySubmenu("Government Payments");
            }
        });

        return view;
    }

    private int dpToPx(int dp) {
        if (getContext() == null) return dp * 2;
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void openCategorySubmenu(String category) {
        if (getContext() == null) return;
        Intent intent = new Intent(getContext(), TileSubmenuActivity.class);
        intent.putExtra("CATEGORY_TITLE", category);
        intent.putExtra("ACTIVE_NAV_ID", R.id.navigation_other);
        startActivity(intent);
    }
}