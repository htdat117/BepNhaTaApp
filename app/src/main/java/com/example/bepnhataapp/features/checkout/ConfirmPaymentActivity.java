package com.example.bepnhataapp.features.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;

public class ConfirmPaymentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);

        View header = findViewById(R.id.header);
        ImageView back = header.findViewById(R.id.btnBack);
        TextView title = header.findViewById(R.id.txtContent);
        TextView change = header.findViewById(R.id.txtChange);
        if(title!=null) title.setText("Xác nhận thanh toán");
        if(change!=null) change.setVisibility(View.GONE);
        if(back!=null) back.setOnClickListener(v -> finish());

        android.content.Intent src = getIntent();
        @SuppressWarnings("unchecked")
        final java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem> purchasedItems =
                (java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem>) src.getSerializableExtra("selected_items");

        final double goodsOld = src.getDoubleExtra("goods_total_old",0);
        final double shipFee = src.getDoubleExtra("shipping_fee",0);
        final double discount = src.getDoubleExtra("discount",0);
        final double saveDiscount = src.getDoubleExtra("save_discount", 0);
        final double voucherDiscount = src.getDoubleExtra("voucher_discount", 0);
        final double grand = src.getDoubleExtra("grand_total",0);
        final String paymentMethod = src.getStringExtra("payment_method");
        final String note = src.getStringExtra("note");

        android.widget.Button btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            if(purchasedItems!=null && !purchasedItems.isEmpty()){
                // Lưu đơn hàng
                com.example.bepnhataapp.common.utils.OrderHelper.saveOrder(
                        ConfirmPaymentActivity.this,
                        purchasedItems,
                        paymentMethod!=null?paymentMethod:"Other",
                        0,
                        shipFee,
                        discount,
                        note,
                        grand
                );

                com.example.bepnhataapp.common.utils.CartHelper.removeProducts(ConfirmPaymentActivity.this, purchasedItems);
            }
            android.content.Intent it=new android.content.Intent(ConfirmPaymentActivity.this, PaymentSuccessActivity.class);
            startActivity(it);
            finish();
        });

        String name = src.getStringExtra("name");
        String phone = src.getStringExtra("phone");
        String address = src.getStringExtra("address");

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));

        TextView tvN = findViewById(R.id.tvName);
        TextView tvP = findViewById(R.id.tvPhone);
        TextView tvAddr = findViewById(R.id.tvAddress);
        if(tvN!=null && name!=null) tvN.setText("Họ và tên: "+name);
        if(tvP!=null && phone!=null) tvP.setText("Số điện thoại: "+phone);
        if(tvAddr!=null && address!=null) tvAddr.setText("Địa chỉ: "+address);

        TextView tvGoods = findViewById(R.id.tvGoodsTotal);
        TextView tvShip = findViewById(R.id.tvShippingFee);
        TextView tvSave = findViewById(R.id.tvSaveDiscount);
        TextView tvVoucher = findViewById(R.id.tvVoucherDiscount);
        TextView tvGrand = findViewById(R.id.tvGrandTotal);
        if(tvGoods!=null) tvGoods.setText(nf.format(goodsOld)+"đ");
        if(tvShip!=null) tvShip.setText(nf.format(shipFee)+"đ");
        if(tvSave!=null) tvSave.setText("-"+nf.format(saveDiscount)+"đ");
        if(tvVoucher!=null) tvVoucher.setText("-"+nf.format(voucherDiscount)+"đ");
        if(tvVoucher!=null){
            View row = (View) tvVoucher.getParent();
            if(row!=null) row.setVisibility(voucherDiscount==0? View.GONE : View.VISIBLE);
        }
        if(tvGrand!=null) tvGrand.setText(nf.format(grand)+"đ");

        java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem> prods = (java.util.ArrayList<com.example.bepnhataapp.common.model.CartItem>) src.getSerializableExtra("selected_items");
        TextView tvEmail = findViewById(R.id.tvAddressNote);
        TextView tvNote = findViewById(R.id.tvNote);
        String email = src.getStringExtra("email");
        if(tvEmail!=null){
            if(email==null || email.trim().isEmpty()) tvEmail.setVisibility(View.GONE); else tvEmail.setText("Ghi chú giao hàng: "+email);
        }
        if(tvNote!=null){
            if(note==null || note.trim().isEmpty()) tvNote.setVisibility(View.GONE); else tvNote.setText("Ghi chú đơn hàng: "+note);
        }

        // inflate products
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(this);
        android.widget.LinearLayout ll = findViewById(R.id.llProducts);
        if(ll!=null && prods!=null){
            ll.removeAllViews();
            for(com.example.bepnhataapp.common.model.CartItem ci: prods){
                android.view.View item = inflater.inflate(R.layout.item_checkout_product, ll, false);
                android.widget.TextView tvName = item.findViewById(R.id.tvProductName);
                if(tvName!=null) tvName.setText(ci.getTitle());
                java.text.NumberFormat nff = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));
                int factor = ci.getServing().startsWith("4")?2:1;
                android.widget.TextView tvPrice = item.findViewById(R.id.tvPrice);
                if(tvPrice!=null) tvPrice.setText(nff.format(ci.getPrice()*factor)+"đ");
                android.widget.TextView tvOld = item.findViewById(R.id.tvOldPrice);
                if(tvOld!=null){
                    tvOld.setText(nff.format(ci.getOldPrice()*factor)+"đ");
                    tvOld.setPaintFlags(tvOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                }
                android.widget.TextView tvVariant = item.findViewById(R.id.tvVariant);
                if(tvVariant!=null) tvVariant.setText("Phân loại: "+ci.getServing());
                android.widget.TextView tvQuantity = item.findViewById(R.id.tvQuantity);
                if(tvQuantity!=null) tvQuantity.setText("x"+ci.getQuantity());
                android.widget.ImageView imgProduct = item.findViewById(R.id.imgProduct);
                if(imgProduct!=null) com.bumptech.glide.Glide.with(item.getContext()).load(ci.getThumb()).placeholder(R.drawable.sample_img).into(imgProduct);
                ll.addView(item);
            }
        }
    }
} 
