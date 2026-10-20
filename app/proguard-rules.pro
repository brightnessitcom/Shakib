# --- SECURE CONFIGURATION ---

# ১. কোড এমনভাবে হিজিবিজি (Obfuscate) করা হবে যাতে কেউ সহজে বুঝতে না পারে
-optimizationpasses 5
-allowaccessmodification
-overloadaggressively

# ২. Metadata এবং Debug তথ্য রিমুভ করা
-keepattributes !SourceFile,!LineNumberTable,!Signature,!InnerClasses,!EnclosingMethod,!Deprecated,!RuntimeVisibleAnnotations,!RuntimeInvisibleAnnotations

# ৩. সব ধরণের লগ (Log) রিমুভ করা
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

# ৪. অ্যাপের রিসোর্স এবং ডাটা প্রোটেকশন
-keepclassmembernames class * {
    @com.google.firebase.database.PropertyName <fields>;
}

# --- KEEP RULES (অ্যাপ যাতে ক্রাশ না করে) ---

# Android Framework
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View
-keep public class * extends androidx.fragment.app.Fragment

# Firebase & Google Services
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class com.bumptech.glide.** { *; }

# ZXing
-keep class com.google.zxing.** { *; }
-keep class com.journeyapps.barcodescanner.** { *; }

# Lottie
-keep class com.airbnb.lottie.** { *; }

# App Models & Classes
-keep class com.sandwabe.tecnol.** { *; }
-keepclassmembers class com.sandwabe.tecnol.** {
    public <init>(...);
    void set*(...);
    *** get*();
}

# Appwrite ProGuard Rules
-keep class io.appwrite.** { *; }
-keep class io.appwrite.models.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn io.appwrite.**

# ViewBinding and Helpers
-keep class com.sandwabe.tecnol.e { *; }
-keep class com.sandwabe.tecnol.databinding.** { *; }
-keep class com.sandwabe.tecnol.CustomCipher { *; }
-keep class com.sandwabe.tecnol.NativeHelper { *; }
-keep class com.sandwabe.tecnol.UpdateChecker { *; }

# Marshaling/Unmarshaling issues এড়াতে
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Biometric Keep Rules
-keep class androidx.biometric.** { *; }
