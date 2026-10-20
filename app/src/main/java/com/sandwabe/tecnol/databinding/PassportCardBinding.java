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
public final class PassportCardBinding {
    public final ImageView arrowIcon;
    public final LinearLayout cardContent;
    public final ImageView cardIcon;
    public final TextView cardTitle;
    public final ImageView copyimgicopassport;
    public final LinearLayout headerLayout;
    public final TextView passportCityText;
    public final TextView passportExpiryDateText;
    public final TextView passportIssueDateText;
    public final TextView passportNumberText;
    public final TextView passportStatusText;
    public final TextView passportTypeText;
    private final MaterialCardView rootView;
    public final View separatorLine;

    private PassportCardBinding(MaterialCardView materialCardView, ImageView imageView, LinearLayout linearLayout, ImageView imageView2, TextView textView, ImageView imageView3, LinearLayout linearLayout2, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, View view) {
        this.rootView = materialCardView;
        this.arrowIcon = imageView;
        this.cardContent = linearLayout;
        this.cardIcon = imageView2;
        this.cardTitle = textView;
        this.copyimgicopassport = imageView3;
        this.headerLayout = linearLayout2;
        this.passportCityText = textView2;
        this.passportExpiryDateText = textView3;
        this.passportIssueDateText = textView4;
        this.passportNumberText = textView5;
        this.passportStatusText = textView6;
        this.passportTypeText = textView7;
        this.separatorLine = view;
    }

    public static PassportCardBinding bind(View view) {
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
                        i4 = R.id.copyimgicopassport;
                        ImageView imageView3 = (ImageView) e.l(view, i4);
                        if (imageView3 != null) {
                            i4 = R.id.header_layout;
                            LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                            if (linearLayout2 != null) {
                                i4 = R.id.passport_city_text;
                                TextView textView2 = (TextView) e.l(view, i4);
                                if (textView2 != null) {
                                    i4 = R.id.passport_expiry_date_text;
                                    TextView textView3 = (TextView) e.l(view, i4);
                                    if (textView3 != null) {
                                        i4 = R.id.passport_issue_date_text;
                                        TextView textView4 = (TextView) e.l(view, i4);
                                        if (textView4 != null) {
                                            i4 = R.id.passport_number_text;
                                            TextView textView5 = (TextView) e.l(view, i4);
                                            if (textView5 != null) {
                                                i4 = R.id.passport_status_text;
                                                TextView textView6 = (TextView) e.l(view, i4);
                                                if (textView6 != null) {
                                                    i4 = R.id.passport_type_text;
                                                    TextView textView7 = (TextView) e.l(view, i4);
                                                    if (textView7 != null && (viewL = e.l(view, (i4 = R.id.separator_line))) != null) {
                                                        return new PassportCardBinding((MaterialCardView) view, imageView, linearLayout, imageView2, textView, imageView3, linearLayout2, textView2, textView3, textView4, textView5, textView6, textView7, viewL);
                                                    }
                                                }
                                            }
                                        }
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

    public static PassportCardBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static PassportCardBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.passport_card, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









