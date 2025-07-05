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

        ImageView back = findViewById(R.id.iv_logo);
        TextView title = findViewById(R.id.txtContent);
        TextView change = findViewById(R.id.txtChange);
        if(title!=null) title.setText("Xác nhận thanh toán");
        if(change!=null) change.setVisibility(View.GONE);
        if(back!=null) back.setOnClickListener(v -> finish());

        android.content.Intent src = getIntent();
        @SuppressWarnings("unchecked")
        final java.util.ArrayList<com.example.bepnhataapp.common.models.CartItem> purchasedItems =
                (java.util.ArrayList<com.example.bepnhataapp.common.models.CartItem>) src.getSerializableExtra("selected_items");

        final int goodsOld = src.getIntExtra("goods_total_old",0);
        final int shipFee = src.getIntExtra("shipping_fee",0);
        final int discount = src.getIntExtra("discount",0);
        final int saveDiscount = src.getIntExtra("save_discount", 0);
        final int voucherDiscount = src.getIntExtra("voucher_discount", 0);
        final int grand = src.getIntExtra("grand_total",0);
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
                        note
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

        java.util.ArrayList<com.example.bepnhataapp.common.models.CartItem> prods = (java.util.ArrayList<com.example.bepnhataapp.common.models.CartItem>) src.getSerializableExtra("selected_items");
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
            for(com.example.bepnhataapp.common.models.CartItem ci: prods){
                android.view.View item = inflater.inflate(R.layout.item_checkout_product, ll, false);
                ((android.widget.TextView)item.findViewById(R.id.tvTitle)).setText(ci.getTitle());
                java.text.NumberFormat nff = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));
                int factor = ci.getServing().startsWith("4")?2:1;
                ((android.widget.TextView)item.findViewById(R.id.tvPrice)).setText(nff.format(ci.getPrice()*factor)+"đ");
                android.widget.TextView tvOld = item.findViewById(R.id.tvOldPrice);
                tvOld.setText(nff.format(ci.getOldPrice()*factor)+"đ");
                tvOld.setPaintFlags(tvOld.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
                ((android.widget.TextView)item.findViewById(R.id.tvVariant)).setText("Phân loại: "+ci.getServing());
                ((android.widget.TextView)item.findViewById(R.id.tvQuantity)).setText("x"+ci.getQuantity());
                com.bumptech.glide.Glide.with(item.getContext()).load(ci.getThumb()).placeholder(R.drawable.sample_img).into((android.widget.ImageView)item.findViewById(R.id.imgProduct));
                ll.addView(item);
            }
        }
    }
} 