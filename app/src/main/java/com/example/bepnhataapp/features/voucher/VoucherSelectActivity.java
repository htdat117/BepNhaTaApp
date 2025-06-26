package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CouponDao;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Coupon;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class VoucherSelectActivity extends AppCompatActivity {
    @Override protected void onCreate(@Nullable Bundle saved){super.onCreate(saved);setContentView(R.layout.activity_select_voucher);
        ImageView back=findViewById(R.id.iv_logo);TextView title=findViewById(R.id.txtContent);TextView change=findViewById(R.id.txtChange);
        if(title!=null)title.setText("Chọn Voucher");if(change!=null)change.setVisibility(View.GONE);if(back!=null)back.setOnClickListener(v->finish());

        RecyclerView rv=findViewById(R.id.recyclerVoucher);rv.setLayoutManager(new LinearLayoutManager(this));
        List<VoucherItem> voucherList = new ArrayList<>();
        // Lấy voucher từ database
        String phone = SessionManager.getPhone(this);
        long customerId = -1;
        if (phone != null) {
            CustomerDao customerDao = new CustomerDao(this);
            Customer customer = customerDao.findByPhone(phone);
            if (customer != null) {
                customerId = customer.getCustomerID();
            }
        }
        CouponDao couponDao = new CouponDao(this);
        List<Coupon> coupons = couponDao.getAvailableForCustomer(customerId, 0); // orderPrice=0 để lấy tất cả
        for (Coupon c : coupons) {
            voucherList.add(new VoucherItem(
                c.getCouponTitle(),
                "Giảm " + c.getCouponValue() + "% tối đa " + (c.getMaxDiscount() != null ? c.getMaxDiscount() + "K" : "") + ", đơn tối thiểu " + c.getMinPrice() + "K",
                c.getExpireDate()
            ));
        }
        VoucherAdapter adapter=new VoucherAdapter(voucherList);rv.setAdapter(adapter);
        if (voucherList.isEmpty()) {
            android.widget.Toast.makeText(this, "Không có voucher khả dụng", android.widget.Toast.LENGTH_LONG).show();
        }

        findViewById(R.id.btnAgree).setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }
} 