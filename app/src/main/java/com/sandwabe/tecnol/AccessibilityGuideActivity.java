package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class AccessibilityGuideActivity extends AppCompatActivity {

    private static class AccItem {
        final int iconRes;
        final String title;
        final String subtitle;
        final String commonPath;
        final String cantFindIt;
        final String[] steps;
        final String footnote;

        AccItem(int iconRes, String title, String subtitle, String commonPath, String cantFindIt, String[] steps, String footnote) {
            this.iconRes = iconRes;
            this.title = title;
            this.subtitle = subtitle;
            this.commonPath = commonPath;
            this.cantFindIt = cantFindIt;
            this.steps = steps;
            this.footnote = footnote;
        }
    }

    private static final AccItem[] ACC_ITEMS = new AccItem[]{
            new AccItem(
                    R.drawable.ic_acc_palette,
                    "Color correction",
                    "Changes how colors appear on screen. Helpful if reds and greens look too similar to tell apart.",
                    "Settings  →  Accessibility  →  Vision enhancements  →  Color correction",
                    "Search \"color correction\" in Settings.",
                    new String[]{
                            "1. Tap Color correction and turn it on.",
                            "2. Choose a correction mode that best matches your needs.",
                            "3. Check the color wheel shown to confirm it helps."
                    },
                    "This changes colors across your whole device, not just this app. Also try Color inversion nearby if bright backgrounds feel uncomfortable."
            ),
            new AccItem(
                    R.drawable.ic_acc_contrast,
                    "High contrast text & bold text",
                    "Makes text sharper and easier to read by adding stronger outlines and thicker letterforms.",
                    "Settings  →  Accessibility  →  Vision enhancements  →  Bold text / High contrast font",
                    "Search bold font in Settings. On older\nAndroid: Settings  →  Display  →  Font settings.",
                    new String[]{
                            "1. Turn on Bold text for thicker, easier-to-read type.",
                            "2. Turn on High contrast text to add stronger outlines to letters.",
                            "3. Use Font size on the same screen to make text larger across all apps."
                    },
                    "Changes apply immediately across all apps, no restart needed. High contrast text works best in light mode. Try both to see which suits you."
            ),
            new AccItem(
                    R.drawable.ic_acc_magnify,
                    "Magnification",
                    "Lets you zoom into any part of the screen. Useful if text or images are too small to see clearly.",
                    "Settings  →  Accessibility  →  Vision enhancements  →  Magnification",
                    "Search \"magnification\" in Settings.",
                    new String[]{
                            "1. Tap Magnification and choose a trigger: triple-tap or the shortcut button.",
                            "2. Triple-tap the screen (or tap the shortcut) to zoom in.",
                            "3. Pinch with two fingers to adjust zoom level. Triple-tap again to zoom out."
                    },
                    "A shortcut button appears as a small floating icon. Drag it to a convenient corner. Use two fingers to pan around the screen while zoomed in."
            ),
            new AccItem(
                    R.drawable.ic_acc_talkback,
                    "TalkBack",
                    "Reads everything on screen aloud. Helpful if you prefer to listen rather than read, or if text is hard to see.",
                    "Settings  →  Accessibility  →  TalkBack",
                    "Search \"TalkBack\" in Settings. Some devices\nlist it as \"Screen reader\" or under Accessibility\n→  Downloaded apps.",
                    new String[]{
                            "1. Tap TalkBack and turn the switch on. Your phone will speak a confirmation.",
                            "2. Single-tap any item to hear it; double-tap to open or activate it.",
                            "3. Swipe right to move to the next item; swipe left to go back."
                    },
                    "Hold both volume keys for 3 seconds to toggle TalkBack on and off quickly. Open the TalkBack tutorial (shown when you first enable it) to practise gestures safely."
            )
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_accessibility_guide);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        View tvOpenSettings = findViewById(R.id.tvOpenAccessibilitySettings);
        if (tvOpenSettings != null) {
            tvOpenSettings.setOnClickListener(v -> {
                try {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                } catch (Exception ignored) {
                }
            });
        }

        LinearLayout container = findViewById(R.id.llAccessibilityCardsContainer);
        if (container != null) {
            populateCards(container);
        }
    }

    private void populateCards(LinearLayout container) {
        for (AccItem item : ACC_ITEMS) {
            MaterialCardView card = new MaterialCardView(this);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.bottomMargin = dp(14);
            card.setLayoutParams(cardParams);
            card.setCardBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.card_background));
            card.setRadius(dp(12));
            card.setCardElevation(0f);
            card.setStrokeWidth(dp(1));
            card.setStrokeColor(androidx.core.content.ContextCompat.getColor(this, R.color.divider_color));

            LinearLayout cardInner = new LinearLayout(this);
            cardInner.setOrientation(LinearLayout.VERTICAL);

            // Header Row
            LinearLayout headerRow = new LinearLayout(this);
            headerRow.setOrientation(LinearLayout.HORIZONTAL);
            headerRow.setPadding(dp(16), dp(18), dp(16), dp(18));
            headerRow.setGravity(Gravity.TOP);
            TypedValue outValue = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            headerRow.setBackgroundResource(outValue.resourceId);
            headerRow.setClickable(true);
            headerRow.setFocusable(true);

            ImageView ivIcon = new ImageView(this);
            LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(dp(34), dp(34));
            iconParams.topMargin = dp(2);
            ivIcon.setLayoutParams(iconParams);
            ivIcon.setImageResource(item.iconRes);
            headerRow.addView(ivIcon);

            LinearLayout textCol = new LinearLayout(this);
            LinearLayout.LayoutParams textColParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );
            textColParams.setMarginStart(dp(16));
            textColParams.setMarginEnd(dp(12));
            textCol.setLayoutParams(textColParams);
            textCol.setOrientation(LinearLayout.VERTICAL);

            TextView tvTitle = new TextView(this);
            tvTitle.setText(item.title);
            tvTitle.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_primary));
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f);
            tvTitle.setTypeface(null, Typeface.BOLD);
            textCol.addView(tvTitle);

            TextView tvSubtitle = new TextView(this);
            LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            subParams.topMargin = dp(6);
            tvSubtitle.setLayoutParams(subParams);
            tvSubtitle.setText(item.subtitle);
            tvSubtitle.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
            tvSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f);
            tvSubtitle.setLineSpacing(dp(2), 1f);
            textCol.addView(tvSubtitle);

            headerRow.addView(textCol);

            ImageView ivChevron = new ImageView(this);
            LinearLayout.LayoutParams chevParams = new LinearLayout.LayoutParams(dp(20), dp(20));
            chevParams.topMargin = dp(6);
            ivChevron.setLayoutParams(chevParams);
            ivChevron.setImageResource(R.drawable.ic_acc_chevron_down);
            ivChevron.setColorFilter(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
            headerRow.addView(ivChevron);

            cardInner.addView(headerRow);

            // Expanded Body Container
            LinearLayout expandedBody = new LinearLayout(this);
            expandedBody.setOrientation(LinearLayout.VERTICAL);
            expandedBody.setVisibility(View.GONE);

            View divider = new View(this);
            divider.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(1)
            ));
            divider.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.divider_color));
            expandedBody.addView(divider);

            LinearLayout bodyContent = new LinearLayout(this);
            bodyContent.setOrientation(LinearLayout.VERTICAL);
            bodyContent.setPadding(dp(16), dp(16), dp(16), dp(18));

            // COMMON PATH
            TextView tvCommonPathLabel = new TextView(this);
            tvCommonPathLabel.setText("COMMON PATH");
            tvCommonPathLabel.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.icon_tint_primary));
            tvCommonPathLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.5f);
            tvCommonPathLabel.setTypeface(null, Typeface.BOLD);
            bodyContent.addView(tvCommonPathLabel);

            TextView tvCommonPathVal = new TextView(this);
            LinearLayout.LayoutParams cpValParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cpValParams.topMargin = dp(6);
            tvCommonPathVal.setLayoutParams(cpValParams);
            tvCommonPathVal.setText(item.commonPath);
            tvCommonPathVal.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
            tvCommonPathVal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.5f);
            tvCommonPathVal.setLineSpacing(dp(2), 1f);
            bodyContent.addView(tvCommonPathVal);

            // CAN'T FIND IT?
            TextView tvCantFindLabel = new TextView(this);
            LinearLayout.LayoutParams cfLabelParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cfLabelParams.topMargin = dp(18);
            tvCantFindLabel.setLayoutParams(cfLabelParams);
            tvCantFindLabel.setText("CAN'T FIND IT?");
            tvCantFindLabel.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.icon_tint_primary));
            tvCantFindLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11.5f);
            tvCantFindLabel.setTypeface(null, Typeface.BOLD);
            bodyContent.addView(tvCantFindLabel);

            TextView tvCantFindVal = new TextView(this);
            LinearLayout.LayoutParams cfValParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cfValParams.topMargin = dp(6);
            tvCantFindVal.setLayoutParams(cfValParams);
            tvCantFindVal.setText(item.cantFindIt);
            tvCantFindVal.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
            tvCantFindVal.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.5f);
            tvCantFindVal.setLineSpacing(dp(2), 1f);
            bodyContent.addView(tvCantFindVal);

            // Green Steps Box
            LinearLayout stepsBox = new LinearLayout(this);
            LinearLayout.LayoutParams stepsBoxParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            stepsBoxParams.topMargin = dp(16);
            stepsBox.setLayoutParams(stepsBoxParams);
            stepsBox.setOrientation(LinearLayout.VERTICAL);
            stepsBox.setBackgroundResource(R.drawable.bg_acc_green_steps);
            stepsBox.setPadding(dp(16), dp(14), dp(16), dp(14));

            for (int i = 0; i < item.steps.length; i++) {
                TextView tvStep = new TextView(this);
                LinearLayout.LayoutParams stepParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                if (i > 0) stepParams.topMargin = dp(10);
                tvStep.setLayoutParams(stepParams);
                tvStep.setText(item.steps[i]);
                tvStep.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_primary));
                tvStep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.5f);
                tvStep.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
                tvStep.setLineSpacing(dp(2), 1f);
                stepsBox.addView(tvStep);
            }
            bodyContent.addView(stepsBox);

            // Footnote Row
            LinearLayout footnoteRow = new LinearLayout(this);
            LinearLayout.LayoutParams fnParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            fnParams.topMargin = dp(14);
            footnoteRow.setLayoutParams(fnParams);
            footnoteRow.setOrientation(LinearLayout.HORIZONTAL);
            footnoteRow.setGravity(Gravity.TOP);

            ImageView ivInfoSmall = new ImageView(this);
            LinearLayout.LayoutParams infoSmallParams = new LinearLayout.LayoutParams(dp(18), dp(18));
            infoSmallParams.topMargin = dp(2);
            ivInfoSmall.setLayoutParams(infoSmallParams);
            ivInfoSmall.setImageResource(R.drawable.ic_acc_info_small);
            footnoteRow.addView(ivInfoSmall);

            TextView tvFootnote = new TextView(this);
            LinearLayout.LayoutParams fnTextParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            );
            fnTextParams.setMarginStart(dp(10));
            tvFootnote.setLayoutParams(fnTextParams);
            tvFootnote.setText(item.footnote);
            tvFootnote.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_secondary));
            tvFootnote.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
            tvFootnote.setLineSpacing(dp(2), 1f);
            footnoteRow.addView(tvFootnote);

            bodyContent.addView(footnoteRow);
            expandedBody.addView(bodyContent);
            cardInner.addView(expandedBody);
            card.addView(cardInner);

            headerRow.setOnClickListener(v -> {
                boolean isExpanded = expandedBody.getVisibility() == View.VISIBLE;
                expandedBody.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
                ivChevron.setRotation(isExpanded ? 0f : 180f);
            });

            container.addView(card);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
