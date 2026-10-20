package com.sandwabe.tecnol;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonaliseSpaceManager {
    private static final String PREF_NAME = "PersonaliseSpacePrefs";
    private static final String KEY_PREFERRED = "preferred_service_ids";
    private static final String KEY_OTHER = "other_service_ids";

    public static final String ID_APPOINTMENTS = "appointments";
    public static final String ID_DELIVERY = "delivery";
    public static final String ID_TRAVEL = "travel";
    public static final String ID_AUTHORIZATIONS = "authorizations";
    public static final String ID_FURIJAT = "furijat";
    public static final String ID_EHSAN = "ehsan";
    public static final String ID_VISIT_VISA = "visit_visa";
    public static final String ID_ACTIVATION_SITES = "activation_sites";
    public static final String ID_QABUL = "qabul";
    public static final String ID_BIRTH_CERT = "birth_cert";
    public static final String ID_DEATH_CERT = "death_cert";
    public static final String ID_GOV_FEES = "gov_fees";
    public static final String ID_RESIDENT_ID = "resident_id";
    public static final String ID_BORDER_NUMBER = "border_number";

    public static class ServiceItem {
        public final String id;
        public final String title;
        public final String otherGridTitle;
        public final int titleResId;
        public final int otherGridTitleResId;
        public final int iconRes;
        public final String tintColor;
        public final boolean showBadge;
        public final Class<?> targetActivity;

        public ServiceItem(String id, int titleResId, int otherGridTitleResId, String title, String otherGridTitle, int iconRes, String tintColor, boolean showBadge, Class<?> targetActivity) {
            this.id = id;
            this.titleResId = titleResId;
            this.otherGridTitleResId = otherGridTitleResId;
            this.title = title;
            this.otherGridTitle = otherGridTitle != null ? otherGridTitle : title;
            this.iconRes = iconRes;
            this.tintColor = tintColor;
            this.showBadge = showBadge;
            this.targetActivity = targetActivity;
        }

        public String getTitle(Context context) {
            if (context != null && titleResId != 0) {
                return context.getString(titleResId);
            }
            return title;
        }

        public String getOtherGridTitle(Context context) {
            if (context != null && otherGridTitleResId != 0) {
                return context.getString(otherGridTitleResId);
            }
            return otherGridTitle;
        }
    }

    private static final Map<String, ServiceItem> ALL_SERVICES = new HashMap<>();

    static {
        register(new ServiceItem(ID_APPOINTMENTS, R.string.service_appointments, R.string.service_appointments, "Manage Appointments", "Manage Appointments", R.drawable.ic_appointments_tile, "#72D09D", false, AppointmentsActivity.class));
        register(new ServiceItem(ID_DELIVERY, R.string.service_delivery, R.string.service_delivery, "Document Delivery", "Document Delivery", R.drawable.ic_delivery_box_tile, "#72D09D", false, DeliveryRequestsActivity.class));
        register(new ServiceItem(ID_TRAVEL, R.string.service_travel, R.string.service_travel, "Absher Travel", "Absher Travel", R.drawable.ic_travel, "#72D09D", false, TravelRequestsActivity.class));
        register(new ServiceItem(ID_AUTHORIZATIONS, R.string.service_authorizations, R.string.service_authorizations, "Manage Authorizations", "Manage Authorizations", R.drawable.ic_authorizations_tile, "#72D09D", false, AuthorizationsActivity.class));
        register(new ServiceItem(ID_FURIJAT, R.string.service_furijat, R.string.service_furijat, "Donate with Furijat", "Donate with Furijat", R.drawable.ic_furijat_clean, "NONE", false, FurijatDonationsActivity.class));
        register(new ServiceItem(ID_EHSAN, R.string.service_ehsan, R.string.service_ehsan, "Donate with Ehsan", "Donate with Ehsan", R.drawable.ic_ihsan_clean, "NONE", false, EhsanDonationsActivity.class));
        register(new ServiceItem(ID_VISIT_VISA, R.string.service_visit_visa, R.string.service_visit_visa, "Manage Visit Visa", "Manage Visit Visa", R.drawable.ic_visa, "#72D09D", false, VisitVisasActivity.class));
        register(new ServiceItem(ID_ACTIVATION_SITES, R.string.service_activation_sites, R.string.service_activation_sites, "Absher Activation Sites", "Absher Activation Sites", R.drawable.ic_activation_kiosk_tile, "#72D09D", false, ActivationSitesActivity.class));
        register(new ServiceItem(ID_QABUL, R.string.service_qabul, R.string.service_qabul, "Manage Qabul Requests", "Manage Qabul Requests", R.drawable.ic_qabul_tile, "#72D09D", false, QabulRequestsActivity.class));
        register(new ServiceItem(ID_BIRTH_CERT, R.string.service_birth_cert, R.string.service_birth_cert_grid, "View Birth Certificates", "Birth Certificates\nServices", R.drawable.ic_birth_certificate_tile, "#72D09D", true, BirthCertificatesServicesActivity.class));
        register(new ServiceItem(ID_DEATH_CERT, R.string.service_death_cert, R.string.service_death_cert_grid, "Death Certificates Services", "Death Certificates\nServices", R.drawable.ic_death_certificate_tile, "#72D09D", true, VerifyDeathCertificateActivity.class));
        register(new ServiceItem(ID_GOV_FEES, R.string.service_gov_fees, R.string.service_gov_payments, "Government Fees", "Government Payments", R.drawable.ic_gov_payments_tile, "#72D09D", false, null));
        register(new ServiceItem(ID_RESIDENT_ID, R.string.service_resident_id, R.string.service_resident_id, "Resident ID Requests", "Resident ID Requests", R.drawable.ic_resident_id_tile, "#72D09D", false, ResidentIdRequestsActivity.class));
        register(new ServiceItem(ID_BORDER_NUMBER, R.string.service_border_number, R.string.service_border_number, "Border Number Inquiry", "Border Number Inquiry", R.drawable.ic_border_tile, "#72D09D", false, BorderNumberInquiryActivity.class));
    }

    private static void register(ServiceItem item) {
        ALL_SERVICES.put(item.id, item);
    }

    public static List<String> getDefaultPreferredIds() {
        return new ArrayList<>(Arrays.asList(ID_APPOINTMENTS, ID_DELIVERY));
    }

    public static List<String> getDefaultOtherIds() {
        return new ArrayList<>(Arrays.asList(
                ID_TRAVEL,
                ID_AUTHORIZATIONS,
                ID_FURIJAT,
                ID_EHSAN,
                ID_VISIT_VISA,
                ID_ACTIVATION_SITES,
                ID_QABUL,
                ID_BIRTH_CERT,
                ID_DEATH_CERT,
                ID_GOV_FEES,
                ID_RESIDENT_ID,
                ID_BORDER_NUMBER
        ));
    }

    public static List<ServiceItem> getPreferredServices(Context context) {
        List<String> ids = getPreferredIds(context);
        List<ServiceItem> items = new ArrayList<>();
        for (String id : ids) {
            ServiceItem item = ALL_SERVICES.get(id);
            if (item != null) items.add(item);
        }
        return items;
    }

    public static List<ServiceItem> getOtherServices(Context context) {
        List<String> ids = getOtherIds(context);
        List<ServiceItem> items = new ArrayList<>();
        for (String id : ids) {
            ServiceItem item = ALL_SERVICES.get(id);
            if (item != null) items.add(item);
        }
        return items;
    }

    public static List<String> getPreferredIds(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String saved = sp.getString(KEY_PREFERRED, null);
        if (saved == null || saved.trim().isEmpty()) {
            return getDefaultPreferredIds();
        }
        String[] parts = saved.split(",");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            p = p.trim();
            if (!p.isEmpty() && ALL_SERVICES.containsKey(p)) {
                list.add(p);
            }
        }
        return list;
    }

    public static List<String> getOtherIds(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String saved = sp.getString(KEY_OTHER, null);
        if (saved == null || saved.trim().isEmpty()) {
            return getDefaultOtherIds();
        }
        String[] parts = saved.split(",");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            p = p.trim();
            if (!p.isEmpty() && ALL_SERVICES.containsKey(p)) {
                list.add(p);
            }
        }

        // Ensure no missing service
        List<String> preferred = getPreferredIds(context);
        for (String id : ALL_SERVICES.keySet()) {
            if (!preferred.contains(id) && !list.contains(id)) {
                list.add(id);
            }
        }
        return list;
    }

    public static void saveOrder(Context context, List<ServiceItem> preferred, List<ServiceItem> other) {
        List<String> prefIds = new ArrayList<>();
        for (ServiceItem s : preferred) prefIds.add(s.id);

        List<String> othIds = new ArrayList<>();
        for (ServiceItem s : other) othIds.add(s.id);

        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_PREFERRED, TextUtils.join(",", prefIds))
                .putString(KEY_OTHER, TextUtils.join(",", othIds))
                .apply();
    }
}
