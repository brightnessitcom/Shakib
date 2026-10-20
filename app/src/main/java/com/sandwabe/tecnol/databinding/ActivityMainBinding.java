package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.sandwabe.tecnol.e;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ActivityMainBinding {
    public final BottomNavigationView bottomNavigation;
    public final FrameLayout container;
    public final FloatingActionButton floatingImage;
    private final SwipeRefreshLayout rootView;
    public final SwipeRefreshLayout swipeRefreshLayout;

    private ActivityMainBinding(SwipeRefreshLayout swipeRefreshLayout, BottomNavigationView bottomNavigationView, FrameLayout frameLayout, FloatingActionButton floatingActionButton, SwipeRefreshLayout swipeRefreshLayout2) {
        this.rootView = swipeRefreshLayout;
        this.bottomNavigation = bottomNavigationView;
        this.container = frameLayout;
        this.floatingImage = floatingActionButton;
        this.swipeRefreshLayout = swipeRefreshLayout2;
    }

    public static ActivityMainBinding bind(View view) {
        int i4 = R.id.bottom_navigation;
        BottomNavigationView bottomNavigationView = (BottomNavigationView) e.l(view, i4);
        if (bottomNavigationView != null) {
            i4 = R.id.container;
            FrameLayout frameLayout = (FrameLayout) e.l(view, i4);
            if (frameLayout != null) {
                i4 = R.id.floating_image;
                FloatingActionButton floatingActionButton = (FloatingActionButton) e.l(view, i4);
                if (floatingActionButton != null) {
                    SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view;
                    return new ActivityMainBinding(swipeRefreshLayout, bottomNavigationView, frameLayout, floatingActionButton, swipeRefreshLayout);
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static ActivityMainBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.activity_main, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public SwipeRefreshLayout getRoot() {
        return this.rootView;
    }
}









