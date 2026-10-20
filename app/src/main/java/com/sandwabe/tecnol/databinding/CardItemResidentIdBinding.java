package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.card.MaterialCardView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class CardItemResidentIdBinding {
    public final MaterialCardView cardrrsidentInv;
    private final MaterialCardView rootView;

    private CardItemResidentIdBinding(MaterialCardView materialCardView, MaterialCardView materialCardView2) {
        this.rootView = materialCardView;
        this.cardrrsidentInv = materialCardView2;
    }

    public static CardItemResidentIdBinding bind(View view) {
        if (view == null) {
            throw new NullPointerException("rootView");
        }
        MaterialCardView materialCardView = (MaterialCardView) view;
        return new CardItemResidentIdBinding(materialCardView, materialCardView);
    }

    public static CardItemResidentIdBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardItemResidentIdBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_item_resident_id, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}






