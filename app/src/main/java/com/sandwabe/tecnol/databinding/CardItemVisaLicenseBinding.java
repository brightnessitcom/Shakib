package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sandwabe.tecnol.e;
import com.google.android.material.card.MaterialCardView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class CardItemVisaLicenseBinding {
    public final MaterialCardView cardItemVisaLicense;
    public final LinearLayout licenseSection;
    private final MaterialCardView rootView;
    public final LinearLayout visaSection;

    private CardItemVisaLicenseBinding(MaterialCardView materialCardView, MaterialCardView materialCardView2, LinearLayout linearLayout, LinearLayout linearLayout2) {
        this.rootView = materialCardView;
        this.cardItemVisaLicense = materialCardView2;
        this.licenseSection = linearLayout;
        this.visaSection = linearLayout2;
    }

    public static CardItemVisaLicenseBinding bind(View view) {
        MaterialCardView materialCardView = (MaterialCardView) view;
        int i4 = R.id.license_section;
        LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
        if (linearLayout != null) {
            i4 = R.id.visa_section;
            LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
            if (linearLayout2 != null) {
                return new CardItemVisaLicenseBinding(materialCardView, materialCardView, linearLayout, linearLayout2);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static CardItemVisaLicenseBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static CardItemVisaLicenseBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.card_item_visa_license, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public MaterialCardView getRoot() {
        return this.rootView;
    }
}









