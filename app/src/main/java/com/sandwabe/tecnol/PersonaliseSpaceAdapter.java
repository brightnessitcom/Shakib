package com.sandwabe.tecnol;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class PersonaliseSpaceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER_PREFERRED = 0;
    public static final int TYPE_SERVICE = 1;
    public static final int TYPE_HEADER_OTHER = 2;

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    private final Context context;
    private final List<PersonaliseSpaceManager.ServiceItem> preferredList;
    private final List<PersonaliseSpaceManager.ServiceItem> otherList;
    private final OnStartDragListener dragListener;

    public PersonaliseSpaceAdapter(Context context,
                                   List<PersonaliseSpaceManager.ServiceItem> preferredList,
                                   List<PersonaliseSpaceManager.ServiceItem> otherList,
                                   OnStartDragListener dragListener) {
        this.context = context;
        this.preferredList = preferredList;
        this.otherList = otherList;
        this.dragListener = dragListener;
    }

    public List<PersonaliseSpaceManager.ServiceItem> getPreferredList() {
        return preferredList;
    }

    public List<PersonaliseSpaceManager.ServiceItem> getOtherList() {
        return otherList;
    }

    @Override
    public int getItemCount() {
        // Preferred Header + Preferred items + Other Header + Other items
        return 1 + preferredList.size() + 1 + otherList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER_PREFERRED;
        } else if (position <= preferredList.size()) {
            return TYPE_SERVICE;
        } else if (position == preferredList.size() + 1) {
            return TYPE_HEADER_OTHER;
        } else {
            return TYPE_SERVICE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER_PREFERRED) {
            View view = inflater.inflate(R.layout.item_personalise_header_preferred, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == TYPE_HEADER_OTHER) {
            View view = inflater.inflate(R.layout.item_personalise_header_other, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_personalise_space_row, parent, false);
            return new ServiceViewHolder(view);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ServiceViewHolder) {
            ServiceViewHolder svh = (ServiceViewHolder) holder;
            PersonaliseSpaceManager.ServiceItem item;
            if (position <= preferredList.size()) {
                item = preferredList.get(position - 1);
            } else {
                int otherIndex = position - (preferredList.size() + 2);
                item = otherList.get(otherIndex);
            }

            svh.tvTitle.setText(item.getTitle(context));
            svh.ivIcon.setImageResource(item.iconRes);

            if ("NONE".equals(item.tintColor)) {
                svh.ivIcon.clearColorFilter();
                svh.ivIcon.setImageTintList(null);
            } else if (item.tintColor != null && !item.tintColor.equalsIgnoreCase("#72D09D")) {
                svh.ivIcon.setImageTintList(null);
                svh.ivIcon.setColorFilter(Color.parseColor(item.tintColor));
            } else {
                svh.ivIcon.clearColorFilter();
                svh.ivIcon.setImageTintList(androidx.core.content.ContextCompat.getColorStateList(context, R.color.icon_tint_primary));
            }

            svh.ivDragHandle.setOnTouchListener((v, event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN && dragListener != null) {
                    dragListener.onStartDrag(svh);
                }
                return false;
            });
        }
    }

    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition == 0 || toPosition == 0) return false;

        int otherHeaderPos = preferredList.size() + 1;
        if (fromPosition == otherHeaderPos) return false;

        // User dragged onto the Other Header: allow stepping into next valid spot
        if (toPosition == otherHeaderPos) {
            if (fromPosition < otherHeaderPos) {
                toPosition = otherHeaderPos + 1;
            } else {
                toPosition = otherHeaderPos - 1;
            }
        }

        // Case 1: Within Preferred list
        if (fromPosition < otherHeaderPos && toPosition < otherHeaderPos) {
            int fromIdx = fromPosition - 1;
            int toIdx = toPosition - 1;
            if (fromIdx >= 0 && fromIdx < preferredList.size() && toIdx >= 0 && toIdx < preferredList.size()) {
                Collections.swap(preferredList, fromIdx, toIdx);
                notifyItemMoved(fromPosition, toPosition);
                return true;
            }
            return false;
        }

        // Case 2: Within Other list
        if (fromPosition > otherHeaderPos && toPosition > otherHeaderPos) {
            int fromIdx = fromPosition - otherHeaderPos - 1;
            int toIdx = toPosition - otherHeaderPos - 1;
            if (fromIdx >= 0 && fromIdx < otherList.size() && toIdx >= 0 && toIdx < otherList.size()) {
                Collections.swap(otherList, fromIdx, toIdx);
                notifyItemMoved(fromPosition, toPosition);
                return true;
            }
            return false;
        }

        // Case 3: Moving from Other into Preferred
        if (fromPosition > otherHeaderPos && toPosition < otherHeaderPos) {
            if (preferredList.size() >= 4) {
                Toast.makeText(context, "You can prefer a maximum of four services", Toast.LENGTH_SHORT).show();
                return false;
            }
            int fromIdx = fromPosition - otherHeaderPos - 1;
            int toIdx = toPosition - 1;
            if (fromIdx >= 0 && fromIdx < otherList.size()) {
                PersonaliseSpaceManager.ServiceItem item = otherList.remove(fromIdx);
                if (toIdx < 0) toIdx = 0;
                if (toIdx > preferredList.size()) toIdx = preferredList.size();
                preferredList.add(toIdx, item);
                notifyItemMoved(fromPosition, toPosition);
                return true;
            }
            return false;
        }

        // Case 4: Moving from Preferred into Other
        if (fromPosition < otherHeaderPos && toPosition > otherHeaderPos) {
            int fromIdx = fromPosition - 1;
            int toIdx = toPosition - otherHeaderPos - 1;
            if (fromIdx >= 0 && fromIdx < preferredList.size()) {
                PersonaliseSpaceManager.ServiceItem item = preferredList.remove(fromIdx);
                if (toIdx < 0) toIdx = 0;
                if (toIdx > otherList.size()) toIdx = otherList.size();
                otherList.add(toIdx, item);
                notifyItemMoved(fromPosition, toPosition);
                return true;
            }
            return false;
        }

        return false;
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        public final ImageView ivIcon;
        public final TextView tvTitle;
        public final ImageView ivDragHandle;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivDragHandle = itemView.findViewById(R.id.ivDragHandle);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
