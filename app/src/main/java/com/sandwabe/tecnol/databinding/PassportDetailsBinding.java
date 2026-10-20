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
public final class PassportDetailsBinding {
    public final ImageView backarrow;
    public final CardAmountPassportBinding cardAmountPassport;
    public final PassportCardBinding passportCard;
    private final LinearLayout rootView;
    public final Toolbar topBar;

    private PassportDetailsBinding(LinearLayout linearLayout, ImageView imageView, CardAmountPassportBinding cardAmountPassportBinding, PassportCardBinding passportCardBinding, Toolbar toolbar) {
        this.rootView = linearLayout;
        this.backarrow = imageView;
        this.cardAmountPassport = cardAmountPassportBinding;
        this.passportCard = passportCardBinding;
        this.topBar = toolbar;
    }

    public static PassportDetailsBinding bind(View view) {
        View viewL;
        int i4 = R.id.backarrow;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null && (viewL = e.l(view, (i4 = R.id.card_amount_passport))) != null) {
            CardAmountPassportBinding cardAmountPassportBindingBind = CardAmountPassportBinding.bind(viewL);
            i4 = R.id.passport_card;
            View viewL2 = e.l(view, i4);
            if (viewL2 != null) {
                PassportCardBinding passportCardBindingBind = PassportCardBinding.bind(viewL2);
                i4 = R.id.topBar;
                Toolbar toolbar = (Toolbar) e.l(view, i4);
                if (toolbar != null) {
                    return new PassportDetailsBinding((LinearLayout) view, imageView, cardAmountPassportBindingBind, passportCardBindingBind, toolbar);
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static PassportDetailsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static PassportDetailsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.passport_details, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }
}









