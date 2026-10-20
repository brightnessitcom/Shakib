package com.sandwabe.tecnol;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class UpdateBirthCertificateActivity extends AppCompatActivity {

    private ImageView ivRadioMyself;
    private ImageView ivRadioFamilyMember;
    private MaterialButton btnContinue;
    private int selectedOption = 0; // 1 = Myself, 2 = Family Member

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_update_birth_certificate);

        ServiceNavHelper.setupBack(this, R.id.ivClose);

        ivRadioMyself = findViewById(R.id.ivRadioMyself);
        ivRadioFamilyMember = findViewById(R.id.ivRadioFamilyMember);
        btnContinue = findViewById(R.id.btnContinue);

        View rowMyself = findViewById(R.id.rowMyself);
        View rowFamilyMember = findViewById(R.id.rowFamilyMember);

        if (rowMyself != null) {
            rowMyself.setOnClickListener(v -> selectOption(1));
        }

        if (rowFamilyMember != null) {
            rowFamilyMember.setOnClickListener(v -> selectOption(2));
        }

        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                if (selectedOption != 0) {
                    CannotProceedActivity.start(
                            this,
                            "Cannot Proceed",
                            "Sorry! You don't have any birth certificate record available to update."
                    );
                }
            });
        }
    }

    private void selectOption(int option) {
        selectedOption = option;
        if (ivRadioMyself != null) {
            ivRadioMyself.setImageResource(option == 1 ? R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
        }
        if (ivRadioFamilyMember != null) {
            ivRadioFamilyMember.setImageResource(option == 2 ? R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
        }
        if (btnContinue != null) {
            btnContinue.setBackgroundTintList(ColorStateList.valueOf(
                    androidx.core.content.ContextCompat.getColor(this, R.color.btn_service_action_bg)
            ));
            btnContinue.setTextColor(
                    androidx.core.content.ContextCompat.getColor(this, R.color.btn_service_action_text)
            );
        }
    }
}
