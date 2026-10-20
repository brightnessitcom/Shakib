package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class LayoutLoadingOverlayBinding {
    public final LottieAnimationView loadingAnimationView;
    public final FrameLayout loadingOverlay;
    public final TextView loadingText;
    private final FrameLayout rootView;

    private LayoutLoadingOverlayBinding(FrameLayout frameLayout, LottieAnimationView lottieAnimationView, FrameLayout frameLayout2, TextView textView) {
        this.rootView = frameLayout;
        this.loadingAnimationView = lottieAnimationView;
        this.loadingOverlay = frameLayout2;
        this.loadingText = textView;
    }

    public static LayoutLoadingOverlayBinding bind(View view) {
        int i4 = R.id.loadingAnimationView;
        LottieAnimationView lottieAnimationView = (LottieAnimationView) e.l(view, i4);
        if (lottieAnimationView != null) {
            FrameLayout frameLayout = (FrameLayout) view;
            int i5 = R.id.loadingText;
            TextView textView = (TextView) e.l(view, i5);
            if (textView != null) {
                return new LayoutLoadingOverlayBinding(frameLayout, lottieAnimationView, frameLayout, textView);
            }
            i4 = i5;
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static LayoutLoadingOverlayBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static LayoutLoadingOverlayBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.layout_loading_overlay, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }
}









