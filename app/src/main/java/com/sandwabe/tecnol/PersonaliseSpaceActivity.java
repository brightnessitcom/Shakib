package com.sandwabe.tecnol;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class PersonaliseSpaceActivity extends AppCompatActivity {

    private PersonaliseSpaceAdapter adapter;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageHelper.wrapContext(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceNavHelper.setupStatusBar(this);
        setContentView(R.layout.activity_personalise_space);

        View ivClose = findViewById(R.id.ivClose);
        if (ivClose != null) {
            ivClose.setOnClickListener(v -> finish());
        }

        View tvSave = findViewById(R.id.tvSave);
        if (tvSave != null) {
            tvSave.setOnClickListener(v -> {
                if (adapter != null) {
                    PersonaliseSpaceManager.saveOrder(this, adapter.getPreferredList(), adapter.getOtherList());
                    Toast.makeText(this, R.string.personalised_space_saved, Toast.LENGTH_SHORT).show();
                }
                finish();
            });
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        ServiceNavHelper.setupBottomNav(this, bottomNav, R.id.navigation_other);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView rvPersonalise = findViewById(R.id.rvPersonalise);
        if (rvPersonalise == null) return;

        rvPersonalise.setLayoutManager(new LinearLayoutManager(this));

        List<PersonaliseSpaceManager.ServiceItem> preferred = PersonaliseSpaceManager.getPreferredServices(this);
        List<PersonaliseSpaceManager.ServiceItem> other = PersonaliseSpaceManager.getOtherServices(this);

        adapter = new PersonaliseSpaceAdapter(this, preferred, other, viewHolder -> {
            if (itemTouchHelper != null) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return false;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
                    viewHolder.itemView.animate().scaleX(1.02f).scaleY(1.02f).alpha(0.88f).setDuration(120).start();
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(120).start();
            }
        };

        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rvPersonalise);

        rvPersonalise.setAdapter(adapter);
    }
}
