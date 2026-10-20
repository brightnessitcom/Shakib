package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sandwabe.tecnol.e;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ItemIdBinding {
    public final ImageView idImage;
    private final LinearLayout rootView;

    private ItemIdBinding(LinearLayout linearLayout, ImageView imageView) {
        this.rootView = linearLayout;
        this.idImage = imageView;
    }

    public static ItemIdBinding bind(View view) {
        int i4 = R.id.idImage;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null) {
            return new ItemIdBinding((LinearLayout) view, imageView);
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i4)));
    }

    public static ItemIdBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ItemIdBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.item_id, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }
}









