package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sandwabe.tecnol.e;
import com.google.android.material.card.MaterialCardView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class CardAmountPassportBinding {
    public final TextView passportAmountText;
    private final MaterialCardView rootView;

    private CardAmountPassportBinding(MaterialCardView materialCardView, TextView textView) {
        this.rootView = materialCardView;
        this.passportAmountText = textView;
    }

    public static CardAmountPassportBinding bind(View view) {
        int i4 = R.id.passport_amount_text;
        TextView textView = (TextView) e.l(view, i4);
        if (textView != null) {
            return new CardAmountPassportBinding((MaterialCardView) view, textView);
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static CardAmountPassportBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardAmountPassportBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_amount_passport, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









