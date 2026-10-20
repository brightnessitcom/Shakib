package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.airbnb.lottie.LottieAnimationView;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class FragmentIdViewBinding {
    public final LinearLayout ducc;
    public final ImageView iqamaidview;
    public final TextView lastUpdatedText;
    public final LottieAnimationView loadingAnimation;
    public final LinearLayout loadingLayout;
    public final RelativeLayout mainContent;
    public final TextView residesnt;
    private final FrameLayout rootView;
    public final ImageView settingsIcon;
    public final SwipeRefreshLayout swipeRefreshLayoutt;
    public final Toolbar topBar;

    private FragmentIdViewBinding(FrameLayout frameLayout, LinearLayout linearLayout, ImageView imageView, TextView textView, LottieAnimationView lottieAnimationView, LinearLayout linearLayout2, RelativeLayout relativeLayout, TextView textView2, ImageView imageView2, SwipeRefreshLayout swipeRefreshLayout, Toolbar toolbar) {
        this.rootView = frameLayout;
        this.ducc = linearLayout;
        this.iqamaidview = imageView;
        this.lastUpdatedText = textView;
        this.loadingAnimation = lottieAnimationView;
        this.loadingLayout = linearLayout2;
        this.mainContent = relativeLayout;
        this.residesnt = textView2;
        this.settingsIcon = imageView2;
        this.swipeRefreshLayoutt = swipeRefreshLayout;
        this.topBar = toolbar;
    }

    public static FragmentIdViewBinding bind(View view) {
        int i4 = R.id.ducc;
        LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
        if (linearLayout != null) {
            i4 = R.id.iqamaidview;
            ImageView imageView = (ImageView) e.l(view, i4);
            if (imageView != null) {
                i4 = R.id.lastUpdatedText;
                TextView textView = (TextView) e.l(view, i4);
                if (textView != null) {
                    i4 = R.id.loadingAnimation;
                    LottieAnimationView lottieAnimationView = (LottieAnimationView) e.l(view, i4);
                    if (lottieAnimationView != null) {
                        i4 = R.id.loadingLayout;
                        LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                        if (linearLayout2 != null) {
                            i4 = R.id.mainContent;
                            RelativeLayout relativeLayout = (RelativeLayout) e.l(view, i4);
                            if (relativeLayout != null) {
                                i4 = R.id.residesnt;
                                TextView textView2 = (TextView) e.l(view, i4);
                                if (textView2 != null) {
                                    i4 = R.id.settingsIcon;
                                    ImageView imageView2 = (ImageView) e.l(view, i4);
                                    if (imageView2 != null) {
                                        i4 = R.id.swipeRefreshLayoutt;
                                        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) e.l(view, i4);
                                        if (swipeRefreshLayout != null) {
                                            i4 = R.id.topBar;
                                            Toolbar toolbar = (Toolbar) e.l(view, i4);
                                            if (toolbar != null) {
                                                return new FragmentIdViewBinding((FrameLayout) view, linearLayout, imageView, textView, lottieAnimationView, linearLayout2, relativeLayout, textView2, imageView2, swipeRefreshLayout, toolbar);
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
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static FragmentIdViewBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static FragmentIdViewBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_id_view, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }
}
