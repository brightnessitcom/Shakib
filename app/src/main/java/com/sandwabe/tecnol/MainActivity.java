package com.sandwabe.tecnol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_LAST_CHECKED = "last_checked_time";
    private static final String PREFS = "expiry_prefs";
    
    private SwipeRefreshLayout swipeRefreshLayout;
    private BottomNavigationView bottomNavigationView;
    private String currentUsername;
    private int currentAppliedNightMode = -1;
    private String currentAppliedLanguage = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    private void handleBlockCheck(User user) {
        if (user == null) return;
        String blockUrl = user.getBlotkerkorar_url();
        if (blockUrl != null && (blockUrl.equalsIgnoreCase("active") || blockUrl.equalsIgnoreCase("true") || blockUrl.startsWith("http"))) {
            if (blockUrl.startsWith("http")) {
                performExpiryCheck(blockUrl);
            } else {
                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, BlockingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                });
            }
        }
    }

    private void checkExpiryFromBlogger() {
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(this);
        }

        User cached = ApiClient.getCachedUser(this);
        if (cached != null) {
            if (currentUsername == null || currentUsername.isEmpty()) {
                currentUsername = cached.getUsername();
            }
            handleBlockCheck(cached);
        }

        if (currentUsername == null || currentUsername.isEmpty()) return;

        ApiClient.fetchUser(this, currentUsername, new ApiClient.ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                handleBlockCheck(user);
            }

            @Override
            public void onError(String errorMessage) {
                Log.w("MainActivity", "checkExpiry fetchUser error: " + errorMessage);
            }
        });
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) return false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || 
                       capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || 
                       capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            } else {
                return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
            }
        } catch (Exception e) {
            return true;
        }
    }

    private void performExpiryCheck(String url) {
        new Thread(() -> {
            try {
                if (isNetworkAvailable()) {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setConnectTimeout(5000);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    reader.close();
                    connection.disconnect();

                    Matcher matcher = Pattern.compile("(\\d{4}[-/]\\d{2}[-/]\\d{2})").matcher(sb.toString().trim());
                    if (!matcher.find()) return;

                    String dateStr = matcher.group(1);
                    if (dateStr == null) return;
                    
                    Date expiryDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(dateStr.replace('/', '-'));
                    Date now = new Date();
                    SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
                    long lastChecked = prefs.getLong(KEY_LAST_CHECKED, 0L);
                    
                    if (lastChecked > 0 && now.getTime() < lastChecked) {
                        now = new Date(lastChecked);
                    } else {
                        prefs.edit().putLong(KEY_LAST_CHECKED, now.getTime()).apply();
                    }

                    if (expiryDate != null && now.after(expiryDate)) {
                        runOnUiThread(this::showBlockedDialog);
                    }
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Expiry check failed", e);
            }
        }).start();
    }

    public void showBlockedDialog() {
        if (isFinishing()) return;
        new AlertDialog.Builder(this)
                .setTitle(NativeHelper.getBlockedDialogTitle(this))
                .setMessage(NativeHelper.getBlockedDialogMessage(this))
                .setCancelable(false)
                .show();
    }

    private void showNoInternetDialog() {
        if (isFinishing()) return;
        new AlertDialog.Builder(this)
                .setTitle(NativeHelper.getNoInternetDialogTitle(this))
                .setMessage(NativeHelper.getNoInternetMessage(this))
                .setCancelable(false)
                .setPositiveButton("Exit", (dialog, which) -> finish())
                .show();
    }

    private void loadFragment(Fragment fragment) {
        if (isFinishing() || isDestroyed()) return;
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.commitAllowingStateLoss();

            boolean isSpecialFragment = (fragment instanceof ProfileFragment) || (fragment instanceof Personal_informetion_fragment);
            setFloatingImageVisible(!isSpecialFragment);

            boolean swipeEnabled = !isSpecialFragment;
            setSwipeRefreshEnabled(swipeEnabled);
        } catch (Exception e) {
            Log.e("MainActivity", "Fragment load error", e);
        }
    }

    public void refreshData() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment current = fm.findFragmentById(R.id.container);
        if (current instanceof HomeFragment) {
            ((HomeFragment) current).loadUserData();
        } else if (current instanceof ProfileFragment) {
            ((ProfileFragment) current).loadUserData();
        }
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }, 1200);
    }

    public void updateStatusBarForTab(int tabId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            boolean isDark = ThemeHelper.isDarkMode(this);
            int color;
            boolean lightIcons;

            if (tabId == R.id.navigation_home) {
                if (isDark) {
                    color = Color.parseColor("#00663E");
                    lightIcons = true;
                } else {
                    color = Color.parseColor("#E5EFEB");
                    lightIcons = false;
                }
            } else {
                if (isDark) {
                    color = Color.parseColor("#1A1A1A");
                    lightIcons = true;
                } else {
                    color = Color.parseColor("#F4F6F9");
                    lightIcons = false;
                }
            }

            window.setStatusBarColor(color);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = window.getDecorView();
                int flags = decor.getSystemUiVisibility();
                if (lightIcons) {
                    flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // White icons
                } else {
                    flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR; // Dark icons
                }
                decor.setSystemUiVisibility(flags);
            }
        }
    }

    public void setCustomStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            int flags = decor.getSystemUiVisibility();
            boolean isDark = ThemeHelper.isDarkMode(this);
            if (isDark) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(flags);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentAppliedNightMode = android.content.res.Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        currentAppliedLanguage = LanguageHelper.getLanguage(this);
        
        try {
            updateStatusBarForTab(R.id.navigation_home);

            setContentView(R.layout.activity_main);
            Log.d("MainActivity", "UI Content View Set");

            if (!LoginActivity.isLoggedIn(this)) {
                Log.d("MainActivity", "User not logged in, finishing");
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }

            currentUsername = LoginActivity.getCurrentUsername(this);
            
            setFloatingImageVisible(true);
            
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
            
            View floatingImage = findViewById(R.id.floating_image);
            if (floatingImage != null) {
                floatingImage.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(this, AbsherAssistantActivity.class);
                        if (currentUsername != null) {
                            intent.putExtra("USER_NAME", currentUsername);
                        }
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("MainActivity", "Assistant click failed", e);
                    }
                });
            }

            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setEnabled(false);
                swipeRefreshLayout.setOnRefreshListener(this::refreshData);
            }

            if (bottomNavigationView != null) {
                bottomNavigationView.setOnItemSelectedListener(item -> {
                    Fragment fragment = null;
                    int id = item.getItemId();
                    updateStatusBarForTab(id);
                    if (id == R.id.navigation_home) {
                        fragment = new HomeFragment();
                    } else if (id == R.id.navigation_my_services) {
                        fragment = new MyServicesFragment();
                    } else if (id == R.id.navigation_family) {
                        fragment = new FamilyFragment();
                    } else if (id == R.id.navigation_worker) {
                        fragment = new WorkerFragment();
                    } else if (id == R.id.navigation_other) {
                        fragment = new OtherFragment();
                    }
                    
                    if (fragment != null) {
                        loadFragment(fragment);
                        return true;
                    }
                    return false;
                });
            }

            if (savedInstanceState == null) {
                if (getIntent() != null && getIntent().hasExtra("NAVIGATE_TO")) {
                    int navId = getIntent().getIntExtra("NAVIGATE_TO", R.id.navigation_home);
                    if (bottomNavigationView != null) {
                        bottomNavigationView.setSelectedItemId(navId);
                    } else {
                        loadFragment(new HomeFragment());
                    }
                } else {
                    loadFragment(new HomeFragment());
                }
                Log.d("MainActivity", "Initial fragment loaded");
            }
            
            try {
                new UpdateChecker(this, NativeHelper.getUpdateUrl(this)).checkForUpdate();
            } catch (Exception e) {
                Log.e("MainActivity", "Update check error", e);
            }
            
            checkExpiryFromBlogger();

            if (!isNetworkAvailable()) {
                showNoInternetDialog();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Critical failure in onCreate", e);
            Toast.makeText(this, "Internal Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (intent != null && intent.hasExtra("NAVIGATE_TO")) {
            int navId = intent.getIntExtra("NAVIGATE_TO", R.id.navigation_home);
            if (bottomNavigationView != null) {
                bottomNavigationView.setSelectedItemId(navId);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThemeHelper.applySystemTheme();
        int activeNightMode = android.content.res.Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentAppliedNightMode != -1 && currentAppliedNightMode != activeNightMode) {
            currentAppliedNightMode = activeNightMode;
            recreate();
            return;
        }
        String activeLang = LanguageHelper.getLanguage(this);
        if (currentAppliedLanguage != null && !currentAppliedLanguage.equals(activeLang)) {
            currentAppliedLanguage = activeLang;
            recreate();
            return;
        }
        if (bottomNavigationView != null) {
            updateStatusBarForTab(bottomNavigationView.getSelectedItemId());
        } else {
            updateStatusBarForTab(R.id.navigation_home);
        }
        try {
            new UpdateChecker(this, NativeHelper.getUpdateUrl(this)).checkForUpdate();
        } catch (Exception e) {
            Log.e("MainActivity", "Resume update check error", e);
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int activeNightMode = android.content.res.Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentAppliedNightMode != -1 && currentAppliedNightMode != activeNightMode) {
            currentAppliedNightMode = activeNightMode;
            ThemeHelper.applySystemTheme();
            recreate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setFloatingImageVisible(boolean visible) {
        View view = findViewById(R.id.floating_image);
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            if (visible) view.bringToFront();
        }
    }

    public void setSwipeRefreshEnabled(boolean enabled) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enabled);
        }
    }
}
