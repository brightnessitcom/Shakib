package com.sandwabe.tecnol;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.LruCache;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DynamicCardRenderer {
    private static final String TAG = "DynamicCardRenderer";

    // 2x Retina resolution matching 1012 x 638 base template (2024 x 1276)
    public static final int CARD_WIDTH = 2024;
    public static final int CARD_HEIGHT = 1276;
    private static final float SCALE = 2.0f;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final LruCache<String, Bitmap> bitmapCache = new LruCache<>(5);

    public interface CardCallback {
        void onCardRendered(@NonNull Bitmap cardBitmap);
        void onError(Exception e);
    }

    private static Typeface bahijMuna;
    private static Typeface cardArial;

    private static synchronized void loadFonts(Context context) {
        if (bahijMuna == null) {
            try {
                bahijMuna = Typeface.createFromAsset(context.getAssets(), "fonts/bahij_muna_black.ttf");
            } catch (Exception e) {
                bahijMuna = Typeface.DEFAULT_BOLD;
            }
        }
        if (cardArial == null) {
            try {
                cardArial = Typeface.createFromAsset(context.getAssets(), "fonts/dig_arial.ttf");
            } catch (Exception e) {
                cardArial = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
            }
        }
    }

    public static void clearCache() {
        bitmapCache.evictAll();
    }

    public static void renderCardAsync(Context context, User user, CardCallback callback) {
        if (user == null) {
            callback.onError(new IllegalArgumentException("User is null"));
            return;
        }

        SharedPreferences prefs = context.getSharedPreferences("UserAppPrefs", Context.MODE_PRIVATE);
        String calType = prefs.getString(Settingsvact.KEY_CALENDAR_TYPE, Settingsvact.CALENDAR_GREGORIAN);
        String cacheKey = (user.getUsername() != null ? user.getUsername() : user.getIqama_number_withid()) 
                + "_" + calType + "_" + user.getId_version() + "_" + user.getIqamaExpire();

        Bitmap cached = bitmapCache.get(cacheKey);
        if (cached != null && !cached.isRecycled()) {
            callback.onCardRendered(cached);
            return;
        }

        executor.execute(() -> {
            try {
                Bitmap rendered = renderCardSync(context.getApplicationContext(), user, calType);
                if (rendered != null) {
                    bitmapCache.put(cacheKey, rendered);
                    mainHandler.post(() -> callback.onCardRendered(rendered));
                } else {
                    mainHandler.post(() -> callback.onError(new Exception("Render returned null")));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error rendering card", e);
                mainHandler.post(() -> callback.onError(e));
            }
        });
    }

    @Nullable
    public static Bitmap renderCardSync(Context context, User user, String calType) {
        loadFonts(context);

        // 1. Create Base Bitmap & Canvas (2024 x 1276)
        Bitmap output = Bitmap.createBitmap(CARD_WIDTH, CARD_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // 2. Draw Background Template
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        Bitmap templateBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.muqeem_card_template, opts);
        if (templateBmp != null) {
            Rect src = new Rect(0, 0, templateBmp.getWidth(), templateBmp.getHeight());
            Rect dst = new Rect(0, 0, CARD_WIDTH, CARD_HEIGHT);
            canvas.drawBitmap(templateBmp, src, dst, null);
            templateBmp.recycle();
        } else {
            canvas.drawColor(Color.WHITE);
        }

        // 3. Draw Resident Photo (Left Column: left: 67, top: 175, width: 253, height: 300)
        String photoUrl = user.getProfile_image_url();
        if (photoUrl == null || photoUrl.isEmpty()) {
            photoUrl = user.getId_image_url();
        }
        Bitmap photoBmp = null;
        if (photoUrl != null && !photoUrl.isEmpty() && !photoUrl.equalsIgnoreCase("null")) {
            try {
                photoBmp = Glide.with(context)
                        .asBitmap()
                        .load(photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .submit(506, 600)
                        .get();
            } catch (Exception e) {
                Log.w(TAG, "Could not load profile photo for card", e);
            }
        }
        if (photoBmp == null) {
            try {
                photoBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.absher);
            } catch (Exception ignored) {}
        }

        if (photoBmp != null) {
            int pLeft = (int) (67 * SCALE);
            int pTop = (int) (175 * SCALE);
            int pRight = (int) ((67 + 253) * SCALE);
            int pBottom = (int) ((175 + 300) * SCALE);
            Rect dstPhoto = new Rect(pLeft, pTop, pRight, pBottom);
            Rect srcPhoto = new Rect(0, 0, photoBmp.getWidth(), photoBmp.getHeight());
            canvas.drawBitmap(photoBmp, srcPhoto, dstPhoto, null);
        }

        // 4. Draw Barcode (Code128 locked inside template's white box: left: 88, top: 594, width: 207, height: 36)
        String barcodeVal = user.getIqama_number_withid();
        if (barcodeVal == null || barcodeVal.isEmpty()) {
            barcodeVal = user.getUsername();
        }
        if (barcodeVal != null && !barcodeVal.isEmpty()) {
            int bWidth = (int) (207 * SCALE);
            int bHeight = (int) (36 * SCALE);
            Bitmap barcodeBmp = generateBarcode(barcodeVal, bWidth, bHeight);
            if (barcodeBmp != null) {
                int bLeft = (int) (88 * SCALE);
                int bTop = (int) (594 * SCALE);
                canvas.drawBitmap(barcodeBmp, bLeft, bTop, null);
                barcodeBmp.recycle();
            }
        }

        // 5. Draw Version Overlay (Top-Left badge: left: 122, top: 102, size: 36)
        String version = user.getId_version();
        if (version == null || version.isEmpty() || version.equalsIgnoreCase("null")) {
            version = "1";
        }
        Paint vPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        vPaint.setTypeface(bahijMuna != null ? bahijMuna : Typeface.DEFAULT_BOLD);
        vPaint.setTextSize(36 * SCALE);
        vPaint.setColor(Color.BLACK);
        vPaint.setTextAlign(Paint.Align.CENTER);
        float vX = (122 + 16) * SCALE;
        float vY = (102 + 28) * SCALE;
        canvas.drawText(ArabicUtils.toArabicNumerals(version), vX, vY, vPaint);

        // 6. Draw Names (Right-aligned, X = 1916)
        // Arabic Name (Top: 178, Text Size: 46 * 2 = 92)
        String nameAr = user.getIqama_holder_name_arabic();
        if (nameAr == null || nameAr.isEmpty() || nameAr.equalsIgnoreCase("null")) {
            nameAr = ArabicUtils.transliterateEnglishNameToArabic(user.getIqama_holder_name());
        }
        Paint nameArPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nameArPaint.setTypeface(bahijMuna != null ? bahijMuna : Typeface.DEFAULT_BOLD);
        nameArPaint.setTextSize(46 * SCALE);
        nameArPaint.setColor(Color.BLACK);
        nameArPaint.setTextAlign(Paint.Align.RIGHT);
        float nameArX = (1012 - 54) * SCALE; // X = 1916px
        float nameArY = (178 + 36) * SCALE;  // Y = 428px
        canvas.drawText(nameAr, nameArX, nameArY, nameArPaint);

        // English Name (Top: 244, Text Size: 26 * 2 = 52, Normal weight)
        String nameEn = user.getIqama_holder_name();
        if (nameEn == null || nameEn.isEmpty() || nameEn.equalsIgnoreCase("null")) {
            nameEn = "MD YASIN AHMED";
        }
        Paint nameEnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nameEnPaint.setTypeface(cardArial != null ? cardArial : Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL));
        nameEnPaint.setTextSize(26 * SCALE);
        nameEnPaint.setColor(Color.BLACK);
        nameEnPaint.setTextAlign(Paint.Align.RIGHT);
        nameEnPaint.setFakeBoldText(false);
        float nameEnX = (1012 - 54) * SCALE; // X = 1916px
        float nameEnY = (244 + 20) * SCALE;  // Y = 528px
        canvas.drawText(nameEn.toUpperCase(), nameEnX, nameEnY, nameEnPaint);

        // 7. Dynamic Fields (Exact 8 rows calibrated 1-to-1 with card-preview.tsx)
        // Field paints
        Paint fieldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fieldPaint.setTypeface(bahijMuna != null ? bahijMuna : Typeface.DEFAULT_BOLD);
        fieldPaint.setTextSize(21 * SCALE); // 42px
        fieldPaint.setColor(Color.BLACK);
        fieldPaint.setTextAlign(Paint.Align.RIGHT);

        Paint numFieldPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numFieldPaint.setTypeface(bahijMuna != null ? bahijMuna : Typeface.DEFAULT_BOLD);
        numFieldPaint.setTextSize(23 * SCALE); // 46px
        numFieldPaint.setColor(Color.BLACK);
        numFieldPaint.setTextAlign(Paint.Align.RIGHT);

        // Baseline X coordinates (Right aligned)
        // Right column (Cols 1 & single fields with padding 126px): (1012 - 54 - 126) * 2 = 1664px
        float colRightX = 1664f;
        // Left column (Col 2 with padding 154px): 485 * 2 = 970px
        float colLeftX = 970f;

        // Baseline Y coordinates for all 8 rows (step ~84.5px)
        float row0Y = 628f;
        float row1Y = 713f;
        float row2Y = 797f;
        float row3Y = 882f;
        float row4Y = 966f;
        float row5Y = 1051f;
        float row6Y = 1135f;
        float row7Y = 1220f;

        // --- ROW 0: ID Number (Right) & Expiry Date (Left) ---
        // Right: رقم الهوية:
        String idNumber = user.getIqama_number_withid();
        if (idNumber == null || idNumber.isEmpty() || idNumber.equalsIgnoreCase("null")) {
            idNumber = user.getUsername();
        }
        if (idNumber != null && !idNumber.isEmpty()) {
            canvas.drawText(ArabicUtils.toArabicNumerals(idNumber), colRightX, row0Y, numFieldPaint);
        }

        // Left: تاريخ الانتهاء:
        String rawExpiry = user.getIqamaExpire();
        String formattedExpiry = DateUtils.formatCardDate(context, rawExpiry);
        if (!formattedExpiry.isEmpty()) {
            canvas.drawText(formattedExpiry, colLeftX, row0Y, numFieldPaint);
        }

        // --- ROW 1: Birth Date (Right) & Birth Place (Left) ---
        // Right: تاريخ الميلاد:
        String rawDob = user.getDateofbirth();
        String formattedDob = DateUtils.formatCardDate(context, rawDob);
        if (!formattedDob.isEmpty()) {
            canvas.drawText(formattedDob, colRightX, row1Y, numFieldPaint);
        }

        // Left: مكان الميلاد: (Always in Arabic, e.g. بنجلاديش)
        String birthPlace = ArabicUtils.toArabicBirthPlace(user.getBirth_city(), user.getBirth_country());
        canvas.drawText(birthPlace, colLeftX, row1Y, fieldPaint);

        // --- ROW 2: Nationality (Right) & Religion (Left) ---
        // Right: الجنسية: (Always in Arabic, e.g. بنجلاديش)
        String nationality = ArabicUtils.toArabicNationality(user.getBirth_country());
        canvas.drawText(nationality, colRightX, row2Y, fieldPaint);

        // Left: الديانة: (Always in Arabic, e.g. الاسلام)
        String religion = ArabicUtils.toArabicReligion(user.getReligion());
        canvas.drawText(religion, colLeftX, row2Y, fieldPaint);

        // --- ROW 3: Profession (Full Width) ---
        // Right: المهنة: (Always in Arabic, e.g. عامل تحميل وتنزيل)
        String profession = ArabicUtils.toArabicProfession(user.getWork_permit());
        canvas.drawText(profession, colRightX, row3Y, fieldPaint);

        // --- ROW 4: Employer ID (Full Width) ---
        // Right: هوية صاحب العمل: (padding: 180px -> X = (1012 - 54 - 180) * 2 = 1556px)
        float employerIdX = 1556f;
        String sponsorId = user.getSponsor_id();
        if (sponsorId == null || sponsorId.isEmpty() || sponsorId.equalsIgnoreCase("null")) {
            sponsorId = "7049112282";
        }
        canvas.drawText(ArabicUtils.toArabicNumerals(sponsorId), employerIdX, row4Y, numFieldPaint);

        // --- ROW 5: Place of Issue (Full Width) ---
        // Right: مكان الإصدار: -> موقع بوابة الوزارة الالكترونية (X = 1664px)
        String issuePlace = ArabicUtils.toArabicIssuePlace(null);
        canvas.drawText(issuePlace, colRightX, row5Y, fieldPaint);

        // --- ROW 6: Work Location (Full Width) ---
        // Right: مكان العمل: -> منطقة الرياض (padding: 121px -> X = (1012 - 54 - 121) * 2 = 1674px)
        float workLocationX = 1674f;
        String workLocation = ArabicUtils.toArabicWorkLocation(null);
        canvas.drawText(workLocation, workLocationX, row6Y, fieldPaint);

        // --- ROW 7: Employer Name (Full Width) ---
        // Right: اسم صاحب العمل: (padding: 183px -> X = (1012 - 54 - 183) * 2 = 1550px)
        float employerNameX = 1550f;
        String sponsorName = user.getSponsor_name();
        if (sponsorName == null || sponsorName.isEmpty() || sponsorName.equalsIgnoreCase("null")) {
            sponsorName = "شركة وضحاء بنت ثمر المطيري";
        }
        if (sponsorName.length() > 38) {
            Paint smallPaint = new Paint(fieldPaint);
            smallPaint.setTextSize(19 * SCALE);
            canvas.drawText(sponsorName, employerNameX, row7Y, smallPaint);
        } else {
            canvas.drawText(sponsorName, employerNameX, row7Y, fieldPaint);
        }

        // 8. Produce Final Anti-Aliased Rounded Card Bitmap (matching web rounded-[28px])
        Bitmap finalOutput = Bitmap.createBitmap(CARD_WIDTH, CARD_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas roundCanvas = new Canvas(finalOutput);

        Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        BitmapShader shader = new BitmapShader(output, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        roundPaint.setShader(shader);

        RectF cardRect = new RectF(0, 0, CARD_WIDTH, CARD_HEIGHT);
        float cornerRadius = 44 * SCALE; // 88px in 2x scale (44px in 1x) - beautifully rounded matching mobile layout
        roundCanvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, roundPaint);

        // Subtle realistic border matching web preview (border border-slate-300/80)
        Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.argb(120, 203, 213, 225));
        borderPaint.setStrokeWidth(1.5f * SCALE);
        RectF borderRect = new RectF(0.75f * SCALE, 0.75f * SCALE, CARD_WIDTH - 0.75f * SCALE, CARD_HEIGHT - 0.75f * SCALE);
        roundCanvas.drawRoundRect(borderRect, cornerRadius, cornerRadius, borderPaint);

        output.recycle();
        return finalOutput;
    }

    private static Bitmap generateBarcode(String data, int width, int height) {
        try {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(data.trim(), BarcodeFormat.CODE_128, width, height);
            int w = matrix.getWidth();
            int h = matrix.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.TRANSPARENT;
                }
            }
            Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bmp.setPixels(pixels, 0, w, 0, 0, w, h);
            return bmp;
        } catch (Exception e) {
            Log.w(TAG, "Barcode generation failed", e);
            return null;
        }
    }
}
