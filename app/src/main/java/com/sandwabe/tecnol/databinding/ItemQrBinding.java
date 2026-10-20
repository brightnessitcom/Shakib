package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ItemQrBinding {
    public final CardView outerCard;
    public final View qrCard;
    public final TextView qrCountdown;
    public final ImageView qrImage;
    public final TextView qrLabel;
    public final LinearLayout qrLabelLayout;
    private final ConstraintLayout rootView;

    private ItemQrBinding(ConstraintLayout constraintLayout, CardView cardView, View cardView2, TextView textView, ImageView imageView, TextView textView2, LinearLayout linearLayout) {
        this.rootView = constraintLayout;
        this.outerCard = cardView;
        this.qrCard = cardView2;
        this.qrCountdown = textView;
        this.qrImage = imageView;
        this.qrLabel = textView2;
        this.qrLabelLayout = linearLayout;
    }

    public static ItemQrBinding bind(View view) {
        int i4 = R.id.outerCard;
        CardView cardView = (CardView) e.l(view, i4);
        if (cardView != null) {
            i4 = R.id.qrCard;
            View cardView2 = e.l(view, i4);
            if (cardView2 != null) {
                i4 = R.id.qrCountdown;
                TextView textView = (TextView) e.l(view, i4);
                if (textView != null) {
                    i4 = R.id.qrImage;
                    ImageView imageView = (ImageView) e.l(view, i4);
                    if (imageView != null) {
                        i4 = R.id.qrLabel;
                        TextView textView2 = (TextView) e.l(view, i4);
                        if (textView2 != null) {
                            i4 = R.id.qrLabelLayout;
                            LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
                            if (linearLayout != null) {
                                return new ItemQrBinding((ConstraintLayout) view, cardView, cardView2, textView, imageView, textView2, linearLayout);
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static ItemQrBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ItemQrBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.item_qr, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }
}









