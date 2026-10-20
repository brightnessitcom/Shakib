package com.sandwabe.tecnol;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class ProfileFragment extends Fragment {
    private User currentUser;
    private String currentUsername;
    private TextView iqamaHolderName;
    private TextView iqamaNumberText;
    private ShapeableImageView profileImageView;
    private TextView toolbarTitle;
    private TextView tvTravelStatusBadge;

    public static class ProfileClickListener implements View.OnClickListener {
        private final ProfileFragment fragment;
        private final int actionId;

        public ProfileClickListener(ProfileFragment fragment, int actionId) {
            this.fragment = fragment;
            this.actionId = actionId;
        }

        @Override
        public void onClick(View v) {
            switch (actionId) {
                case 0: fragment.handleBackPress(v); break;
                case 1: fragment.handlePersonalInformation(v); break;
                case 2: fragment.handlePassportDetails(v); break;
                case 3: fragment.handleResidentDetails(v); break;
                case 4: fragment.handleDrivingLicense(v); break;
                case 5: fragment.handleVisa(v); break;
                case 6: fragment.handleLaborClick(v); break;
                case 7: fragment.handleCopyIqamaNumber(v); break;
                case 8: fragment.handleTravelRecordsClick(v); break;
            }
        }
    }

    public void loadUserData() {
        if (!isAdded()) return;
        Context ctx = getContext();
        if (ctx == null) return;

        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(ctx);
        }

        User cached = ApiClient.getCachedUser(ctx);
        if (cached != null) {
            currentUser = cached;
            if (currentUsername == null || currentUsername.isEmpty()) {
                currentUsername = currentUser.getUsername();
            }
            updateUserUI(currentUser);
        }

        if (currentUsername != null && !currentUsername.isEmpty()) {
            ApiClient.fetchUser(ctx, currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (!isAdded()) return;
                    currentUser = user;
                    requireActivity().runOnUiThread(() -> updateUserUI(currentUser));
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w("ProfileFragment", "ApiClient error: " + errorMessage);
                }
            });
        }
    }

    public void handleBackPress(View view) {
        if (isAdded()) {
            getParentFragmentManager().popBackStack();
        }
    }

    public void handlePersonalInformation(View view) {
        navigateToFragment(new Personal_informetion_fragment());
    }

    public void handlePassportDetails(View view) {
        navigateToFragment(new Passport_details_freg());
    }

    public void handleResidentDetails(View view) {
        navigateToFragment(new Resident_detals_freg());
    }

    private void navigateToFragment(Fragment fragment) {
        if (!isAdded()) return;
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", currentUser);
        bundle.putString("USERNAME", currentUsername);
        fragment.setArguments(bundle);
        
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void handleDrivingLicense(View view) {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), SimpleDetailsActivity.class);
        intent.putExtra("TITLE", getString(R.string.My_Driving_License));
        intent.putExtra("ICON", R.drawable.ic_residdent_id);
        intent.putExtra("HEADING", getString(R.string.no_license));
        intent.putExtra("DESCRIPTION", getString(R.string.no_license_description));
        startActivity(intent);
    }

    public void handleVisa(View view) {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), SimpleDetailsActivity.class);
        intent.putExtra("TITLE", getString(R.string.My_Visa));
        intent.putExtra("ICON", R.drawable.ic_visa);
        intent.putExtra("HEADING", getString(R.string.no_visa));
        intent.putExtra("DESCRIPTION", getString(R.string.no_visa_description));
        startActivity(intent);
    }

    public void handleLaborClick(View view) {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), SimpleDetailsActivity.class);
        intent.putExtra("TITLE", getString(R.string.labour_importations_title));
        intent.putExtra("ICON", R.drawable.ic_worker_outline);
        intent.putExtra("HEADING", getString(R.string.no_labor_importations_heading));
        intent.putExtra("DESCRIPTION", getString(R.string.no_labor_importations_description));
        startActivity(intent);
    }

    public void handleCopyIqamaNumber(View view) {
        if (iqamaNumberText != null) {
            String text = iqamaNumberText.getText().toString()
                    .replace("ID No. ", "")
                    .replace("رقم الهوية ", "")
                    .trim();
            ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                ClipData clip = ClipData.newPlainText("Iqama Number", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Iqama Number Copied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void handleTravelRecordsClick(View view) {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), TravelRequestsActivity.class);
        startActivity(intent);
    }

    public void handleToolbarScroll(ScrollView scrollView, CardView cardView, Toolbar toolbar) {
        if (!isAdded() || toolbarTitle == null) return;
        int[] iArr = new int[2];
        cardView.getLocationOnScreen(iArr);
        int i5 = iArr[1];
        int height = cardView.getHeight();
        int[] iArr2 = new int[2];
        toolbar.getLocationOnScreen(iArr2);
        int height2 = toolbar.getHeight() + iArr2[1];
        float f4 = height * 0.2f;
        float fMax = 1.0f;
        if (i5 >= height2 - f4) {
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText(R.string.my_profile_title);
        } else {
            if (i5 + height <= height2) {
                toolbarTitle.setVisibility(View.VISIBLE);
                toolbarTitle.setText(R.string.my_profile_title);
                fMax = 0.0f;
            } else {
                fMax = Math.max(0.0f, Math.min(1.0f, (float)(i5 - height2) / f4));
                toolbarTitle.setVisibility(View.INVISIBLE);
            }
        }
        cardView.setAlpha(fMax);
    }

    private void loadProfileImage(String str) {
        if (str == null || str.isEmpty() || Objects.equals(str, "null")) {
            if (profileImageView != null) profileImageView.setImageResource(R.drawable.absher);
            return;
        }
        try {
            Context ctx = requireContext();
            String url = str;
            if (!str.startsWith("http")) {
                try {
                    url = CustomCipher.decrypt(str, CustomCipher.getSecret(ctx), ctx);
                } catch (Exception e) {
                    android.util.Log.e("ProfileFragment", "Decryption failed");
                }
            }
            
            if (url != null && !url.isEmpty() && !Objects.equals(url, "null")) {
                Glide.with(this).load(url).placeholder(R.drawable.placeholder).error(R.drawable.absher).into(profileImageView);
            } else {
                if (profileImageView != null) profileImageView.setImageResource(R.drawable.absher);
            }
        } catch (Exception e) {
            if (profileImageView != null) profileImageView.setImageResource(R.drawable.absher);
        }
    }

    private void showDefaultData() {
        if (iqamaHolderName != null) iqamaHolderName.setText(getString(R.string.status_unknown_placeholder));
        if (iqamaNumberText != null) iqamaNumberText.setText(getString(R.string.status_unknown_placeholder));
        if (profileImageView != null) profileImageView.setImageResource(R.drawable.absher);
    }

    private void updateUserUI(User user) {
        String name = user.getIqama_holder_name();
        if (iqamaHolderName != null) iqamaHolderName.setText(name != null && !name.isEmpty() ? name : user.getUsername());
        if (iqamaNumberText != null) {
            String rawId = user.getIqama_number_withid();
            if (rawId == null || rawId.isEmpty()) rawId = user.getUsername();
            if (LanguageHelper.isArabic(requireContext())) {
                iqamaNumberText.setText("رقم الهوية " + (rawId != null ? rawId : ""));
            } else {
                iqamaNumberText.setText("ID No. " + (rawId != null ? rawId : ""));
            }
        }
        if (tvTravelStatusBadge != null) {
            String status = user.getTravel_status();
            if (status != null && !status.isEmpty() && !status.equals("null")) {
                if (LanguageHelper.isArabic(requireContext())) {
                    if (status.toLowerCase().contains("inside")) {
                        tvTravelStatusBadge.setText("داخل المملكة");
                    } else if (status.toLowerCase().contains("outside")) {
                        tvTravelStatusBadge.setText("خارج المملكة");
                    } else {
                        tvTravelStatusBadge.setText(status);
                    }
                } else {
                    tvTravelStatusBadge.setText(status);
                }
            }
        }
        loadProfileImage(user.getProfile_image_url());
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            currentUser = (User) getArguments().getSerializable("USER");
            currentUsername = getArguments().getString("USERNAME");
        }
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(requireContext());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImageView = view.findViewById(R.id.profileImage);
        if (profileImageView != null) {
            profileImageView.setClickable(true);
            profileImageView.setFocusable(true);
            profileImageView.setOnClickListener(v -> {
                String imgUrl = currentUser != null ? currentUser.getProfile_image_url() : null;
                Context ctx = getContext();
                if (ctx != null) {
                    Intent intent = new Intent(ctx, PreviewPhotoActivity.class);
                    if (imgUrl != null) intent.putExtra("IMAGE_URL", imgUrl);
                    startActivity(intent);
                }
            });
        }
        iqamaHolderName = view.findViewById(R.id.iqamaHolderName);
        iqamaNumberText = view.findViewById(R.id.iqamaNumberText);
        toolbarTitle = view.findViewById(R.id.toolbarTitle);
        tvTravelStatusBadge = view.findViewById(R.id.tvTravelStatusBadge);

        loadUserData();

        view.findViewById(R.id.backarrowprofile).setOnClickListener(new ProfileClickListener(this, 0));
        view.findViewById(R.id.detailsLabel).setOnClickListener(new ProfileClickListener(this, 1));
        
        View passportCard = view.findViewById(R.id.cardpassport_inv);
        if (passportCard != null) passportCard.setOnClickListener(new ProfileClickListener(this, 2));
        
        View residentCard = view.findViewById(R.id.cardrrsident_inv);
        if (residentCard != null) residentCard.setOnClickListener(new ProfileClickListener(this, 3));
        
        View licenseSection = view.findViewById(R.id.license_section);
        if (licenseSection != null) licenseSection.setOnClickListener(new ProfileClickListener(this, 4));
        
        View visaSection = view.findViewById(R.id.visa_section);
        if (visaSection != null) visaSection.setOnClickListener(new ProfileClickListener(this, 5));
        
        View laborLayout = view.findViewById(R.id.lebor_click_layout);
        if (laborLayout != null) laborLayout.setOnClickListener(new ProfileClickListener(this, 6));
        
        View copyIcon = view.findViewById(R.id.copyimgiconresedent);
        if (copyIcon != null) copyIcon.setOnClickListener(new ProfileClickListener(this, 7));
        
        View travelRecordsCard = view.findViewById(R.id.card_travel_records);
        if (travelRecordsCard != null) travelRecordsCard.setOnClickListener(new ProfileClickListener(this, 8));

        final ScrollView scrollView = view.findViewById(R.id.scrollView);
        final Toolbar toolbar = view.findViewById(R.id.topBar);
        final CardView mainCard = view.findViewById(R.id.mainCard);
        
        if (scrollView != null && mainCard != null && toolbar != null) {
            scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> 
                handleToolbarScroll(scrollView, mainCard, toolbar)
            );
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
        if (requireActivity() instanceof MainActivity) {
            ((MainActivity) requireActivity()).setSwipeRefreshEnabled(false);
            ((MainActivity) requireActivity()).setFloatingImageVisible(false);
        }
    }
}
