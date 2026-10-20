package com.sandwabe.tecnol;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {
    private static final int MODULES_V30 = 177;
    private static final String MODULE_SHAPE = "square";
    private static final ErrorCorrectionLevel QR_ERROR_CORRECTION = ErrorCorrectionLevel.M;
    private static final int QR_MARGIN = 3;
    private static final int QR_SIZE = 3540;
    private static final int QR_VERSION = 30;
    private static final String TAG = "QRCodeGenerator";

    public static float calculateDataDensity(String str) {
        try {
            Map<EncodeHintType, Object> map = new HashMap<>();
            map.put(EncodeHintType.ERROR_CORRECTION, QR_ERROR_CORRECTION);
            map.put(EncodeHintType.MARGIN, 3);
            map.put(EncodeHintType.QR_VERSION, QR_VERSION);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, MODULES_V30, MODULES_V30, map);
            int i4 = 0;
            for (int i5 = 0; i5 < MODULES_V30; i5++) {
                for (int i6 = 0; i6 < MODULES_V30; i6++) {
                    if (bitMatrix.get(i6, i5)) {
                        i4++;
                    }
                }
            }
            return (i4 / (float)(MODULES_V30 * MODULES_V30)) * 100.0f;
        } catch (Exception e) {
            Log.e(TAG, "Error calculating density", e);
            return 0.0f;
        }
    }

    private static void drawRoundedRect(Canvas canvas, float f4, float f5, float f6, float f7, float f8, Paint paint) {
        Path path = new Path();
        float f9 = f4 + f8;
        path.moveTo(f9, f5);
        float f10 = f6 - f8;
        path.lineTo(f10, f5);
        float f11 = f5 + f8;
        path.quadTo(f6, f5, f6, f11);
        float f12 = f7 - f8;
        path.lineTo(f6, f12);
        path.quadTo(f6, f7, f10, f7);
        path.lineTo(f9, f7);
        path.quadTo(f4, f7, f4, f12);
        path.lineTo(f4, f11);
        path.quadTo(f4, f5, f9, f5);
        path.close();
        canvas.drawPath(path, paint);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable == null) {
            return null;
        }
        if ((drawable instanceof BitmapDrawable) && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null) {
            return bitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(Math.max(drawable.getIntrinsicWidth(), 100), Math.max(drawable.getIntrinsicHeight(), 100), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    public static Bitmap generateBankStyleQRCode(String str, int i4) {
        try {
            Map<EncodeHintType, Object> map = new HashMap<>();
            map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            map.put(EncodeHintType.MARGIN, 4);
            map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            int i7 = i4 * 3;
            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, i7, i7, map);
                Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i7, i7, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmapCreateBitmap);
                canvas.drawColor(-1);
                int i8 = bitMatrix.getWidth();
                int i9 = bitMatrix.getHeight();
                float f4 = i7;
                float f5 = f4 / i8;
                float f6 = f4 / i9;
                Paint paint = new Paint();
                paint.setColor(-16777216);
                paint.setStyle(Paint.Style.FILL);
                paint.setAntiAlias(false);
                for (int i11 = 0; i11 < i9; i11++) {
                    for (int i12 = 0; i12 < i8; i12++) {
                        if (bitMatrix.get(i12, i11)) {
                            canvas.drawRect(i12 * f5, i11 * f6, (i12 + 1) * f5, (i11 + 1) * f6, paint);
                        }
                    }
                }
                Bitmap bitmapCreateBitmap2 = Bitmap.createBitmap(i4, i4, Bitmap.Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmapCreateBitmap2);
                canvas2.drawColor(-1);
                Paint paint2 = new Paint();
                paint2.setFilterBitmap(false);
                float f7 = i4;
                canvas2.drawBitmap(bitmapCreateBitmap, (Rect) null, new RectF(0.0f, 0.0f, f7, f7), paint2);
                bitmapCreateBitmap.recycle();
                return bitmapCreateBitmap2;
            } catch (WriterException e) {
                Log.e(TAG, "Error generating bank-style QR code", e);
                return generateQRCode(str, i4);
            }
        } catch (Exception e4) {
             Log.e(TAG, "General error in bank-style QR generation", e4);
             return null;
        }
    }

    public static Bitmap generateColoredQRCode(String str, int i4, int i5) {
        try {
            Map<EncodeHintType, Object> map = new HashMap<>();
            map.put(EncodeHintType.ERROR_CORRECTION, QR_ERROR_CORRECTION);
            map.put(EncodeHintType.MARGIN, 3);
            map.put(EncodeHintType.QR_VERSION, QR_VERSION);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE, map);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(QR_SIZE, QR_SIZE, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            canvas.drawColor(i5);
            int i6 = bitMatrix.getWidth();
            int i7 = bitMatrix.getHeight();
            float f4 = 3540.0f / i6;
            float f5 = 3540.0f / i7;
            Paint paint = new Paint();
            paint.setColor(i4);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(false);
            for (int i8 = 0; i8 < i7; i8++) {
                for (int i9 = 0; i9 < i6; i9++) {
                    if (bitMatrix.get(i9, i8)) {
                        float f6 = i9 * f4;
                        float f7 = i8 * f5;
                        canvas.drawRect(Math.round(f6), Math.round(f7), Math.round(f6 + f4), Math.round(f7 + f5), paint);
                    }
                }
            }
            return bitmapCreateBitmap;
        } catch (Exception e) {
            Log.e(TAG, "Error generating colored QR code", e);
            return null;
        }
    }

    public static Bitmap generateHighQualityQRCode(String str) {
        return generateHighQualityQRCode(str, QR_SIZE);
    }

    public static Bitmap generateQRCode(String str) {
        return generateQRCode(str, QR_SIZE);
    }

    private static Bitmap generateSimpleQRCode(String str, int i4) {
        try {
            Map<EncodeHintType, Object> map = new HashMap<>();
            map.put(EncodeHintType.ERROR_CORRECTION, QR_ERROR_CORRECTION);
            map.put(EncodeHintType.MARGIN, 3);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, i4, i4, map);
            return renderQRCode(bitMatrix, i4, MODULE_SHAPE);
        } catch (Exception e) {
            Log.e(TAG, "Error in simple QR generation", e);
            return null;
        }
    }

    public static String getErrorCorrection() {
        return "M (15%)";
    }

    public static int getMargin() {
        return 3;
    }

    public static int getModuleCount() {
        return MODULES_V30;
    }

    public static int getQRSize() {
        return QR_SIZE;
    }

    private static Bitmap renderQRCode(BitMatrix bVar, int i4, String str) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i4, i4, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        canvas.drawColor(-1);
        int i6 = bVar.getWidth();
        int i7 = bVar.getHeight();
        float f4 = i4;
        float f5 = f4 / i6;
        float f6 = f4 / i7;
        Paint paint = new Paint();
        paint.setColor(-16777216);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        paint.setFilterBitmap(false);
        for (int i9 = 0; i9 < i7; i9++) {
            for (int i10 = 0; i10 < i6; i10++) {
                if (bVar.get(i10, i9)) {
                    float f7 = i10 * f5;
                    float f8 = i9 * f6;
                    float f9 = f7 + f5;
                    float f10 = f8 + f6;
                    if (str.equals("circle")) {
                        canvas.drawCircle((f7 + f9) / 2.0f, (f8 + f10) / 2.0f, Math.min(f5, f6) / 2.0f, paint);
                    } else if (str.equals("rounded")) {
                        drawRoundedRect(canvas, f7, f8, f9, f10, 0.15f * f5, paint);
                    } else {
                        canvas.drawRect(Math.round(f7), Math.round(f8), Math.round(f9), Math.round(f10), paint);
                    }
                }
            }
        }
        return bitmapCreateBitmap;
    }

    public static Bitmap generateHighQualityQRCode(String str, int i4) {
        try {
            Map<EncodeHintType, Object> map = new HashMap<>();
            map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            map.put(EncodeHintType.MARGIN, 3);
            map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            int i5 = i4 * 4;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, i5, i5, map);
            Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i5, i5, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapCreateBitmap);
            canvas.drawColor(-1);
            int i8 = bitMatrix.getWidth();
            int i9 = bitMatrix.getHeight();
            float f4 = i5;
            float f5 = f4 / i8;
            float f6 = f4 / i9;
            Paint paint = new Paint();
            paint.setColor(-16777216);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            for (int i10 = 0; i10 < i9; i10++) {
                for (int i11 = 0; i11 < i8; i11++) {
                    if (bitMatrix.get(i11, i10)) {
                        float f7 = i11 * f5;
                        float f8 = i10 * f6;
                        canvas.drawRect(f7, f8, f7 + f5, f8 + f6, paint);
                    }
                }
            }
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmapCreateBitmap, i4, i4, true);
            bitmapCreateBitmap.recycle();
            return bitmapCreateScaledBitmap;
        } catch (Exception e) {
            Log.e(TAG, "Error generating high quality QR code", e);
            return generateQRCode(str, i4);
        }
    }

    public static Bitmap generateQRCode(String str, int i4) {
        try {
            Map<EncodeHintType, Object> map = new HashMap<>();
            map.put(EncodeHintType.ERROR_CORRECTION, QR_ERROR_CORRECTION);
            map.put(EncodeHintType.MARGIN, 3);
            map.put(EncodeHintType.QR_VERSION, QR_VERSION);
            map.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            int i5 = i4 * 2;
            BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, i5, i5, map);
            return renderQRCode(bitMatrix, i4, MODULE_SHAPE);
        } catch (Exception e) {
            Log.e(TAG, "Error generating QR code", e);
            return generateSimpleQRCode(str, i4);
        }
    }

    public static Bitmap generateBankStyleQRCode(String str) {
        return generateBankStyleQRCode(str, QR_SIZE);
    }
}






