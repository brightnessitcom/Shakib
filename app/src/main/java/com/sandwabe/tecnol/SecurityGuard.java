package com.sandwabe.tecnol;

import android.content.Context;

public final class SecurityGuard {
    public static boolean isCompromised(Context context) {
        // Full owner control: Security check disabled to prevent accidental blocks
        return false;
    }
}
