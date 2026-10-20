package com.sandwabe.tecnol;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class SMSVerificationActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "absher_sms_channel";
    private static final String CHANNEL_NAME = "Absher SMS";
    private static final int NOTIFICATION_ID = 12345;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private EditText etCode1, etCode2, etCode3, etCode4;
    private TextView tvError;
    private String verificationCode;
    private String phoneNumber = "******7043"; // Default fallback
    private boolean isAutoFilling = false;

    public class AnonymousClass2 extends CodeTextWatcher {
        public AnonymousClass2(EditText current) {
            super(current, null);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            super.afterTextChanged(editable);
            if (editable.length() != 1 || isAutoFilling) return;
            new Handler(Looper.getMainLooper()).postDelayed(SMSVerificationActivity.this::verifyCode, 100L);
        }
    }

    public static class CodeTextWatcher implements TextWatcher {
        private final EditText nextView;

        public CodeTextWatcher(EditText current, EditText next) {
            this.nextView = next;
        }

        @Override
        public void afterTextChanged(Editable s) {}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }
    }

    private void autoFillAllBoxes() {
        if (verificationCode == null) return;
        isAutoFilling = true;
        try {
            etCode1.setText(String.valueOf(verificationCode.charAt(0)));
            etCode2.setText(String.valueOf(verificationCode.charAt(1)));
            etCode3.setText(String.valueOf(verificationCode.charAt(2)));
            etCode4.setText(String.valueOf(verificationCode.charAt(3)));
        } catch (Exception e) {
            Log.e("SMS", "Autofill failed", e);
        }
        isAutoFilling = false;
        verifyCode();
    }

    private void completeVerification() {
        String username = getIntent().getStringExtra("USERNAME");
        String password = getIntent().getStringExtra("PASSWORD");
        boolean rememberMe = getIntent().getBooleanExtra("REMEMBER_ME", false);

        SharedPreferences.Editor editor = getSharedPreferences("UserAppPrefs", MODE_PRIVATE).edit();
        editor.putBoolean(LoginActivity.KEY_IS_LOGGED_IN, true);
        editor.putBoolean(LoginActivity.KEY_IS_VERIFIED, true);
        editor.putLong(SessionManager.KEY_LOGIN_TIMESTAMP, System.currentTimeMillis());
        editor.putString(LoginActivity.KEY_USERNAME, username);
        if (rememberMe) {
            editor.putString(LoginActivity.KEY_PASSWORD, password);
            editor.putBoolean(LoginActivity.KEY_REMEMBER_ME, true);
        }
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void verifyCode() {
        if (etCode1 == null || etCode2 == null || etCode3 == null || etCode4 == null) return;
        String enteredCode = etCode1.getText().toString() + etCode2.getText().toString() + 
                            etCode3.getText().toString() + etCode4.getText().toString();
        
        if (enteredCode.length() == 4 && Objects.equals(enteredCode, verificationCode)) {
            completeVerification();
        } else if (enteredCode.length() == 4) {
            if (tvError != null) tvError.setVisibility(View.VISIBLE);
            etCode1.setText(""); etCode2.setText(""); etCode3.setText(""); etCode4.setText("");
            etCode1.requestFocus();
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Verification codes from Absher");
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    private String phoneNumberDisplay = "******7043";

    private void initViews() {
        try {
            findViewById(R.id.ivBack).setOnClickListener(v -> finish());
            TextView tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
            TextView tvPhoneNumberNormal = findViewById(R.id.tvPhoneNumberNormal);
            etCode1 = findViewById(R.id.etCode1);
            etCode2 = findViewById(R.id.etCode2);
            etCode3 = findViewById(R.id.etCode3);
            etCode4 = findViewById(R.id.etCode4);
            tvError = findViewById(R.id.tvError);
            MaterialButton btnUseAuthenticator = findViewById(R.id.btnUseAuthenticator);

            if (btnUseAuthenticator != null) btnUseAuthenticator.setOnClickListener(v -> autoFillAllBoxes());
            
            String fbPhone = getIntent().getStringExtra("PHONE_NUMBER");
            if ((fbPhone == null || fbPhone.isEmpty()) && ApiClient.getCachedUser() != null) {
                fbPhone = ApiClient.getCachedUser().getPhone_number();
            }
            if (fbPhone != null && !fbPhone.isEmpty()) {
                if (fbPhone.length() > 4) {
                    this.phoneNumber = "******" + fbPhone.substring(fbPhone.length() - 4);
                } else {
                    this.phoneNumber = fbPhone;
                }
            }

            if (tvPhoneNumber != null) tvPhoneNumber.setText(this.phoneNumber);
            if (tvPhoneNumberNormal != null) tvPhoneNumberNormal.setText(" via SMS");

            if (etCode1 != null) etCode1.addTextChangedListener(new CodeTextWatcher(etCode1, etCode2));
            if (etCode2 != null) etCode2.addTextChangedListener(new CodeTextWatcher(etCode2, etCode3));
            if (etCode3 != null) etCode3.addTextChangedListener(new CodeTextWatcher(etCode3, etCode4));
            if (etCode4 != null) etCode4.addTextChangedListener(new AnonymousClass2(etCode4));
        } catch (Exception e) {
            Log.e("SMS", "Init failed", e);
        }
    }

    private void sendNotification() {
        try {
            createNotificationChannel();
            verificationCode = String.format(Locale.US, "%04d", new Random().nextInt(10000));
            
            new Handler(Looper.getMainLooper()).postDelayed(this::autoFillAllBoxes, 2000);

            Intent intent = new Intent(this, SMSVerificationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.absher)
                    .setContentTitle("Absher")
                    .setContentText("Absher: Verification code " + verificationCode + " for login. Do not share it.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            if (LanguageHelper.isArabic(this)) {
                builder.setContentText("أبشر: رمز التحقق هو " + verificationCode + ". لا تشارك هذا الرمز.");
            }

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) manager.notify(NOTIFICATION_ID, builder.build());
        } catch (Exception e) {
            Log.e("SMS", "Notification failed", e);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ServiceNavHelper.setupStatusBar(this);
            setContentView(R.layout.activity_sms_verification);
            initViews();
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != 0) {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, PERMISSION_REQUEST_CODE);
                }
            }
            
            new Handler(Looper.getMainLooper()).postDelayed(this::sendNotification, 1500);
        } catch (Exception e) {
            Log.e("SMS", "onCreate failed", e);
            Toast.makeText(this, "Verification error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
