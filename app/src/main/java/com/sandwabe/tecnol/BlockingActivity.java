package com.sandwabe.tecnol;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

public class BlockingActivity extends Activity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    public void onBackPressed() {
        // Disabled
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        new AlertDialog.Builder(this)
                .setTitle("Security Alert")
                .setMessage("This app has been tampered with and cannot run.")
                .setCancelable(false)
                .setPositiveButton("Exit", (dialog, which) -> {
                    finishAffinity();
                    System.exit(0);
                })
                .show();
    }
}
