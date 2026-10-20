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
public final class CardItemSponsorBinding {
    public final ImageView arrowIcon;
    public final LinearLayout cardContent;
    public final ImageView cardIcon;
    public final TextView cardTitle;
    public final LinearLayout headerLayout;
    private final MaterialCardView rootView;
    public final View separatorLine;
    public final TextView sponsorName;
    public final ImageView sponsorcopyimgview;
    public final TextView sponsortextv;

    private CardItemSponsorBinding(MaterialCardView materialCardView, ImageView imageView, LinearLayout linearLayout, ImageView imageView2, TextView textView, LinearLayout linearLayout2, View view, TextView textView2, ImageView imageView3, TextView textView3) {
        this.rootView = materialCardView;
        this.arrowIcon = imageView;
        this.cardContent = linearLayout;
        this.cardIcon = imageView2;
        this.cardTitle = textView;
        this.headerLayout = linearLayout2;
        this.separatorLine = view;
        this.sponsorName = textView2;
        this.sponsorcopyimgview = imageView3;
        this.sponsortextv = textView3;
    }

    public static CardItemSponsorBinding bind(View view) {
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
                            i4 = R.id.sponsorName;
                            TextView textView2 = (TextView) e.l(view, i4);
                            if (textView2 != null) {
                                i4 = R.id.sponsorcopyimgview;
                                ImageView imageView3 = (ImageView) e.l(view, i4);
                                if (imageView3 != null) {
                                    i4 = R.id.sponsortextv;
                                    TextView textView3 = (TextView) e.l(view, i4);
                                    if (textView3 != null) {
                                        return new CardItemSponsorBinding((MaterialCardView) view, imageView, linearLayout, imageView2, textView, linearLayout2, viewL, textView2, imageView3, textView3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static CardItemSponsorBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardItemSponsorBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_item_sponsor, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









