package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.sandwabe.tecnol.e;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class LayoutSettingsvactBinding {
    public final ImageView backarrow;
    public final RadioGroup calendarRadioGroup;
    public final RadioGroup languageRadioGroup;
    public final LinearLayout logoutButton;
    public final MaterialRadioButton radioArabic;
    public final MaterialRadioButton radioEnglish;
    public final MaterialRadioButton radioGregorian;
    public final MaterialRadioButton radioHijri;
    private final LinearLayout rootView;
    public final ShapeableImageView settingProfileIc;
    public final Switch switchBlur;
    public final Switch switchBlur2;
    public final Toolbar topBar;
    public final TextView tvUserId;
    public final TextView tvUserName;

    private LayoutSettingsvactBinding(LinearLayout linearLayout, ImageView imageView, RadioGroup radioGroup, RadioGroup radioGroup2, LinearLayout linearLayout2, MaterialRadioButton materialRadioButton, MaterialRadioButton materialRadioButton2, MaterialRadioButton materialRadioButton3, MaterialRadioButton materialRadioButton4, ShapeableImageView shapeableImageView, Switch r11, Switch r12, Toolbar toolbar, TextView textView, TextView textView2) {
        this.rootView = linearLayout;
        this.backarrow = imageView;
        this.calendarRadioGroup = radioGroup;
        this.languageRadioGroup = radioGroup2;
        this.logoutButton = linearLayout2;
        this.radioArabic = materialRadioButton;
        this.radioEnglish = materialRadioButton2;
        this.radioGregorian = materialRadioButton3;
        this.radioHijri = materialRadioButton4;
        this.settingProfileIc = shapeableImageView;
        this.switchBlur = r11;
        this.switchBlur2 = r12;
        this.topBar = toolbar;
        this.tvUserId = textView;
        this.tvUserName = textView2;
    }

    public static LayoutSettingsvactBinding bind(View view) {
        int i4 = R.id.backarrow;
        ImageView imageView = (ImageView) e.l(view, i4);
        if (imageView != null) {
            i4 = R.id.calendarRadioGroup;
            RadioGroup radioGroup = (RadioGroup) e.l(view, i4);
            if (radioGroup != null) {
                i4 = R.id.languageRadioGroup;
                RadioGroup radioGroup2 = (RadioGroup) e.l(view, i4);
                if (radioGroup2 != null) {
                    i4 = R.id.logoutButton;
                    LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
                    if (linearLayout != null) {
                        i4 = R.id.radio_arabic;
                        MaterialRadioButton materialRadioButton = (MaterialRadioButton) e.l(view, i4);
                        if (materialRadioButton != null) {
                            i4 = R.id.radio_english;
                            MaterialRadioButton materialRadioButton2 = (MaterialRadioButton) e.l(view, i4);
                            if (materialRadioButton2 != null) {
                                i4 = R.id.radio_gregorian;
                                MaterialRadioButton materialRadioButton3 = (MaterialRadioButton) e.l(view, i4);
                                if (materialRadioButton3 != null) {
                                    i4 = R.id.radio_hijri;
                                    MaterialRadioButton materialRadioButton4 = (MaterialRadioButton) e.l(view, i4);
                                    if (materialRadioButton4 != null) {
                                        i4 = R.id.setting_profile_ic;
                                        ShapeableImageView shapeableImageView = (ShapeableImageView) e.l(view, i4);
                                        if (shapeableImageView != null) {
                                            i4 = R.id.switch_blur;
                                            Switch r14 = (Switch) e.l(view, i4);
                                            if (r14 != null) {
                                                i4 = R.id.switch_blur2;
                                                Switch r15 = (Switch) e.l(view, i4);
                                                if (r15 != null) {
                                                    i4 = R.id.topBar;
                                                    Toolbar toolbar = (Toolbar) e.l(view, i4);
                                                    if (toolbar != null) {
                                                        i4 = R.id.tvUserId;
                                                        TextView textView = (TextView) e.l(view, i4);
                                                        if (textView != null) {
                                                            i4 = R.id.tvUserName;
                                                            TextView textView2 = (TextView) e.l(view, i4);
                                                            if (textView2 != null) {
                                                                return new LayoutSettingsvactBinding((LinearLayout) view, imageView, radioGroup, radioGroup2, linearLayout, materialRadioButton, materialRadioButton2, materialRadioButton3, materialRadioButton4, shapeableImageView, r14, r15, toolbar, textView, textView2);
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

    public static LayoutSettingsvactBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static LayoutSettingsvactBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.layout_settingsvact, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }
}









