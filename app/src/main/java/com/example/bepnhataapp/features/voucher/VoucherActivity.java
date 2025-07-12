package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.core.view.WindowCompat;
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

import androidx.core.content.ContextCompat;

public class VoucherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity hiện tại
                }
            });
        }

        RecyclerView rvVouchers = findViewById(R.id.rvVouchers);
        LinearLayout emptyView = findViewById(R.id.layoutEmptyVoucher);
        if (rvVouchers != null) {
            rvVouchers.setLayoutManager(new LinearLayoutManager(this));
            List<VoucherItem> voucherList = new ArrayList<>();
            // Lấy voucher từ database
            String phone = SessionManager.getPhone(this);
            long customerId = -1;
            int userPoint = 0;
            if (phone != null) {
                CustomerDao customerDao = new CustomerDao(this);
                Customer customer = customerDao.findByPhone(phone);
                if (customer != null) {
                    customerId = customer.getCustomerID();
                    userPoint = customer.getLoyaltyPoint(); // Lấy điểm user
                }
            }
            CouponDao couponDao = new CouponDao(this);
            List<Coupon> coupons = couponDao.getAvailableForCustomer(customerId, 0); // orderPrice=0 để lấy tất cả
            for (Coupon c : coupons) {
                voucherList.add(new VoucherItem(
                    c.getCouponTitle(),
                    (c.getCouponValue()<=100? ("Giảm "+c.getCouponValue()+"%") : ("Giảm "+java.text.NumberFormat.getInstance().format(c.getCouponValue())+"đ")) +
                            (c.getMaxDiscount()!=null? (" tối đa "+java.text.NumberFormat.getInstance().format(c.getMaxDiscount())+"đ") : "") +
                            ", đơn tối thiểu "+java.text.NumberFormat.getInstance().format(c.getMinPrice())+"đ",
                    c.getExpireDate(),
                    true,
                    36 // điểm cần để đổi voucher, bạn có thể sửa lại cho phù hợp
                ));
            }
            VoucherDisplayAdapter adapter = new VoucherDisplayAdapter(voucherList, this, userPoint);
            rvVouchers.setAdapter(adapter);
            if (emptyView != null) {
                emptyView.setVisibility(voucherList.isEmpty() ? View.VISIBLE : View.GONE);
            }
            rvVouchers.setVisibility(voucherList.isEmpty() ? View.GONE : View.VISIBLE);
        }

        // Màu status bar + icon trắng
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary1));
        WindowInsetsControllerCompat ic = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if(ic!=null) ic.setAppearanceLightStatusBars(false);
    }
}
