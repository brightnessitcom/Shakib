package com.sandwabe.tecnol;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AbsherDialogHelper {

    public static void show(Context context, String title, String message) {
        if (context == null) return;
        try {
            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_digital_document_expired);
            dialog.setCancelable(true);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.88);
                dialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setDimAmount(0.65f);
            }

            TextView tvTitle = dialog.findViewById(R.id.tvDialogTitle);
            TextView tvMessage = dialog.findViewById(R.id.tvDialogMessage);
            if (tvTitle != null && title != null) {
                tvTitle.setText(title);
            }
            if (tvMessage != null && message != null) {
                tvMessage.setText(message);
            }

            View btnOk = dialog.findViewById(R.id.btnOk);
            if (btnOk != null) {
                btnOk.setOnClickListener(v -> dialog.dismiss());
            }

            dialog.show();
        } catch (Exception e) {
            Log.e("AbsherDialogHelper", "Error displaying dialog", e);
        }
    }
}
