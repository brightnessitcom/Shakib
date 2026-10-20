package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.cardview.widget.CardView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class CardItemLicenseBinding {
    public final CardView cardItemLicense;
    private final CardView rootView;

    private CardItemLicenseBinding(CardView cardView, CardView cardView2) {
        this.rootView = cardView;
        this.cardItemLicense = cardView2;
    }

    public static CardItemLicenseBinding bind(View view) {
        if (view == null) {
            throw new NullPointerException("rootView");
        }
        CardView cardView = (CardView) view;
        return new CardItemLicenseBinding(cardView, cardView);
    }

    public static CardItemLicenseBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardItemLicenseBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_item_license, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public CardView getRoot() {
        return this.rootView;
    }
}






