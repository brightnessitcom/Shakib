package com.sandwabe.tecnol;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Locale;

public class FullScreenIdActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    private String currentUsername;
    private ViewPager2 viewPager;
    private FullScreenAdapter adapter;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        
        setContentView(R.layout.activity_full_screen_id);
        
        viewPager = findViewById(R.id.viewPager);
        View backBtn = findViewById(R.id.backBtn);
        
        if (backBtn != null) backBtn.setOnClickListener(v -> finish());

        if (getIntent() != null) {
            if (getIntent().hasExtra("USER")) {
                currentUser = (User) getIntent().getSerializableExtra("USER");
            }
            if (getIntent().hasExtra("USERNAME")) {
                currentUsername = getIntent().getStringExtra("USERNAME");
            }
        }
        
        if (currentUsername == null || currentUsername.isEmpty()) {
            currentUsername = LoginActivity.getCurrentUsername(this);
        }

        adapter = new FullScreenAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        
        loadData();
    }

    private String resolveImageUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.trim().isEmpty() || "null".equalsIgnoreCase(rawUrl.trim())) {
            return null;
        }
        String url = rawUrl.trim();
        if (!url.startsWith("http") && !url.startsWith("/")) {
            try {
                String decrypted = CustomCipher.decrypt(url, CustomCipher.getSecret(this), this);
                if (!decrypted.trim().isEmpty() && !"null".equalsIgnoreCase(decrypted.trim())) {
                    return decrypted.trim();
                }
            } catch (Exception e) {
                Log.e("FullScreenIdActivity", "Decryption failed for URL: " + url, e);
            }
        }
        return url;
    }

    private void loadData() {
        if (currentUser == null) {
            currentUser = ApiClient.getCachedUser(this);
            if (currentUser != null) {
                if (currentUsername == null || currentUsername.isEmpty()) {
                    currentUsername = currentUser.getUsername();
                }
                adapter.notifyDataSetChanged();
            }
        }

        if (currentUsername == null || currentUsername.isEmpty()) return;

        ApiClient.fetchUser(this, currentUsername, new ApiClient.ApiCallback<User>() {
            @Override
            public void onSuccess(User user) {
                currentUser = user;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Log.w("FullScreenIdActivity", "ApiClient error: " + errorMessage);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.activeQrViewHolder != null && adapter.activeQrViewHolder.timer != null) {
            adapter.activeQrViewHolder.timer.cancel();
        }
    }

    private class FullScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_ID = 0;
        private static final int TYPE_QR = 1;
        QrViewHolder activeQrViewHolder;

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? TYPE_ID : TYPE_QR;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_ID) {
                return new IdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fullscreen_id, parent, false));
            } else {
                QrViewHolder qrVh = new QrViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fullscreen_qr, parent, false));
                activeQrViewHolder = qrVh;
                return qrVh;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof IdViewHolder) {
                ((IdViewHolder) holder).bind();
            } else if (holder instanceof QrViewHolder) {
                ((QrViewHolder) holder).bind();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        class IdViewHolder extends RecyclerView.ViewHolder {
            TouchImageView ivIdCard;

            IdViewHolder(View itemView) {
                super(itemView);
                ivIdCard = itemView.findViewById(R.id.id_card_rotated);
            }

            void bind() {
                if (currentUser != null) {
                    DynamicCardRenderer.renderCardAsync(FullScreenIdActivity.this, currentUser, new DynamicCardRenderer.CardCallback() {
                        @Override
                        public void onCardRendered(@androidx.annotation.NonNull android.graphics.Bitmap cardBitmap) {
                            if (!isFinishing() && !isDestroyed()) {
                                ivIdCard.setImageBitmap(cardBitmap);
                            }
                        }
                        @Override
                        public void onError(Exception e) {
                            String url = resolveImageUrl(currentUser.getId_image_url());
                            if (url != null && !url.isEmpty()) {
                                Glide.with(FullScreenIdActivity.this)
                                    .load(url)
                                    .placeholder(R.drawable.absher)
                                    .error(R.drawable.absher)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .override(1800)
                                    .into(ivIdCard);
                            }
                        }
                    });
                    if (true) return;
                    String url = resolveImageUrl(currentUser.getId_image_url());
                    if (url != null && !url.isEmpty()) {
                        Glide.with(FullScreenIdActivity.this)
                            .load(url)
                            .placeholder(R.drawable.absher)
                            .error(R.drawable.absher)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .override(1800)
                            .into(ivIdCard);
                    }
                }
            }
        }

        class QrViewHolder extends RecyclerView.ViewHolder {
            ImageView qrImage;
            TextView qrCountdown;
            CountDownTimer timer;

            QrViewHolder(View itemView) {
                super(itemView);
                qrImage = itemView.findViewById(R.id.qrImage);
                qrCountdown = itemView.findViewById(R.id.qrCountdown);
            }

            void bind() {
                if (currentUser != null) {
                    applyQrImage(currentUser.getQr_image_url());
                }
                startTimer();
            }

            void refreshQr() {
                if (currentUsername == null || currentUsername.isEmpty()) return;
                ApiClient.fetchUser(FullScreenIdActivity.this, currentUsername, new ApiClient.ApiCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        currentUser = user;
                        applyQrImage(currentUser.getQr_image_url());
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.w("FullScreenIdActivity", "refreshQr error: " + errorMessage);
                    }
                });
            }
            
            private void applyQrImage(String rawUrl) {
                String url = resolveImageUrl(rawUrl);
                if (url == null || url.isEmpty()) return;
                final String finalUrl = url;
                qrImage.animate()
                    .alpha(0.0f)
                    .scaleX(0.4f)
                    .scaleY(0.4f)
                    .setDuration(150)
                    .withEndAction(() -> {
                        Glide.with(FullScreenIdActivity.this)
                            .load(finalUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(qrImage);
                        qrImage.animate()
                            .alpha(1.0f)
                            .scaleX(1.2f)
                            .scaleY(1.2f)
                            .setDuration(150)
                            .withEndAction(() -> {
                                qrImage.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(150)
                                    .start();
                            }).start();
                    }).start();
            }

            void startTimer() {
                if (timer != null) timer.cancel();
                timer = new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (qrCountdown != null) {
                            qrCountdown.setText(String.format(Locale.US, "00:%02d", millisUntilFinished / 1000));
                        }
                    }
                    @Override
                    public void onFinish() {
                        refreshQr();
                        startTimer();
                    }
                }.start();
            }
        }
    }
}
