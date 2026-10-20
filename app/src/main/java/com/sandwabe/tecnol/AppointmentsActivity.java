package com.sandwabe.tecnol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class AppointmentsActivity extends AppCompatActivity {

    private final CaptchaHelper captchaHelper = new CaptchaHelper();
    private boolean isGregorian = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_appointments);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        ImageView ivCaptchaImage = findViewById(R.id.ivCaptchaImage);
        ImageView ivRefreshCaptcha = findViewById(R.id.ivRefreshCaptcha);
        EditText etIdNumber = findViewById(R.id.etIdNumber);
        View layoutCalendar = findViewById(R.id.layoutCalendar);
        TextView tvCalendar = findViewById(R.id.tvCalendar);
        View layoutDob = findViewById(R.id.layoutDob);
        EditText etDob = findViewById(R.id.etDob);
        EditText etCaptchaCode = findViewById(R.id.etCaptchaCode);
        MaterialButton btnVerifyDetails = findViewById(R.id.btnVerifyDetails);

        // Setup Captcha
        captchaHelper.generateCaptcha(ivCaptchaImage);
        if (ivRefreshCaptcha != null) {
            ivRefreshCaptcha.setOnClickListener(v -> captchaHelper.generateCaptcha(ivCaptchaImage));
        }

        // Setup Calendar toggle
        if (layoutCalendar != null && tvCalendar != null) {
            layoutCalendar.setOnClickListener(v -> {
                isGregorian = !isGregorian;
                tvCalendar.setText(isGregorian ? "Gregorian" : "Hijri");
            });
        }

        // Setup DatePicker
        View.OnClickListener datePickerListener = v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                if (etDob != null) {
                    etDob.setText(date);
                }
            }, c.get(Calendar.YEAR) - 25, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        };

        if (layoutDob != null) layoutDob.setOnClickListener(datePickerListener);
        if (etDob != null) etDob.setOnClickListener(datePickerListener);

        // Form validation listener for dynamic button tint and text color
        Runnable updateButtonState = () -> {
            String idStr = etIdNumber != null ? etIdNumber.getText().toString().trim() : "";
            String dobStr = etDob != null ? etDob.getText().toString().trim() : "";
            String capStr = etCaptchaCode != null ? etCaptchaCode.getText().toString().trim() : "";

            boolean isValid = !idStr.isEmpty() && !dobStr.isEmpty() && !capStr.isEmpty();
            if (btnVerifyDetails != null) {
                btnVerifyDetails.setBackgroundTintList(ContextCompat.getColorStateList(
                        this,
                        isValid ? R.color.login_btn_active_bg : R.color.login_btn_disabled_bg
                ));
                btnVerifyDetails.setTextColor(ContextCompat.getColor(
                        this,
                        isValid ? R.color.login_btn_active_text : R.color.login_btn_disabled_text
                ));
            }
        };

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState.run();
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        if (etIdNumber != null) etIdNumber.addTextChangedListener(watcher);
        if (etDob != null) etDob.addTextChangedListener(watcher);
        if (etCaptchaCode != null) etCaptchaCode.addTextChangedListener(watcher);

        // Apply initial disabled state
        updateButtonState.run();

        if (btnVerifyDetails != null) {
            btnVerifyDetails.setOnClickListener(v -> {
                String idStr = etIdNumber != null ? etIdNumber.getText().toString().trim() : "";
                if (idStr.isEmpty()) {
                    Toast.makeText(this, "Please enter your ID Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                String dobStr = etDob != null ? etDob.getText().toString().trim() : "";
                if (dobStr.isEmpty()) {
                    Toast.makeText(this, "Please select Date of Birth", Toast.LENGTH_SHORT).show();
                    return;
                }
                String enteredCaptcha = etCaptchaCode != null ? etCaptchaCode.getText().toString().trim() : "";
                if (enteredCaptcha.isEmpty()) {
                    Toast.makeText(this, "Please enter the captcha code", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!enteredCaptcha.equals(captchaHelper.getCurrentCode())) {
                    Toast.makeText(this, "Incorrect captcha code. Please try again.", Toast.LENGTH_SHORT).show();
                    captchaHelper.generateCaptcha(ivCaptchaImage);
                    if (etCaptchaCode != null) etCaptchaCode.setText("");
                    return;
                }

                Toast.makeText(this, "Details verified. Loading appointment slots...", Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }
}
