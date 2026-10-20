package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ResidentDetalsBinding {
    public final ImageView backarrow;
    public final CardResidentBinding cardResident;
    private final LinearLayout rootView;
    public final Toolbar topBar;

    private ResidentDetalsBinding(LinearLayout linearLayout, ImageView imageView, CardResidentBinding cardResidentBinding, Toolbar toolbar) {
        this.rootView = linearLayout;
        this.backarrow = imageView;
        this.cardResident = cardResidentBinding;
        this.topBar = toolbar;
    }

    public static ResidentDetalsBinding bind(View view) {
        View viewL;
        int i4 = R.id.backarrow;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null && (viewL = e.l(view, (i4 = R.id.card_resident))) != null) {
            CardResidentBinding cardResidentBindingBind = CardResidentBinding.bind(viewL);
            int i5 = R.id.topBar;
            Toolbar toolbar = (Toolbar) e.l(view, i5);
            if (toolbar != null) {
                return new ResidentDetalsBinding((LinearLayout) view, imageView, cardResidentBindingBind, toolbar);
            }
            i4 = i5;
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static ResidentDetalsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ResidentDetalsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.resident_detals, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }
}









