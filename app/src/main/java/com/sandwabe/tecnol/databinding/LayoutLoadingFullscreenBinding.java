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
public final class LayoutLoadingFullscreenBinding {
    public final LottieAnimationView fullScreenLoadingAnimation;
    public final TextView fullScreenLoadingText;
    private final FrameLayout rootView;

    private LayoutLoadingFullscreenBinding(FrameLayout frameLayout, LottieAnimationView lottieAnimationView, TextView textView) {
        this.rootView = frameLayout;
        this.fullScreenLoadingAnimation = lottieAnimationView;
        this.fullScreenLoadingText = textView;
    }

    public static LayoutLoadingFullscreenBinding bind(View view) {
        int i4 = R.id.fullScreenLoadingAnimation;
        LottieAnimationView lottieAnimationView = (LottieAnimationView) e.l(view, i4);
        if (lottieAnimationView != null) {
            i4 = R.id.fullScreenLoadingText;
            TextView textView = (TextView) e.l(view, i4);
            if (textView != null) {
                return new LayoutLoadingFullscreenBinding((FrameLayout) view, lottieAnimationView, textView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static LayoutLoadingFullscreenBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static LayoutLoadingFullscreenBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.layout_loading_fullscreen, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }
}









