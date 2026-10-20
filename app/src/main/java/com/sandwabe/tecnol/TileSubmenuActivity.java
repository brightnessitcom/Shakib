package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TileSubmenuActivity extends AppCompatActivity {

    public static class SubmenuItem {
        public String title;
        public String subtitle;
        public int iconRes;

        public SubmenuItem(String title, String subtitle, int iconRes) {
            this.title = title;
            this.subtitle = subtitle;
            this.iconRes = iconRes;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_tile_submenu);

        String categoryTitle = getIntent().getStringExtra("CATEGORY_TITLE");
        if (categoryTitle == null || categoryTitle.isEmpty()) {
            categoryTitle = "Services";
        }

        TextView tvSubmenuTitle = findViewById(R.id.tvSubmenuTitle);
        if (tvSubmenuTitle != null) tvSubmenuTitle.setText(categoryTitle);

        ServiceNavHelper.setupBack(this, R.id.ivBack);

        RecyclerView rvSubmenuItems = findViewById(R.id.rvSubmenuItems);
        if (rvSubmenuItems != null) {
            rvSubmenuItems.setLayoutManager(new LinearLayoutManager(this));
            List<SubmenuItem> items = getSubmenuItemsForCategory(categoryTitle);
            rvSubmenuItems.setAdapter(new SubmenuAdapter(items));
        }

        com.google.android.material.bottomnavigation.BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        int activeNavId = getIntent().getIntExtra("ACTIVE_NAV_ID", R.id.navigation_other);
        ServiceNavHelper.setupBottomNav(this, bottomNav, activeNavId);
    }

    private List<SubmenuItem> getSubmenuItemsForCategory(String category) {
        List<SubmenuItem> list = new ArrayList<>();
        String catLower = category.toLowerCase();

        if (catLower.contains("vehicle") || catLower.contains("مركبات")) {
            list.add(new SubmenuItem("My Vehicles List", "View registered vehicles under your ID", R.drawable.ic_car));
            list.add(new SubmenuItem("Plate Digital Wallet", "Manage and auction special plates", R.drawable.ic_plate_wallet_tile));
            list.add(new SubmenuItem("Traffic Violations", "Inquire and pay traffic fines", R.drawable.ic_traffic_violations_tile));
            list.add(new SubmenuItem("Waive or Object Accident", "Najm accident objection requests", R.drawable.ic_waive_accident_tile));
            list.add(new SubmenuItem("Manage Vehicle Ownership", "Transfer or manage vehicle ownership", R.drawable.ic_manage_vehicle_ownership_tile));
            list.add(new SubmenuItem("Request Accident Report", "View and request official accident reports", R.drawable.ic_request_accident_report_tile));
        } else if (catLower.contains("passport") || catLower.contains("جوازات")) {
            list.add(new SubmenuItem("Passport Renewal", "Issue or renew Saudi passport", R.drawable.passport_ic_gray));
            list.add(new SubmenuItem("Passport Information", "View passport number, issue city and expiry", R.drawable.passport_ic_gray));
            list.add(new SubmenuItem("Update Passport Information", "Update resident passport details", R.drawable.passport_ic_gray));
            list.add(new SubmenuItem("Tawasul Services", "General passport department inquiries", R.drawable.ic_chat));
        } else if (catLower.contains("driving") || catLower.contains("license") || catLower.contains("قيادة")) {
            list.add(new SubmenuItem("Renew Driving License", "Online driver license renewal", R.drawable.ic_car));
            list.add(new SubmenuItem("Driving License Details", "View license category, status, and expiry", R.drawable.ic_car));
            list.add(new SubmenuItem("Driving School Appointments", "Book practical driving test appointments", R.drawable.ic_public_history));
        } else if (catLower.contains("travel") || catLower.contains("سفر")) {
            list.add(new SubmenuItem("Travel Authorization", "Issue travel permits for dependents", R.drawable.ic_travel));
            list.add(new SubmenuItem("Absher Travel Visitor", "Verify visitor travel status and permits", R.drawable.ic_travel));
            list.add(new SubmenuItem("Travel Records History", "View past departure and arrival logs", R.drawable.ic_travel));
        } else if (catLower.contains("birth") || catLower.contains("newborn") || catLower.contains("ولادة")) {
            list.add(new SubmenuItem("Register Newborn", "Register and issue birth certificate", R.drawable.ic_services_outline));
            list.add(new SubmenuItem("View Birth Certificate", "View birth certificate details and beneficiary", R.drawable.ic_services_outline));
            list.add(new SubmenuItem("Update Birth Certificate", "Update birth certificate information", R.drawable.ic_services_outline));
            list.add(new SubmenuItem("Issue Replacement Certificate", "Request duplicate birth certificate", R.drawable.ic_services_outline));
        } else if (catLower.contains("death") || catLower.contains("وفاة")) {
            list.add(new SubmenuItem("Register Death", "Register and issue death certificate", R.drawable.ic_services_outline));
            list.add(new SubmenuItem("Death Certificate Inquiry", "Inquire death certificate details", R.drawable.ic_services_outline));
        } else if (catLower.contains("qabul") || catLower.contains("قبول")) {
            list.add(new SubmenuItem("Submit Qabul Request", "Submit admission or approval request", R.drawable.ic_services_outline));
            list.add(new SubmenuItem("View Qabul Status", "Inquire existing Qabul application status", R.drawable.ic_services_outline));
        } else if (catLower.contains("payment") || catLower.contains("مدفوعات")) {
            list.add(new SubmenuItem("Government Payments Inquiry", "View outstanding government fees", R.drawable.ic_services_outline));
            list.add(new SubmenuItem("Refund Government Payments", "Submit fee refund request", R.drawable.ic_services_outline));
        } else {
            list.add(new SubmenuItem(category + " Query", "Inquire current status and details", R.drawable.ic_services_outline));
            list.add(new SubmenuItem(category + " Request", "Submit new request or application", R.drawable.ic_services_outline));
            list.add(new SubmenuItem(category + " History", "View past transaction history", R.drawable.ic_public_history));
        }
        return list;
    }

    private class SubmenuAdapter extends RecyclerView.Adapter<SubmenuAdapter.ViewHolder> {
        private final List<SubmenuItem> items;

        SubmenuAdapter(List<SubmenuItem> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SubmenuItem item = items.get(position);
            holder.tvTitle.setText(item.title);
            holder.tvSubtitle.setText(item.subtitle);
            holder.ivIcon.setImageResource(item.iconRes);

            holder.itemView.setOnClickListener(v -> {
                if (item.title != null && item.title.toLowerCase().contains("plate digital wallet")) {
                    startActivity(new Intent(TileSubmenuActivity.this, PlateWalletPromoActivity.class));
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("traffic violation")) {
                    startActivity(new Intent(TileSubmenuActivity.this, TrafficViolationsActivity.class));
                    return;
                }
                if (item.title != null && (item.title.toLowerCase().contains("waive") || item.title.toLowerCase().contains("object accident"))) {
                    startActivity(new Intent(TileSubmenuActivity.this, WaiveAccidentsActivity.class));
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("manage vehicle ownership")) {
                    startActivity(new Intent(TileSubmenuActivity.this, VehicleOwnershipActivity.class));
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("request accident report")) {
                    startActivity(new Intent(TileSubmenuActivity.this, RequestAccidentReportActivity.class));
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("minor accident")) {
                    Intent intent = new Intent(TileSubmenuActivity.this, ReportMinorAccidentActivity.class);
                    startActivity(intent);
                    return;
                }
                if (item.title != null && (item.title.toLowerCase().contains("renew driving license") || item.title.toLowerCase().contains("pending delivery"))) {
                    Intent intent = new Intent(TileSubmenuActivity.this, RenewDrivingLicenseActivity.class);
                    startActivity(intent);
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("update passport")) {
                    CannotProceedActivity.startPassportUpdateError(TileSubmenuActivity.this);
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("register newborn")) {
                    Intent intent = new Intent(TileSubmenuActivity.this, RegisterNewbornActivity.class);
                    startActivity(intent);
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("view birth certificate")) {
                    Intent intent = new Intent(TileSubmenuActivity.this, ViewBirthCertificateActivity.class);
                    startActivity(intent);
                    return;
                }
                if (item.title != null && item.title.toLowerCase().contains("update birth certificate")) {
                    Intent intent = new Intent(TileSubmenuActivity.this, UpdateBirthCertificateActivity.class);
                    startActivity(intent);
                    return;
                }
                if (item.title != null && (item.title.toLowerCase().contains("death") || item.title.toLowerCase().contains("وفاة"))) {
                    Intent intent = new Intent(TileSubmenuActivity.this, VerifyDeathCertificateActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent = new Intent(TileSubmenuActivity.this, ExpandedCardActivity.class);
                intent.putExtra("ITEM_TITLE", item.title);
                intent.putExtra("ITEM_SUBTITLE", item.subtitle);
                intent.putExtra("ITEM_ICON", item.iconRes);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivIcon;
            TextView tvTitle;
            TextView tvSubtitle;

            ViewHolder(View itemView) {
                super(itemView);
                ivIcon = itemView.findViewById(R.id.ivItemIcon);
                tvTitle = itemView.findViewById(R.id.tvItemTitle);
                tvSubtitle = itemView.findViewById(R.id.tvItemSubtitle);
            }
        }
    }
}