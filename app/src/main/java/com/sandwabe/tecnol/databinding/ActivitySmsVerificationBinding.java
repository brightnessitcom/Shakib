package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sandwabe.tecnol.e;
import com.google.android.material.button.MaterialButton;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ActivitySmsVerificationBinding {
    public final LinearLayout bottomContainer;
    public final MaterialButton btnUseAuthenticator;
    public final LinearLayout codeContainer;
    public final EditText etCode1;
    public final EditText etCode2;
    public final EditText etCode3;
    public final EditText etCode4;
    public final ImageView ivBack;
    public final ImageView ivLogoLeft;
    public final ImageView ivLogoRight;
    private final FrameLayout rootView;
    public final TextView tvError;
    public final TextView tvInstruction1;
    public final TextView tvPhoneNumber;
    public final TextView tvPhoneNumberNormal;
    public final TextView tvTitle;

    private ActivitySmsVerificationBinding(FrameLayout frameLayout, LinearLayout linearLayout, MaterialButton materialButton, LinearLayout linearLayout2, EditText editText, EditText editText2, EditText editText3, EditText editText4, ImageView imageView, ImageView imageView2, ImageView imageView3, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5) {
        this.rootView = frameLayout;
        this.bottomContainer = linearLayout;
        this.btnUseAuthenticator = materialButton;
        this.codeContainer = linearLayout2;
        this.etCode1 = editText;
        this.etCode2 = editText2;
        this.etCode3 = editText3;
        this.etCode4 = editText4;
        this.ivBack = imageView;
        this.ivLogoLeft = imageView2;
        this.ivLogoRight = imageView3;
        this.tvError = textView;
        this.tvInstruction1 = textView2;
        this.tvPhoneNumber = textView3;
        this.tvPhoneNumberNormal = textView4;
        this.tvTitle = textView5;
    }

    public static ActivitySmsVerificationBinding bind(View view) {
        int i4 = R.id.bottomContainer;
        LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
        if (linearLayout != null) {
            i4 = R.id.btnUseAuthenticator;
            MaterialButton materialButton = (MaterialButton) e.l(view, i4);
            if (materialButton != null) {
                i4 = R.id.codeContainer;
                LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                if (linearLayout2 != null) {
                    i4 = R.id.etCode1;
                    EditText editText = (EditText) e.l(view, i4);
                    if (editText != null) {
                        i4 = R.id.etCode2;
                        EditText editText2 = (EditText) e.l(view, i4);
                        if (editText2 != null) {
                            i4 = R.id.etCode3;
                            EditText editText3 = (EditText) e.l(view, i4);
                            if (editText3 != null) {
                                i4 = R.id.etCode4;
                                EditText editText4 = (EditText) e.l(view, i4);
                                if (editText4 != null) {
                                    i4 = R.id.ivBack;
                                    ImageView imageView = (ImageView) e.l(view, i4);
                                    if (imageView != null) {
                                        i4 = R.id.ivLogoLeft;
                                        ImageView imageView2 = (ImageView) e.l(view, i4);
                                        if (imageView2 != null) {
                                            i4 = R.id.ivLogoRight;
                                            ImageView imageView3 = (ImageView) e.l(view, i4);
                                            if (imageView3 != null) {
                                                i4 = R.id.tvError;
                                                TextView textView = (TextView) e.l(view, i4);
                                                if (textView != null) {
                                                    i4 = R.id.tvInstruction1;
                                                    TextView textView2 = (TextView) e.l(view, i4);
                                                    if (textView2 != null) {
                                                        i4 = R.id.tvPhoneNumber;
                                                        TextView textView3 = (TextView) e.l(view, i4);
                                                        if (textView3 != null) {
                                                            i4 = R.id.tvPhoneNumberNormal;
                                                            TextView textView4 = (TextView) e.l(view, i4);
                                                            if (textView4 != null) {
                                                                i4 = R.id.tvTitle;
                                                                TextView textView5 = (TextView) e.l(view, i4);
                                                                if (textView5 != null) {
                                                                    return new ActivitySmsVerificationBinding((FrameLayout) view, linearLayout, materialButton, linearLayout2, editText, editText2, editText3, editText4, imageView, imageView2, imageView3, textView, textView2, textView3, textView4, textView5);
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

    public static ActivitySmsVerificationBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ActivitySmsVerificationBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.activity_sms_verification, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }
}









