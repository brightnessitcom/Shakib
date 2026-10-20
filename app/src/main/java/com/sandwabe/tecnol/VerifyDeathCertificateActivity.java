package com.sandwabe.tecnol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Locale;

public class VerifyDeathCertificateActivity extends AppCompatActivity {

    private EditText etIdNumber;
    private TextView tvCalendar;
    private TextView tvDob;
    private EditText etDeathCertNumber;
    private MaterialButton btnVerifyDeathCertificate;

    private boolean isGregorian = true;
    private boolean isDobSelected = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_verify_death_certificate);

        ServiceNavHelper.setupBack(this, R.id.ivClose);

        etIdNumber = findViewById(R.id.etIdNumber);
        tvCalendar = findViewById(R.id.tvCalendar);
        tvDob = findViewById(R.id.tvDob);
        etDeathCertNumber = findViewById(R.id.etDeathCertNumber);
        btnVerifyDeathCertificate = findViewById(R.id.btnVerifyDeathCertificate);

        View containerCalendar = findViewById(R.id.containerCalendar);
        View containerDob = findViewById(R.id.containerDob);

        if (containerCalendar != null) {
            containerCalendar.setOnClickListener(v -> toggleCalendar());
        }

        if (containerDob != null) {
            containerDob.setOnClickListener(v -> showDatePicker());
        }

        TextWatcher inputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFormValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        if (etIdNumber != null) {
            etIdNumber.addTextChangedListener(inputWatcher);
        }
        if (etDeathCertNumber != null) {
            etDeathCertNumber.addTextChangedListener(inputWatcher);
        }

        if (btnVerifyDeathCertificate != null) {
            btnVerifyDeathCertificate.setOnClickListener(v -> {
                String idText = etIdNumber != null ? etIdNumber.getText().toString().trim() : "";
                String certText = etDeathCertNumber != null ? etDeathCertNumber.getText().toString().trim() : "";

                if (idText.isEmpty() && certText.isEmpty() && !isDobSelected) {
                    Toast.makeText(this, "Please enter ID number and certificate details", Toast.LENGTH_SHORT).show();
                    return;
                }

                CannotProceedActivity.start(
                        this,
                        "Cannot Proceed",
                        "Sorry! No death certificate found matching the entered information."
                );
            });
        }
    }

    private void toggleCalendar() {
        isGregorian = !isGregorian;
        if (tvCalendar != null) {
            tvCalendar.setText(isGregorian ? "Gregorian" : "Hijri");
        }
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR) - 30;
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, y, m, d) -> {
                    isDobSelected = true;
                    if (tvDob != null) {
                        tvDob.setText(String.format(Locale.US, "%02d/%02d/%04d", d, m + 1, y));
                        tvDob.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_primary));
                    }
                    checkFormValidity();
                },
                year,
                month,
                day
        );
        dialog.show();
    }

    private void checkFormValidity() {
        String idText = etIdNumber != null ? etIdNumber.getText().toString().trim() : "";
        String certText = etDeathCertNumber != null ? etDeathCertNumber.getText().toString().trim() : "";

        boolean hasInput = !idText.isEmpty() || !certText.isEmpty() || isDobSelected;

        if (btnVerifyDeathCertificate != null) {
            boolean isDark = ThemeHelper.isDarkMode(this);
            if (hasInput) {
                int activeBg = isDark ? Color.parseColor("#A2DABE") : Color.parseColor("#00663D");
                int activeText = isDark ? Color.parseColor("#082A18") : Color.parseColor("#FFFFFF");
                btnVerifyDeathCertificate.setBackgroundTintList(ColorStateList.valueOf(activeBg));
                btnVerifyDeathCertificate.setTextColor(activeText);
            } else {
                int inactiveBg = isDark ? Color.parseColor("#5B786A") : Color.parseColor("#C3D9CE");
                int inactiveText = isDark ? Color.parseColor("#203328") : Color.parseColor("#7B9387");
                btnVerifyDeathCertificate.setBackgroundTintList(ColorStateList.valueOf(inactiveBg));
                btnVerifyDeathCertificate.setTextColor(inactiveText);
            }
        }
    }
}
