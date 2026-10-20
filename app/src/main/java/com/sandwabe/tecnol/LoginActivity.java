package com.sandwabe.tecnol;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.text.Editable;
import android.text.TextWatcher;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {
    public static final String KEY_BIOMETRIC_ENABLED = "biometric_enabled";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_IS_VERIFIED = "is_verified";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_REMEMBER_ME = "remember_me";
    public static final String KEY_USERNAME = "username";
    public static final String PREFS_NAME = "UserAppPrefs";

    private CheckBox cbRememberMe;
    private EditText etPassword;
    private EditText etUsername;
    private MaterialButton btnLogin;
    private SharedPreferences sharedPreferences;
    private LinearLayout usernameContainer, passwordContainer;
    private Dialog loadingDialog;
    private final Handler loginHandler = new Handler(Looper.getMainLooper());
    private Runnable loginTimeoutRunnable;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dynamic status bar: Light mode (#FFFFFF with dark icons) vs Dark mode (#161719 with white icons)
        boolean isDark = ThemeHelper.isDarkMode(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(isDark ? Color.parseColor("#1A1A1A") : Color.parseColor("#FFFFFF"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            int flags = decor.getSystemUiVisibility();
            if (isDark) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // white icons
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;  // dark icons
            }
            decor.setSystemUiVisibility(flags);
        }
        
        try {
            setContentView(R.layout.activity_login);
            Log.d("LoginActivity", "UI Loaded");

            sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

            etUsername = findViewById(R.id.etUsername);
            etPassword = findViewById(R.id.etPassword);
            cbRememberMe = findViewById(R.id.cbRememberMe);
            btnLogin = findViewById(R.id.btnLogin);
            ImageView ivBack = findViewById(R.id.ivBack);
            View tvForgotPassword = findViewById(R.id.tvForgotPassword);
            usernameContainer = findViewById(R.id.usernameContainer);
            passwordContainer = findViewById(R.id.passwordContainer);

            if (ivBack != null) ivBack.setOnClickListener(v -> {
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_out_right_smooth);
            });
            if (tvForgotPassword != null) {
                tvForgotPassword.setOnClickListener(v ->
                    Toast.makeText(this, "To reset password, please visit absher.sa or contact support.", Toast.LENGTH_LONG).show()
                );
            }

            if (btnLogin != null) {
                btnLogin.setOnClickListener(v -> {
                    String user = etUsername.getText().toString().trim();
                    String pass = etPassword.getText().toString().trim();
                    if (user.isEmpty() || pass.isEmpty()) {
                        Toast.makeText(this, "Please enter details", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    showStayLoggedInModal(user, pass);
                });
            }

            setupFocusChangeListeners();
            setupInputWatchers();
            loadSavedCredentials();
        } catch (Exception e) {
            Log.e("LoginActivity", "Init Error", e);
        }
    }

    private void showStayLoggedInModal(final String username, final String password) {
        if (isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())) {
            return;
        }

        if (cbRememberMe != null) {
            cbRememberMe.setChecked(true);
        }

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stay_logged_in);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.88);
            dialog.getWindow().setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setDimAmount(0.6f);
        }

        TextView btnCancel = dialog.findViewById(R.id.btnModalCancel);
        TextView btnNotNow = dialog.findViewById(R.id.btnModalNotNow);
        if (btnNotNow != null) btnNotNow.setOnClickListener(v -> dialog.dismiss());
        TextView btnStayLoggedIn = dialog.findViewById(R.id.btnModalStayLoggedIn);

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> dialog.dismiss());
        }

        if (btnStayLoggedIn != null) {
            btnStayLoggedIn.setOnClickListener(v -> {
                dialog.dismiss();
                Toast.makeText(this, "Verifying credentials...", Toast.LENGTH_SHORT).show();
                attemptLogin(username, password, true);
            });
        }

        try {
            dialog.show();
        } catch (Exception ignored) {}
    }

    private void showLoginLoading(String message) {
        if (isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())) {
            return;
        }
            loadingDialog = new Dialog(this);
            loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loadingDialog.setContentView(R.layout.dialog_login_loading);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.setOnCancelListener(d -> hideLoginLoading());
            if (loadingDialog.getWindow() != null) {
                loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                loadingDialog.getWindow().setLayout(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
                loadingDialog.getWindow().setDimAmount(0.6f);
            }
        TextView tv = loadingDialog.findViewById(R.id.tvLoadingText);
        if (tv != null && message != null) {
            tv.setText(message);
        }
        if (!loadingDialog.isShowing()) {
            try {
                loadingDialog.show();
            } catch (Exception ignored) {}
        }
    }

    private void hideLoginLoading() {
        if (loginTimeoutRunnable != null) {
            loginHandler.removeCallbacks(loginTimeoutRunnable);
        }
        if (loadingDialog != null && loadingDialog.isShowing()) {
            try {
                loadingDialog.dismiss();
            } catch (Exception ignored) {}
        }
        if (btnLogin != null) {
            btnLogin.setEnabled(true);
            updateLoginButtonState();
        }
    }

    private void attemptLogin(final String username, final String password, final boolean rememberMe) {
        Log.d("LoginActivity", "Attempting login for ID: " + username);

        Button btnLogin = findViewById(R.id.btnLogin);
        if (btnLogin != null) {
            btnLogin.setEnabled(false);
        }
        showLoginLoading("Verifying credentials...");

        // Safety timeout to prevent infinite hang under any network condition
        loginTimeoutRunnable = () -> {
            hideLoginLoading();
            Toast.makeText(LoginActivity.this, "Connection timed out. Please check your internet connection.", Toast.LENGTH_LONG).show();
        };
        loginHandler.postDelayed(loginTimeoutRunnable, 7000);

        final boolean[] responded = {false};

        ApiClient.login(LoginActivity.this, username, password, new ApiClient.ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                if (responded[0]) return;
                responded[0] = true;
                hideLoginLoading();
                validateAndLogin(user, username, password, rememberMe);
            }

            @Override
            public void onError(String errorMessage) {
                if (responded[0]) return;
                responded[0] = true;
                hideLoginLoading();
                Toast.makeText(LoginActivity.this, errorMessage != null ? errorMessage : "Invalid user ID or password", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void validateAndLogin(User user, String username, String password, boolean rememberMe) {
        if (user == null) {
            Toast.makeText(this, "User ID not found in database", Toast.LENGTH_LONG).show();
            return;
        }

        // Ensure password field exists in database under this ID
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            Toast.makeText(this, "Password missing in database for this ID", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.equals(user.getPassword())) {
            ApiClient.setCachedUser(this, user);
            saveLoginCredentials(username, password, rememberMe);

            Intent intent = new Intent(LoginActivity.this, SMSVerificationActivity.class);
            intent.putExtra("USERNAME", username);
            intent.putExtra("PASSWORD", password);
            intent.putExtra("REMEMBER_ME", rememberMe);
            intent.putExtra("PHONE_NUMBER", user.getPhone_number());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getCurrentUsername(Context context) {
        if (context == null) return "";
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).getString(KEY_USERNAME, "");
    }

    public static boolean isLoggedIn(Context context) {
        return SessionManager.isSessionValid(context);
    }

    private void loadSavedCredentials() {
        if (sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)) {
            etUsername.setText(sharedPreferences.getString(KEY_USERNAME, ""));
            etPassword.setText(sharedPreferences.getString(KEY_PASSWORD, ""));
            cbRememberMe.setChecked(true);
        }
        updateLoginButtonState();
    }

    private void setupInputWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                updateLoginButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        if (etUsername != null) etUsername.addTextChangedListener(watcher);
        if (etPassword != null) etPassword.addTextChangedListener(watcher);
        updateLoginButtonState();
    }

    private void updateLoginButtonState() {
        if (btnLogin == null || etUsername == null || etPassword == null) return;
        boolean hasUser = !etUsername.getText().toString().trim().isEmpty();
        boolean hasPass = !etPassword.getText().toString().trim().isEmpty();
        boolean isDark = ThemeHelper.isDarkMode(this);
        if (hasUser && hasPass) {
            int bgColor = isDark ? Color.parseColor("#72D09D") : Color.parseColor("#00663D");
            int textColor = isDark ? Color.parseColor("#082A18") : Color.parseColor("#FFFFFF");
            btnLogin.setBackgroundTintList(android.content.res.ColorStateList.valueOf(bgColor));
            btnLogin.setTextColor(textColor);
        } else {
            int bgColor = isDark ? Color.parseColor("#587E6C") : Color.parseColor("#739E8A");
            int textColor = isDark ? Color.parseColor("#082A18") : Color.parseColor("#FFFFFF");
            btnLogin.setBackgroundTintList(android.content.res.ColorStateList.valueOf(bgColor));
            btnLogin.setTextColor(textColor);
        }
    }

    public void saveLoginCredentials(String user, String pass, boolean remember) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putLong(SessionManager.KEY_LOGIN_TIMESTAMP, System.currentTimeMillis());
        if (remember) {
            editor.putString(KEY_PASSWORD, pass);
            editor.putBoolean(KEY_REMEMBER_ME, true);
        } else {
            editor.remove(KEY_PASSWORD);
            editor.putBoolean(KEY_REMEMBER_ME, false);
        }
        editor.apply();
    }

    private void setupFocusChangeListeners() {
        View.OnFocusChangeListener listener = (v, hasFocus) -> {
            if (usernameContainer == null || passwordContainer == null) return;
            if (v == etUsername) usernameContainer.setBackgroundResource(hasFocus ? R.drawable.bg_input_pill_focused : R.drawable.bg_input_pill_normal);
            if (v == etPassword) passwordContainer.setBackgroundResource(hasFocus ? R.drawable.bg_input_pill_focused : R.drawable.bg_input_pill_normal);
        };
        if (etUsername != null) etUsername.setOnFocusChangeListener(listener);
        if (etPassword != null) etPassword.setOnFocusChangeListener(listener);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_right_smooth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            new UpdateChecker(this).checkForUpdate();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoginLoading();
    }
}
