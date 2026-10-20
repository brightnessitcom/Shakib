package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AuthenticationRequestsActivity extends AppCompatActivity {

    private int selectedOption = 1; // 1: ID Number, 2: GCC, 3: Non-GCC

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_authentication_requests);

        ServiceNavHelper.setupBack(this, R.id.ivClose);

        View optIdNumber = findViewById(R.id.optIdNumber);
        View optGcc = findViewById(R.id.optGcc);
        View optNonGcc = findViewById(R.id.optNonGcc);

        ImageView radioIdNumber = findViewById(R.id.radioIdNumber);
        ImageView radioGcc = findViewById(R.id.radioGcc);
        ImageView radioNonGcc = findViewById(R.id.radioNonGcc);

        View.OnClickListener listener = v -> {
            if (v == optIdNumber) selectedOption = 1;
            else if (v == optGcc) selectedOption = 2;
            else if (v == optNonGcc) selectedOption = 3;

            int activeColor = ThemeHelper.isDarkMode(this) ? 0xFF72D09D : 0xFF00663D;
            int inactiveColor = 0xFF8A9095;

            radioIdNumber.setImageResource(selectedOption == 1 ? R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
            radioIdNumber.setColorFilter(selectedOption == 1 ? activeColor : inactiveColor);

            radioGcc.setImageResource(selectedOption == 2 ? R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
            radioGcc.setColorFilter(selectedOption == 2 ? activeColor : inactiveColor);

            radioNonGcc.setImageResource(selectedOption == 3 ? R.drawable.ic_radio_selected : R.drawable.ic_radio_unselected);
            radioNonGcc.setColorFilter(selectedOption == 3 ? activeColor : inactiveColor);
        };

        if (optIdNumber != null) optIdNumber.setOnClickListener(listener);
        if (optGcc != null) optGcc.setOnClickListener(listener);
        if (optNonGcc != null) optNonGcc.setOnClickListener(listener);

        findViewById(R.id.btnContinue).setOnClickListener(v -> {
            Toast.makeText(this, "Proceeding with authentication...", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
