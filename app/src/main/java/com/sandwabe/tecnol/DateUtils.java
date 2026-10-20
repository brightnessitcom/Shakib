package com.sandwabe.tecnol;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.IslamicCalendar;
import android.icu.util.ULocale;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    
    public static String formatDisplayDate(Context context, String dateStr) {
        if (dateStr == null || dateStr.isEmpty() || dateStr.equalsIgnoreCase("null") || dateStr.equals("-") || dateStr.equals("_")) {
            return dateStr;
        }

        try {
            // Support both - and / as separators
            String cleanDate = dateStr.replace("/", "-");
            String[] parts = cleanDate.split("-");
            if (parts.length != 3) return dateStr;

            int year, month, day;
            // Detect which part is the year (usually 4 digits or at a specific position)
            if (parts[0].length() == 4) { // yyyy-MM-dd
                year = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                day = Integer.parseInt(parts[2]);
            } else if (parts[2].length() == 4) { // dd-MM-yyyy
                day = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                year = Integer.parseInt(parts[2]);
            } else {
                // Fallback: assume last part is year if not specified
                day = Integer.parseInt(parts[0]);
                month = Integer.parseInt(parts[1]);
                year = Integer.parseInt(parts[2]);
            }

            SharedPreferences prefs = context.getSharedPreferences("UserAppPrefs", Context.MODE_PRIVATE);
            String calendarType = prefs.getString(Settingsvact.KEY_CALENDAR_TYPE, Settingsvact.CALENDAR_GREGORIAN);
            boolean showHijri = Settingsvact.CALENDAR_HIJRI.equals(calendarType);

            // Heuristic: If year < 1600, it's treated as Hijri input
            boolean isInputHijri = (year < 1600);

            if (isInputHijri == showHijri) {
                // Already in desired format, just ensure English digits and dd/MM/yyyy
                return String.format(Locale.US, "%02d/%02d/%04d", day, month, year);
            }

            if (showHijri) {
                // Convert Gregorian (Input) -> Hijri (Display)
                java.util.Calendar gregCal = java.util.Calendar.getInstance();
                gregCal.set(year, month - 1, day, 12, 0); // Noon to avoid boundary errors
                
                ULocale locale = new ULocale("en@calendar=islamic-umalqura");
                IslamicCalendar hijriCalendar = new IslamicCalendar(locale);
                hijriCalendar.setTimeInMillis(gregCal.getTimeInMillis());
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", locale);
                return sdf.format(hijriCalendar);
            } else {
                // Convert Hijri (Input) -> Gregorian (Display)
                ULocale locale = new ULocale("en@calendar=islamic-umalqura");
                IslamicCalendar hijriCalendar = new IslamicCalendar(locale);
                hijriCalendar.set(IslamicCalendar.YEAR, year);
                hijriCalendar.set(IslamicCalendar.MONTH, month - 1);
                hijriCalendar.set(IslamicCalendar.DAY_OF_MONTH, day);
                hijriCalendar.set(IslamicCalendar.HOUR_OF_DAY, 12);
                
                Date date = new Date(hijriCalendar.getTimeInMillis());
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.US);
                return sdf.format(date);
            }
        } catch (Exception e) {
            return dateStr;
        }
    }

    public static String formatCurrentDateTime(Context context) {
        String pattern = "EEEE dd MMMM yyyy h:mm a";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.ENGLISH);
        return sdf.format(new java.util.Date());
    }

    public static void toggleCalendarType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UserAppPrefs", Context.MODE_PRIVATE);
        String current = prefs.getString(Settingsvact.KEY_CALENDAR_TYPE, Settingsvact.CALENDAR_GREGORIAN);
        String next = Settingsvact.CALENDAR_HIJRI.equals(current) ? Settingsvact.CALENDAR_GREGORIAN : Settingsvact.CALENDAR_HIJRI;
        prefs.edit().putString(Settingsvact.KEY_CALENDAR_TYPE, next).apply();
    }
}
