package com.sandwabe.tecnol;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ViewBirthCertificateActivity extends AppCompatActivity {

    private ImageView ivRadioMyself;
    private ImageView ivRadioFamilyMember;
    private MaterialButton btnViewBirthCertificate;
    private int selectedOption = 0; // 1 = Myself, 2 = Family Member

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_view_birth_certificate);

        ServiceNavHelper.setupBack(this, R.id.ivClose);

        ivRadioMyself = findViewById(R.id.ivRadioMyself);
        ivRadioFamilyMember = findViewById(R.id.ivRadioFamilyMember);
        btnViewBirthCertificate = findViewById(R.id.btnViewBirthCertificate);

        View rowMyself = findViewById(R.id.rowMyself);
        View rowFamilyMember = findViewById(R.id.rowFamilyMember);

        if (rowMyself != null) {
            rowMyself.setOnClickListener(v -> selectOption(1));
        }

        if (rowFamilyMember != null) {
            rowFamilyMember.setOnClickListener(v -> selectOption(2));
        }

        if (btnViewBirthCertificate != null) {
            btnViewBirthCertificate.setOnClickListener(v -> {
                if (selectedOption != 0) {
                    CannotProceedActivity.start(
                            this,
                            "Cannot Proceed",
                            "Sorry! You don't have any birth certificate record available."
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
        if (btnViewBirthCertificate != null) {
            btnViewBirthCertificate.setBackgroundTintList(ColorStateList.valueOf(
                    androidx.core.content.ContextCompat.getColor(this, R.color.btn_service_action_bg)
            ));
            btnViewBirthCertificate.setTextColor(
                    androidx.core.content.ContextCompat.getColor(this, R.color.btn_service_action_text)
            );
        }
    }
}
