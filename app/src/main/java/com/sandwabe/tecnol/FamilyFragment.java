package com.sandwabe.tecnol;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FamilyFragment extends Fragment {
    private String currentUsername;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_family, container, false);

        View notificationsIcon = view.findViewById(R.id.notificationsIcon);
        if (notificationsIcon != null) {
            notificationsIcon.setOnClickListener(v ->
                startActivity(new Intent(getContext(), InboxActivity.class))
            );
        }

        View settingsIcon = view.findViewById(R.id.settingsIcon);
        if (settingsIcon != null) {
            settingsIcon.setOnClickListener(v ->
                startActivity(new Intent(getContext(), Settingsvact.class))
            );
        }

        EditText etSearchFamily = view.findViewById(R.id.etSearchFamily);
        TextView tvEmptyTitle = view.findViewById(R.id.tvEmptyTitle);
        TextView tvEmptySubtitle = view.findViewById(R.id.tvEmptySubtitle);

        if (etSearchFamily != null) {
            etSearchFamily.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s != null && s.length() > 0) {
                        if (tvEmptyTitle != null) tvEmptyTitle.setText("No Results Found");
                        if (tvEmptySubtitle != null) tvEmptySubtitle.setText("No family members match \"" + s + "\"");
                    } else {
                        if (tvEmptyTitle != null) tvEmptyTitle.setText("No Family Members");
                        if (tvEmptySubtitle != null) tvEmptySubtitle.setText("Once you have family members, they will display here.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        fetchFamilyData();
        return view;
    }

    private void fetchFamilyData() {
        if (!isAdded()) return;
        currentUsername = LoginActivity.getCurrentUsername(requireContext());
        User cached = ApiClient.getCachedUser(getContext());
        if (cached != null) {
            if (currentUsername == null || currentUsername.isEmpty()) {
                currentUsername = cached.getUsername();
            }
            updateUI(cached);
        }

        if (currentUsername != null && !currentUsername.isEmpty()) {
            ApiClient.fetchUser(requireContext(), currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (!isAdded()) return;
                    updateUI(user);
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w("FamilyFragment", "fetchUser error: " + errorMessage);
                }
            });
        }
    }

    private void updateUI(User user) {
        View view = getView();
        if (view == null || user == null) return;
    }
}
