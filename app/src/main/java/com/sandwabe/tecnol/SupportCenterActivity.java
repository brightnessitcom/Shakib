package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SupportCenterActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_support_center);

        View ivBack = findViewById(R.id.ivBack);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        TextView tvTermsFooter = findViewById(R.id.tvTermsFooter);
        if (tvTermsFooter != null) {
            int greenColor = androidx.core.content.ContextCompat.getColor(this, R.color.icon_tint_primary);
            String greenHex = String.format("#%06X", (0xFFFFFF & greenColor));
            tvTermsFooter.setText(Html.fromHtml("Read the <font color='" + greenHex + "'>Terms &amp; Conditions</font> that apply to contacting us."));
        }

        View rowSupportRequests = findViewById(R.id.rowSupportRequests);
        if (rowSupportRequests != null) {
            rowSupportRequests.setOnClickListener(v ->
                    Toast.makeText(this, "No active support requests", Toast.LENGTH_SHORT).show()
            );
        }

        View rowCallSupport = findViewById(R.id.rowCallSupport);
        if (rowCallSupport != null) {
            rowCallSupport.setOnClickListener(v -> {
                try {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:920020405"));
                    startActivity(dialIntent);
                } catch (Exception ignored) {
                }
            });
        }

        View rowFacebook = findViewById(R.id.rowFacebook);
        if (rowFacebook != null) {
            rowFacebook.setOnClickListener(v -> openUrl("https://www.facebook.com/AbsherKSA"));
        }

        View rowTwitter = findViewById(R.id.rowTwitter);
        if (rowTwitter != null) {
            rowTwitter.setOnClickListener(v -> openUrl("https://x.com/Absher"));
        }

        View rowSnapchat = findViewById(R.id.rowSnapchat);
        if (rowSnapchat != null) {
            rowSnapchat.setOnClickListener(v -> openUrl("https://www.snapchat.com/add/absher.ksa"));
        }
    }

    private void openUrl(String url) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        } catch (Exception ignored) {
        }
    }
}
