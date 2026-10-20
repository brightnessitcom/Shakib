package com.sandwabe.tecnol;

import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Objects;

public class IdViewFragment extends Fragment {
    private static final int LOADING_TIME = 3000;
    private User currentUser;
    private String currentUsername;
    private ImageView iqamaidview;
    private TextView lastUpdatedText;
    private LottieAnimationView loadingAnimation;
    private LinearLayout loadingLayout;
    private SwipeRefreshLayout swipeRefreshLayoutt;
    private View llExpiredWarning;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isDataLoaded = false;

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void updateExpiredWarning() {
        if (llExpiredWarning != null) {
            boolean isExpired = currentUser != null && currentUser.isExpired();
            llExpiredWarning.setVisibility(isExpired ? View.VISIBLE : View.GONE);
        }
    }

    private void loadIdImage(String url) {
        if (url == null || url.trim().isEmpty() || Objects.equals(url, "null")) {
            iqamaidview.setImageResource(R.drawable.absher);
            return;
        }
        
        Glide.with(this)
                .load(url.trim())
                .placeholder(R.drawable.absher)
                .error(R.drawable.absher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(1200)
                .into(iqamaidview);
    }

    private void loadIdData() {
        if (!isAdded()) return;
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(requireContext());
        }

        User cached = ApiClient.getCachedUser();
        if (cached != null) {
            currentUser = cached;
            loadIdImage(currentUser.getId_image_url());
            updateExpiredWarning();
            isDataLoaded = true;
        }

        Context ctx = getContext();
        if (ctx != null && currentUsername != null && !currentUsername.isEmpty()) {
            ApiClient.fetchUser(ctx, currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (!isAdded()) return;
                    currentUser = user;
                    requireActivity().runOnUiThread(() -> {
                        loadIdImage(currentUser.getId_image_url());
                        updateExpiredWarning();
                        isDataLoaded = true;
                    });
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w("IdViewFragment", "ApiClient error: " + errorMessage);
                }
            });
        } else if (ctx != null && (currentUsername == null || currentUsername.isEmpty()) && currentUser == null) {
            if (getActivity() instanceof DigitalDocumentsActivity) {
                Intent intent = new Intent(ctx, LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        }
    }

    private void navigateToHome() {
        if (!isAdded()) return;
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, new HomeFragment());
        ft.commit();
        
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFloatingImageVisible(true);
        }
    }

    public void refreshData() {
        if (loadingLayout != null) loadingLayout.setVisibility(View.VISIBLE);
        if (swipeRefreshLayoutt != null) swipeRefreshLayoutt.setVisibility(View.GONE);
        if (loadingAnimation != null) loadingAnimation.playAnimation();
        
        handler.postDelayed(() -> {
            if (!isAdded()) return;
            if (loadingLayout != null) loadingLayout.setVisibility(View.GONE);
            if (swipeRefreshLayoutt != null) swipeRefreshLayoutt.setVisibility(View.VISIBLE);
            String time = DateUtils.formatCurrentDateTime(requireContext());
            if (lastUpdatedText != null) lastUpdatedText.setText(getString(R.string.last_updated_status, time));
            loadIdData();
            if (swipeRefreshLayoutt != null) swipeRefreshLayoutt.setRefreshing(false);
        }, LOADING_TIME);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUsername = LoginActivity.getCurrentUsername(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_id_view, container, false);
        
        loadingLayout = view.findViewById(R.id.loadingLayout);
        loadingAnimation = view.findViewById(R.id.loadingAnimation);
        iqamaidview = view.findViewById(R.id.iqamaidview);
        llExpiredWarning = view.findViewById(R.id.llExpiredWarning);
        if (llExpiredWarning != null) {
            llExpiredWarning.setOnClickListener(v -> ExpiredDocumentDialogHelper.show(getContext()));
        }
        updateExpiredWarning();
        swipeRefreshLayoutt = view.findViewById(R.id.swipeRefreshLayoutt);
        lastUpdatedText = view.findViewById(R.id.lastUpdatedText);
        ImageView settingsIcon = view.findViewById(R.id.settingsIcon);

        int start = dpToPx(120);
        int end = dpToPx(200);
        if (swipeRefreshLayoutt != null) {
            swipeRefreshLayoutt.setProgressViewOffset(true, start, end);
            swipeRefreshLayoutt.setOnRefreshListener(() -> handler.post(this::refreshData));
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFloatingImageVisible(false);
        }

        View fragmentTopBar = view.findViewById(R.id.topBar);
        if (fragmentTopBar != null && getActivity() instanceof DigitalDocumentsActivity) {
            fragmentTopBar.setVisibility(View.GONE);
        }

        if (settingsIcon != null) {
            settingsIcon.setOnClickListener(v -> {
                if (getActivity() instanceof DigitalDocumentsActivity) {
                    requireActivity().finish();
                } else {
                    handler.post(this::navigateToHome);
                }
            });
        }
        
        if (iqamaidview != null) {
            iqamaidview.setOnClickListener(v -> {
                if (currentUser != null) {
                    if (currentUser.isExpired()) {
                        ExpiredDocumentDialogHelper.show(getContext());
                        return;
                    }
                    Intent intent = new Intent(getContext(), FullScreenIdActivity.class);
                    intent.putExtra("USER", currentUser);
                    intent.putExtra("USERNAME", currentUsername);
                    startActivity(intent);
                }
            });
            iqamaidview.setClickable(true);
            iqamaidview.setFocusable(true);
        }

        if (loadingAnimation != null) loadingAnimation.playAnimation();
        if (loadingLayout != null) loadingLayout.setVisibility(View.VISIBLE);
        if (swipeRefreshLayoutt != null) swipeRefreshLayoutt.setVisibility(View.GONE);
        
        String initialTime = DateUtils.formatCurrentDateTime(requireContext());
        if (lastUpdatedText != null) {
            lastUpdatedText.setText(getString(R.string.last_updated_status, initialTime));
        }
        
        handler.postDelayed(() -> {
            if (!isAdded()) return;
            if (loadingLayout != null) loadingLayout.setVisibility(View.GONE);
            if (swipeRefreshLayoutt != null) swipeRefreshLayoutt.setVisibility(View.VISIBLE);
            loadIdData();
            String finalTime = DateUtils.formatCurrentDateTime(requireContext());
            if (lastUpdatedText != null) {
                lastUpdatedText.setText(getString(R.string.last_updated_status, finalTime));
            }
        }, LOADING_TIME);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setFloatingImageVisible(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (loadingAnimation != null) {
            loadingAnimation.cancelAnimation();
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
