package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;

public class HomeFragment extends Fragment {
    private User currentUser;
    private String currentUsername;
    private ImageView idImageView;
    private LottieAnimationView idLoadingAnimation;
    private FrameLayout lottieOverlay;
    private View llIdError;
    private ShapeableImageView profileImageView;
    private TextView tvProfileId, tvProfileName, tvProfileNameArabic, tvSeeAll;

    public void loadUserData() {
        if (!isAdded()) return;
        Context ctx = getContext();
        if (ctx == null) return;

        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(ctx);
        }

        // 1. Immediately apply cached user if available (from memory or persistent storage)
        User cached = ApiClient.getCachedUser(ctx);
        if (cached != null) {
            currentUser = cached;
            if (currentUsername == null || currentUsername.isEmpty()) {
                currentUsername = currentUser.getUsername();
            }
            updateUserUI(currentUser);
        }

        // 2. Fetch fresh user data from REST API backend
        if (currentUsername != null && !currentUsername.isEmpty()) {
            ApiClient.fetchUser(ctx, currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (!isAdded()) return;
                    currentUser = user;
                    requireActivity().runOnUiThread(() -> {
                        String block = currentUser.getBlotkerkorar_url();
                        if (block != null && (block.equalsIgnoreCase("active") || block.equalsIgnoreCase("true"))) {
                            Intent intent = new Intent(getContext(), BlockingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        updateUserUI(currentUser);
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w("HomeFragment", "ApiClient.fetchUser error: " + errorMessage);
                }
            });
        }
    }

    private void hideLoadingOverlayAndShowImage() {
        if (lottieOverlay != null) lottieOverlay.setVisibility(View.GONE);
        if (idLoadingAnimation != null) idLoadingAnimation.cancelAnimation();
        if (idImageView != null) {
            idImageView.setVisibility(View.VISIBLE);
            idImageView.setAlpha(1.0f);
        }
    }

    private void renderDynamicCard(User user) {
        if (idImageView == null || getContext() == null || user == null) return;
        if (lottieOverlay != null) lottieOverlay.setVisibility(View.VISIBLE);
        if (idLoadingAnimation != null) idLoadingAnimation.playAnimation();
        new Handler(Looper.getMainLooper()).postDelayed(this::hideLoadingOverlayAndShowImage, 2500);

        DynamicCardRenderer.renderCardAsync(getContext(), user, new DynamicCardRenderer.CardCallback() {
            @Override
            public void onCardRendered(@NonNull android.graphics.Bitmap cardBitmap) {
                if (!isAdded()) return;
                idImageView.setImageBitmap(cardBitmap);
                hideLoadingOverlayAndShowImage();
            }

            @Override
            public void onError(Exception e) {
                if (!isAdded()) return;
                loadIdImage(user.getId_image_url());
            }
        });
    }

    private void loadIdImage(String str) {
        if (idImageView == null) return;
        
        if (str == null || str.trim().isEmpty() || str.equalsIgnoreCase("null")) {
            idImageView.setImageResource(R.drawable.absher);
            hideLoadingOverlayAndShowImage();
            return;
        }
        
        if (lottieOverlay != null) lottieOverlay.setVisibility(View.VISIBLE);
        if (idLoadingAnimation != null) idLoadingAnimation.playAnimation();

        // Safety timeout to prevent loading animation from getting stuck
        new Handler(Looper.getMainLooper()).postDelayed(this::hideLoadingOverlayAndShowImage, 2500);

        try {
            Context context = getContext();
            if (context == null) return;
            
            String url = str.trim();
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.absher)
                    .error(R.drawable.absher)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object m, Target<Drawable> t, boolean f) {
                            if (isAdded()) requireActivity().runOnUiThread(() -> hideLoadingOverlayAndShowImage());
                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable r, Object m, Target<Drawable> t, DataSource d, boolean f) {
                            if (isAdded()) requireActivity().runOnUiThread(() -> hideLoadingOverlayAndShowImage());
                            return false;
                        }
                    })
                    .into(idImageView);
        } catch (Exception e) {
            hideLoadingOverlayAndShowImage();
        }
    }

    private void updateUserUI(User user) {
        if (user == null || !isAdded()) return;
        
        boolean isAr = LanguageHelper.isArabic(requireContext());
        String name = user.getIqama_holder_name();
        String arabicName = user.getIqama_holder_name_arabic();

        if (isAr) {
            tvProfileName.setText(arabicName != null && !arabicName.isEmpty() ? arabicName : (name != null ? name.toUpperCase() : ""));
            if (name != null && !name.isEmpty() && arabicName != null && !arabicName.isEmpty()) {
                tvProfileNameArabic.setVisibility(View.GONE);
                tvProfileNameArabic.setText(name.toUpperCase());
            } else {
                tvProfileNameArabic.setVisibility(View.GONE);
            }
        } else {
            tvProfileName.setText(name != null ? name.toUpperCase() : "");
            if (arabicName != null && !arabicName.isEmpty()) {
                tvProfileNameArabic.setVisibility(View.GONE);
                tvProfileNameArabic.setText(arabicName);
            } else {
                tvProfileNameArabic.setVisibility(View.GONE);
            }
        }
        
        String iqamaNum = user.getIqama_number_withid();
        if (iqamaNum != null) {
            String displayId = iqamaNum;
            if (displayId.toUpperCase().startsWith("ID:")) displayId = displayId.substring(3).trim();
            if (displayId.toUpperCase().startsWith("ID NO.:")) displayId = displayId.substring(7).trim();
            try {
                tvProfileId.setText(getString(R.string.iqama_number_withid, displayId));
            } catch (Exception e) {
                tvProfileId.setText(displayId);
            }
        }

        if (llIdError != null) {
            llIdError.setVisibility(user.isExpired() ? View.VISIBLE : View.GONE);
        }

        loadProfileImage(user.getProfile_image_url());
        renderDynamicCard(user);
    }

    private void loadProfileImage(String str) {
        if (profileImageView == null || !isAdded()) return;
        if (str == null || str.trim().isEmpty() || str.equalsIgnoreCase("null")) {
            profileImageView.setImageResource(R.drawable.absher);
            return;
        }
        
        Glide.with(this)
                .load(str.trim())
                .placeholder(R.drawable.absher)
                .error(R.drawable.absher)
                .into(profileImageView);
    }

    private void showDefaultData() {
        if (!isAdded()) return;
        tvProfileName.setText("");
        tvProfileId.setText("");
        tvProfileNameArabic.setVisibility(View.GONE);
        if (profileImageView != null) profileImageView.setImageResource(R.drawable.absher);
        if (idImageView != null) idImageView.setImageResource(R.drawable.absher);
        hideLoadingOverlayAndShowImage();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        profileImageView = view.findViewById(R.id.profileImageView);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfileNameArabic = view.findViewById(R.id.tvProfileNameArabic);
        tvProfileId = view.findViewById(R.id.tvProfileId);
        tvSeeAll = view.findViewById(R.id.tvSeeAll);
        idImageView = view.findViewById(R.id.idImageView);
        idLoadingAnimation = view.findViewById(R.id.idLoadingAnimation);
        lottieOverlay = view.findViewById(R.id.lottieOverlay);
        llIdError = view.findViewById(R.id.llIdError);
        View btnStartSurvey = view.findViewById(R.id.btnStartSurvey);
        if (btnStartSurvey != null) {
            btnStartSurvey.setOnClickListener(v -> {
                Toast.makeText(getContext(), "Survey opened", Toast.LENGTH_SHORT).show();
            });
        }

        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                    .replace(R.id.container, new IdViewFragment())
                    .addToBackStack(null).commit());
        }

        view.findViewById(R.id.profileCard).setOnClickListener(v -> {
            ProfileFragment pf = new ProfileFragment();
            Bundle b = new Bundle(); b.putSerializable("USER", currentUser);
            pf.setArguments(b);
            getParentFragmentManager().beginTransaction().replace(R.id.container, pf).addToBackStack(null).commit();
        });

        View.OnClickListener cardClickListener = v -> {
            boolean isExpired = (currentUser != null && currentUser.isExpired())
                    || (llIdError != null && llIdError.getVisibility() == View.VISIBLE);
            if (isExpired) {
                ExpiredDocumentDialogHelper.show(getContext());
                return;
            }

            if (currentUser != null) {
                Intent intent = new Intent(getContext(), FullScreenIdActivity.class);
                intent.putExtra("USER", currentUser);
                intent.putExtra("USERNAME", currentUsername);
                intent.putExtra("ENCRYPTED_ID_URL", currentUser.getId_image_url());
                startActivity(intent);
            }
        };

        View squareCard = view.findViewById(R.id.squareCard);
        if (squareCard != null) squareCard.setOnClickListener(cardClickListener);
        if (idImageView != null) idImageView.setOnClickListener(cardClickListener);
        if (llIdError != null) llIdError.setOnClickListener(cardClickListener);

        view.findViewById(R.id.settingsIcon).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), Settingsvact.class);
            intent.putExtra("USERNAME", currentUsername);
            intent.putExtra("USER", currentUser);
            startActivity(intent);
        });

        View searchIcon = view.findViewById(R.id.searchIcon);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> startActivity(new Intent(getContext(), SearchActivity.class)));
        }

        View notificationsIcon = view.findViewById(R.id.notificationsIcon);
        if (notificationsIcon != null) {
            notificationsIcon.setOnClickListener(v -> startActivity(new Intent(getContext(), InboxActivity.class)));
        }

        View cardManageDigitalIdentity = view.findViewById(R.id.cardManageDigitalIdentity);
        if (cardManageDigitalIdentity != null) {
            cardManageDigitalIdentity.setOnClickListener(v -> startActivity(new Intent(getContext(), ManageDigitalIdentityMenuActivity.class)));
        }

        View cardDigitalDocuments = view.findViewById(R.id.cardDigitalDocuments);
        if (cardDigitalDocuments != null) {
            cardDigitalDocuments.setOnClickListener(v -> startActivity(new Intent(getContext(), DigitalDocumentsActivity.class)));
        }

        View cardLaborImport = view.findViewById(R.id.cardLaborImport);
        if (cardLaborImport != null) {
            cardLaborImport.setOnClickListener(v -> startActivity(new Intent(getContext(), LaborImportationsActivity.class)));
        }
        
        view.findViewById(R.id.cardMyVehicles).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SimpleDetailsActivity.class);
            intent.putExtra("TITLE", getString(R.string.my_vehicles_title));
            intent.putExtra("ICON", R.drawable.ic_car);
            intent.putExtra("HEADING", getString(R.string.no_vehicles));
            intent.putExtra("DESCRIPTION", getString(R.string.no_vehicles_description));
            startActivity(intent);
        });

        View cardAuthServices = view.findViewById(R.id.cardAuthServices);
        if (cardAuthServices != null) {
            cardAuthServices.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), AuthenticationServicesActivity.class);
                startActivity(intent);
            });
        }

        View cardAbsherTravel = view.findViewById(R.id.cardAbsherTravel);
        if (cardAbsherTravel != null) {
            cardAbsherTravel.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), TravelRequestsActivity.class);
                startActivity(intent);
            });
        }

        View cardReportAccident = view.findViewById(R.id.cardReportAccident);
        if (cardReportAccident != null) {
            cardReportAccident.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), ReportMinorAccidentActivity.class);
                startActivity(intent);
            });
        }

        View cardUpdatePhoto = view.findViewById(R.id.cardUpdatePhoto);
        if (cardUpdatePhoto != null) {
            cardUpdatePhoto.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PhotoRequestsActivity.class);
                startActivity(intent);
            });
        }

        View cardMyWeapons = view.findViewById(R.id.cardMyWeapons);
        if (cardMyWeapons != null) {
            cardMyWeapons.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), SimpleDetailsActivity.class);
                intent.putExtra("TITLE", getString(R.string.my_weapons));
                intent.putExtra("ICON", R.drawable.ic_weapon_outline);
                intent.putExtra("HEADING", getString(R.string.no_weapons));
                intent.putExtra("DESCRIPTION", getString(R.string.no_weapons_description));
                startActivity(intent);
            });
        }

        loadUserData();
        return view;
    }
}
