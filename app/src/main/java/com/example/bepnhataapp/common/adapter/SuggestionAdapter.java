package com.example.bepnhataapp.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

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

        // Ẩn tag dinh dưỡng và icon trong mục gợi ý
        holder.tvNutrition.setVisibility(View.GONE);
        holder.ivNutrition.setVisibility(View.GONE);

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
        ImageView ivNutrition;
        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            imgProduct.setBackground(androidx.core.content.ContextCompat.getDrawable(itemView.getContext(), R.drawable.bg_rounded_8));
            imgProduct.setClipToOutline(true);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvKcal = itemView.findViewById(R.id.tvKcal);
            tvNutrition = itemView.findViewById(R.id.tvNutrition);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnBuy = itemView.findViewById(R.id.btnBuy);
            btnAddCart = itemView.findViewById(R.id.btnAddCart);
            ivNutrition = itemView.findViewById(R.id.ivNutritionIcon);
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

        int currentPrice = p.getProductPrice2() * (100 - p.getSalePercent2()) / 100;
        ((TextView)sheet.findViewById(R.id.tvPrice)).setText(formatPrice(currentPrice));
        TextView tvOld = sheet.findViewById(R.id.tvOldPrice);
        if(p.getSalePercent2()>0){
            tvOld.setText(formatPrice(p.getProductPrice2()));
            tvOld.setPaintFlags(tvOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
            tvOld.setVisibility(View.VISIBLE);
        } else {
            tvOld.setVisibility(View.GONE);
        }

        ((TextView)sheet.findViewById(R.id.tvDesc)).setText(p.getProductDescription()!=null ? p.getProductDescription() : "");

        // Stock information if available
        TextView tvStock = sheet.findViewById(R.id.tvStock);
        tvStock.setText("Kho: " + p.getInventory2());

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
        final int[] pricePerPack = {currentPrice};
        updateTotalPrice(tvTotalPrice, pricePerPack[0], quantity[0], servingFactor[0]);

        btnPlus.setOnClickListener(v -> {
            quantity[0]++;
            tvQuantity.setText(String.valueOf(quantity[0]));
            updateTotalPrice(tvTotalPrice, pricePerPack[0], quantity[0], servingFactor[0]);
        });

        btnMinus.setOnClickListener(v -> {
            if(quantity[0]>1){
                quantity[0]--;
                tvQuantity.setText(String.valueOf(quantity[0]));
                updateTotalPrice(tvTotalPrice, pricePerPack[0], quantity[0], servingFactor[0]);
            }
        });

        com.google.android.material.button.MaterialButtonToggleGroup group = sheet.findViewById(R.id.toggleServing);
        // initial macro display
        updateMacroViews(sheet, detail, servingFactor[0]);

        group.addOnButtonCheckedListener((g, checkedId, isChecked) -> {
            if(isChecked){
                if(checkedId==R.id.btnServing2){
                    servingFactor[0] = 1;
                }else if(checkedId==R.id.btnServing4){
                    servingFactor[0] = 2;
                }
                pricePerPack[0] = servingFactor[0]==1 ? p.getProductPrice2() * (100-p.getSalePercent2())/100
                                                      : p.getProductPrice4() * (100-p.getSalePercent4())/100;
                ((TextView)sheet.findViewById(R.id.tvPrice)).setText(formatPrice(pricePerPack[0]));
                TextView tvOldP = sheet.findViewById(R.id.tvOldPrice);
                if((servingFactor[0]==1 && p.getSalePercent2()>0) || (servingFactor[0]==2 && p.getSalePercent4()>0)){
                    tvOldP.setVisibility(View.VISIBLE);
                    int old = servingFactor[0]==1 ? p.getProductPrice2() : p.getProductPrice4();
                    tvOldP.setText(formatPrice(old));
                    tvOldP.setPaintFlags(tvOldP.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    tvOldP.setVisibility(View.GONE);
                }

                // update stock availability (inventory per package)
                int stockPackages = servingFactor[0]==1 ? p.getInventory2() : p.getInventory4();
                tvStock.setText("Kho: " + stockPackages);

                updateTotalPrice(tvTotalPrice, pricePerPack[0], quantity[0], servingFactor[0]);
                updateMacroViews(sheet, detail, servingFactor[0]);
            }
        });

        // -----------------------
        // ACTION BUTTONS
        // -----------------------
        ImageView btnAddCart = sheet.findViewById(R.id.btnDialogAddCart);
        Button btnBuyNow = sheet.findViewById(R.id.btnDialogBuyNow);

        btnAddCart.setOnClickListener(v -> {
            // Thêm sản phẩm vào giỏ theo số lượng & khẩu phần đã chọn
            for(int i=0;i<quantity[0];i++){
                com.example.bepnhataapp.common.utils.CartHelper.addProduct(context, p, servingFactor[0]);
            }
            android.widget.Toast.makeText(context, "Đã thêm giỏ hàng thành công", android.widget.Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnBuyNow.setOnClickListener(v -> {
            int serving = servingFactor[0];
            int originalVariantPrice = (serving == 1) ? p.getProductPrice2() : p.getProductPrice4();
            int salePercentVariant = (serving == 1) ? p.getSalePercent2() : p.getSalePercent4();
            int discountedVariantPrice = originalVariantPrice * (100 - salePercentVariant) / 100;

            int pricePer2Pack = discountedVariantPrice / serving;
            int oldPricePer2Pack = originalVariantPrice / serving;

            com.example.bepnhataapp.common.model.CartItem cartItem = new com.example.bepnhataapp.common.model.CartItem(
                    p.getProductID(),
                    p.getProductName(),
                    pricePer2Pack,
                    oldPricePer2Pack,
                    quantity[0],
                    p.getProductThumb()
            );
            cartItem.setServing(serving == 2 ? "4 người" : "2 người");

            java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem> selectedItems = new java.util.ArrayList<>();
            selectedItems.add(cartItem);

            android.content.Intent intent = new android.content.Intent(context, com.example.bepnhataapp.features.checkout.CheckoutActivity.class);
            intent.putExtra("selected_items", selectedItems);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            dialog.dismiss();
        });

        dialog.setContentView(sheet);
        dialog.show();
    }

    private void updateTotalPrice(TextView tv, int basePrice, int quantity, int servingFactor){
        // Luôn hiển thị giá gốc (một gói) theo khẩu phần hiện tại, không nhân số lượng
        tv.setText(formatPrice(basePrice));
    }

    private String formatMacro(double value,String label){
        if("Kcal".equals(label)){
            return (int)value+" "+label;
        }
        return (int)value+"g "+label;
    }

    private void updateMacroViews(View root, ProductDetail detail, int factor){
        if(detail==null) return;
        double carbs = factor==1 ? (detail.getCarbs2()!=0?detail.getCarbs2():detail.getCarbs()) : (detail.getCarbs4()!=0?detail.getCarbs4():detail.getCarbs2()*2);
        double protein = factor==1 ? (detail.getProtein2()!=0?detail.getProtein2():detail.getProtein()) : (detail.getProtein4()!=0?detail.getProtein4():detail.getProtein2()*2);
        double fat = factor==1 ? (detail.getFat2()!=0?detail.getFat2():detail.getFat()) : (detail.getFat4()!=0?detail.getFat4():detail.getFat2()*2);
        double calo = factor==1 ? (detail.getCalo2()!=0?detail.getCalo2():detail.getCalo()) : (detail.getCalo4()!=0?detail.getCalo4():detail.getCalo2()*2);

        ((TextView)root.findViewById(R.id.tvCarbs)).setText(formatMacro(carbs,"carbs"));
        ((TextView)root.findViewById(R.id.tvProtein)).setText(formatMacro(protein,"proteins"));
        ((TextView)root.findViewById(R.id.tvCalo)).setText(formatMacro(calo,"Kcal"));
        ((TextView)root.findViewById(R.id.tvFat)).setText(formatMacro(fat,"fat"));
    }

    private String capitalize(String s){
        if(s==null || s.isEmpty()) return s;
        return s.substring(0,1).toUpperCase()+s.substring(1);
    }

    // Same mapping function as in ProductAdapter
    private int getNutritionIconResId(String tag){
        if(tag==null || tag.isEmpty()) return R.drawable.ic_nutrition;
        String key = normalize(tag);
        if(key.contains("omega")) return R.drawable.ic_omega3;
        if(key.contains("dam") || key.contains("đam") || key.contains("protein")) return R.drawable.ic_protein_nutrition;
        if(key.contains("vitamin")) return R.drawable.ic_vitamin;
        if(key.contains("cholesterol")) return R.drawable.ic_cholesterol;
        if(key.contains("itbeo") || key.contains("lowfat") || key.contains("chatbeo")) return R.drawable.ic_low_fat;
        if(key.contains("canxi") || key.contains("calcium")) return R.drawable.ic_canxi;
        if(key.contains("chatxo") || key.contains("fiber")) return R.drawable.ic_nutrition;
        return R.drawable.ic_nutrition;
    }

    private String normalize(String s){
        String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        temp = temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        temp = temp.replace('đ','d').replace('Đ','D');
        temp = temp.toLowerCase(java.util.Locale.ROOT).replace("-", "").replace(" ", "");
        return temp;
    }
} 
