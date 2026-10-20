package com.sandwabe.tecnol;

import android.content.Context;

public class NativeHelper {
    public static void setContext(Context context) {
        // Disabled native code
    }

    public static String getAbsherUrl(Context context) {
        return "https://www.absher.sa";
    }

    public static String getUpdateUrl(Context context) {
        return "https://www.absher.sa"; 
    }

    public static String getBlockedDialogTitle(Context context) {
        return "Account Alert";
    }

    public static String getBlockedDialogMessage(Context context) {
        return "Your access has expired. Please contact support.";
    }

    public static String getNoInternetDialogTitle(Context context) {
        return "No Connection";
    }

    public static String getNoInternetMessage(Context context) {
        return "Please check your internet connection and try again.";
    }
}
