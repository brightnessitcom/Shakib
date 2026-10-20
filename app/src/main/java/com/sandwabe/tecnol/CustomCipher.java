package com.sandwabe.tecnol;

import android.content.Context;

public class CustomCipher {
    public static String decrypt(String str, String secret, Context context) {
        // Plain text URLs/Data in Database.
        // This method simply returns the string as-is.
        return str;
    }

    public static String getSecret(Context context) {
        return "owner_secret_key";
    }

    @Deprecated
    public static String decrypt(String str, String secret) {
        return str;
    }

    @Deprecated
    public static String getSecret() {
        return "owner_secret_key";
    }
}
