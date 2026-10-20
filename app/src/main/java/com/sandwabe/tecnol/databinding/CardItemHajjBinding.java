package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sandwabe.tecnol.e;
import com.google.android.material.card.MaterialCardView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class CardItemHajjBinding {
    public final ImageView arrowIcon;
    public final LinearLayout cardContent;
    public final ImageView cardIcon;
    public final TextView cardTitle;
    public final LinearLayout headerLayout;
    private final MaterialCardView rootView;
    public final View separatorLine;

    private CardItemHajjBinding(MaterialCardView materialCardView, ImageView imageView, LinearLayout linearLayout, ImageView imageView2, TextView textView, LinearLayout linearLayout2, View view) {
        this.rootView = materialCardView;
        this.arrowIcon = imageView;
        this.cardContent = linearLayout;
        this.cardIcon = imageView2;
        this.cardTitle = textView;
        this.headerLayout = linearLayout2;
        this.separatorLine = view;
    }

    public static CardItemHajjBinding bind(View view) {
        View viewL;
        int i4 = R.id.arrow_icon;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null) {
            i4 = R.id.card_content;
            LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
            if (linearLayout != null) {
                i4 = R.id.card_icon;
                ImageView imageView2 = (ImageView) e.l(view, i4);
                if (imageView2 != null) {
                    i4 = R.id.card_title;
                    TextView textView = (TextView) e.l(view, i4);
                    if (textView != null) {
                        i4 = R.id.header_layout;
                        LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                        if (linearLayout2 != null && (viewL = e.l(view, (i4 = R.id.separator_line))) != null) {
                            return new CardItemHajjBinding((MaterialCardView) view, imageView, linearLayout, imageView2, textView, linearLayout2, viewL);
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static CardItemHajjBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardItemHajjBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_item_hajj, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









