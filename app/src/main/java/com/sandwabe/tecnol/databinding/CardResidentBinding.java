package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sandwabe.tecnol.e;
import com.google.android.material.card.MaterialCardView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class CardResidentBinding {
    public final ImageView copyimgiconresedent;
    public final TextView expiryDateText;
    public final TextView idVersion;
    public final TextView issueDateText;
    public final TextView residentIdText;
    private final MaterialCardView rootView;
    public final View separatorLine;

    private CardResidentBinding(MaterialCardView materialCardView, ImageView imageView, TextView textView, TextView textView2, TextView textView3, TextView textView4, View view) {
        this.rootView = materialCardView;
        this.copyimgiconresedent = imageView;
        this.expiryDateText = textView;
        this.idVersion = textView2;
        this.issueDateText = textView3;
        this.residentIdText = textView4;
        this.separatorLine = view;
    }

    public static CardResidentBinding bind(View view) {
        View viewL;
        int i4 = R.id.copyimgiconresedent;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null) {
            i4 = R.id.expiry_date_text;
            TextView textView = (TextView) e.l(view, i4);
            if (textView != null) {
                i4 = R.id.id_version;
                TextView textView2 = (TextView) e.l(view, i4);
                if (textView2 != null) {
                    i4 = R.id.issue_date_text;
                    TextView textView3 = (TextView) e.l(view, i4);
                    if (textView3 != null) {
                        i4 = R.id.resident_id_text;
                        TextView textView4 = (TextView) e.l(view, i4);
                        if (textView4 != null && (viewL = e.l(view, (i4 = R.id.separator_line))) != null) {
                            return new CardResidentBinding((MaterialCardView) view, imageView, textView, textView2, textView3, textView4, viewL);
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static CardResidentBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardResidentBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_resident, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









