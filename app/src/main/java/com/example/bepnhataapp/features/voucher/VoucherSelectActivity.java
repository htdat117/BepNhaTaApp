package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        RecyclerView rv=findViewById(R.id.recyclerVoucher);
        rv.setLayoutManager(new LinearLayoutManager(this));
        // Nhận subtotal từ intent để ước tính mức giảm sơ bộ
        final int orderSubtotalIntent = getIntent().getIntExtra("order_subtotal",0);
        final long preselectedId = getIntent().getLongExtra("selected_coupon_id",-1);
        // ==== Load coupon list ====
        List<VoucherItem> voucherList = new ArrayList<>();
        String phone = SessionManager.getPhone(this);
        long customerId = -1;
        if (phone != null) {
            CustomerDao customerDao = new CustomerDao(this);
            Customer customer = customerDao.findByPhone(phone);
            if (customer != null) customerId = customer.getCustomerID();
        }
        CouponDao couponDao = new CouponDao(this);
        final List<Coupon> coupons = couponDao.getAvailableForCustomer(customerId, 0);
        for (Coupon c : coupons) {
            boolean enabled = orderSubtotalIntent >= c.getMinPrice();
            voucherList.add(new VoucherItem(
                    c.getCouponTitle(),
                    (c.getCouponValue()<=100? ("Giảm "+c.getCouponValue()+"%") : ("Giảm "+java.text.NumberFormat.getInstance().format(c.getCouponValue())+"đ")) +
                            (c.getMaxDiscount()!=null? (" tối đa "+java.text.NumberFormat.getInstance().format(c.getMaxDiscount())+"đ") : "") +
                            ", đơn tối thiểu "+java.text.NumberFormat.getInstance().format(c.getMinPrice())+"đ",
                    c.getExpireDate(),
                    enabled
            ));
        }
        VoucherAdapter adapter=new VoucherAdapter(voucherList);
        rv.setAdapter(adapter);
        if (voucherList.isEmpty()) {
            android.widget.Toast.makeText(this, "Không có voucher khả dụng", android.widget.Toast.LENGTH_LONG).show();
        }

        TextView tvCodeSel = findViewById(R.id.tvVoucherCode);
        TextView tvDiscSel = findViewById(R.id.tvVoucherDiscount);
        final android.view.View summaryRow = findViewById(R.id.layoutSummary);
        if(summaryRow!=null) summaryRow.setVisibility(android.view.View.GONE);

        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));

        // Nếu có voucher đã chọn trước đó thì đánh dấu radio và cập nhật summary
        if(preselectedId!=-1){
            int idxSel=-1;
            for(int i=0;i<coupons.size();i++){ if(coupons.get(i).getCouponID()==preselectedId){ idxSel=i; break; } }
            if(idxSel!=-1){
                adapter.setOnVoucherSelectListener(null); // tránh callback khi set selected
                java.lang.reflect.Field f=null;
                try{f=adapter.getClass().getDeclaredField("selected");f.setAccessible(true);f.setInt(adapter,idxSel);}catch(Exception ignored){}
                adapter.notifyDataSetChanged();
                // also update bottom summary
                Coupon sel=coupons.get(idxSel);
                if(tvCodeSel!=null){
                    ViewGroup row = (ViewGroup) tvCodeSel.getParent();
                    if(row.getChildCount()>0){
                        TextView lbl = (TextView) row.getChildAt(0);
                        lbl.setText("Đã giảm giá ");
                        lbl.setVisibility(View.VISIBLE);
                    }
                    if(row.getChildCount()>2){ row.getChildAt(2).setVisibility(View.GONE); }
                }
                int discTemp;
                if(orderSubtotalIntent<=0) discTemp=0; else{
                    if(sel.getCouponValue()<=100){ discTemp= orderSubtotalIntent*sel.getCouponValue()/100; if(sel.getMaxDiscount()!=null) discTemp=Math.min(discTemp, sel.getMaxDiscount()); }
                    else{ discTemp= sel.getCouponValue(); if(sel.getMaxDiscount()!=null) discTemp=Math.min(discTemp, sel.getMaxDiscount()); }
                    discTemp=Math.min(discTemp, orderSubtotalIntent);
                }
                if(tvDiscSel!=null) tvDiscSel.setText("-"+nf.format(discTemp)+"đ");
                // restore listener
                adapter.setOnVoucherSelectListener(idx->{
                    Coupon cpSel=coupons.get(idx);
                    int disc; if(orderSubtotalIntent<=0) disc=0; else{
                        if(cpSel.getCouponValue()<=100){ disc=orderSubtotalIntent*cpSel.getCouponValue()/100; if(cpSel.getMaxDiscount()!=null) disc=Math.min(disc, cpSel.getMaxDiscount()); }
                        else{ disc=cpSel.getCouponValue(); if(cpSel.getMaxDiscount()!=null) disc=Math.min(disc, cpSel.getMaxDiscount()); }
                        disc=Math.min(disc, orderSubtotalIntent);
                    } if(tvCodeSel!=null){
                        ViewGroup row = (ViewGroup) tvCodeSel.getParent();
                        tvCodeSel.setVisibility(View.GONE);
                        if(row.getChildCount()>0){ ((TextView)row.getChildAt(0)).setText("Đã giảm giá "); }
                        if(row.getChildCount()>2){ row.getChildAt(2).setVisibility(View.GONE); }
                    }
                    if(tvDiscSel!=null) tvDiscSel.setText("-"+nf.format(disc)+"đ");
                    // Summary row (LinearLayout chứa các TextView) – ẩn ban đầu
                    if(summaryRow!=null && summaryRow.getVisibility()!=android.view.View.VISIBLE) summaryRow.setVisibility(android.view.View.VISIBLE);
                });
            }
        }

        // Listener cập nhật realtime khi chọn voucher
        adapter.setOnVoucherSelectListener(idx->{
            Coupon cpSel = coupons.get(idx);
            int disc;
            if(orderSubtotalIntent<=0){ disc=0; }
            else{
                if(cpSel.getCouponValue()<=100){ disc = orderSubtotalIntent * cpSel.getCouponValue()/100; if(cpSel.getMaxDiscount()!=null) disc=Math.min(disc, cpSel.getMaxDiscount()); }
                else{ disc = cpSel.getCouponValue(); if(cpSel.getMaxDiscount()!=null) disc=Math.min(disc, cpSel.getMaxDiscount()); }
                disc = Math.min(disc, orderSubtotalIntent);
            }
            if(tvCodeSel!=null){
                ViewGroup row = (ViewGroup) tvCodeSel.getParent();
                tvCodeSel.setVisibility(View.GONE);
                if(row.getChildCount()>0){ ((TextView)row.getChildAt(0)).setText("Đã giảm giá "); }
                if(row.getChildCount()>2){ row.getChildAt(2).setVisibility(View.GONE); }
            }
            if(tvDiscSel!=null) tvDiscSel.setText("-"+nf.format(disc)+"đ");
            if(summaryRow!=null && summaryRow.getVisibility()!=View.VISIBLE) summaryRow.setVisibility(View.VISIBLE);
        });

        findViewById(R.id.btnAgree).setOnClickListener(v->{
            int idx = adapter.getSelectedIndex();
            if(idx<0){
                android.widget.Toast.makeText(this, "Vui lòng chọn voucher", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            Coupon selectedCoupon = coupons.get(idx);
            android.content.Intent result = new android.content.Intent();
            result.putExtra("selected_coupon", selectedCoupon);
            setResult(RESULT_OK, result);
            finish();
        });
    }
} 