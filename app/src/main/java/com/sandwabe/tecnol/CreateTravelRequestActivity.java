package com.sandwabe.tecnol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateTravelRequestActivity extends AppCompatActivity {

    private int selectedOption = 0; // 1: GCC, 2: Non-GCC

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_create_travel_request);

        View ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) {
            ivClose.setOnClickListener(v -> showExitConfirmationDialog());
        }

        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmationDialog();
            }
        });

        View optGcc = findViewById(R.id.optGcc);
        View optNonGcc = findViewById(R.id.optNonGcc);

        ImageView radioGcc = findViewById(R.id.radioGcc);
        ImageView radioNonGcc = findViewById(R.id.radioNonGcc);

        com.google.android.material.button.MaterialButton btnContinue = findViewById(R.id.btnContinue);

        View.OnClickListener listener = v -> {
            if (v == optGcc) selectedOption = 1;
            else if (v == optNonGcc) selectedOption = 2;

            int activeColor = ThemeHelper.isDarkMode(this) ? 0xFF72D09D : 0xFF00663D;
            int inactiveColor = 0xFF8A9095;

            radioGcc.setImageResource(selectedOption == 1 ? R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
            radioGcc.setColorFilter(selectedOption == 1 ? activeColor : inactiveColor);

            radioNonGcc.setImageResource(selectedOption == 2 ? R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
            radioNonGcc.setColorFilter(selectedOption == 2 ? activeColor : inactiveColor);

            if (btnContinue != null) {
                btnContinue.setBackgroundTintList(androidx.core.content.ContextCompat.getColorStateList(this, R.color.login_btn_active_bg));
            }
        };

        if (optGcc != null) optGcc.setOnClickListener(listener);
        if (optNonGcc != null) optNonGcc.setOnClickListener(listener);

        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                if (selectedOption == 0) {
                    Toast.makeText(this, "Please select an identity type", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "Proceeding with authentication...", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit_service);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }

        View btnNo = dialog.findViewById(R.id.btnNo);
        View btnYes = dialog.findViewById(R.id.btnYes);

        if (btnNo != null) {
            btnNo.setOnClickListener(v -> dialog.dismiss());
        }
        if (btnYes != null) {
            btnYes.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }

        dialog.show();
    }
}
