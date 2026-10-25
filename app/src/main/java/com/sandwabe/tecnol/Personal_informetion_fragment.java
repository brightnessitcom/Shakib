package com.sandwabe.tecnol;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class Personal_informetion_fragment extends Fragment {
    private User currentUser;
    private String currentUsername;
    private View root;

    private void loadUserData() {
        if (!isAdded() || root == null) return;
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(requireContext());
        }

        User cached = ApiClient.getCachedUser(getContext());
        if (cached != null) {
            currentUser = cached;
            if (currentUsername == null || currentUsername.isEmpty()) {
                currentUsername = currentUser.getUsername();
            }
            updateUserUI(root, currentUser);
        }

        Context ctx = getContext();
        if (ctx != null && currentUsername != null && !currentUsername.isEmpty()) {
            ApiClient.fetchUser(ctx, currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (!isAdded() || root == null) return;
                    currentUser = user;
                    updateUserUI(root, currentUser);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w("Personal_informetion", "ApiClient.fetchUser error: " + errorMessage);
                }
            });
        }
    }

    public void handleCopyText(TextView textView, View view) {
        if (textView == null) return;
        String text = textView.getText().toString();
        if (text.isEmpty() || Objects.equals(text, "-") || Objects.equals(text, "Not Available")) {
            Toast.makeText(getContext(), "No data to copy", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

    public void handleBackPress(View view) {
        if (isAdded()) {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateStatusBarForTab(R.id.navigation_home);
            }
            getParentFragmentManager().popBackStack();
        }
    }

    private void loadProfileImage(String str, ShapeableImageView shapeableImageView) {
        if (shapeableImageView == null) return;
        shapeableImageView.setVisibility(View.VISIBLE);
        if (str == null || str.isEmpty() || Objects.equals(str, "null")) {
            shapeableImageView.setImageResource(R.drawable.absher);
            return;
        }
        
        Glide.with(this)
                .load(str.trim())
                .override(400, 400)
                .placeholder(R.drawable.absher)
                .error(R.drawable.absher)
                .into(shapeableImageView);
    }

    private static final int[] CARD_IDS = new int[]{
        R.id.card_personal,
        R.id.card_sponsor,
        R.id.card_health,
        R.id.card_hajj
    };

    private void setupCard(View view, int includeId, boolean defaultExpanded, String title) {
        View cardView = view.findViewById(includeId);
        if (cardView == null) return;
        
        final LinearLayout contentLayout = cardView.findViewById(R.id.card_content);
        final ImageView arrowIcon = cardView.findViewById(R.id.arrow_icon);
        View header = cardView.findViewById(R.id.header_layout);
        TextView titleTv = cardView.findViewById(R.id.card_title);
        
        if (titleTv != null) titleTv.setText(title);

        if (contentLayout != null) {
            contentLayout.setVisibility(defaultExpanded ? View.VISIBLE : View.GONE);
        }
        if (arrowIcon != null) {
            arrowIcon.setRotation(defaultExpanded ? 180.0f : 0.0f);
        }

        if (header != null) {
            header.setOnClickListener(v -> toggleCard(view, includeId));
        }
    }

    private void toggleCard(View rootView, int targetIncludeId) {
        if (rootView == null) return;
        View targetCard = rootView.findViewById(targetIncludeId);
        if (targetCard == null) return;

        LinearLayout targetContent = targetCard.findViewById(R.id.card_content);
        ImageView targetArrow = targetCard.findViewById(R.id.arrow_icon);
        if (targetContent == null || targetArrow == null) return;

        boolean isCurrentlyExpanded = targetContent.getVisibility() == View.VISIBLE;

        // Collapse all other cards
        for (int cardId : CARD_IDS) {
            if (cardId != targetIncludeId) {
                View otherCard = rootView.findViewById(cardId);
                if (otherCard != null) {
                    LinearLayout otherContent = otherCard.findViewById(R.id.card_content);
                    ImageView otherArrow = otherCard.findViewById(R.id.arrow_icon);
                    if (otherContent != null && otherContent.getVisibility() == View.VISIBLE) {
                        otherContent.setVisibility(View.GONE);
                    }
                    if (otherArrow != null) {
                        otherArrow.setRotation(0.0f);
                    }
                }
            }
        }

        // Toggle target card
        if (isCurrentlyExpanded) {
            targetContent.setVisibility(View.GONE);
            targetArrow.setRotation(0.0f);
        } else {
            targetContent.setVisibility(View.VISIBLE);
            targetArrow.setRotation(180.0f);
        }
    }

    private String formatDate(String str) {
        return DateUtils.formatDisplayDate(requireContext(), str);
    }

    private void updateUserUI(View view, User user) {
        if (user == null) return;

        View cardPersonal = view.findViewById(R.id.card_personal);
        if (cardPersonal != null) {
            ShapeableImageView icon = cardPersonal.findViewById(R.id.card_icon);
            loadProfileImage(user.getProfile_image_url(), icon);
            if (icon != null) {
                icon.setClickable(true);
                icon.setFocusable(true);
                icon.setOnClickListener(v -> {
                    String imgUrl = currentUser != null ? currentUser.getProfile_image_url() : null;
                    Context ctx = getContext();
                    if (ctx != null) {
                        Intent intent = new Intent(ctx, PreviewPhotoActivity.class);
                        if (imgUrl != null) intent.putExtra("IMAGE_URL", imgUrl);
                        startActivity(intent);
                    }
                });
            }
            
            setTextOrDash(cardPersonal.findViewById(R.id.nameuser), user.getIqama_holder_name());
            setTextOrDash(cardPersonal.findViewById(R.id.birthCity), "-");
            setTextOrDash(cardPersonal.findViewById(R.id.birthCountry), user.getBirth_country());
            setTextOrDash(cardPersonal.findViewById(R.id.dateOfBirth), formatDate(user.getDateofbirth()));
            setTextOrDash(cardPersonal.findViewById(R.id.maritalStatus), localizeValue(user.getMarital_status()));
            setTextOrDash(cardPersonal.findViewById(R.id.sponsorshipTransfers), user.getSponsorship_transfers());
            setTextOrDash(cardPersonal.findViewById(R.id.religion), localizeValue(user.getReligion()));
            setTextOrDash(cardPersonal.findViewById(R.id.workPermit), "-");
            setTextOrDash(cardPersonal.findViewById(R.id.biometricsCollected), localizeValue(user.getBiometrics_collected()));
            setTextOrDash(cardPersonal.findViewById(R.id.travelStatus), localizeValue(user.getTravel_status()));
        }

        View cardSponsor = view.findViewById(R.id.card_sponsor);
        if (cardSponsor != null) {
            setTextOrDash(cardSponsor.findViewById(R.id.sponsorName), user.getSponsor_name());
            TextView sponsorIdTv = cardSponsor.findViewById(R.id.sponsortextv);
            if (sponsorIdTv != null) {
                sponsorIdTv.setTextDirection(View.TEXT_DIRECTION_LTR);
            }
            setTextOrDash(sponsorIdTv, User.normalizeDigits(user.getSponsor_id()));
            
            View copyBtn = cardSponsor.findViewById(R.id.sponsorcopyimgview);
            if (copyBtn != null) {
                copyBtn.setOnClickListener(v -> handleCopyText(sponsorIdTv, v));
            }
        }

        View cardHealth = view.findViewById(R.id.card_health);
        if (cardHealth != null) {
            setTextOrDash(cardHealth.findViewById(R.id.healthIssueDate), formatDate(user.getH_issue_date()));
            setTextOrDash(cardHealth.findViewById(R.id.healthExpiryDate), formatDate(user.getH_exp_date()));
            setTextOrDash(cardHealth.findViewById(R.id.bloodGroup), "-");
        }

        View cardHajj = view.findViewById(R.id.card_hajj);
        if (cardHajj != null) {
            setTextOrDash(cardHajj.findViewById(R.id.hajjStatus), localizeValue(user.getHajj_status()));
            TextView tvLastHajj = cardHajj.findViewById(R.id.lastHajjYear);
            if (tvLastHajj != null) {
                tvLastHajj.setVisibility(View.GONE);
            }
        }
    }

    private String localizeValue(String value) {
        if (value == null || value.isEmpty()) return value;
        String lower = value.toLowerCase();
        if (lower.contains("inside")) return getString(R.string.value_travel_status);
        if (lower.equals("yes")) return getString(R.string.yes);
        if (lower.equals("no")) return getString(R.string.status_unknown_placeholder);
        if (lower.equals("single")) return getString(R.string.value_marital_status);
        if (lower.equals("married")) return "MARRIED";
        if (lower.equals("islam")) return getString(R.string.value_religion);
        if (lower.contains("eligible")) return getString(R.string.Eligible_for_Hajj);
        return value;
    }

    private void setTextOrDash(TextView textView, String value) {
        if (textView == null) return;
        if (value == null || value.trim().isEmpty() || value.equals("null")) {
            textView.setText("-");
        } else {
            textView.setText(value);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            currentUser = (User) bundle.getSerializable("SAVED_USER");
            currentUsername = bundle.getString("SAVED_USERNAME");
        }
        if (getArguments() != null) {
            if (currentUser == null) currentUser = (User) getArguments().getSerializable("USER");
            if (currentUsername == null) currentUsername = getArguments().getString("USERNAME");
        }
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(requireContext());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentUser != null) outState.putSerializable("SAVED_USER", currentUser);
        if (currentUsername != null) outState.putString("SAVED_USERNAME", currentUsername);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        root = inflater.inflate(R.layout.personal_informetion_fragment, container, false);
        
        applyTheme(root);

        View backArrow = root.findViewById(R.id.backarrow);
        if (backArrow != null) {
            backArrow.setOnClickListener(this::handleBackPress);
        }
        
        setupCard(root, R.id.card_personal, true, getString(R.string.personal_details));
        setupCard(root, R.id.card_sponsor, false, getString(R.string.sponsor_details));
        setupCard(root, R.id.card_health, false, getString(R.string.health_insurance));
        setupCard(root, R.id.card_hajj, false, getString(R.string.hajj_details));
        
        loadUserData();
        
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        applyTheme(root);
        loadUserData();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).setSwipeRefreshEnabled(false);
            ((MainActivity) requireActivity()).setFloatingImageVisible(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateStatusBarForTab(R.id.navigation_home);
        }
    }

    private void applyTheme(View view) {
        if (view == null || getContext() == null) return;
        boolean isDark = ThemeHelper.isDarkMode(getContext());

        int bgColor = isDark ? Color.parseColor("#161719") : Color.parseColor("#F4F6F9");
        int cardBgColor = isDark ? Color.parseColor("#282B29") : Color.parseColor("#FFFFFF");
        int primaryTextColor = isDark ? Color.parseColor("#FFFFFF") : Color.parseColor("#1B1E21");
        int secondaryTextColor = isDark ? Color.parseColor("#B0B5B3") : Color.parseColor("#6C757D");
        int accentColor = isDark ? Color.parseColor("#72D09D") : Color.parseColor("#00663D");
        int separatorColor = isDark ? Color.parseColor("#383C3A") : Color.parseColor("#EAEFEA");
        int strokeColor = isDark ? Color.TRANSPARENT : Color.parseColor("#EAEFEA");
        int strokeWidth = isDark ? 0 : (int) (1 * getResources().getDisplayMetrics().density);

        view.setBackgroundColor(bgColor);

        View topBar = view.findViewById(R.id.topBar);
        if (topBar != null) topBar.setBackgroundColor(bgColor);

        ImageView backArrow = view.findViewById(R.id.backarrow);
        if (backArrow != null) backArrow.setColorFilter(accentColor);

        TextView toolbarTitle = view.findViewById(R.id.toolbarTitle);
        if (toolbarTitle != null) toolbarTitle.setTextColor(primaryTextColor);

        int[] cardIds = new int[]{R.id.card_personal, R.id.card_sponsor, R.id.card_health, R.id.card_hajj};
        for (int cardId : cardIds) {
            View card = view.findViewById(cardId);
            if (card != null) {
                styleCardView(card, cardBgColor, primaryTextColor, secondaryTextColor, accentColor, separatorColor, strokeColor, strokeWidth);
            }
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setCustomStatusBarColor(bgColor);
        }
    }

    private void styleCardView(View cardContainer, int cardBgColor, int primaryTextColor, int secondaryTextColor, int accentColor, int separatorColor, int strokeColor, int strokeWidth) {
        if (cardContainer instanceof MaterialCardView) {
            MaterialCardView mcv = (MaterialCardView) cardContainer;
            mcv.setCardBackgroundColor(cardBgColor);
            mcv.setStrokeColor(strokeColor);
            mcv.setStrokeWidth(strokeWidth);
        }
        styleViewsRecursively(cardContainer, cardBgColor, primaryTextColor, secondaryTextColor, accentColor, separatorColor);
    }

    private void styleViewsRecursively(View view, int cardBgColor, int primaryTextColor, int secondaryTextColor, int accentColor, int separatorColor) {
        if (view == null) return;
        if (view.getId() == R.id.topBar || view.getId() == R.id.backarrow || view.getId() == R.id.toolbarTitle) {
            return;
        }

        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            if (!(view instanceof MaterialCardView)) {
                if (view.getId() == R.id.header_layout || view.getId() == R.id.card_content || view.getId() == View.NO_ID) {
                    view.setBackgroundColor(cardBgColor);
                }
            }
            for (int i = 0; i < vg.getChildCount(); i++) {
                styleViewsRecursively(vg.getChildAt(i), cardBgColor, primaryTextColor, secondaryTextColor, accentColor, separatorColor);
            }
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if (tv.getId() == R.id.card_title) {
                tv.setTextColor(primaryTextColor);
            } else if (tv.getText() != null && tv.getText().toString().equals("-")) {
                tv.setTextColor(secondaryTextColor);
            } else if (tv.getTypeface() != null && tv.getTypeface().isBold()) {
                tv.setTextColor(primaryTextColor);
            } else {
                tv.setTextColor(secondaryTextColor);
            }
        } else if (view instanceof ImageView) {
            ImageView iv = (ImageView) view;
            if (iv.getId() == R.id.arrow_icon) {
                iv.setColorFilter(secondaryTextColor);
            } else if (iv.getId() == R.id.sponsorcopyimgview) {
                iv.setColorFilter(accentColor);
            } else if (iv.getId() == R.id.card_icon && !(iv instanceof ShapeableImageView)) {
                iv.setColorFilter(secondaryTextColor);
            }
        } else if (view.getId() == R.id.separator_line) {
            view.setBackgroundColor(separatorColor);
        }
    }

}
