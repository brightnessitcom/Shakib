package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import com.sandwabe.tecnol.R;
import com.sandwabe.tecnol.e;

public final class ActivityFullScreenIdBinding {
    @NonNull
    private final FrameLayout rootView;
    @NonNull
    public final ImageView backBtn;
    @NonNull
    public final ViewPager2 viewPager;

    private ActivityFullScreenIdBinding(@NonNull FrameLayout rootView, @NonNull ImageView backBtn,
                                       @NonNull ViewPager2 viewPager) {
        this.rootView = rootView;
        this.backBtn = backBtn;
        this.viewPager = viewPager;
    }

    @NonNull
    public static ActivityFullScreenIdBinding bind(@NonNull View view) {
        int backBtnId = R.id.backBtn;
        ImageView backBtn = (ImageView) e.l(view, backBtnId);
        if (backBtn != null) {
            int viewPagerId = R.id.viewPager;
            ViewPager2 viewPager = (ViewPager2) e.l(view, viewPagerId);
            if (viewPager != null) {
                return new ActivityFullScreenIdBinding((FrameLayout) view, backBtn, viewPager);
            }
            backBtnId = viewPagerId;
        }
        throw new NullPointerException("Missing view with ID: " + view.getResources().getResourceName(backBtnId));
    }

    @NonNull
    public static ActivityFullScreenIdBinding inflate(@NonNull LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    @NonNull
    public static ActivityFullScreenIdBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.activity_full_screen_id, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    @NonNull
    public FrameLayout getRoot() {
        return rootView;
    }
}
