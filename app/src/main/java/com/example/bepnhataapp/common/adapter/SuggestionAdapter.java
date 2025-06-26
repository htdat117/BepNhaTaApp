package com.example.bepnhataapp.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Product;
import com.example.bepnhataapp.common.dao.ProductDetailDao;
import com.example.bepnhataapp.common.model.ProductDetail;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.bumptech.glide.Glide;
import java.text.NumberFormat;
import java.util.Locale;

import java.util.List;

import com.example.bepnhataapp.features.products.ProductDetailActivity;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    private Context context;
    private List<Product> list;

    public SuggestionAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Product p = list.get(position);

        String thumb=p.getProductThumb();
        if(thumb!=null&&!thumb.isEmpty()){
            Glide.with(context).load(thumb).placeholder(R.drawable.sample_img).into(holder.imgProduct);
        }else {
            holder.imgProduct.setImageResource(R.drawable.sample_img);
        }

        holder.tvName.setText(p.getProductName());

        // Bind detail data
        ProductDetail detail = new ProductDetailDao(context).getByProductId(p.getProductID());
        if(detail!=null){
            holder.tvKcal.setText((int)detail.getCalo()+" Kcal");
            holder.tvTime.setText(detail.getCookingTimeMinutes()+" phút");
        }else{
            holder.tvKcal.setText("");
            holder.tvTime.setText("");
        }
        holder.tvNutrition.setText("");
        holder.tvPrice.setText(formatPrice(p.getProductPrice()*(100-p.getSalePercent())/100));

        // Add to cart button shows quick-add dialog
        holder.btnAddCart.setOnClickListener(v -> showQuickAddDialog(p));

        // Buy now button (if present) also shows quick-add dialog
        holder.btnBuy.setOnClickListener(v -> showQuickAddDialog(p));

        // Clicking elsewhere navigates to product detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("productId", p.getProductID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvKcal, tvNutrition, tvTime, tvPrice;
        View btnBuy;
        ImageView btnAddCart;
        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvKcal = itemView.findViewById(R.id.tvKcal);
            tvNutrition = itemView.findViewById(R.id.tvNutrition);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnBuy = itemView.findViewById(R.id.btnBuy);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
        }
    }

    private String formatPrice(int price){
        NumberFormat nf=NumberFormat.getInstance(new Locale("vi","VN"));
        return nf.format(price)+"đ";
    }

    /**
     * Show the quick-add bottom sheet dialog for the given product.
     */
    private void showQuickAddDialog(Product p){
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View sheet = LayoutInflater.from(context).inflate(R.layout.dialog_quick_add, null);

        String thumb = p.getProductThumb();
        if(thumb!=null && !thumb.isEmpty()){
            Glide.with(context).load(thumb).placeholder(R.drawable.sample_img).error(R.drawable.sample_img)
                    .into((ImageView)sheet.findViewById(R.id.imgProduct));
        }else{
            ((ImageView)sheet.findViewById(R.id.imgProduct)).setImageResource(R.drawable.sample_img);
        }

        ((TextView)sheet.findViewById(R.id.tvName)).setText(p.getProductName());

        int currentPrice = p.getProductPrice() * (100 - p.getSalePercent()) / 100;
        ((TextView)sheet.findViewById(R.id.tvPrice)).setText(formatPrice(currentPrice));
        TextView tvOld = sheet.findViewById(R.id.tvOldPrice);
        if(p.getSalePercent()>0){
            tvOld.setText(formatPrice(p.getProductPrice()));
            tvOld.setPaintFlags(tvOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            tvOld.setVisibility(View.VISIBLE);
        } else {
            tvOld.setVisibility(View.GONE);
        }

        ((TextView)sheet.findViewById(R.id.tvDesc)).setText(p.getProductDescription()!=null ? p.getProductDescription() : "");

        // Stock information if available
        ((TextView)sheet.findViewById(R.id.tvStock)).setText("Kho: " + p.getInventory());

        // Bind nutrition macros if available
        ProductDetail detail = new ProductDetailDao(context).getByProductId(p.getProductID());
        if(detail!=null){
            ((TextView)sheet.findViewById(R.id.tvCarbs)).setText(formatMacro(detail.getCarbs(),"carbs"));
            ((TextView)sheet.findViewById(R.id.tvProtein)).setText(formatMacro(detail.getProtein(),"proteins"));
            ((TextView)sheet.findViewById(R.id.tvCalo)).setText(formatMacro(detail.getCalo(),"Kcal"));
            ((TextView)sheet.findViewById(R.id.tvFat)).setText(formatMacro(detail.getFat(),"fat"));
        }

        // Quantity and serving logic
        final int[] quantity = {1};
        final int[] servingFactor = {1}; // 1 for 2 người, 2 for 4 người

        android.widget.TextView tvQuantity = sheet.findViewById(R.id.tvQuantity);
        android.widget.TextView tvTotalPrice = sheet.findViewById(R.id.tvTotalPrice);

        View btnMinus = sheet.findViewById(R.id.btnMinus);
        View btnPlus = sheet.findViewById(R.id.btnPlus);

        // initial total price display
        tvTotalPrice.setText(formatPrice(currentPrice));

        btnPlus.setOnClickListener(v -> {
            quantity[0]++;
            tvQuantity.setText(String.valueOf(quantity[0]));
            updateTotalPrice(tvTotalPrice, currentPrice, quantity[0], servingFactor[0]);
        });

        btnMinus.setOnClickListener(v -> {
            if(quantity[0]>1){
                quantity[0]--;
                tvQuantity.setText(String.valueOf(quantity[0]));
                updateTotalPrice(tvTotalPrice, currentPrice, quantity[0], servingFactor[0]);
            }
        });

        com.google.android.material.button.MaterialButtonToggleGroup group = sheet.findViewById(R.id.toggleServing);
        group.addOnButtonCheckedListener((g, checkedId, isChecked) -> {
            if(isChecked){
                if(checkedId==R.id.btnServing2){
                    servingFactor[0] = 1;
                }else if(checkedId==R.id.btnServing4){
                    servingFactor[0] = 2;
                }
                updateTotalPrice(tvTotalPrice, currentPrice, quantity[0], servingFactor[0]);
            }
        });

        dialog.setContentView(sheet);
        dialog.show();
    }

    private void updateTotalPrice(TextView tv, int basePrice, int quantity, int servingFactor){
        int total = basePrice * quantity * servingFactor;
        tv.setText(formatPrice(total));
    }

    private String formatMacro(double value,String label){
        if("Kcal".equals(label)){
            return (int)value+" "+label;
        }
        return (int)value+"g "+label;
    }
} 