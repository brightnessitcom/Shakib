package com.sandwabe.tecnol;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    public static class SearchItem {
        private final String title;
        private final String category;

        public SearchItem(String title, String category) {
            this.title = title;
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return category;
        }
    }

    private EditText etSearchQuery;
    private ImageView ivClear;
    private TextView tvResultsCount;
    private TextView tvFilterByText;
    private View cardResults;
    private View layoutNoResults;
    private RecyclerView rvSearchResults;
    private SearchAdapter adapter;

    private final List<SearchItem> allItems = new ArrayList<>();
    private final List<SearchItem> filteredItems = new ArrayList<>();
    private String selectedCategory = "All";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isDark = ThemeHelper.isDarkMode(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(isDark ? Color.parseColor("#1A1A1A") : Color.parseColor("#FFFFFF"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            int flags = decor.getSystemUiVisibility();
            if (isDark) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(flags);
        }

        setContentView(R.layout.activity_search);

        initViews();
        loadCatalogData();
        setupRecyclerView();
        setupSearchAndFilters();

        // Focus search and open keyboard automatically
        etSearchQuery.requestFocus();
        etSearchQuery.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(etSearchQuery, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    private void initViews() {
        ImageView ivBack = findViewById(R.id.ivBack);
        if (ivBack != null) {
            ivBack.setOnClickListener(v -> finish());
        }

        etSearchQuery = findViewById(R.id.etSearchQuery);
        ivClear = findViewById(R.id.ivClear);
        tvResultsCount = findViewById(R.id.tvResultsCount);
        tvFilterByText = findViewById(R.id.tvFilterByText);
        cardResults = findViewById(R.id.cardResults);
        layoutNoResults = findViewById(R.id.layoutNoResults);
        rvSearchResults = findViewById(R.id.rvSearchResults);

        if (ivClear != null) {
            ivClear.setOnClickListener(v -> etSearchQuery.setText(""));
        }

        View btnFilterBy = findViewById(R.id.btnFilterBy);
        if (btnFilterBy != null) {
            btnFilterBy.setOnClickListener(v -> showFilterDialog());
        }
    }

    private void loadCatalogData() {
        allItems.clear();
        try {
            InputStream is = getResources().openRawResource(R.raw.search_catalog);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String jsonStr = new String(buffer, StandardCharsets.UTF_8);

            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String title = obj.optString("t", "");
                String category = obj.optString("s", "");
                if (!title.isEmpty()) {
                    allItems.add(new SearchItem(title, category));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If for any reason catalog was empty, supply standard fallbacks
        if (allItems.isEmpty()) {
            allItems.add(new SearchItem("Update Residence Passport Information", "Passports"));
            allItems.add(new SearchItem("Saudi Passport Renewal For Family Member", "Passports"));
            allItems.add(new SearchItem("Query Border Number", "Passports"));
            allItems.add(new SearchItem("Query Hajj Eligibility", "Passports"));
            allItems.add(new SearchItem("Request Replacement For Damaged Id", "Civil Affairs"));
            allItems.add(new SearchItem("E-Authorization", "Civil Affairs"));
            allItems.add(new SearchItem("Register Newborn Service", "Civil Affairs"));
            allItems.add(new SearchItem("Exit/Re-Entry Visa Status", "Passports"));
            allItems.add(new SearchItem("Driving License Information", "Public Security"));
            allItems.add(new SearchItem("Vehicle Ownership Transfer", "Public Security"));
            allItems.add(new SearchItem("Issue Vehicle Repair Permit", "Public Security"));
            allItems.add(new SearchItem("Cancellation Of Final Exit Visa", "Passports"));
            allItems.add(new SearchItem("Issuance Of Final Exit Visa", "Passports"));
        }

        filteredItems.clear();
        filteredItems.addAll(allItems);
        updateResultsCount();
    }

    private void setupRecyclerView() {
        adapter = new SearchAdapter(filteredItems, item -> {
            String title = item.getTitle();
            String titleLower = title.toLowerCase(Locale.ROOT);
            Intent intent;
            if (titleLower.contains("travel")) {
                intent = new Intent(this, TravelRequestsActivity.class);
            } else if (titleLower.contains("newborn")) {
                intent = new Intent(this, RegisterNewbornActivity.class);
            } else if (titleLower.contains("photo") || titleLower.contains("damaged id")) {
                intent = new Intent(this, PhotoRequestsActivity.class);
            } else if (titleLower.contains("driving") || titleLower.contains("license") || titleLower.contains("pending delivery")) {
                intent = new Intent(this, RenewDrivingLicenseActivity.class);
            } else if (titleLower.contains("border")) {
                intent = new Intent(this, BorderNumberInquiryActivity.class);
            } else if (titleLower.contains("authoriz")) {
                intent = new Intent(this, AuthorizationsActivity.class);
            } else if (titleLower.contains("accident") || titleLower.contains("حادث")) {
                intent = new Intent(this, ReportMinorAccidentActivity.class);
            } else if (titleLower.contains("update passport")) {
                CannotProceedActivity.startPassportUpdateError(this);
                return;
            } else if (titleLower.contains("appointment")) {
                intent = new Intent(this, AppointmentsActivity.class);
            } else {
                intent = new Intent(this, ExpandedCardActivity.class);
                intent.putExtra("ITEM_TITLE", title);
                intent.putExtra("ITEM_SUBTITLE", item.getCategory() + " Service");
                intent.putExtra("ITEM_ICON", R.drawable.ic_services_outline);
            }
            startActivity(intent);
        });
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        rvSearchResults.setAdapter(adapter);
    }

    private void setupSearchAndFilters() {
        etSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (ivClear != null) {
                    ivClear.setVisibility(s != null && s.length() > 0 ? View.VISIBLE : View.GONE);
                }
                filterList(s != null ? s.toString() : "");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList(String query) {
        String cleanQuery = query.toLowerCase(Locale.ROOT).trim();
        filteredItems.clear();

        for (SearchItem item : allItems) {
            boolean matchesCategory = selectedCategory.equalsIgnoreCase("All")
                    || item.getCategory().equalsIgnoreCase(selectedCategory);

            if (!matchesCategory) continue;

            if (cleanQuery.isEmpty()
                    || item.getTitle().toLowerCase(Locale.ROOT).contains(cleanQuery)
                    || item.getCategory().toLowerCase(Locale.ROOT).contains(cleanQuery)) {
                filteredItems.add(item);
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        updateResultsCount();

        boolean hasResults = !filteredItems.isEmpty();
        if (cardResults != null) cardResults.setVisibility(hasResults ? View.VISIBLE : View.GONE);
        if (layoutNoResults != null) layoutNoResults.setVisibility(hasResults ? View.GONE : View.VISIBLE);
    }

    private void updateResultsCount() {
        if (tvResultsCount != null) {
            tvResultsCount.setText(String.format(Locale.getDefault(), "Showing %d results", filteredItems.size()));
        }
    }

    private void showFilterDialog() {
        String[] categories = new String[]{"All", "Passports", "Civil Affairs", "Public Security", "General Services"};
        new AlertDialog.Builder(this)
                .setTitle("Filter by Category")
                .setItems(categories, (dialog, which) -> {
                    selectedCategory = categories[which];
                    if (tvFilterByText != null) {
                        if (selectedCategory.equalsIgnoreCase("All")) {
                            tvFilterByText.setText("Filter by");
                        } else {
                            tvFilterByText.setText(String.format("Filter by (%s)", selectedCategory));
                        }
                    }
                    filterList(etSearchQuery.getText() != null ? etSearchQuery.getText().toString() : "");
                })
                .show();
    }

    private static class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        interface OnItemClickListener {
            void onItemClick(SearchItem item);
        }

        private final List<SearchItem> items;
        private final OnItemClickListener listener;

        SearchAdapter(List<SearchItem> items, OnItemClickListener listener) {
            this.items = items;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SearchItem item = items.get(position);
            holder.tvTitle.setText(item.getTitle());
            holder.tvCategory.setText(item.getCategory());

            // Hide bottom divider for last item in card
            if (holder.itemDivider != null) {
                holder.itemDivider.setVisibility(position == items.size() - 1 ? View.GONE : View.VISIBLE);
            }

            holder.itemContainer.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(item);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final View itemContainer;
            final TextView tvTitle;
            final TextView tvCategory;
            final View itemDivider;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemContainer = itemView.findViewById(R.id.itemContainer);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvCategory = itemView.findViewById(R.id.tvCategory);
                itemDivider = itemView.findViewById(R.id.itemDivider);
            }
        }
    }
}