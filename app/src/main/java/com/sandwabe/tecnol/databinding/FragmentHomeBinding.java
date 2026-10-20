package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.sandwabe.tecnol.R;
import com.sandwabe.tecnol.e;

public final class FragmentHomeBinding {
    private final FrameLayout rootView;
    public final ImageView settingsIcon;
    public final ImageView notificationsIcon;
    public final MaterialCardView profileCard;
    public final ShapeableImageView profileImageView;
    public final TextView tvProfileName;
    public final TextView tvProfileNameArabic;
    public final TextView tvProfileId;
    public final TextView tvSeeAll;
    public final MaterialCardView squareCard;
    public final ImageView idImageView;
    public final FrameLayout lottieOverlay;
    public final LottieAnimationView idLoadingAnimation;

    private FragmentHomeBinding(FrameLayout rootView, ImageView settingsIcon, ImageView notificationsIcon,
                               MaterialCardView profileCard, ShapeableImageView profileImageView,
                               TextView tvProfileName, TextView tvProfileNameArabic, TextView tvProfileId,
                               TextView tvSeeAll, MaterialCardView squareCard,
                               ImageView idImageView, FrameLayout lottieOverlay,
                               LottieAnimationView idLoadingAnimation) {
        this.rootView = rootView;
        this.settingsIcon = settingsIcon;
        this.notificationsIcon = notificationsIcon;
        this.profileCard = profileCard;
        this.profileImageView = profileImageView;
        this.tvProfileName = tvProfileName;
        this.tvProfileNameArabic = tvProfileNameArabic;
        this.tvProfileId = tvProfileId;
        this.tvSeeAll = tvSeeAll;
        this.squareCard = squareCard;
        this.idImageView = idImageView;
        this.lottieOverlay = lottieOverlay;
        this.idLoadingAnimation = idLoadingAnimation;
    }

    public static FragmentHomeBinding bind(View view) {
        int i = R.id.settingsIcon;
        ImageView settingsIcon = (ImageView) e.l(view, i);
        if (settingsIcon != null) {
            i = R.id.notificationsIcon;
            ImageView notificationsIcon = (ImageView) e.l(view, i);
            if (notificationsIcon != null) {
                i = R.id.profileCard;
                MaterialCardView profileCard = (MaterialCardView) e.l(view, i);
                if (profileCard != null) {
                    i = R.id.profileImageView;
                    ShapeableImageView profileImageView = (ShapeableImageView) e.l(view, i);
                    if (profileImageView != null) {
                        i = R.id.tvProfileName;
                        TextView tvProfileName = (TextView) e.l(view, i);
                        if (tvProfileName != null) {
                            i = R.id.tvProfileNameArabic;
                            TextView tvProfileNameArabic = (TextView) e.l(view, i);
                            if (tvProfileNameArabic != null) {
                                i = R.id.tvProfileId;
                                TextView tvProfileId = (TextView) e.l(view, i);
                                if (tvProfileId != null) {
                                    i = R.id.tvSeeAll;
                                    TextView tvSeeAll = (TextView) e.l(view, i);
                                    if (tvSeeAll != null) {
                                        i = R.id.squareCard;
                                        MaterialCardView squareCard = (MaterialCardView) e.l(view, i);
                                        if (squareCard != null) {
                                            i = R.id.idImageView;
                                            ImageView idImageView = (ImageView) e.l(view, i);
                                            if (idImageView != null) {
                                                i = R.id.lottieOverlay;
                                                FrameLayout lottieOverlay = (FrameLayout) e.l(view, i);
                                                if (lottieOverlay != null) {
                                                    i = R.id.idLoadingAnimation;
                                                    LottieAnimationView idLoadingAnimation = (LottieAnimationView) e.l(view, i);
                                                    if (idLoadingAnimation != null) {
                                                        return new FragmentHomeBinding((FrameLayout) view, settingsIcon, notificationsIcon, profileCard, profileImageView, tvProfileName, tvProfileNameArabic, tvProfileId, tvSeeAll, squareCard, idImageView, lottieOverlay, idLoadingAnimation);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }

    public static FragmentHomeBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static FragmentHomeBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.fragment_home, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public FrameLayout getRoot() {
        return rootView;
    }
}
