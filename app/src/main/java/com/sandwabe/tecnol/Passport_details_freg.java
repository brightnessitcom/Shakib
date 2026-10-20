package com.sandwabe.tecnol;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class Passport_details_freg extends Fragment {
    private User currentUser;
    private String currentUsername;
    private TextView passportAmountText;
    private TextView passportCityText;
    private TextView passportExpiryDateText;
    private TextView passportIssueDateText;
    private TextView passportNumberText;
    private TextView passportStatusText;

    private void loadUserData() {
        if (!isAdded()) return;
        if (this.currentUsername == null || this.currentUsername.isEmpty()) {
            this.currentUsername = LoginActivity.getCurrentUsername(requireContext());
        }

        User cached = ApiClient.getCachedUser(getContext());
        if (cached != null) {
            currentUser = cached;
            if (this.currentUsername == null || this.currentUsername.isEmpty()) {
                this.currentUsername = currentUser.getUsername();
            }
            updatePassportUI(currentUser);
        }

        Context ctx = getContext();
        if (ctx != null && this.currentUsername != null && !this.currentUsername.isEmpty()) {
            ApiClient.fetchUser(ctx, this.currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (!isAdded()) return;
                    currentUser = user;
                    updatePassportUI(currentUser);
                }

                @Override
                public void onError(String errorMessage) {
                    android.util.Log.w("PassportDetails", "ApiClient error: " + errorMessage);
                }
            });
        }
    }

    private String formatDate(String str) {
        return DateUtils.formatDisplayDate(requireContext(), str);
    }

    private void initializeViews(View view) {
        View viewFindViewById = view.findViewById(R.id.card_amount_passport);
        if (viewFindViewById != null) {
            this.passportAmountText = viewFindViewById.findViewById(R.id.passport_amount_text);
        }
        View viewFindViewById2 = view.findViewById(R.id.passport_card);
        if (viewFindViewById2 != null) {
            this.passportNumberText = viewFindViewById2.findViewById(R.id.passport_number_text);
            this.passportIssueDateText = viewFindViewById2.findViewById(R.id.passport_issue_date_text);
            this.passportExpiryDateText = viewFindViewById2.findViewById(R.id.passport_expiry_date_text);
            this.passportCityText = viewFindViewById2.findViewById(R.id.passport_city_text);
            this.passportStatusText = viewFindViewById2.findViewById(R.id.passport_status_text);
        }
    }

    public void handleBackPress(View view) {
        if (isAdded()) {
            getParentFragmentManager().popBackStack();
        }
    }

    public void handleCopyPassportNumber(View view) {
        if (this.passportNumberText == null) return;
        String string = this.passportNumberText.getText().toString();
        if (Objects.equals(string, "Not Available") || string.isEmpty()) {
            Toast.makeText(getContext(), "No passport number available", Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager cm = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("Passport Number", string));
                Toast.makeText(getContext(), "Passport Number Copied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupCopyFunctionality(View view) {
        ImageView imageView = view.findViewById(R.id.copyimgicopassport);
        if (imageView == null || this.passportNumberText == null) {
            return;
        }
        imageView.setOnClickListener(this::handleCopyPassportNumber);
    }

    private void setupExpandCollapseFunctionality(View view) {
        final LinearLayout linearLayout = view.findViewById(R.id.card_content);
        LinearLayout headerLayout = view.findViewById(R.id.header_layout);
        final ImageView imageView = view.findViewById(R.id.arrow_icon);
        if (linearLayout != null) {
            linearLayout.setVisibility(View.VISIBLE);
        }
        if (imageView != null) {
            imageView.setImageResource(R.drawable.ic_arrow_up);
        }
        if (headerLayout != null) {
            headerLayout.setOnClickListener(v -> toggleContent(linearLayout, imageView));
        }
    }

    private void showDefaultData() {
        if (this.passportAmountText != null) this.passportAmountText.setText(R.string.not_set);
        if (this.passportNumberText != null) this.passportNumberText.setText(R.string.not_set);
        if (this.passportIssueDateText != null) this.passportIssueDateText.setText(R.string.not_set);
        if (this.passportExpiryDateText != null) this.passportExpiryDateText.setText(R.string.not_set);
        if (this.passportCityText != null) this.passportCityText.setText(R.string.not_set);
        if (this.passportStatusText != null) {
            this.passportStatusText.setText("Unknown");
            this.passportStatusText.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray));
        }
    }

    private void toggleContent(LinearLayout linearLayout, ImageView imageView) {
        if (linearLayout == null || imageView == null) {
            return;
        }
        if (linearLayout.getVisibility() == View.GONE) {
            linearLayout.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.ic_arrow_up);
        } else {
            linearLayout.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.ic_arrow_down);
        }
    }

    private void updatePassportUI(User user) {
        String passport_amount = user.getPassport_amount();
        if (passport_amount == null || passport_amount.isEmpty()) {
            if (this.passportAmountText != null) this.passportAmountText.setText(R.string.not_set);
        } else {
            if (this.passportAmountText != null) this.passportAmountText.setText(passport_amount);
        }
        String passport_number = user.getPassport_number();
        if (passport_number == null || passport_number.isEmpty()) {
            if (this.passportNumberText != null) this.passportNumberText.setText(R.string.not_set);
        } else {
            if (this.passportNumberText != null) this.passportNumberText.setText(passport_number);
        }
        String passport_issue_date = user.getPassport_issue_date();
        if (passport_issue_date == null || passport_issue_date.isEmpty()) {
            if (this.passportIssueDateText != null) this.passportIssueDateText.setText(R.string.not_set);
        } else {
            if (this.passportIssueDateText != null) this.passportIssueDateText.setText(formatDate(passport_issue_date));
        }
        String passport_expiry_date = user.getPassport_expiry_date();
        if (passport_expiry_date == null || passport_expiry_date.isEmpty()) {
            if (this.passportExpiryDateText != null) this.passportExpiryDateText.setText(R.string.not_set);
            if (this.passportStatusText != null) this.passportStatusText.setText(getString(R.string.status_unknown_placeholder));
        } else {
            if (this.passportExpiryDateText != null) this.passportExpiryDateText.setText(formatDate(passport_expiry_date));
        }
        String passport_city = user.getPassport_city();
        if (passport_city == null || passport_city.isEmpty()) {
            if (this.passportCityText != null) this.passportCityText.setText(R.string.not_set);
        } else {
            if (this.passportCityText != null) this.passportCityText.setText(passport_city);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getArguments() != null) {
            this.currentUser = (User) getArguments().getSerializable("USER");
            this.currentUsername = getArguments().getString("USERNAME");
        }
        if (this.currentUsername == null || this.currentUsername.isEmpty()) {
            this.currentUsername = LoginActivity.getCurrentUsername(requireContext());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.passport_details, viewGroup, false);
        initializeViews(viewInflate);
        ImageView imageView = viewInflate.findViewById(R.id.backarrow);
        if (imageView != null) {
            imageView.setOnClickListener(this::handleBackPress);
        }
        loadUserData();
        setupCopyFunctionality(viewInflate);
        setupExpandCollapseFunctionality(viewInflate);
        return viewInflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
        if (requireContext() instanceof MainActivity) {
            ((MainActivity) requireContext()).setSwipeRefreshEnabled(false);
        }
    }
}
