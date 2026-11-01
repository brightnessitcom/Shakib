package com.sandwabe.tecnol;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class Resident_detals_freg extends Fragment {
    private User currentUser;
    private String currentUsername;
    private TextView expiryDateText;
    private TextView id_version;
    private TextView issueDateText;
    private TextView residentIdText;

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
            updateResidentUI(currentUser);
        }

        Context ctx = getContext();
        if (ctx != null && this.currentUsername != null && !this.currentUsername.isEmpty()) {
            ApiClient.fetchUser(ctx, this.currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (!isAdded()) return;
                    currentUser = user;
                    updateResidentUI(currentUser);
                }

                @Override
                public void onError(String errorMessage) {
                    android.util.Log.w("ResidentDetails", "ApiClient error: " + errorMessage);
                }
            });
        }
    }

    private String formatDate(String str) {
        return DateUtils.formatDisplayDate(requireContext(), str);
    }

    private void initializeViews(View view) {
        View viewFindViewById = view.findViewById(R.id.card_resident);
        if (viewFindViewById != null) {
            this.residentIdText = viewFindViewById.findViewById(R.id.resident_id_text);
            this.issueDateText = viewFindViewById.findViewById(R.id.issue_date_text);
            this.expiryDateText = viewFindViewById.findViewById(R.id.expiry_date_text);
            this.id_version = viewFindViewById.findViewById(R.id.id_version);
        }
    }

    private boolean isValidData(String str) {
        return str != null && !str.isEmpty() && !Objects.equals(str, "null");
    }

    public void handleBackPress(View view) {
        if (isAdded()) {
            getParentFragmentManager().popBackStack();
        }
    }

    public void handleCopyId(View view) {
        if (this.residentIdText == null) return;
        String string = this.residentIdText.getText().toString();
        if (Objects.equals(string, "Not Available") || Objects.equals(string, "Loading...")) {
            Toast.makeText(getContext(), "No resident ID available", Toast.LENGTH_SHORT).show();
        } else {
            ClipboardManager cm = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("Resident ID", string));
                Toast.makeText(getContext(), "Resident ID Copied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupCopyFunctionality(View view) {
        ImageView imageView = view.findViewById(R.id.copyimgiconresedent);
        if (imageView == null || this.residentIdText == null) {
            return;
        }
        imageView.setOnClickListener(this::handleCopyId);
    }

    private void showDefaultData() {
        if (this.residentIdText != null) this.residentIdText.setText(R.string.not_set);
        if (this.issueDateText != null) this.issueDateText.setText(R.string.not_set);
        if (this.expiryDateText != null) this.expiryDateText.setText(R.string.not_set);
        if (this.id_version != null) this.id_version.setText(R.string.not_set);
    }

    private void updateResidentUI(User user) {
        String iqama_number_withidTow = user.getMyResidentId();
        if (isValidData(iqama_number_withidTow)) {
            this.residentIdText.setText(getString(R.string.iqama_number_withidTow, iqama_number_withidTow));
        } else {
            String iqama_number_withid = user.getIqama_number_withid();
            if (isValidData(iqama_number_withid)) {
                this.residentIdText.setText(getString(R.string.iqama_number_withidTow, iqama_number_withid));
            } else if (isValidData(user.getUsername())) {
                this.residentIdText.setText(getString(R.string.iqama_number_withidTow, user.getUsername()));
            } else {
                if (this.residentIdText != null) this.residentIdText.setText(R.string.not_set);
            }
        }
        String iqamaIssue = user.getIqamaIssue();
        if (isValidData(iqamaIssue)) {
            this.issueDateText.setText(formatDate(iqamaIssue));
        } else {
            if (this.issueDateText != null) this.issueDateText.setText(R.string.not_set);
        }
        String iqamaExpire = user.getIqamaExpire();
        if (isValidData(iqamaExpire)) {
            this.expiryDateText.setText(formatDate(iqamaExpire));
        } else {
            if (this.expiryDateText != null) this.expiryDateText.setText(R.string.not_set);
        }
        String id_version = user.getId_version();
        if (isValidData(id_version)) {
            this.id_version.setText(id_version);
        } else {
            if (this.id_version != null) this.id_version.setText(R.string.not_set);
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
        View viewInflate = layoutInflater.inflate(R.layout.resident_detals, viewGroup, false);
        initializeViews(viewInflate);
        ImageView imageView = viewInflate.findViewById(R.id.backarrow);
        if (imageView != null) {
            imageView.setOnClickListener(this::handleBackPress);
        }
        loadUserData();
        setupCopyFunctionality(viewInflate);
        return viewInflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }
}
