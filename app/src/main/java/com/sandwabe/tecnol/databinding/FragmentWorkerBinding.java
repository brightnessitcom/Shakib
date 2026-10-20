package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.appcompat.widget.Toolbar;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class FragmentWorkerBinding {
    public final LinearLayout leftIcons;
    public final ImageView notificationsIcon;
    private final RelativeLayout rootView;
    public final ImageView settingsIcon;
    public final Toolbar topBar;

    private FragmentWorkerBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, ImageView imageView, ImageView imageView2, Toolbar toolbar) {
        this.rootView = relativeLayout;
        this.leftIcons = linearLayout;
        this.notificationsIcon = imageView;
        this.settingsIcon = imageView2;
        this.topBar = toolbar;
    }

    public static FragmentWorkerBinding bind(View view) {
        int i4 = R.id.leftIcons;
        LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
        if (linearLayout != null) {
            i4 = R.id.notificationsIcon;
            ImageView imageView = (ImageView) e.l(view, i4);
            if (imageView != null) {
                i4 = R.id.settingsIcon;
                ImageView imageView2 = (ImageView) e.l(view, i4);
                if (imageView2 != null) {
                    i4 = R.id.topBar;
                    Toolbar toolbar = (Toolbar) e.l(view, i4);
                    if (toolbar != null) {
                        return new FragmentWorkerBinding((RelativeLayout) view, linearLayout, imageView, imageView2, toolbar);
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static FragmentWorkerBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static FragmentWorkerBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_worker, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }
}









