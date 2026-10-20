package com.sandwabe.tecnol.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.sandwabe.tecnol.e;
import com.google.android.material.button.MaterialButton;
import com.sandwabe.tecnol.R;

/* JADX INFO: loaded from: classes.dex */
public final class ActivityLoginBinding {
    public final MaterialButton btnLogin;
    public final CheckBox cbRememberMe;
    public final EditText etPassword;
    public final EditText etUsername;
    public final ImageView ivBack;
    public final LinearLayout passwordContainer;
    private final ConstraintLayout rootView;
    public final ScrollView scrollView;
    public final TextView tvForgotPassword;
    public final LinearLayout usernameContainer;

    private ActivityLoginBinding(ConstraintLayout constraintLayout, MaterialButton materialButton, CheckBox checkBox, EditText editText, EditText editText2, ImageView imageView, LinearLayout linearLayout, ScrollView scrollView, TextView textView, LinearLayout linearLayout2) {
        this.rootView = constraintLayout;
        this.btnLogin = materialButton;
        this.cbRememberMe = checkBox;
        this.etPassword = editText;
        this.etUsername = editText2;
        this.ivBack = imageView;
        this.passwordContainer = linearLayout;
        this.scrollView = scrollView;
        this.tvForgotPassword = textView;
        this.usernameContainer = linearLayout2;
    }

    public static ActivityLoginBinding bind(View view) {
        int i4 = R.id.btnLogin;
        MaterialButton materialButton = (MaterialButton) e.l(view, i4);
        if (materialButton != null) {
            i4 = R.id.cbRememberMe;
            CheckBox checkBox = (CheckBox) e.l(view, i4);
            if (checkBox != null) {
                i4 = R.id.etPassword;
                EditText editText = (EditText) e.l(view, i4);
                if (editText != null) {
                    i4 = R.id.etUsername;
                    EditText editText2 = (EditText) e.l(view, i4);
                    if (editText2 != null) {
                        i4 = R.id.ivBack;
                        ImageView imageView = (ImageView) e.l(view, i4);
                        if (imageView != null) {
                            i4 = R.id.passwordContainer;
                            LinearLayout linearLayout = (LinearLayout) e.l(view, i4);
                            if (linearLayout != null) {
                                i4 = R.id.scrollView;
                                ScrollView scrollView = (ScrollView) e.l(view, i4);
                                if (scrollView != null) {
                                    i4 = R.id.tvForgotPassword;
                                    TextView textView = (TextView) e.l(view, i4);
                                    if (textView != null) {
                                        i4 = R.id.usernameContainer;
                                        LinearLayout linearLayout2 = (LinearLayout) e.l(view, i4);
                                        if (linearLayout2 != null) {
                                            return new ActivityLoginBinding((ConstraintLayout) view, materialButton, checkBox, editText, editText2, imageView, linearLayout, scrollView, textView, linearLayout2);
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

    public static ActivityLoginBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, null, false);
    }

    public static ActivityLoginBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z2) {
        View viewInflate = layoutInflater.inflate(R.layout.activity_login, viewGroup, false);
        if (z2) {
            viewGroup.addView(viewInflate);
        }
        return bind(viewInflate);
    }

    public ConstraintLayout getRoot() {
        return this.rootView;
    }
}









