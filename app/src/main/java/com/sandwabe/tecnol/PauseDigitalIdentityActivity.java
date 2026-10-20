package com.sandwabe.tecnol;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class PauseDigitalIdentityActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_pause_digital_identity);

        ServiceNavHelper.setupBack(this, R.id.ivClose);

        EditText etDob = findViewById(R.id.etDob);
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

        TextView tvReason = findViewById(R.id.tvReason);
        if (tvReason != null) {
            findViewById(R.id.layoutReason).setOnClickListener(v -> {
                String[] reasons = {"Device Lost", "Temporary Suspension", "Security Concern", "Other Reason"};
                new AlertDialog.Builder(this)
                    .setTitle("Select Reason")
                    .setItems(reasons, (dialog, which) -> tvReason.setText(reasons[which]))
                    .show();
            });
        }

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            EditText etIdNumber = findViewById(R.id.etIdNumber);
            String idStr = etIdNumber != null ? etIdNumber.getText().toString().trim() : "";
            if (idStr.isEmpty()) {
                Toast.makeText(this, "Please enter your ID Number", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Pause request submitted successfully.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}