package com.sandwabe.tecnol;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class WorkerFragment extends Fragment {

    public void onBackClick(View view) {
        if (isAdded()) {
            requireActivity().getOnBackPressedDispatcher().onBackPressed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worker, container, false);

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

        EditText etSearchWorker = view.findViewById(R.id.etSearchWorker);
        TextView tvEmptyTitle = view.findViewById(R.id.tvEmptyTitle);
        TextView tvEmptySubtitle = view.findViewById(R.id.tvEmptySubtitle);

        if (etSearchWorker != null) {
            etSearchWorker.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s != null && s.length() > 0) {
                        if (tvEmptyTitle != null) tvEmptyTitle.setText("No Results Found");
                        if (tvEmptySubtitle != null) tvEmptySubtitle.setText("No workers match \"" + s + "\"");
                    } else {
                        if (tvEmptyTitle != null) tvEmptyTitle.setText("No Workers");
                        if (tvEmptySubtitle != null) tvEmptySubtitle.setText("Once you have workers under your sponsorship they will display here.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        return view;
    }
}
