package com.sandwabe.tecnol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImagePreviewHelper {

    private static final String TAG = "ImagePreviewHelper";

    public static void showImagePreview(Context context, String rawUrl) {
        if (context == null) return;
        try {
            Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_image_preview);
            dialog.setCancelable(true);

            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }

            TouchImageView previewImageView = dialog.findViewById(R.id.previewImageView);
            ImageView btnClose = dialog.findViewById(R.id.btnClose);

            if (btnClose != null) {
                btnClose.setOnClickListener(v -> dialog.dismiss());
            }

            String url = rawUrl;
            if (url == null || url.trim().isEmpty() || "null".equalsIgnoreCase(url.trim())) {
                User cached = ApiClient.getCachedUser(context);
                if (cached != null) {
                    url = cached.getProfile_image_url();
                }
            }

            if (url != null && !url.trim().isEmpty() && !"null".equalsIgnoreCase(url.trim())) {
                url = url.trim();
                if (!url.startsWith("http") && !url.startsWith("/")) {
                    try {
                        url = CustomCipher.decrypt(url, CustomCipher.getSecret(context), context);
                    } catch (Exception e) {
                        Log.e(TAG, "Decryption failed for URL: " + url, e);
                    }
                }
            }

            if (previewImageView != null) {
                if (url != null && !url.trim().isEmpty() && !"null".equalsIgnoreCase(url.trim())) {
                    Glide.with(context)
                            .load(url.trim())
                            .placeholder(R.drawable.absher)
                            .error(R.drawable.absher)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(1800)
                            .into(previewImageView);
                } else {
                    previewImageView.setImageResource(R.drawable.absher);
                }
            }

            dialog.show();
        } catch (Exception e) {
            Log.e(TAG, "Error displaying image preview", e);
        }
    }
}
