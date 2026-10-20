package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ActivityBiometricBinding {
    private final LinearLayout rootView;

    private ActivityBiometricBinding(LinearLayout linearLayout) {
        this.rootView = linearLayout;
    }

    public static ActivityBiometricBinding bind(View view) {
        if (view != null) {
            return new ActivityBiometricBinding((LinearLayout) view);
        }
        throw new NullPointerException("rootView");
    }

    public static ActivityBiometricBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ActivityBiometricBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.activity_biometric, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }
}






