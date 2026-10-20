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
public final class CardItemHealthBinding {
    public final ImageView arrowIcon;
    public final TextView bloodGroup;
    public final LinearLayout cardContent;
    public final ImageView cardIcon;
    public final TextView cardTitle;
    public final LinearLayout headerLayout;
    public final TextView healthExpiryDate;
    public final TextView healthIssueDate;
    private final MaterialCardView rootView;
    public final View separatorLine;

    private CardItemHealthBinding(MaterialCardView materialCardView, ImageView imageView, TextView textView, LinearLayout linearLayout, ImageView imageView2, TextView textView2, LinearLayout linearLayout2, TextView textView3, TextView textView4, View view) {
        this.rootView = materialCardView;
        this.arrowIcon = imageView;
        this.bloodGroup = textView;
        this.cardContent = linearLayout;
        this.cardIcon = imageView2;
        this.cardTitle = textView2;
        this.headerLayout = linearLayout2;
        this.healthExpiryDate = textView3;
        this.healthIssueDate = textView4;
        this.separatorLine = view;
    }

    public static CardItemHealthBinding bind(View view) {
        View viewL;
        int i4 = R.id.arrow_icon;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null) {
            TextView textView = null;
            i4 = R.id.card_content;
            LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
                if (linearLayout != null) {
                    i4 = R.id.card_icon;
                    ImageView imageView2 = (ImageView) e.l(view, i4);
                    if (imageView2 != null) {
                        i4 = R.id.card_title;
                        TextView textView2 = (TextView) e.l(view, i4);
                        if (textView2 != null) {
                            i4 = R.id.header_layout;
                            LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                            if (linearLayout2 != null) {
                                i4 = R.id.healthExpiryDate;
                                TextView textView3 = (TextView) e.l(view, i4);
                                if (textView3 != null) {
                                    i4 = R.id.healthIssueDate;
                                    TextView textView4 = (TextView) e.l(view, i4);
                                    if (textView4 != null && (viewL = e.l(view, (i4 = R.id.separator_line))) != null) {
                                        return new CardItemHealthBinding((MaterialCardView) view, imageView, textView, linearLayout, imageView2, textView2, linearLayout2, textView3, textView4, viewL);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static CardItemHealthBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardItemHealthBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_item_health, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









