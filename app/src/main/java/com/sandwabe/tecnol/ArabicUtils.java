package com.sandwabe.tecnol;

import java.util.HashMap;
import java.util.Map;

public class ArabicUtils {
    private static final char[] ARABIC_DIGITS = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};

    private static final Map<String, String> NAME_DICT = new HashMap<>();
    private static final Map<String, String> COUNTRY_DICT = new HashMap<>();
    private static final Map<String, String> RELIGION_DICT = new HashMap<>();

    static {
        // Common Name Dictionary (Muslim / Arabic / South Asian)
        NAME_DICT.put("MD", "محمد");
        NAME_DICT.put("MD.", "محمد");
        NAME_DICT.put("MOHAMMAD", "محمد");
        NAME_DICT.put("MOHAMED", "محمد");
        NAME_DICT.put("MOHAMMED", "محمد");
        NAME_DICT.put("MUHAMMAD", "محمد");
        NAME_DICT.put("MUHAMMED", "محمد");
        NAME_DICT.put("YASIN", "ياسين");
        NAME_DICT.put("YASEEN", "ياسين");
        NAME_DICT.put("YASSIN", "ياسين");
        NAME_DICT.put("AHMED", "احمد");
        NAME_DICT.put("AHMAD", "احمد");
        NAME_DICT.put("ALI", "علي");
        NAME_DICT.put("HOSSAIN", "حسين");
        NAME_DICT.put("HUSAIN", "حسين");
        NAME_DICT.put("HUSSAIN", "حسين");
        NAME_DICT.put("HOSEN", "حسين");
        NAME_DICT.put("HASAN", "حسن");
        NAME_DICT.put("HASSAN", "حسن");
        NAME_DICT.put("RAHMAN", "رحمن");
        NAME_DICT.put("ROHMAN", "رحمن");
        NAME_DICT.put("RASEL", "راسل");
        NAME_DICT.put("RUSSEL", "راسل");
        NAME_DICT.put("ROSEL", "راسل");
        NAME_DICT.put("MIA", "ميا");
        NAME_DICT.put("MIAH", "ميا");
        NAME_DICT.put("MIYA", "ميا");
        NAME_DICT.put("KHAN", "خان");
        NAME_DICT.put("ISLAM", "اسلام");
        NAME_DICT.put("SHEIKH", "شيخ");
        NAME_DICT.put("SHAIKH", "شيخ");
        NAME_DICT.put("UDDIN", "الدين");
        NAME_DICT.put("ALOM", "عالم");
        NAME_DICT.put("ALAM", "عالم");
        NAME_DICT.put("CHOWDHURY", "شودري");
        NAME_DICT.put("BEGUM", "بيجوم");
        NAME_DICT.put("AKTER", "اختر");
        NAME_DICT.put("AKHTAR", "اختر");
        NAME_DICT.put("KABIR", "كبير");
        NAME_DICT.put("MAHMUD", "محمود");
        NAME_DICT.put("MAHMOOD", "محمود");
        NAME_DICT.put("IBRAHIM", "ابراهيم");
        NAME_DICT.put("ISMAIL", "اسماعيل");
        NAME_DICT.put("RANA", "رنا");
        NAME_DICT.put("SHAHIN", "شاهين");
        NAME_DICT.put("SHAHEEN", "شاهين");
        NAME_DICT.put("RUBEL", "روبيل");
        NAME_DICT.put("BILLAH", "بالله");
        NAME_DICT.put("SARKAR", "سركار");
        NAME_DICT.put("MOSTAFA", "مصطفى");
        NAME_DICT.put("MUSTAFA", "مصطفى");
        NAME_DICT.put("SHOHAG", "سوهاج");
        NAME_DICT.put("SUMON", "سومون");
        NAME_DICT.put("BABU", "بابو");
        NAME_DICT.put("ROFIQ", "رفيق");
        NAME_DICT.put("RAFIQ", "رفيق");
        NAME_DICT.put("SHAFIQ", "شفيق");
        NAME_DICT.put("SAFIQ", "شفيق");
        NAME_DICT.put("TARIQ", "طارق");
        NAME_DICT.put("TAREQ", "طارق");
        NAME_DICT.put("SHAKIL", "شكيل");
        NAME_DICT.put("SAIFUL", "سيف الاسلام");
        NAME_DICT.put("SAYFUL", "سيف الاسلام");
        NAME_DICT.put("AZIZ", "عزيز");
        NAME_DICT.put("KARIM", "كريم");
        NAME_DICT.put("SOHEL", "سهيل");
        NAME_DICT.put("ABDUL", "عبدال");
        NAME_DICT.put("ABDOUL", "عبدال");
        NAME_DICT.put("ABDULLAH", "عبدالله");
        NAME_DICT.put("ABID", "عابد");
        NAME_DICT.put("ABIR", "عبير");
        NAME_DICT.put("ABU", "ابو");
        NAME_DICT.put("AL", "ال");
        NAME_DICT.put("AMIN", "امين");
        NAME_DICT.put("ANWAR", "انور");
        NAME_DICT.put("ARIF", "عارف");
        NAME_DICT.put("ASHIK", "عاشق");
        NAME_DICT.put("ASHRAFUL", "اشرف");
        NAME_DICT.put("ASIF", "عاصف");
        NAME_DICT.put("EMRAN", "عمران");
        NAME_DICT.put("FAHAD", "فهد");
        NAME_DICT.put("FAISAL", "فيصل");
        NAME_DICT.put("FARUK", "فاروق");
        NAME_DICT.put("FARUQ", "فاروق");
        NAME_DICT.put("FAROOQ", "فاروق");
        NAME_DICT.put("HABIB", "حبيب");
        NAME_DICT.put("HAFIZ", "حافظ");
        NAME_DICT.put("HAMZA", "حمزة");
        NAME_DICT.put("HARUN", "هارون");
        NAME_DICT.put("JAHID", "جاهد");
        NAME_DICT.put("JAMAL", "جمال");
        NAME_DICT.put("KAMAL", "كمال");
        NAME_DICT.put("LITON", "ليتون");
        NAME_DICT.put("MANIK", "مانيك");
        NAME_DICT.put("MASUD", "مسعود");
        NAME_DICT.put("MEHEDI", "مهدي");
        NAME_DICT.put("MILON", "ميلون");
        NAME_DICT.put("MITHU", "ميثو");
        NAME_DICT.put("MONIR", "منير");
        NAME_DICT.put("MORSHED", "مرشد");
        NAME_DICT.put("NASIR", "ناصر");
        NAME_DICT.put("NAZIM", "ناظم");
        NAME_DICT.put("NUR", "نور");
        NAME_DICT.put("OMAR", "عمر");
        NAME_DICT.put("PARVEZ", "برويز");
        NAME_DICT.put("RABBI", "ربي");
        NAME_DICT.put("RAKIB", "رقيب");
        NAME_DICT.put("RAYHAN", "ريحان");
        NAME_DICT.put("REZA", "رضا");
        NAME_DICT.put("RIPON", "ريبون");
        NAME_DICT.put("SAJIB", "سجيب");
        NAME_DICT.put("SALIM", "سليم");
        NAME_DICT.put("SHAMIM", "شميم");
        NAME_DICT.put("SHORIF", "شريف");
        NAME_DICT.put("SHARIF", "شريف");
        NAME_DICT.put("SUJON", "سوجون");
        NAME_DICT.put("ZAKIR", "ذاكر");
        NAME_DICT.put("ZIA", "ضياء");

        // Country / Nationality Dictionary
        COUNTRY_DICT.put("BANGLADESH", "بنجلاديش");
        COUNTRY_DICT.put("BANGLADESHI", "بنجلاديش");
        COUNTRY_DICT.put("INDIA", "الهند");
        COUNTRY_DICT.put("INDIAN", "الهند");
        COUNTRY_DICT.put("PAKISTAN", "باكستان");
        COUNTRY_DICT.put("PAKISTANI", "باكستان");
        COUNTRY_DICT.put("NEPAL", "نيبال");
        COUNTRY_DICT.put("NEPALI", "نيبال");
        COUNTRY_DICT.put("PHILIPPINES", "الفلبين");
        COUNTRY_DICT.put("FILIPINO", "الفلبين");
        COUNTRY_DICT.put("EGYPT", "مصر");
        COUNTRY_DICT.put("EGYPTIAN", "مصر");
        COUNTRY_DICT.put("YEMEN", "اليمن");
        COUNTRY_DICT.put("YEMENI", "اليمن");
        COUNTRY_DICT.put("SUDAN", "السودان");
        COUNTRY_DICT.put("SUDANESE", "السودان");
        COUNTRY_DICT.put("SAUDI ARABIA", "المملكة العربية السعودية");
        COUNTRY_DICT.put("SAUDI", "المملكة العربية السعودية");

        // Religion Dictionary
        RELIGION_DICT.put("ISLAM", "الاسلام");
        RELIGION_DICT.put("MUSLIM", "الاسلام");
        RELIGION_DICT.put("CHRISTIAN", "المسيحية");
        RELIGION_DICT.put("CHRISTIANITY", "المسيحية");
        RELIGION_DICT.put("HINDU", "الهندوسية");
        RELIGION_DICT.put("HINDUISM", "الهندوسية");
        RELIGION_DICT.put("OTHER", "أخرى");
    }

    public static String toArabicNumerals(String input) {
        if (input == null || input.trim().isEmpty() || "null".equalsIgnoreCase(input.trim())) {
            return "";
        }
        String western = toWesternNumerals(input.trim());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < western.length(); i++) {
            char c = western.charAt(i);
            if (c >= '0' && c <= '9') {
                sb.append(ARABIC_DIGITS[c - '0']);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toWesternNumerals(String input) {
        if (input == null || input.trim().isEmpty() || "null".equalsIgnoreCase(input.trim())) {
            return "";
        }
        String res = input.trim();
        for (int i = 0; i < ARABIC_DIGITS.length; i++) {
            res = res.replace(ARABIC_DIGITS[i], (char) ('0' + i));
        }
        return res;
    }

    public static String formatArabicCardDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty() || "null".equalsIgnoreCase(dateStr.trim())) {
            return "";
        }
        String normalized = dateStr.trim().replace('-', '/');
        return toArabicNumerals(normalized);
    }

    public static boolean hasArabicCharacters(String str) {
        if (str == null) return false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 0x0600 && c <= 0x06FF) {
                return true;
            }
        }
        return false;
    }

    public static String toArabicNationality(String country) {
        if (country == null || country.trim().isEmpty() || "null".equalsIgnoreCase(country.trim())) {
            return "بنجلاديش";
        }
        String clean = country.trim();
        if (hasArabicCharacters(clean)) {
            return clean;
        }
        String upper = clean.toUpperCase();
        if (COUNTRY_DICT.containsKey(upper)) {
            return COUNTRY_DICT.get(upper);
        }
        for (Map.Entry<String, String> entry : COUNTRY_DICT.entrySet()) {
            if (upper.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "بنجلاديش";
    }

    public static String toArabicReligion(String religion) {
        if (religion == null || religion.trim().isEmpty() || "null".equalsIgnoreCase(religion.trim())) {
            return "الاسلام";
        }
        String clean = religion.trim();
        if (hasArabicCharacters(clean)) {
            return clean;
        }
        String upper = clean.toUpperCase();
        if (RELIGION_DICT.containsKey(upper)) {
            return RELIGION_DICT.get(upper);
        }
        for (Map.Entry<String, String> entry : RELIGION_DICT.entrySet()) {
            if (upper.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "الاسلام";
    }

    public static String toArabicBirthPlace(String birthCity, String birthCountry) {
        if (birthCity != null && !birthCity.trim().isEmpty() && !birthCity.equalsIgnoreCase("null")) {
            String clean = birthCity.trim();
            if (hasArabicCharacters(clean)) {
                return clean;
            }
            if (clean.equalsIgnoreCase("DHAKA") || clean.equalsIgnoreCase("DACCA")) {
                return "بنجلاديش";
            }
        }
        return toArabicNationality(birthCountry);
    }

    public static String toArabicProfession(String profession) {
        if (profession == null || profession.trim().isEmpty() || "null".equalsIgnoreCase(profession.trim())) {
            return "عامل تحميل وتنزيل";
        }
        String clean = profession.trim();
        if (hasArabicCharacters(clean)) {
            return clean;
        }
        String upper = clean.toUpperCase();
        if (upper.contains("LOADING") || upper.contains("UNLOADING") || upper.contains("LABOR") || upper.contains("WORKER")) {
            return "عامل تحميل وتنزيل";
        }
        if (upper.contains("CLEAN")) {
            return "عامل نظافة";
        }
        if (upper.contains("DRIVER")) {
            return "سائق";
        }
        if (upper.contains("TECHNICIAN")) {
            return "فني";
        }
        if (upper.contains("ENGINEER")) {
            return "مهندس";
        }
        if (upper.contains("ACCOUNTANT")) {
            return "محاسب";
        }
        return "عامل تحميل وتنزيل";
    }

    public static String toArabicIssuePlace(String issuePlace) {
        if (issuePlace != null && hasArabicCharacters(issuePlace) && issuePlace.length() > 5) {
            return issuePlace.trim();
        }
        return "موقع بوابة الوزارة الالكترونية";
    }

    public static String toArabicWorkLocation(String workLocation) {
        if (workLocation != null && hasArabicCharacters(workLocation) && workLocation.length() > 3) {
            return workLocation.trim();
        }
        return "منطقة الرياض";
    }

    public static String transliterateEnglishNameToArabic(String enName) {
        if (enName == null || enName.trim().isEmpty() || "null".equalsIgnoreCase(enName.trim())) {
            return "محمد ياسين احمد";
        }
        String clean = enName.trim();
        if (hasArabicCharacters(clean)) {
            return clean;
        }

        String[] parts = clean.split("[\\s_]+");
        StringBuilder result = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) continue;
            String upper = part.toUpperCase();

            // Strip trailing dots (e.g. MD. -> MD)
            if (upper.endsWith(".")) {
                upper = upper.substring(0, upper.length() - 1);
            }

            if (NAME_DICT.containsKey(upper)) {
                if (result.length() > 0) result.append(" ");
                result.append(NAME_DICT.get(upper));
            } else {
                // Phonetic fallback
                String arWord = phoneticTransliterate(upper);
                if (!arWord.isEmpty()) {
                    if (result.length() > 0) result.append(" ");
                    result.append(arWord);
                }
            }
        }

        String out = result.toString().trim();
        return out.isEmpty() ? "محمد ياسين احمد" : out;
    }

    private static String phoneticTransliterate(String word) {
        String w = word.toLowerCase();
        StringBuilder sb = new StringBuilder();
        int len = w.length();
        int i = 0;
        while (i < len) {
            // Check two-char digraphs
            if (i + 1 < len) {
                String sub = w.substring(i, i + 2);
                if (sub.equals("sh")) { sb.append("ش"); i += 2; continue; }
                if (sub.equals("ch")) { sb.append("تش"); i += 2; continue; }
                if (sub.equals("th")) { sb.append("ث"); i += 2; continue; }
                if (sub.equals("kh")) { sb.append("خ"); i += 2; continue; }
                if (sub.equals("gh")) { sb.append("غ"); i += 2; continue; }
                if (sub.equals("ph")) { sb.append("ف"); i += 2; continue; }
                if (sub.equals("ee") || sub.equals("ea")) { sb.append("ي"); i += 2; continue; }
                if (sub.equals("oo") || sub.equals("ou")) { sb.append("و"); i += 2; continue; }
                if (sub.equals("dh")) { sb.append("ذ"); i += 2; continue; }
            }
            char c = w.charAt(i);
            switch (c) {
                case 'a': sb.append("ا"); break;
                case 'b': sb.append("ب"); break;
                case 'c': sb.append("ك"); break;
                case 'd': sb.append("د"); break;
                case 'e': sb.append("ي"); break;
                case 'f': sb.append("ف"); break;
                case 'g': sb.append("ج"); break;
                case 'h': sb.append("ه"); break;
                case 'i': sb.append("ي"); break;
                case 'j': sb.append("ج"); break;
                case 'k': sb.append("ك"); break;
                case 'l': sb.append("ل"); break;
                case 'm': sb.append("م"); break;
                case 'n': sb.append("ن"); break;
                case 'o': sb.append("و"); break;
                case 'p': sb.append("ب"); break;
                case 'q': sb.append("ق"); break;
                case 'r': sb.append("ر"); break;
                case 's': sb.append("س"); break;
                case 't': sb.append("ت"); break;
                case 'u': sb.append("و"); break;
                case 'v': sb.append("ف"); break;
                case 'w': sb.append("و"); break;
                case 'x': sb.append("كس"); break;
                case 'y': sb.append("ي"); break;
                case 'z': sb.append("ز"); break;
                default: break;
            }
            i++;
        }
        return sb.toString();
    }
}
