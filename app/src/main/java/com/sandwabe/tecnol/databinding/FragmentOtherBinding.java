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
public final class FragmentOtherBinding {
    public final ImageView holescreen;
    public final LinearLayout leftIcons;
    public final ImageView notificationsIcon;
    private final RelativeLayout rootView;
    public final ImageView settingsIcon;
    public final Toolbar topBar;

    private FragmentOtherBinding(RelativeLayout relativeLayout, ImageView imageView, LinearLayout linearLayout, ImageView imageView2, ImageView imageView3, Toolbar toolbar) {
        this.rootView = relativeLayout;
        this.holescreen = imageView;
        this.leftIcons = linearLayout;
        this.notificationsIcon = imageView2;
        this.settingsIcon = imageView3;
        this.topBar = toolbar;
    }

    public static FragmentOtherBinding bind(View view) {
        int i4 = R.id.holescreen;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null) {
            i4 = R.id.leftIcons;
            LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
            if (linearLayout != null) {
                i4 = R.id.notificationsIcon;
                ImageView imageView2 = (ImageView) e.l(view, i4);
                if (imageView2 != null) {
                    i4 = R.id.settingsIcon;
                    ImageView imageView3 = (ImageView) e.l(view, i4);
                    if (imageView3 != null) {
                        i4 = R.id.topBar;
                        Toolbar toolbar = (Toolbar) e.l(view, i4);
                        if (toolbar != null) {
                            return new FragmentOtherBinding((RelativeLayout) view, imageView, linearLayout, imageView2, imageView3, toolbar);
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static FragmentOtherBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static FragmentOtherBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_other, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }
}









