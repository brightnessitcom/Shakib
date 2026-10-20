package com.sandwabe.tecnol;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.Locale;
import java.util.Objects;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ID = 0;
    private static final int VIEW_TYPE_QR = 1;
    private final Context context;
    private final String currentUsername;
    private final Object[] images;

    public static class IdViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public IdViewHolder(@NonNull View view) {
            super(view);
            this.imageView = view.findViewById(R.id.imageView);
        }
    }

    public class QrViewHolder extends RecyclerView.ViewHolder {
        CountDownTimer countDownTimer;
        TextView qrCountdown;
        ImageView qrImage;

        public QrViewHolder(@NonNull View view) {
            super(view);
            this.qrImage = view.findViewById(R.id.qrImage);
            this.qrCountdown = view.findViewById(R.id.qrCountdown);
        }

        public void refreshQrImage() {
            if (currentUsername == null || currentUsername.isEmpty()) return;
            Context ctx = qrImage != null ? qrImage.getContext() : context;
            if (ctx == null) return;
            
            ApiClient.fetchUser(ctx, currentUsername, new ApiClient.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    if (user != null && !user.getQr_image_url().isEmpty()) {
                        try {
                            String encrypted = user.getQr_image_url();
                            String url;
                            if (encrypted.startsWith("http") || encrypted.startsWith("/")) {
                                url = encrypted;
                            } else {
                                url = CustomCipher.decrypt(encrypted, CustomCipher.getSecret(ctx), ctx);
                            }
                            
                            if (url != null && !url.isEmpty() && qrImage != null) {
                                Glide.with(ctx).load(url).override(1200).into(qrImage);
                            }
                        } catch (Exception e) {
                            Log.e("ImageAdapter", "Error refreshing QR image", e);
                        }
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.w("ImageAdapter", "refreshQrImage error: " + errorMessage);
                }
            });
        }

        public void cancelTimer() {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }
        }

        public class QrCountdownTimer extends CountDownTimer {
            public QrCountdownTimer(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }

            public void restartTimer() {
                startTimer();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                if (qrCountdown != null) {
                    qrCountdown.setText(String.format(Locale.getDefault(), "%02d:%02d", 0, seconds));
                    if (seconds <= 5) {
                        qrCountdown.setBackgroundResource(R.drawable.bg_countdown_red);
                        qrCountdown.setTextColor(-1); // White
                    } else {
                        qrCountdown.setBackgroundResource(R.drawable.bg_countdown);
                        qrCountdown.setTextColor(-16777216); // Black
                    }
                }
            }

            @Override
            public void onFinish() {
                if (qrCountdown != null) {
                    qrCountdown.setText(context.getString(R.string.qr_countdown_initial_value));
                    qrCountdown.setBackgroundResource(R.drawable.bg_countdown_red);
                    qrCountdown.setTextColor(-1);
                }
                
                if (qrImage != null) {
                    // Refresh Effect: Sudden white flash + scale pulse to make it look like it changed
                    qrImage.animate()
                        .alpha(0.0f)
                        .scaleX(0.5f)
                        .scaleY(0.5f)
                        .setDuration(150)
                        .withEndAction(() -> {
                            refreshQrImage();
                            qrImage.animate()
                                .alpha(1.0f)
                                .scaleX(1.1f)
                                .scaleY(1.1f)
                                .setDuration(100)
                                .withEndAction(() -> {
                                    qrImage.animate()
                                        .scaleX(1.0f)
                                        .scaleY(1.0f)
                                        .setDuration(100)
                                        .withEndAction(this::restartTimer)
                                        .start();
                                })
                                .start();
                        })
                        .start();
                }
            }
        }

        public void startTimer() {
            cancelTimer();
            countDownTimer = new QrCountdownTimer(30000, 1000).start();
        }
    }

    public ImageAdapter(Object[] images, Context context, String currentUsername) {
        this.images = images;
        this.context = context;
        this.currentUsername = currentUsername;
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_ID : VIEW_TYPE_QR;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        
        switch (viewType) {
            case VIEW_TYPE_ID:
                return new IdViewHolder(inflater.inflate(R.layout.item_image, parent, false));
            case VIEW_TYPE_QR:
            default:
                return new QrViewHolder(inflater.inflate(R.layout.item_qr, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = images[position];
        if (holder instanceof IdViewHolder) {
            IdViewHolder idViewHolder = (IdViewHolder) holder;
            if (item instanceof String) {
                Glide.with(idViewHolder.imageView.getContext()).load((String) item)
                    .override(Target.SIZE_ORIGINAL)
                    .into(idViewHolder.imageView);
            } else if (item instanceof Integer) {
                idViewHolder.imageView.setImageResource((Integer) item);
            }
        } else if (holder instanceof QrViewHolder) {
            QrViewHolder qrViewHolder = (QrViewHolder) holder;
            if (item instanceof String && !((String) item).isEmpty() && !Objects.equals(item, "null")) {
                Glide.with(qrViewHolder.qrImage.getContext()).load((String) item)
                    .override(Target.SIZE_ORIGINAL)
                    .into(qrViewHolder.qrImage);
            } else {
                qrViewHolder.qrImage.setImageDrawable(null);
            }
            qrViewHolder.refreshQrImage();
            qrViewHolder.startTimer();
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof QrViewHolder) {
            ((QrViewHolder) holder).cancelTimer();
        }
    }
}
