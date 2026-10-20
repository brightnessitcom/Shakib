package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ActivitySplashScreenBinding {
    public final CoordinatorLayout coordinator;
    public final TextView loadingText;
    public final LottieAnimationView lottieAnimationView;
    private final CoordinatorLayout rootView;

    private ActivitySplashScreenBinding(CoordinatorLayout coordinatorLayout, CoordinatorLayout coordinatorLayout2, TextView textView, LottieAnimationView lottieAnimationView) {
        this.rootView = coordinatorLayout;
        this.coordinator = coordinatorLayout2;
        this.loadingText = textView;
        this.lottieAnimationView = lottieAnimationView;
    }

    public static ActivitySplashScreenBinding bind(View view) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view;
        int i4 = R.id.loading_text;
        TextView textView = (TextView) e.l(view, i4);
        if (textView != null) {
            i4 = R.id.lottie_animation_view;
            LottieAnimationView lottieAnimationView = (LottieAnimationView) e.l(view, i4);
            if (lottieAnimationView != null) {
                return new ActivitySplashScreenBinding(coordinatorLayout, coordinatorLayout, textView, lottieAnimationView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static ActivitySplashScreenBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ActivitySplashScreenBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.activity_splash_screen, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public CoordinatorLayout getRoot() {
        return this.rootView;
    }
}









