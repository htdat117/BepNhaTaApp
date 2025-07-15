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
import com.example.bepnhataapp.common.model.CartItem;
import android.widget.CheckBox;
import android.widget.ImageButton;
import com.example.bepnhataapp.common.utils.CartHelper;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> items;
    private boolean isEditMode = false;
    private CartListener listener;
    public interface CartListener{ void onCartChanged(); }
    public CartAdapter(List<CartItem> items){this.items=items;}
    public void setCartListener(CartListener l){this.listener=l;}
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
        int factor = item.getServing().startsWith("4")?2:1;
        holder.price.setText(formatPrice(item.getPrice()*factor));
        holder.oldPrice.setText(formatPrice(item.getOldPrice()*factor));
        holder.oldPrice.setPaintFlags(holder.oldPrice.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.btnServing.setText(item.getServing());
        holder.checkbox.setChecked(item.isSelected());

        holder.checkbox.setOnCheckedChangeListener((btn,checked)->{
            item.setSelected(checked);
            if(listener!=null) listener.onCartChanged();
        });

        holder.btnPlus.setOnClickListener(v->{
            int q = item.getQuantity() + 1;
            item.setQuantity(q);
            int servingFactor = item.getServing().startsWith("4") ? 2 : 1;
            CartHelper.setQuantity(v.getContext(), item.getProductId(), servingFactor, q);
            notifyItemChanged(position);
            if(listener!=null) listener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v->{
            if(item.getQuantity()>1){
                int q = item.getQuantity() - 1;
                item.setQuantity(q);
                int servingFactor = item.getServing().startsWith("4") ? 2 : 1;
                CartHelper.setQuantity(v.getContext(), item.getProductId(), servingFactor, q);
                notifyItemChanged(position);
                if(listener!=null) listener.onCartChanged();
            }
        });

        final int pos = position;
        holder.btnServing.setOnClickListener(v->{
            PopupMenu popup=new PopupMenu(v.getContext(),holder.btnServing);
            popup.getMenu().add("2 người");
            popup.getMenu().add("4 người");
            popup.setOnMenuItemClickListener(mi->{
                String selected = mi.getTitle().toString();
                int oldFactor = item.getServing().startsWith("4") ? 2 : 1;
                int newFactor = selected.startsWith("4") ? 2 : 1;

                if(oldFactor != newFactor){
                    // cập nhật DB giữ nguyên quantity hiện tại
                    CartHelper.changeServing(v.getContext(), item.getProductId(), oldFactor, newFactor, item.getQuantity());
                }

                item.setServing(selected);
                holder.btnServing.setText(selected);
                notifyItemChanged(pos);
                if(listener!=null) listener.onCartChanged();
                return true;
            });
            popup.show();
        });

        // load product image
        if(item.getThumb()!=null){
            Glide.with(holder.imgProduct.getContext())
                    .load(item.getThumb())
                    .placeholder(R.drawable.sample_img)
                    .into(holder.imgProduct);
        }

        // Thêm sự kiện click để mở ProductDetailActivity
        holder.itemView.setOnClickListener(v -> {
            android.content.Context context = v.getContext();
            android.content.Intent intent = new android.content.Intent(context, com.example.bepnhataapp.features.products.ProductDetailActivity.class);
            intent.putExtra("productId", item.getProductId());
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() { return items == null ? 0 : items.size(); }
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, oldPrice, quantity;
        ImageButton btnPlus, btnMinus; Button btnServing; CheckBox checkbox; android.widget.ImageView imgProduct;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvProductName); // Sửa lại đúng id
            price = itemView.findViewById(R.id.tvPrice);
            oldPrice=itemView.findViewById(R.id.tvOldPrice);
            quantity=itemView.findViewById(R.id.tvQuantity);
            btnServing=itemView.findViewById(R.id.btnServing);
            btnPlus=itemView.findViewById(R.id.btnPlus);
            btnMinus=itemView.findViewById(R.id.btnMinus);
            checkbox=itemView.findViewById(R.id.checkbox);
            imgProduct=itemView.findViewById(R.id.imgProduct);
        }
    }
    private String formatPrice(int p){
        java.text.NumberFormat nf=java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));
        return nf.format(p)+"đ";
    }
    public List<CartItem> getItems(){return items;}
} 
