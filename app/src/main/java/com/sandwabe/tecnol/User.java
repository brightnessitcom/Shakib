package com.sandwabe.tecnol;

import android.util.Log;
import org.json.JSONObject;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class User implements Serializable {
    private String biometrics_collected = "";
    private String birth_city = "";
    private String birth_country = "";
    private String blotkerkorar_url = "";
    private String customImage = "";
    private String customText = "";
    private String customUrl = "";
    private String dateofbirth = "";
    private String h_blood_group = "";
    private String h_exp_date = "";
    private String h_issue_date = "";
    private String id_image_url = "";
    private String id_version = "";
    private String iqamaExpire = "";
    private String iqamaIssue = "";
    private String iqama_holder_name = "";
    private String iqama_holder_name_arabic = "";
    private String iqama_number_withid = "";
    private String iqama_number_withidTow = "";
    private boolean isAdmin = false;
    private String marital_status = "";
    private String passport_amount = "";
    private String passport_city = "";
    private String passport_expiry_date = "";
    private String passport_issue_date = "";
    private String passport_number = "";
    private String password = "";
    private String profile_image_url = "";
    private String qr_image_url = "";
    private String religion = "";
    private String sponsorship_transfers = "";
    private String travel_status = "";
    private String username = "";
    private String work_permit = "";
    private String phone_number = "";
    private String sponsor_name = "";
    private String sponsor_id = "";
    private String hajj_status = "";
    private String last_hajj_year = "";
    private String passport_status = "";
    private String resident_status = "";
    private String travel_status_badge = "";

    public User() {}

    public static User fromMap(Map<String, Object> map) {
        User user = new User();
        if (map == null) return user;
        
        try {
            // AUTH & ID
            user.username = getV(map, "username", "id");
            user.password = getV(map, "password", "pass");
            user.phone_number = getV(map, "phone number", "phone_number", "phone");
            
            // NAMES
            user.iqama_holder_name = getV(map, "iqama holder_name", "iqama_holder_name", "name");
            user.iqama_holder_name_arabic = getV(map, "iqama holder_name_arabic", "iqama_holder_name_arabic", "arabic_name");
            
            // IQAMA DATES & INFO (Crucial mapping for Expiry)
            user.iqama_number_withid = getV(map, "iqama_number_withid", "iqama number", "iqama_no");
            user.iqama_number_withidTow = getV(map, "iqama_number_withidTow", "id_tow");
            user.iqamaIssue = getV(map, "iqamaIssue", "iqama_issue", "issue_date");
            user.iqamaExpire = getV(map, "iqamaExpire", "iqama_expire", "expiry_date", "iqama expiry");
            user.id_version = getV(map, "id_version", "id version", "version");
            
            // PASSPORT DATES & INFO
            user.passport_number = getV(map, "passport number", "passport_number", "pasaport_number");
            user.passport_city = getV(map, "passport city", "passport_city", "pasaport_city");
            user.passport_amount = getV(map, "passport_amount", "amount", "pasaport_amount");
            user.passport_issue_date = getV(map, "passport issue_date", "passport_issue_date", "pasaport_issue_date");
            user.passport_expiry_date = getV(map, "passport_expiry_date", "passport expiry_date", "pasaport_expiry_date");
            user.passport_status = getV(map, "passport_status", "status", "pasaport_status");
            
            // PERSONAL DETAILS
            user.dateofbirth = getV(map, "dateofbirth", "birth_date", "dob");
            user.birth_city = getV(map, "birth city", "birth_city", "city");
            user.birth_country = getV(map, "birth country", "birth_country", "country");
            user.religion = getV(map, "religion", "faith");
            user.marital_status = getV(map, "marital statusg", "marital_status", "marriage_status");
            user.biometrics_collected = getV(map, "biometrics_collected", "biometrics");
            user.work_permit = getV(map, "work permit", "work_permit", "permit");
            user.h_blood_group = getV(map, "h_blood_group", "blood_group", "blood_type", "bloodType", "blood", "h_blood_type");
            
            // TRAVEL & HAJJ
            user.hajj_status = getV(map, "hajj status", "hajj_status", "hajj");
            user.last_hajj_year = getV(map, "last_hajj_year", "hajj_year");
            user.h_issue_date = getV(map, "h_issue_date", "health_issue");
            user.h_exp_date = getV(map, "h_exp_date", "health_exp");
            user.travel_status = getV(map, "travel status", "travel_status", "status");
            user.travel_status_badge = getV(map, "travel status badge", "travel_status_badge", "travel_badge");
            user.resident_status = getV(map, "resident_status", "status_resident");
            
            // SPONSOR INFO
            user.sponsor_name = getV(map, "sponsor name", "sponsor_name", "sponsor");
            user.sponsor_id = normalizeDigits(getV(map, "sponsor id", "sponsor_id", "sponsor_no"));
            user.sponsorship_transfers = getV(map, "sponsorship transfers", "sponsorship_transfers", "transfers");
            
            // MEDIA & URLS
            user.profile_image_url = getV(map, "profile_image_url", "profile_image", "profile");
            user.id_image_url = getV(map, "id_image_url", "id_image", "image");
            user.qr_image_url = getV(map, "qr_image_url", "qr_image", "qr");
            user.blotkerkorar_url = getV(map, "blotkerkorar_url", "block_url", "url");

            Object admin = map.get("isAdmin");
            if (admin instanceof Boolean) user.isAdmin = (Boolean) admin;
            else if (admin instanceof Number) user.isAdmin = ((Number) admin).intValue() == 1;
            
        } catch (Exception e) {
            Log.e("User", "Map parsing failed", e);
        }
        return user;
    }

    private static String getV(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key)) return s(map.get(key));
        }
        return "";
    }

    private static String s(Object obj) {
        if (obj == null) return "";
        String val = String.valueOf(obj).trim();
        if (val.equalsIgnoreCase("null")) return "";
        if (obj instanceof Double || obj instanceof Float || obj instanceof Long || obj instanceof Integer) {
            return String.format(Locale.US, "%.0f", ((Number) obj).doubleValue());
        }
        return val;
    }

    // Getters
    public String getBirth_city() { return birth_city; }
    public String getBirth_country() { return birth_country; }
    public String getBlotkerkorar_url() { return blotkerkorar_url; }
    public String getCustomImage() { return customImage; }
    public String getCustomText() { return customText; }
    public String getCustomUrl() { return customUrl; }
    public String getDateofbirth() { return dateofbirth; }
    public String getH_blood_group() { return h_blood_group; }
    public String getH_exp_date() { return h_exp_date; }
    public String getH_issue_date() { return h_issue_date; }
    public String getId_image_url() { return id_image_url; }
    public String getId_version() { return id_version; }
    public String getIqamaExpire() { return iqamaExpire; }
    public String getIqamaIssue() { return iqamaIssue; }
    public String getIqama_holder_name() { return iqama_holder_name; }
    public String getIqama_holder_name_arabic() { return iqama_holder_name_arabic; }
    public String getIqama_number_withid() { return iqama_number_withid; }
    public String getIqama_number_withidTow() { return iqama_number_withidTow; }
    public boolean isAdmin() { return isAdmin; }
    public String getMarital_status() { return marital_status; }
    public String getPassport_amount() { return passport_amount; }
    public String getPassport_city() { return passport_city; }
    public String getPassport_expiry_date() { return passport_expiry_date; }
    public String getPassport_issue_date() { return passport_issue_date; }
    public String getPassport_number() { return passport_number; }
    public String getPassword() { return password; }
    public String getProfile_image_url() { return profile_image_url; }
    public String getQr_image_url() { return qr_image_url; }
    public String getReligion() { return religion; }
    public String getSponsorship_transfers() { return sponsorship_transfers; }
    public String getTravel_status() { return travel_status; }
    public String getUsername() { return username; }
    public String getWork_permit() { return work_permit; }
    public String getPhone_number() { return phone_number; }
    public String getBiometrics_collected() { return biometrics_collected; }
    public String getSponsor_name() { return sponsor_name; }
    public String getSponsor_id() { return normalizeDigits(sponsor_id); }
    public String getHajj_status() { return hajj_status; }
    public String getLast_hajj_year() { return last_hajj_year; }
    public String getPassport_status() { return passport_status; }
    public String getResident_status() { return resident_status; }
    public String getTravel_status_badge() { return travel_status_badge; }

    public static String normalizeDigits(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                sb.append(c);
            } else if (c >= '٠' && c <= '٩') {
                sb.append((char) ('0' + (c - '٠')));
            } else if (c >= '۰' && c <= '۹') {
                sb.append((char) ('0' + (c - '۰')));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public boolean isExpired() {
        if (resident_status != null && !resident_status.isEmpty()) {
            String rs = resident_status.trim().toLowerCase();
            if (rs.contains("expired") || rs.contains("invalid") || rs.contains("منتهي") || rs.contains("غير ساري")) {
                return true;
            }
        }
        if (iqamaExpire != null && !iqamaExpire.trim().isEmpty()) {
            String exp = normalizeDigits(iqamaExpire.trim()).replace('-', '/').replace('.', '/');
            try {
                String[] parts = exp.split("/");
                Date expiryDate = null;
                if (parts.length == 3) {
                    int y, m, d;
                    if (parts[0].length() == 4) {
                        y = Integer.parseInt(parts[0]);
                        m = Integer.parseInt(parts[1]);
                        d = Integer.parseInt(parts[2]);
                    } else if (parts[2].length() == 4) {
                        d = Integer.parseInt(parts[0]);
                        m = Integer.parseInt(parts[1]);
                        y = Integer.parseInt(parts[2]);
                    } else {
                        d = Integer.parseInt(parts[0]);
                        m = Integer.parseInt(parts[1]);
                        y = Integer.parseInt(parts[2]);
                    }
                    if (y < 1600) {
                        android.icu.util.ULocale locale = new android.icu.util.ULocale("en@calendar=islamic-umalqura");
                        android.icu.util.IslamicCalendar hijriCalendar = new android.icu.util.IslamicCalendar(locale);
                        hijriCalendar.set(android.icu.util.IslamicCalendar.YEAR, y);
                        hijriCalendar.set(android.icu.util.IslamicCalendar.MONTH, m - 1);
                        hijriCalendar.set(android.icu.util.IslamicCalendar.DAY_OF_MONTH, d);
                        hijriCalendar.set(android.icu.util.IslamicCalendar.HOUR_OF_DAY, 23);
                        hijriCalendar.set(android.icu.util.IslamicCalendar.MINUTE, 59);
                        expiryDate = new Date(hijriCalendar.getTimeInMillis());
                    } else {
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.set(y, m - 1, d, 23, 59, 59);
                        expiryDate = cal.getTime();
                    }
                }
                if (expiryDate != null) {
                    return new Date().after(expiryDate);
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(Object password) { this.password = s(password); }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            json.put("phone_number", phone_number);
            json.put("iqama_holder_name", iqama_holder_name);
            json.put("iqama_holder_name_arabic", iqama_holder_name_arabic);
            json.put("iqama_number_withid", iqama_number_withid);
            json.put("iqama_number_withidTow", iqama_number_withidTow);
            json.put("iqamaIssue", iqamaIssue);
            json.put("iqamaExpire", iqamaExpire);
            json.put("id_version", id_version);
            json.put("dateofbirth", dateofbirth);
            json.put("birth_city", birth_city);
            json.put("birth_country", birth_country);
            json.put("religion", religion);
            json.put("marital_status", marital_status);
            json.put("work_permit", work_permit);
            json.put("biometrics_collected", biometrics_collected);
            json.put("h_issue_date", h_issue_date);
            json.put("h_exp_date", h_exp_date);
            json.put("h_blood_group", h_blood_group);
            json.put("passport_number", passport_number);
            json.put("passport_city", passport_city);
            json.put("passport_amount", passport_amount);
            json.put("passport_issue_date", passport_issue_date);
            json.put("passport_expiry_date", passport_expiry_date);
            json.put("passport_status", passport_status);
            json.put("sponsor_name", sponsor_name);
            json.put("sponsor_id", sponsor_id);
            json.put("sponsorship_transfers", sponsorship_transfers);
            json.put("hajj_status", hajj_status);
            json.put("last_hajj_year", last_hajj_year);
            json.put("travel_status", travel_status);
            json.put("travel_status_badge", travel_status_badge);
            json.put("resident_status", resident_status);
            json.put("profile_image_url", profile_image_url);
            json.put("id_image_url", id_image_url);
            json.put("qr_image_url", qr_image_url);
            json.put("blotkerkorar_url", blotkerkorar_url);
            json.put("customImage", customImage);
            json.put("customText", customText);
            json.put("customUrl", customUrl);
            json.put("isAdmin", isAdmin);
        } catch (Exception e) {
            Log.e("User", "toJson failed", e);
        }
        return json;
    }

    public static User fromJson(JSONObject json) {
        if (json == null) return new User();
        Map<String, Object> map = new java.util.HashMap<>();
        java.util.Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, json.opt(key));
        }
        return fromMap(map);
    }

    public static User fromJson(String jsonStr) {
        if (jsonStr == null || jsonStr.trim().isEmpty()) return null;
        try {
            return fromJson(new JSONObject(jsonStr));
        } catch (Exception e) {
            Log.e("User", "fromJson failed", e);
            return null;
        }
    }
}
