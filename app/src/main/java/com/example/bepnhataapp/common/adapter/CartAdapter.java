package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> items;
    private boolean isEditMode = false;
    public CartAdapter(List<CartItem> items) { this.items = items; }
    public void setItems(List<CartItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    public void setEditMode(boolean editMode) {
        this.isEditMode = editMode;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.price.setText(item.getPrice());
        holder.btnServing.setText(item.getServing());
        holder.btnServing.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), holder.btnServing);
            popupMenu.getMenu().add("2 người");
            popupMenu.getMenu().add("4 người");
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                String selected = menuItem.getTitle().toString();
                holder.btnServing.setText(selected);
                item.setServing(selected);
                return true;
            });
            popupMenu.show();
        });
        // show / hide checkbox according to edit mode
        if (holder.checkbox != null) {
            holder.checkbox.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        Button btnServing;
        android.widget.CheckBox checkbox;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            price = itemView.findViewById(R.id.tvPrice);
            btnServing = itemView.findViewById(R.id.btnServing);
            checkbox = itemView.findViewById(R.id.checkbox);
            if (checkbox != null) {
                checkbox.setVisibility(View.VISIBLE);
            }
        }
    }
} 