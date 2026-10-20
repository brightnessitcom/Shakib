package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ItemImageBinding {
    public final CardView cardView;
    public final ImageView imageView;
    private final ConstraintLayout rootView;

    private ItemImageBinding(ConstraintLayout constraintLayout, CardView cardView, ImageView imageView) {
        this.rootView = constraintLayout;
        this.cardView = cardView;
        this.imageView = imageView;
    }

    public static ItemImageBinding bind(View view) {
        int i4 = R.id.cardView;
        CardView cardView = (CardView) e.l(view, i4);
        if (cardView != null) {
            i4 = R.id.imageView;
            ImageView imageView = (ImageView) e.l(view, i4);
            if (imageView != null) {
                return new ItemImageBinding((ConstraintLayout) view, cardView, imageView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static ItemImageBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ItemImageBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.item_image, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }
}









