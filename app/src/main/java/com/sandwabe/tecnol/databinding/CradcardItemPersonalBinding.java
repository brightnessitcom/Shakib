package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sandwabe.tecnol.e;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class CradcardItemPersonalBinding {
    public final ImageView arrowIcon;
    public final TextView biometricsCollected;
    public final TextView birthCity;
    public final TextView birthCountry;
    public final LinearLayout cardContent;
    public final ShapeableImageView cardIcon;
    public final TextView cardTitle;
    public final TextView dateOfBirth;
    public final LinearLayout headerLayout;
    public final TextView maritalStatus;
    public final TextView nameuser;
    public final TextView religion;
    private final MaterialCardView rootView;
    public final View separatorLine;
    public final TextView sponsorshipTransfers;
    public final TextView travelStatus;
    public final TextView workPermit;

    private CradcardItemPersonalBinding(MaterialCardView materialCardView, ImageView imageView, TextView textView, TextView textView2, TextView textView3, LinearLayout linearLayout, ShapeableImageView shapeableImageView, TextView textView4, TextView textView5, LinearLayout linearLayout2, TextView textView6, TextView textView7, TextView textView8, View view, TextView textView9, TextView textView10, TextView textView11) {
        this.rootView = materialCardView;
        this.arrowIcon = imageView;
        this.biometricsCollected = textView;
        this.birthCity = textView2;
        this.birthCountry = textView3;
        this.cardContent = linearLayout;
        this.cardIcon = shapeableImageView;
        this.cardTitle = textView4;
        this.dateOfBirth = textView5;
        this.headerLayout = linearLayout2;
        this.maritalStatus = textView6;
        this.nameuser = textView7;
        this.religion = textView8;
        this.separatorLine = view;
        this.sponsorshipTransfers = textView9;
        this.travelStatus = textView10;
        this.workPermit = textView11;
    }

    public static CradcardItemPersonalBinding bind(View view) {
        View viewL;
        int i4 = R.id.arrow_icon;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null) {
            i4 = R.id.biometricsCollected;
            TextView textView = (TextView) e.l(view, i4);
            if (textView != null) {
                i4 = R.id.birthCity;
                TextView textView2 = (TextView) e.l(view, i4);
                if (textView2 != null) {
                    i4 = R.id.birthCountry;
                    TextView textView3 = (TextView) e.l(view, i4);
                    if (textView3 != null) {
                        i4 = R.id.card_content;
                        LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
                        if (linearLayout != null) {
                            i4 = R.id.card_icon;
                            ShapeableImageView shapeableImageView = (ShapeableImageView) e.l(view, i4);
                            if (shapeableImageView != null) {
                                i4 = R.id.card_title;
                                TextView textView4 = (TextView) e.l(view, i4);
                                if (textView4 != null) {
                                    i4 = R.id.dateOfBirth;
                                    TextView textView5 = (TextView) e.l(view, i4);
                                    if (textView5 != null) {
                                        i4 = R.id.header_layout;
                                        LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                                        if (linearLayout2 != null) {
                                            i4 = R.id.maritalStatus;
                                            TextView textView6 = (TextView) e.l(view, i4);
                                            if (textView6 != null) {
                                                i4 = R.id.nameuser;
                                                TextView textView7 = (TextView) e.l(view, i4);
                                                if (textView7 != null) {
                                                    i4 = R.id.religion;
                                                    TextView textView8 = (TextView) e.l(view, i4);
                                                    if (textView8 != null && (viewL = e.l(view, (i4 = R.id.separator_line))) != null) {
                                                        i4 = R.id.sponsorshipTransfers;
                                                        TextView textView9 = (TextView) e.l(view, i4);
                                                        if (textView9 != null) {
                                                            i4 = R.id.travelStatus;
                                                            TextView textView10 = (TextView) e.l(view, i4);
                                                            if (textView10 != null) {
                                                                i4 = R.id.workPermit;
                                                                TextView textView11 = (TextView) e.l(view, i4);
                                                                if (textView11 != null) {
                                                                    return new CradcardItemPersonalBinding((MaterialCardView) view, imageView, textView, textView2, textView3, linearLayout, shapeableImageView, textView4, textView5, linearLayout2, textView6, textView7, textView8, viewL, textView9, textView10, textView11);
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
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static CradcardItemPersonalBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CradcardItemPersonalBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.cradcard_item_personal, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









