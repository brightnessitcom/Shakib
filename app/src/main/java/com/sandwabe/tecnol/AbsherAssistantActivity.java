package com.sandwabe.tecnol;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class AbsherAssistantActivity extends AppCompatActivity {

    private View layoutConsent;
    private View layoutChat;
    private CheckBox cbConsent;
    private MaterialButton btnStartConversation;
    private LinearLayout llMessagesContainer;
    private ScrollView chatScrollView;
    private EditText etMessage;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_absher_assistant);

        layoutConsent = findViewById(R.id.layoutConsent);
        layoutChat = findViewById(R.id.layoutChat);
        cbConsent = findViewById(R.id.cbConsent);
        btnStartConversation = findViewById(R.id.btnStartConversation);
        llMessagesContainer = findViewById(R.id.llMessagesContainer);
        chatScrollView = findViewById(R.id.chatScrollView);
        etMessage = findViewById(R.id.etMessage);

        // Style "Terms & Conditions." in green (icon_tint_primary)
        TextView tvTermsText = findViewById(R.id.tvTermsText);
        if (tvTermsText != null) {
            String fullText = "Before proceeding read and agree to Absher's Terms & Conditions.";
            SpannableString spannable = new SpannableString(fullText);
            int start = fullText.indexOf("Terms & Conditions.");
            if (start >= 0) {
                int greenColor = ContextCompat.getColor(this, R.color.icon_tint_primary);
                spannable.setSpan(
                        new ForegroundColorSpan(greenColor),
                        start,
                        fullText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
            tvTermsText.setText(spannable);
        }

        // Handle checkbox row click
        View llConsentCheckRow = findViewById(R.id.llConsentCheckRow);
        if (llConsentCheckRow != null && cbConsent != null) {
            llConsentCheckRow.setOnClickListener(v -> {
                cbConsent.setChecked(!cbConsent.isChecked());
                updateStartButtonState(cbConsent.isChecked());
            });
        }

        if (btnStartConversation != null) {
            btnStartConversation.setOnClickListener(v -> {
                if (cbConsent != null && cbConsent.isChecked()) {
                    showChatScreen();
                }
            });
        }

        // Restore state if recreated
        if (savedInstanceState != null) {
            boolean isChatVisible = savedInstanceState.getBoolean("IS_CHAT_VISIBLE", false);
            if (isChatVisible) {
                showChatScreen();
            }
            boolean cbChecked = savedInstanceState.getBoolean("CB_CONSENT", false);
            if (cbConsent != null) {
                cbConsent.setChecked(cbChecked);
                updateStartButtonState(cbChecked);
            }
        } else {
            updateStartButtonState(cbConsent != null && cbConsent.isChecked());
        }

        if (getIntent().getBooleanExtra("SKIP_CONSENT", false)) {
            showChatScreen();
        }

        // Format Greeting Name ("Hello MD SAKIB 💚")
        String shortName = resolveShortUserName();
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        if (tvGreeting != null) {
            tvGreeting.setText("Hello " + shortName + " 💚");
        }

        View ivCloseConsent = findViewById(R.id.ivCloseConsent);
        if (ivCloseConsent != null) ivCloseConsent.setOnClickListener(v -> finish());

        View ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) ivClose.setOnClickListener(v -> finish());

        View btnSend = findViewById(R.id.btnSend);
        if (btnSend != null) {
            btnSend.setOnClickListener(v -> sendUserMessage());
        }

        if (etMessage != null) {
            etMessage.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendUserMessage();
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean isChatVisible = (layoutChat != null && layoutChat.getVisibility() == View.VISIBLE);
        outState.putBoolean("IS_CHAT_VISIBLE", isChatVisible);
        if (cbConsent != null) {
            outState.putBoolean("CB_CONSENT", cbConsent.isChecked());
        }
    }

    private void updateStartButtonState(boolean isChecked) {
        if (btnStartConversation == null) return;
        if (isChecked) {
            btnStartConversation.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.login_btn_active_bg));
            btnStartConversation.setTextColor(ContextCompat.getColor(this, R.color.login_btn_active_text));
        } else {
            btnStartConversation.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.login_btn_disabled_bg));
            btnStartConversation.setTextColor(ContextCompat.getColor(this, R.color.login_btn_disabled_text));
        }
    }

    private void showChatScreen() {
        if (layoutConsent != null) layoutConsent.setVisibility(View.GONE);
        if (layoutChat != null) layoutChat.setVisibility(View.VISIBLE);
    }

    private String resolveShortUserName() {
        String rawName = getIntent().getStringExtra("USER_NAME");
        if (rawName == null || rawName.trim().isEmpty()) {
            User cached = ApiClient.getCachedUser(this);
            if (cached != null && cached.getIqama_holder_name() != null) {
                rawName = cached.getIqama_holder_name();
            }
        }
        if (rawName == null || rawName.trim().isEmpty()) {
            return "MD SAKIB";
        }
        String[] parts = rawName.trim().toUpperCase().split("\\s+");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1];
        }
        return parts[0];
    }

    private void sendUserMessage() {
        if (etMessage == null || llMessagesContainer == null) return;
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        etMessage.setText("");
        hideKeyboard();

        // Add user message bubble (right-aligned)
        MaterialCardView userCard = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.gravity = Gravity.END;
        cardParams.topMargin = dpToPx(12);
        cardParams.leftMargin = dpToPx(48);
        userCard.setLayoutParams(cardParams);
        userCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.login_btn_active_bg));
        userCard.setRadius(dpToPx(16));
        userCard.setCardElevation(0f);
        userCard.setStrokeWidth(0);

        TextView tvUserMsg = new TextView(this);
        tvUserMsg.setText(text);
        tvUserMsg.setTextColor(ContextCompat.getColor(this, R.color.login_btn_active_text));
        tvUserMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tvUserMsg.setPadding(dpToPx(14), dpToPx(10), dpToPx(14), dpToPx(10));
        userCard.addView(tvUserMsg);

        llMessagesContainer.addView(userCard);
        scrollToBottom();

        // Add assistant reply after short delay
        llMessagesContainer.postDelayed(() -> addAssistantReply(
                "Thank you for contacting Absher Smart Assistant. Please select or describe the service you would like assistance with, or call 920020405 for direct support."
        ), 600);
    }

    private void addAssistantReply(String replyText) {
        if (llMessagesContainer == null) return;

        LinearLayout row = new LinearLayout(this);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.topMargin = dpToPx(12);
        row.setLayoutParams(rowParams);
        row.setOrientation(LinearLayout.HORIZONTAL);

        ImageView avatar = new ImageView(this);
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(dpToPx(38), dpToPx(38));
        avatarParams.gravity = Gravity.BOTTOM;
        avatar.setLayoutParams(avatarParams);
        avatar.setBackgroundResource(R.drawable.bg_chat_avatar_circle);
        int pad = dpToPx(8);
        avatar.setPadding(pad, pad, pad, pad);
        avatar.setImageResource(R.drawable.ab2);
        avatar.setColorFilter(ContextCompat.getColor(this, R.color.icon_tint_primary));
        row.addView(avatar);

        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.leftMargin = dpToPx(10);
        card.setLayoutParams(cardParams);
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_background));
        card.setRadius(dpToPx(16));
        card.setCardElevation(0f);
        card.setStrokeWidth(dpToPx(1));
        card.setStrokeColor(ContextCompat.getColor(this, R.color.divider_color));

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(dpToPx(14), dpToPx(12), dpToPx(14), dpToPx(12));

        TextView tvTitle = new TextView(this);
        tvTitle.setText("Absher Assistant");
        tvTitle.setTextColor(ContextCompat.getColor(this, R.color.icon_tint_primary));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        inner.addView(tvTitle);

        TextView tvBody = new TextView(this);
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        bodyParams.topMargin = dpToPx(4);
        tvBody.setLayoutParams(bodyParams);
        tvBody.setMaxWidth(dpToPx(260));
        tvBody.setText(replyText);
        tvBody.setTextColor(ContextCompat.getColor(this, R.color.text_primary));
        tvBody.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        tvBody.setLineSpacing(dpToPx(3), 1.0f);
        inner.addView(tvBody);

        card.addView(inner);
        row.addView(card);

        llMessagesContainer.addView(row);
        scrollToBottom();
    }

    private void scrollToBottom() {
        if (chatScrollView != null) {
            chatScrollView.post(() -> chatScrollView.fullScroll(View.FOCUS_DOWN));
        }
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
