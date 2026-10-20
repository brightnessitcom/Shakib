package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;

public class PreLoginActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dynamic status bar styling: Light theme (#E5EFEB with dark icons) vs Dark theme (#00663D with white icons)
        boolean isDark = ThemeHelper.isDarkMode(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(isDark ? Color.parseColor("#00663D") : Color.parseColor("#E5EFEB"));
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

        setContentView(R.layout.activity_pre_login);

        HorizontalScrollView hsv = findViewById(R.id.hsvPublicServices);
        if (hsv != null) {
            hsv.post(() -> hsv.scrollTo(0, 0));
        }

        // Login buttons
        View btnLogin = findViewById(R.id.btnLogin);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                String currentUsername = LoginActivity.getCurrentUsername(this);
                boolean hasSavedUser = (currentUsername != null && !currentUsername.trim().isEmpty())
                        || (ApiClient.getCachedUser(this) != null);
                if (hasSavedUser && BiometricActivity.shouldUseBiometric(this)) {
                    startActivity(new Intent(this, BiometricActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_right_smooth, R.anim.stay);
                }
            });
        }

        View btnLoginNafath = findViewById(R.id.btnLoginNafath);
        if (btnLoginNafath != null) {
            btnLoginNafath.setOnClickListener(v -> showStayLoggedInModal());
        }

        // Top actions
        View ivSettings = findViewById(R.id.ivSettings);
        if (ivSettings != null) {
            ivSettings.setOnClickListener(v -> startActivity(new Intent(this, PreLoginSettingsActivity.class)));
        }

        View ivNotifications = findViewById(R.id.ivNotifications);
        if (ivNotifications != null) {
            ivNotifications.setOnClickListener(v -> {
                Intent intent = new Intent(this, AnnouncementsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right_smooth, R.anim.stay);
            });
        }

        // Ensure "Public Services" text is boldly rendered across all custom system fonts
        TextView tvPublicServices = findViewById(R.id.tvPublicServices);
        if (tvPublicServices != null) {
            tvPublicServices.setTypeface(null, Typeface.BOLD);
            tvPublicServices.getPaint().setFakeBoldText(true);
        }

        // See all public services
        TextView tvSeeAll = findViewById(R.id.tvSeeAll);
        if (tvSeeAll != null) {
            tvSeeAll.setOnClickListener(v -> {
                startActivity(new Intent(this, PublicServicesActivity.class));
                overridePendingTransition(R.anim.slide_in_right_smooth, R.anim.stay);
            });
        }

        // Public service cards
        View cardManageIdentity = findViewById(R.id.cardManageIdentity);
        if (cardManageIdentity != null) {
            cardManageIdentity.setOnClickListener(v -> startActivity(new Intent(this, ManageDigitalIdentityMenuActivity.class)));
        }

        View cardAuthServices = findViewById(R.id.cardAuthServices);
        if (cardAuthServices != null) {
            cardAuthServices.setOnClickListener(v -> startActivity(new Intent(this, AuthenticationServicesActivity.class)));
        }

        View cardTravelVisitor = findViewById(R.id.cardTravelVisitor);
        if (cardTravelVisitor != null) {
            cardTravelVisitor.setOnClickListener(v -> startActivity(new Intent(this, CreateTravelRequestActivity.class)));
        }

        View cardViewDocuments = findViewById(R.id.cardViewDocuments);
        if (cardViewDocuments != null) {
            cardViewDocuments.setOnClickListener(v -> {
                String lastUser = LoginActivity.getCurrentUsername(this);
                User cached = ApiClient.getCachedUser(this);
                if ((lastUser == null || lastUser.trim().isEmpty()) && cached == null) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    startActivity(new Intent(this, DigitalDocumentsActivity.class));
                }
            });
        }

        View cardCivilAppointment = findViewById(R.id.cardCivilAppointment);
        if (cardCivilAppointment != null) {
            cardCivilAppointment.setOnClickListener(v -> startActivity(new Intent(this, AppointmentsActivity.class)));
        }

        View cardVisitorDoc = findViewById(R.id.cardVisitorDoc);
        if (cardVisitorDoc != null) {
            cardVisitorDoc.setOnClickListener(v -> startActivity(new Intent(this, VisitorDigitalDocumentActivity.class)));
        }

        View cardPassportAppt = findViewById(R.id.cardPassportAppt);
        if (cardPassportAppt != null) {
            cardPassportAppt.setOnClickListener(v -> startActivity(new Intent(this, AppointmentsActivity.class)));
        }

        View cardActivationSites = findViewById(R.id.cardActivationSites);
        if (cardActivationSites != null) {
            cardActivationSites.setOnClickListener(v -> startActivity(new Intent(this, ActivationSitesActivity.class)));
        }

        View cardAuthenticator = findViewById(R.id.cardAuthenticator);
        if (cardAuthenticator != null) {
            cardAuthenticator.setOnClickListener(v -> startActivity(new Intent(this, AbsherAuthenticatorActivity.class)));
        }

        try {
            new UpdateChecker(this).checkForUpdate();
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            new UpdateChecker(this).checkForUpdate();
        } catch (Exception ignored) {
        }
    }

    private void showStayLoggedInModal() {
        if (isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isDestroyed())) {
            return;
        }

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_stay_logged_in);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            int dialogWidth = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
            dialog.getWindow().setLayout(dialogWidth, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setDimAmount(0.6f);
        }

        TextView btnStayLoggedIn = dialog.findViewById(R.id.btnModalStayLoggedIn);
        TextView btnNotNow = dialog.findViewById(R.id.btnModalNotNow);
        TextView btnCancel = dialog.findViewById(R.id.btnModalCancel);

        if (btnStayLoggedIn != null) {
            btnStayLoggedIn.setOnClickListener(v -> {
                dialog.dismiss();
                showNafathLoading();
            });
        }

        if (btnNotNow != null) {
            btnNotNow.setOnClickListener(v -> dialog.dismiss());
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> dialog.dismiss());
        }

        try {
            dialog.show();
        } catch (Exception ignored) {}
    }

    private void showNafathLoading() {
        Dialog loadingDialog = new Dialog(this);
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(R.layout.dialog_login_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        if (loadingDialog.getWindow() != null) {
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            loadingDialog.getWindow().setDimAmount(0.6f);
        }
        loadingDialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (!isFinishing() && !isDestroyed()) {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                Intent intent = new Intent(this, NafathActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        }, 700);
    }
}
