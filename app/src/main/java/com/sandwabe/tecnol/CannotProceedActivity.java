package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CannotProceedActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final String DEFAULT_TITLE = "Cannot Proceed";
    public static final String DEFAULT_PASSPORT_MESSAGE = 
            "Sorry! You cannot update the passport information. The expiration date of the old passport is greater than 1 year.";

    public static void start(Context context, String title, String message) {
        Intent intent = new Intent(context, CannotProceedActivity.class);
        if (title != null) intent.putExtra(EXTRA_TITLE, title);
        if (message != null) intent.putExtra(EXTRA_MESSAGE, message);
        context.startActivity(intent);
    }

    public static void startPassportUpdateError(Context context) {
        start(context, DEFAULT_TITLE, DEFAULT_PASSPORT_MESSAGE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_cannot_proceed);

        ServiceNavHelper.setupBack(this, R.id.ivClose);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvSubtitle = findViewById(R.id.tvSubtitle);

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title != null && !title.trim().isEmpty()) {
            if (tvTitle != null) tvTitle.setText(title);
        } else {
            if (tvTitle != null) tvTitle.setText(DEFAULT_TITLE);
        }

        String message = getIntent().getStringExtra(EXTRA_MESSAGE);
        if (message != null && !message.trim().isEmpty()) {
            if (tvSubtitle != null) tvSubtitle.setText(message);
        } else {
            if (tvSubtitle != null) tvSubtitle.setText(DEFAULT_PASSPORT_MESSAGE);
        }
    }
}
