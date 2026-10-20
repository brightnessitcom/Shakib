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
public final class PersonalInformetionFragmentBinding {
    public final ImageView backarrow;
    public final CardItemHajjBinding cardHajj;
    public final CardItemHealthBinding cardHealth;
    public final CradcardItemPersonalBinding cardPersonal;
    public final CardItemSponsorBinding cardSponsor;
    private final LinearLayout rootView;
    public final Toolbar topBar;

    private PersonalInformetionFragmentBinding(LinearLayout linearLayout, ImageView imageView, CardItemHajjBinding cardItemHajjBinding, CardItemHealthBinding cardItemHealthBinding, CradcardItemPersonalBinding cradcardItemPersonalBinding, CardItemSponsorBinding cardItemSponsorBinding, Toolbar toolbar) {
        this.rootView = linearLayout;
        this.backarrow = imageView;
        this.cardHajj = cardItemHajjBinding;
        this.cardHealth = cardItemHealthBinding;
        this.cardPersonal = cradcardItemPersonalBinding;
        this.cardSponsor = cardItemSponsorBinding;
        this.topBar = toolbar;
    }

    public static PersonalInformetionFragmentBinding bind(View view) {
        View viewL;
        int i4 = R.id.backarrow;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null && (viewL = e.l(view, (i4 = R.id.card_hajj))) != null) {
            CardItemHajjBinding cardItemHajjBindingBind = CardItemHajjBinding.bind(viewL);
            i4 = R.id.card_health;
            View viewL2 = e.l(view, i4);
            if (viewL2 != null) {
                CardItemHealthBinding cardItemHealthBindingBind = CardItemHealthBinding.bind(viewL2);
                i4 = R.id.card_personal;
                View viewL3 = e.l(view, i4);
                if (viewL3 != null) {
                    CradcardItemPersonalBinding cradcardItemPersonalBindingBind = CradcardItemPersonalBinding.bind(viewL3);
                    i4 = R.id.card_sponsor;
                    View viewL4 = e.l(view, i4);
                    if (viewL4 != null) {
                        CardItemSponsorBinding cardItemSponsorBindingBind = CardItemSponsorBinding.bind(viewL4);
                        i4 = R.id.topBar;
                        Toolbar toolbar = (Toolbar) e.l(view, i4);
                        if (toolbar != null) {
                            return new PersonalInformetionFragmentBinding((LinearLayout) view, imageView, cardItemHajjBindingBind, cardItemHealthBindingBind, cradcardItemPersonalBindingBind, cardItemSponsorBindingBind, toolbar);
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static PersonalInformetionFragmentBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static PersonalInformetionFragmentBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.personal_informetion_fragment, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }
}









