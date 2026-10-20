package com.sandwabe.tecnol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Production-Grade Single-Active In-App OTA Update Engine.
 * Guarantees:
 * 1. Only the single currently ACTIVE release on the server is ever displayed.
 * 2. Singleton modal lock: never stacks multiple update dialogs.
 * 3. Instant dismissal if an admin deactivates/pauses the active update on the dashboard.
 * 4. Automatic cleanup of old/stale APK files and pre-install APK integrity verification.
 */
public class UpdateChecker {
    private static final String TAG = "UpdateChecker";
    private static final String PREFS_UPDATE = "update_prefs";
    private static final String KEY_DISMISSED_RELEASE_ID = "dismissed_release_id";
    private static final String KEY_DISMISSED_AT = "dismissed_at_ms";
    private static final String KEY_INSTALLED_RELEASE_ID = "installed_release_id";
    private static final long DISMISS_COOLDOWN_MS = 6 * 60 * 60 * 1000L; // 6 hours for optional updates

    private static volatile boolean isChecking = false;
    private static volatile boolean isDownloading = false;
    private static Dialog activeDialog = null;
    private static String activeDialogReleaseKey = null;

    private final Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final String legacyJsonUrl;

    public UpdateChecker(Context context) {
        this.context = context;
        this.legacyJsonUrl = null;
    }

    public UpdateChecker(Context context, String legacyUrl) {
        this.context = context;
        this.legacyJsonUrl = legacyUrl;
    }

    public static long getAppVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return pInfo.getLongVersionCode();
            } else {
                return pInfo.versionCode;
            }
        } catch (Exception e) {
            return 20;
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName != null ? pInfo.versionName : "1.2.0";
        } catch (Exception e) {
            return "1.2.0";
        }
    }

    public void checkForUpdate() {
        if (isChecking || isDownloading) return;
        isChecking = true;

        new Thread(() -> {
            try {
                long currentCode = getAppVersionCode(context);
                List<String> candidates = ApiClient.getCandidateBaseUrls(context);

                attemptCheckCandidate(candidates, 0, currentCode);
            } catch (Exception e) {
                Log.e(TAG, "Update check initiated error", e);
                isChecking = false;
            }
        }).start();
    }

    private String getInstalledReleaseId() {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_UPDATE, Context.MODE_PRIVATE);
            return prefs.getString(KEY_INSTALLED_RELEASE_ID, "");
        } catch (Exception ignored) {
            return "";
        }
    }

    private void recordInstalledReleaseId(String releaseId) {
        if (releaseId == null || releaseId.isEmpty()) return;
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_UPDATE, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_INSTALLED_RELEASE_ID, releaseId).apply();
        } catch (Exception ignored) {
        }
    }

    private void attemptCheckCandidate(List<String> candidates, int index, long currentCode) {
        if (index >= candidates.size()) {
            isChecking = false;
            return;
        }

        String baseUrl = candidates.get(index);
        String installedReleaseId = getInstalledReleaseId();
        String url = baseUrl + "/api/v1/mobile/check-update?versionCode=" + currentCode
                + "&installedReleaseId=" + Uri.encode(installedReleaseId)
                + "&t=" + System.currentTimeMillis();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .readTimeout(4, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("User-Agent", "AbsherAndroid/OTA")
                .addHeader("Cache-Control", "no-cache, no-store, must-revalidate")
                .addHeader("Pragma", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                attemptCheckCandidate(candidates, index + 1, currentCode);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody body = response.body()) {
                    if (!response.isSuccessful() || body == null) {
                        attemptCheckCandidate(candidates, index + 1, currentCode);
                        return;
                    }

                    String jsonStr = body.string();
                    JSONObject json = new JSONObject(jsonStr);

                    boolean hasUpdate = json.optBoolean("hasUpdate", false);
                    boolean isActive = json.optBoolean("isActive", true);

                    if (!hasUpdate || !isActive) {
                        isChecking = false;
                        // If admin paused/deactivated the update or user is already on the active version,
                        // dismiss any stale dialog and clean up old APK files.
                        handler.post(() -> {
                            dismissActiveDialogIfAny();
                            cleanupOldApkFiles();
                        });
                        return;
                    }

                    int targetVersionCode = json.optInt("versionCode", 0);
                    String releaseId = json.optString("releaseId", "v" + targetVersionCode);
                    String priority = json.optString("updatePriority", "RECOMMENDED");
                    String versionName = json.optString("versionName", "1.2.1");
                    String title = json.optString("title", "New Update Available");
                    String releaseNotes = json.optString("releaseNotes", "Bug fixes and performance improvements.");
                    String apkUrl = json.optString("apkUrl", "");
                    String apkSize = json.optString("apkSize", "6.2 MB");

                    if (apkUrl.isEmpty() || targetVersionCode <= 0) {
                        isChecking = false;
                        return;
                    }

                    // If this exact releaseId was already downloaded & installed on this device, do not re-prompt
                    if (!installedReleaseId.isEmpty() && Objects.equals(releaseId, installedReleaseId)) {
                        isChecking = false;
                        return;
                    }

                    final boolean isForce = "FORCE".equalsIgnoreCase(priority);
                    final String releaseKey = releaseId + "_" + targetVersionCode + "_" + priority;

                    // If this is an optional (RECOMMENDED) update and the user already tapped "Later" recently
                    // for this exact releaseId + versionCode, respect their choice unless the admin changed it to FORCE.
                    if (!isForce && isRecentlyDismissed(releaseKey)) {
                        isChecking = false;
                        return;
                    }

                    isChecking = false;
                    handler.post(() -> showUpdateModal(releaseId, releaseKey, targetVersionCode, title, versionName, priority, releaseNotes, apkSize, apkUrl));
                } catch (Exception e) {
                    Log.e(TAG, "Parse error on " + baseUrl, e);
                    attemptCheckCandidate(candidates, index + 1, currentCode);
                }
            }
        });
    }

    private boolean isRecentlyDismissed(String releaseKey) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_UPDATE, Context.MODE_PRIVATE);
            String dismissedKey = prefs.getString(KEY_DISMISSED_RELEASE_ID, null);
            long dismissedAt = prefs.getLong(KEY_DISMISSED_AT, 0L);
            if (Objects.equals(releaseKey, dismissedKey)) {
                return (System.currentTimeMillis() - dismissedAt) < DISMISS_COOLDOWN_MS;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    private void recordDismissed(String releaseKey) {
        try {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_UPDATE, Context.MODE_PRIVATE);
            prefs.edit()
                    .putString(KEY_DISMISSED_RELEASE_ID, releaseKey)
                    .putLong(KEY_DISMISSED_AT, System.currentTimeMillis())
                    .apply();
        } catch (Exception ignored) {
        }
    }

    private static void dismissActiveDialogIfAny() {
        try {
            if (activeDialog != null && activeDialog.isShowing()) {
                activeDialog.dismiss();
            }
        } catch (Exception ignored) {
        } finally {
            activeDialog = null;
            activeDialogReleaseKey = null;
        }
    }

    private void cleanupOldApkFiles() {
        try {
            File targetDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            if (targetDir == null) targetDir = context.getCacheDir();
            if (targetDir != null && targetDir.exists()) {
                File[] files = targetDir.listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.getName().startsWith("absher_update")) {
                            f.delete();
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Show custom Absher-styled update modal (strictly 1 modal at a time)
     */
    private void showUpdateModal(String releaseId, String releaseKey, int targetVersionCode, String title, String versionName,
                                 String priority, String releaseNotes, String apkSize, String apkUrl) {
        if (!(context instanceof Activity)) return;
        Activity activity = (Activity) context;
        if (activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return;
        }

        // If the exact same release modal is already showing on screen, do not duplicate it
        if (activeDialog != null && activeDialog.isShowing() && Objects.equals(activeDialogReleaseKey, releaseKey)) {
            return;
        }

        // Dismiss any older/previous update modal so two modals NEVER overlap
        dismissActiveDialogIfAny();

        final boolean isForce = "FORCE".equalsIgnoreCase(priority);

        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_app_update, null);
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(
                    (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90),
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setDimAmount(0.65f);
        }

        dialog.setCancelable(!isForce);
        dialog.setCanceledOnTouchOutside(!isForce);

        TextView tvTitle = view.findViewById(R.id.tvUpdateTitle);
        TextView tvVersion = view.findViewById(R.id.tvVersionBadge);
        TextView tvPriority = view.findViewById(R.id.tvPriorityBadge);
        TextView tvSize = view.findViewById(R.id.tvSizeBadge);
        TextView tvChangelog = view.findViewById(R.id.tvChangelog);
        LinearLayout layoutButtons = view.findViewById(R.id.layoutButtons);
        LinearLayout layoutProgress = view.findViewById(R.id.layoutProgress);
        ProgressBar progressBar = view.findViewById(R.id.updateProgressBar);
        TextView tvProgressText = view.findViewById(R.id.tvProgressText);
        Button btnUpdateNow = view.findViewById(R.id.btnUpdateNow);
        Button btnLater = view.findViewById(R.id.btnLater);

        if (tvTitle != null) tvTitle.setText(title);
        if (tvVersion != null) tvVersion.setText("v" + versionName);
        if (tvSize != null) tvSize.setText(apkSize);
        if (tvChangelog != null) tvChangelog.setText(releaseNotes);

        if (tvPriority != null) {
            if (isForce) {
                tvPriority.setText("Required");
                tvPriority.setTextColor(Color.parseColor("#B91C1C"));
                tvPriority.setBackgroundColor(Color.parseColor("#FEE2E2"));
            } else {
                tvPriority.setText("Recommended");
                tvPriority.setTextColor(Color.parseColor("#047857"));
                tvPriority.setBackgroundColor(Color.parseColor("#E6F4EA"));
            }
        }

        if (isForce) {
            if (btnLater != null) btnLater.setVisibility(View.GONE);
        } else {
            if (btnLater != null) {
                btnLater.setVisibility(View.VISIBLE);
                btnLater.setOnClickListener(v -> {
                    recordDismissed(releaseKey);
                    dismissActiveDialogIfAny();
                });
            }
        }

        if (btnUpdateNow != null) {
            btnUpdateNow.setOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!activity.getPackageManager().canRequestPackageInstalls()) {
                        promptInstallPermission(activity);
                        return;
                    }
                }

                layoutButtons.setVisibility(View.GONE);
                layoutProgress.setVisibility(View.VISIBLE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                startDownload(activity, dialog, releaseId, targetVersionCode, apkUrl, progressBar, tvProgressText, layoutButtons, layoutProgress);
            });
        }

        try {
            activeDialog = dialog;
            activeDialogReleaseKey = releaseKey;
            dialog.setOnDismissListener(d -> {
                if (activeDialog == dialog) {
                    activeDialog = null;
                    activeDialogReleaseKey = null;
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, "Failed to show update dialog", e);
        }
    }

    private void promptInstallPermission(Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Permission Required")
                .setMessage("To install the updated version directly, please allow this app to install unknown apps in device settings.")
                .setPositiveButton("Open Settings", (d, w) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        activity.startActivity(new Intent(
                                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                                Uri.parse("package:" + activity.getPackageName())
                        ));
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void startDownload(Activity activity, Dialog dialog, String releaseId, int targetVersionCode, String apkUrl,
                               ProgressBar progressBar, TextView tvProgressText,
                               LinearLayout layoutButtons, LinearLayout layoutProgress) {

        isDownloading = true;
        File targetDir = activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (targetDir == null) targetDir = activity.getCacheDir();
        if (!targetDir.exists()) targetDir.mkdirs();

        // Remove any previous downloaded APK files so old builds never conflict
        cleanupOldApkFiles();

        final File tempFile = new File(targetDir, "absher_update_v" + targetVersionCode + ".apk.tmp");
        final File apkFile = new File(targetDir, "absher_update_v" + targetVersionCode + ".apk");

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(apkUrl)
                        .addHeader("User-Agent", "AbsherAndroid/OTA")
                        .build();

                Response response = client.newCall(request).execute();
                if (!response.isSuccessful() || response.body() == null) {
                    throw new IOException("HTTP error: " + response.code());
                }

                long contentLength = response.body().contentLength();
                InputStream in = response.body().byteStream();
                FileOutputStream out = new FileOutputStream(tempFile);

                byte[] buffer = new byte[8192];
                long downloaded = 0;
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                    downloaded += read;
                    if (contentLength > 0) {
                        final int percent = (int) ((downloaded * 100) / contentLength);
                        handler.post(() -> {
                            if (progressBar != null) progressBar.setProgress(percent);
                            if (tvProgressText != null) tvProgressText.setText("Downloading... " + percent + "%");
                        });
                    }
                }

                out.flush();
                out.close();
                in.close();
                response.close();

                if (apkFile.exists()) apkFile.delete();
                if (!tempFile.renameTo(apkFile)) {
                    throw new IOException("Failed to finalize APK file");
                }

                // Verify APK integrity before launching installer
                PackageManager pm = activity.getPackageManager();
                PackageInfo archiveInfo = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), 0);
                if (archiveInfo == null) {
                    apkFile.delete();
                    throw new IOException("Downloaded file is not a valid Android APK package");
                }

                isDownloading = false;
                recordInstalledReleaseId(releaseId);
                handler.post(() -> {
                    dismissActiveDialogIfAny();
                    installApk(activity, apkFile);
                });

            } catch (Exception e) {
                isDownloading = false;
                if (tempFile.exists()) tempFile.delete();
                Log.e(TAG, "Download error", e);
                handler.post(() -> {
                    layoutButtons.setVisibility(View.VISIBLE);
                    layoutProgress.setVisibility(View.GONE);
                    dialog.setCancelable(true);
                    Toast.makeText(activity, "Download failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    public static void installApk(Context context, File apkFile) {
        if (apkFile == null || !apkFile.exists() || apkFile.length() == 0) {
            Toast.makeText(context, "Downloaded file is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri apkUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    apkFile
            );

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        } catch (Exception e) {
            Log.e(TAG, "Install intent failed", e);
            Toast.makeText(context, "Failed to launch installer: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
