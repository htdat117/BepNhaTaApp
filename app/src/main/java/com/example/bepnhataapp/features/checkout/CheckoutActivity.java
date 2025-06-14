package com.example.bepnhataapp.features.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    /**
     * Demo flag: set to true if you want to test luồng "đã đăng nhập" (chọn địa chỉ sẵn có).
     * Giữ false mặc định để mở trang nhập thông tin giao hàng.
     */
    private static final boolean DEMO_LOGGED_IN = false;
    private RecyclerView recyclerView;
    private TextView tvGrandTotal, tvBottomTotal, tvBottomSave;
    private Button btnPlaceOrder;
    private View cardShipping;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // header controls
        ImageView ivBack = findViewById(R.id.iv_logo); // back arrow in header layout_back_header
        TextView tvTitle = findViewById(R.id.txtContent);
        TextView tvChange = findViewById(R.id.txtChange);
        if (tvTitle != null) tvTitle.setText("Thanh toán");
        if (tvChange != null) tvChange.setVisibility(View.GONE);
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerOrderItems);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        tvBottomTotal = findViewById(R.id.tvBottomTotal);
        tvBottomSave = findViewById(R.id.tvBottomSave);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderItemAdapter adapter = new OrderItemAdapter(getSampleItems());
        recyclerView.setAdapter(adapter);

        boolean isLoggedIn = DEMO_LOGGED_IN;
        cardShipping = findViewById(R.id.cardShipping);
        if(cardShipping!=null){
            cardShipping.setOnClickListener(v->{
                android.content.Intent intent;
                if(isLoggedIn){
                    intent = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.address.AddressSelectActivity.class);
                }else{
                    intent = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.checkout.ShippingInfoActivity.class);
                }
                startActivity(intent);
            });
        }

        TextView tvVoucherLink = findViewById(R.id.tvVoucher);
        if(tvVoucherLink!=null){
            tvVoucherLink.setOnClickListener(v->{
                android.content.Intent it=new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.voucher.VoucherSelectActivity.class);
                startActivity(it);
            });
        }

        btnPlaceOrder.setOnClickListener(v -> {
            android.content.Intent it = new android.content.Intent(CheckoutActivity.this, ConfirmPaymentActivity.class);
            startActivity(it);
        });
    }

    private List<CartItem> getSampleItems() {
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem("Gói nguyên liệu Bò kho","90.000đ","2 người"));
        list.add(new CartItem("Gói nguyên liệu Bò kho","90.000đ","2 người"));
        return list;
    }
} 