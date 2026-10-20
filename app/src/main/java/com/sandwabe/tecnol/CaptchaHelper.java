package com.sandwabe.tecnol;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.widget.ImageView;

import java.util.Random;

public class CaptchaHelper {

    private static final Random RANDOM = new Random();
    private String currentCode = "";

    public String generateCaptcha(ImageView imageView) {
        int code = 1000 + RANDOM.nextInt(9000);
        currentCode = String.valueOf(code);

        int width = 340;
        int height = 90;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        // Subtle gradient background matching official Absher
        Paint bgPaint = new Paint();
        bgPaint.setShader(new LinearGradient(
                0, 0, 0, height,
                Color.parseColor("#E8EBEF"),
                Color.parseColor("#FFFFFF"),
                Shader.TileMode.CLAMP
        ));
        canvas.drawRect(0, 0, width, height, bgPaint);

        float centerY = height / 2f + 2f;
        float startDigitsX = 112f;
        float spacing = 34f;

        // Render digits onto a temporary text bitmap to apply wave ripple scanlines
        Bitmap textBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);

        // Base text paint with bold royal blue
        Paint baseTextPaint = new Paint();
        baseTextPaint.setColor(Color.parseColor("#0E34BC"));
        baseTextPaint.setTextSize(52f);
        baseTextPaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        baseTextPaint.setFakeBoldText(true);
        baseTextPaint.setAntiAlias(true);

        // Soft blur paint for ink feathering
        Paint blurPaint = new Paint(baseTextPaint);
        blurPaint.setColor(Color.parseColor("#0E34BC"));
        blurPaint.setMaskFilter(new BlurMaskFilter(2.0f, BlurMaskFilter.Blur.NORMAL));
        blurPaint.setAlpha(170);

        for (int i = 0; i < currentCode.length(); i++) {
            float x = startDigitsX + i * spacing + (RANDOM.nextInt(4) - 2);
            float y = centerY + 17f + (RANDOM.nextInt(4) - 2);
            float rot = (RANDOM.nextInt(8) - 4);
            String digit = String.valueOf(currentCode.charAt(i));

            textCanvas.save();
            textCanvas.rotate(rot, x, y);

            // Draw blurred halo behind digit
            textCanvas.drawText(digit, x, y, blurPaint);

            // Draw micro-jitter passes for authentic ink spread
            textCanvas.drawText(digit, x - 0.7f, y + 0.5f, blurPaint);
            textCanvas.drawText(digit, x + 0.7f, y - 0.5f, blurPaint);

            // Draw main digit
            textCanvas.drawText(digit, x, y, baseTextPaint);

            textCanvas.restore();
        }

        // Apply authentic sinusoidal wave ripple (scanline displacement) onto canvas
        android.graphics.Rect srcRect = new android.graphics.Rect();
        android.graphics.RectF dstRect = new android.graphics.RectF();
        float waveFreq = 0.28f + RANDOM.nextFloat() * 0.12f;
        float waveAmp = 1.3f + RANDOM.nextFloat() * 0.6f;
        float phase = RANDOM.nextFloat() * 6.28f;

        for (int y = 0; y < height; y++) {
            float shiftX = (float) (Math.sin(y * waveFreq + phase) * waveAmp);
            srcRect.set(0, y, width, y + 1);
            dstRect.set(shiftX, y, width + shiftX, y + 1);
            canvas.drawBitmap(textBitmap, srcRect, dstRect, null);
        }

        // Add subtle ink specks around digits
        Paint speckPaint = new Paint();
        speckPaint.setColor(Color.parseColor("#0E34BC"));
        speckPaint.setAlpha(85);
        speckPaint.setAntiAlias(true);
        for (int s = 0; s < 14; s++) {
            float sx = startDigitsX - 12f + RANDOM.nextFloat() * (spacing * 4 + 24f);
            float sy = centerY - 16f + RANDOM.nextFloat() * 42f;
            canvas.drawCircle(sx, sy, 0.7f + RANDOM.nextFloat() * 1.3f, speckPaint);
        }

        // Security Stroke Paint
        Paint linePaint = new Paint();
        linePaint.setColor(Color.parseColor("#141414"));
        linePaint.setStrokeWidth(3.4f);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setAntiAlias(true);

        // Randomly pick one of 3 authentic security stroke archetypes
        int archetype = RANDOM.nextInt(3);

        if (archetype == 0) {
            // Archetype 0: Stepped Hook / Crank Line + Top Skimming Bar (as in official Absher 0747)
            float stepStartX = 55f + RANDOM.nextFloat() * 12f;
            float stepStartY = centerY + 18f + RANDOM.nextFloat() * 8f;
            float cornerX = stepStartX + 8f + RANDOM.nextFloat() * 8f;
            float cornerY = centerY - 1f + RANDOM.nextFloat() * 6f;
            float hookEndX = cornerX + 30f + RANDOM.nextFloat() * 12f;
            float hookEndY = cornerY - 2f;

            Path crankPath = new Path();
            crankPath.moveTo(stepStartX, stepStartY);
            crankPath.lineTo(cornerX, cornerY);
            crankPath.lineTo(hookEndX, hookEndY);
            canvas.drawPath(crankPath, linePaint);

            // Upper skimming bar
            float barStartX = stepStartX - 5f + RANDOM.nextFloat() * 6f;
            float barStartY = cornerY - 14f - RANDOM.nextFloat() * 6f;
            float barEndX = width - 20f - RANDOM.nextFloat() * 15f;
            float barEndY = barStartY + (RANDOM.nextFloat() * 6f - 3f);

            Path barPath = new Path();
            barPath.moveTo(barStartX, barStartY);
            float cpX = barStartX + (barEndX - barStartX) * 0.5f;
            float cpY = (barStartY + barEndY) / 2f + (RANDOM.nextFloat() * 6f - 3f);
            barPath.quadTo(cpX, cpY, barEndX, barEndY);
            canvas.drawPath(barPath, linePaint);

        } else if (archetype == 1) {
            // Archetype 1: Left Arrowhead with Downward Hook Barb (as in official Absher 3116)
            float startX = 42f + RANDOM.nextInt(8);
            float startY = centerY - 2f + (RANDOM.nextInt(6) - 3);
            float endX = width - 25f - RANDOM.nextInt(15);
            float endY = centerY + (RANDOM.nextFloat() * 14f - 7f);

            Path mainLine = new Path();
            mainLine.moveTo(startX, startY);
            float cp1X = startX + (endX - startX) * 0.4f;
            float cp1Y = centerY + (RANDOM.nextFloat() * 12f - 6f);
            float cp2X = startX + (endX - startX) * 0.7f;
            float cp2Y = centerY + (RANDOM.nextFloat() * 12f - 6f);
            mainLine.cubicTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY);
            canvas.drawPath(mainLine, linePaint);

            // Upper barb (straight up-left)
            Path fork1 = new Path();
            fork1.moveTo(startX, startY);
            fork1.lineTo(startX - 20f - RANDOM.nextFloat() * 8f, startY - 10f - RANDOM.nextFloat() * 8f);
            canvas.drawPath(fork1, linePaint);

            // Lower barb (curving downward into hook)
            Path fork2 = new Path();
            fork2.moveTo(startX, startY);
            float hookCpX = startX - 12f;
            float hookCpY = startY + 12f;
            float hookDestX = startX - 18f - RANDOM.nextFloat() * 10f;
            float hookDestY = startY + 22f + RANDOM.nextFloat() * 10f;
            fork2.quadTo(hookCpX, hookCpY, hookDestX, hookDestY);
            canvas.drawPath(fork2, linePaint);

        } else {
            // Archetype 2: Curved Descending Arc with Forked Barbs (as in official Absher 7249)
            float startX = 40f + RANDOM.nextInt(8);
            float startY = centerY - 6f + (RANDOM.nextInt(6) - 3);
            float endX = width - 25f - RANDOM.nextInt(15);
            float endY = centerY + 16f + RANDOM.nextFloat() * 14f;

            Path mainLine = new Path();
            mainLine.moveTo(startX, startY);
            float cp1X = startX + (endX - startX) * 0.35f;
            float cp1Y = centerY - 10f + (RANDOM.nextFloat() * 8f);
            float cp2X = startX + (endX - startX) * 0.70f;
            float cp2Y = centerY + 4f + (RANDOM.nextFloat() * 8f);
            mainLine.cubicTo(cp1X, cp1Y, cp2X, cp2Y, endX, endY);
            canvas.drawPath(mainLine, linePaint);

            // Upper fork
            Path fork1 = new Path();
            fork1.moveTo(startX, startY);
            fork1.lineTo(startX - 22f - RANDOM.nextFloat() * 10f, startY - 8f - RANDOM.nextFloat() * 10f);
            canvas.drawPath(fork1, linePaint);

            // Lower fork
            Path fork2 = new Path();
            fork2.moveTo(startX - 2f, startY + 4f);
            fork2.lineTo(startX - 20f - RANDOM.nextFloat() * 10f, startY + 14f + RANDOM.nextFloat() * 12f);
            canvas.drawPath(fork2, linePaint);
        }

        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }

        return currentCode;
    }

    public String getCurrentCode() {
        return currentCode;
    }
}
