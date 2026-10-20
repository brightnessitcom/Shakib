package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.sandwabe.tecnol.e;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class FragmentProfileBinding {
    public final ImageView backarrowprofile;
    public final CardItemVisaLicenseBinding cardItemVisaLicense;
    public final CardItemPassportBinding cardpassportInv;
    public final CardItemResidentIdBinding cardrrsidentInv;
    public final ImageView copyimgiconresedent;
    public final TextView detailsLabel;
    public final LinearLayout detailsLayout;
    public final View greenBg;
    public final GridLayout gridLayout;
    public final TextView iqamaHolderName;
    public final TextView iqamaNumberText;
    public final LinearLayout leborClickLayout;
    public final MaterialCardView mainCard;
    public final ShapeableImageView profileImage;
    private final ConstraintLayout rootView;
    public final ScrollView scrollView;
    public final TextView toolbarTitle;
    public final Toolbar topBar;

    private FragmentProfileBinding(ConstraintLayout constraintLayout, ImageView imageView, CardItemVisaLicenseBinding cardItemVisaLicenseBinding, CardItemPassportBinding cardItemPassportBinding, CardItemResidentIdBinding cardItemResidentIdBinding, ImageView imageView2, TextView textView, LinearLayout linearLayout, View view, GridLayout gridLayout, TextView textView2, TextView textView3, LinearLayout linearLayout2, MaterialCardView materialCardView, ShapeableImageView shapeableImageView, ScrollView scrollView, TextView textView4, Toolbar toolbar) {
        this.rootView = constraintLayout;
        this.backarrowprofile = imageView;
        this.cardItemVisaLicense = cardItemVisaLicenseBinding;
        this.cardpassportInv = cardItemPassportBinding;
        this.cardrrsidentInv = cardItemResidentIdBinding;
        this.copyimgiconresedent = imageView2;
        this.detailsLabel = textView;
        this.detailsLayout = linearLayout;
        this.greenBg = view;
        this.gridLayout = gridLayout;
        this.iqamaHolderName = textView2;
        this.iqamaNumberText = textView3;
        this.leborClickLayout = linearLayout2;
        this.mainCard = materialCardView;
        this.profileImage = shapeableImageView;
        this.scrollView = scrollView;
        this.toolbarTitle = textView4;
        this.topBar = toolbar;
    }

    public static FragmentProfileBinding bind(View view) {
        View viewL;
        View viewL2;
        int i4 = R.id.backarrowprofile;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null && (viewL = e.l(view, (i4 = R.id.card_item_visa_license))) != null) {
            CardItemVisaLicenseBinding cardItemVisaLicenseBindingBind = CardItemVisaLicenseBinding.bind(viewL);
            i4 = R.id.cardpassport_inv;
            View viewL3 = e.l(view, i4);
            if (viewL3 != null) {
                CardItemPassportBinding cardItemPassportBindingBind = CardItemPassportBinding.bind(viewL3);
                i4 = R.id.cardrrsident_inv;
                View viewL4 = e.l(view, i4);
                if (viewL4 != null) {
                    CardItemResidentIdBinding cardItemResidentIdBindingBind = CardItemResidentIdBinding.bind(viewL4);
                    i4 = R.id.copyimgiconresedent;
                    ImageView imageView2 = (ImageView) e.l(view, i4);
                    if (imageView2 != null) {
                        i4 = R.id.detailsLabel;
                        TextView textView = (TextView) e.l(view, i4);
                        if (textView != null) {
                            i4 = R.id.detailsLayout;
                            LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
                            if (linearLayout != null && (viewL2 = e.l(view, (i4 = R.id.greenBg))) != null) {
                                i4 = R.id.gridLayout;
                                GridLayout gridLayout = (GridLayout) e.l(view, i4);
                                if (gridLayout != null) {
                                    i4 = R.id.iqamaHolderName;
                                    TextView textView2 = (TextView) e.l(view, i4);
                                    if (textView2 != null) {
                                        i4 = R.id.iqamaNumberText;
                                        TextView textView3 = (TextView) e.l(view, i4);
                                        if (textView3 != null) {
                                            i4 = R.id.lebor_click_layout;
                                            LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                                            if (linearLayout2 != null) {
                                                i4 = R.id.mainCard;
                                                MaterialCardView materialCardView = (MaterialCardView) e.l(view, i4);
                                                if (materialCardView != null) {
                                                    i4 = R.id.profileImage;
                                                    ShapeableImageView shapeableImageView = (ShapeableImageView) e.l(view, i4);
                                                    if (shapeableImageView != null) {
                                                        i4 = R.id.scrollView;
                                                        ScrollView scrollView = (ScrollView) e.l(view, i4);
                                                        if (scrollView != null) {
                                                            i4 = R.id.toolbarTitle;
                                                            TextView textView4 = (TextView) e.l(view, i4);
                                                            if (textView4 != null) {
                                                                i4 = R.id.topBar;
                                                                Toolbar toolbar = (Toolbar) e.l(view, i4);
                                                                if (toolbar != null) {
                                                                    return new FragmentProfileBinding((ConstraintLayout) view, imageView, cardItemVisaLicenseBindingBind, cardItemPassportBindingBind, cardItemResidentIdBindingBind, imageView2, textView, linearLayout, viewL2, gridLayout, textView2, textView3, linearLayout2, materialCardView, shapeableImageView, scrollView, textView4, toolbar);
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

    public static FragmentProfileBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static FragmentProfileBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_profile, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }
}









