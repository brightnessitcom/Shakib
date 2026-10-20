package com.sandwabe.tecnol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class VisitorDigitalDocumentActivity extends AppCompatActivity {

    private final CaptchaHelper captchaHelper = new CaptchaHelper();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_visitor_digital_document);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        ImageView ivCaptchaImage = findViewById(R.id.ivCaptchaImage);
        ImageView ivRefreshCaptcha = findViewById(R.id.ivRefreshCaptcha);
        EditText etDob = findViewById(R.id.etDob);
        EditText etCaptchaCode = findViewById(R.id.etCaptchaCode);

        // Setup Captcha
        captchaHelper.generateCaptcha(ivCaptchaImage);
        if (ivRefreshCaptcha != null) {
            ivRefreshCaptcha.setOnClickListener(v -> captchaHelper.generateCaptcha(ivCaptchaImage));
        }

        // Setup DatePicker
        if (etDob != null) {
            etDob.setOnClickListener(v -> {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dpd = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                    String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                    etDob.setText(date);
                }, c.get(Calendar.YEAR) - 25, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dpd.show();
            });
        }

        findViewById(R.id.btnViewDoc).setOnClickListener(v -> {
            EditText etIdNumber = findViewById(R.id.etIdNumber);
            String idStr = etIdNumber != null ? etIdNumber.getText().toString().trim() : "";
            if (idStr.isEmpty()) {
                Toast.makeText(this, "Please enter your ID Number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (idStr.length() != 10 || (!idStr.startsWith("3") && !idStr.startsWith("4") && !idStr.startsWith("5") && !idStr.startsWith("6"))) {
                Toast.makeText(this, "ID Number should start with 3, 4, 5 or 6 and be 10 digits.", Toast.LENGTH_SHORT).show();
                return;
            }
            String enteredCaptcha = etCaptchaCode != null ? etCaptchaCode.getText().toString().trim() : "";
            if (enteredCaptcha.isEmpty()) {
                Toast.makeText(this, "Please enter the captcha code", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Loading Visitor Digital Document...", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}