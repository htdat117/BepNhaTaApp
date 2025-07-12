package com.example.bepnhataapp.features.voucher;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
        ImageView back=findViewById(R.id.btnBack);TextView title=findViewById(R.id.txtContent);TextView change=findViewById(R.id.txtChange);
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
        final long currentCustomerId = customerId; // make effectively final for lambdas
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

        //==== New controls for manual voucher input ====
        EditText edtVoucher = findViewById(R.id.edtVoucher);
        Button btnApply  = findViewById(R.id.btnApply);
        // Keep references final so we can use inside lambda
        final List<Coupon> couponsMutable = coupons; // alias to emphasise we may mutate
        final List<VoucherItem> voucherItemsMutable = voucherList;
        if(btnApply!=null && edtVoucher!=null){
            btnApply.setOnClickListener(v->{
                String codeInput = edtVoucher.getText().toString().trim().toUpperCase();
                if(codeInput.isEmpty()){
                    Toast.makeText(this,"Vui lòng nhập mã voucher",Toast.LENGTH_SHORT).show();
                    return;
                }
                // Avoid duplicate insert
                for(Coupon cTmp: couponsMutable){
                    if(cTmp.getCouponTitle().equalsIgnoreCase(codeInput)){
                        Toast.makeText(this,"Voucher đã tồn tại",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Coupon newCp = createSampleCoupon(codeInput, currentCustomerId);
                if(newCp==null){
                    Toast.makeText(this,"Mã voucher không hợp lệ",Toast.LENGTH_SHORT).show();
                    return;
                }
                long idIns = couponDao.insert(newCp);
                if(idIns<=0){
                    Toast.makeText(this,"Thêm voucher thất bại",Toast.LENGTH_SHORT).show();
                    return;
                }
                newCp.setCouponID(idIns);
                couponsMutable.add(newCp);
                boolean enabled = orderSubtotalIntent >= newCp.getMinPrice();
                voucherItemsMutable.add(new VoucherItem(
                        newCp.getCouponTitle(),
                        (newCp.getCouponValue()<=100? ("Giảm "+newCp.getCouponValue()+"%") : ("Giảm "+java.text.NumberFormat.getInstance().format(newCp.getCouponValue())+"đ")) +
                                (newCp.getMaxDiscount()!=null? (" tối đa "+java.text.NumberFormat.getInstance().format(newCp.getMaxDiscount())+"đ") : "") +
                                ", đơn tối thiểu "+java.text.NumberFormat.getInstance().format(newCp.getMinPrice())+"đ",
                        newCp.getExpireDate(),
                        enabled
                ));
                adapter.notifyItemInserted(voucherItemsMutable.size()-1);
                Toast.makeText(this,"Đã thêm voucher thành công",Toast.LENGTH_SHORT).show();
                edtVoucher.setText("");
            });
        }
        //==== End new controls ====

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

    // Helper to create sample coupon objects based on code
    private Coupon createSampleCoupon(String code, long customerId){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String today = sdf.format(cal.getTime());
        cal.add(java.util.Calendar.DAY_OF_YEAR, 30);
        String expire = sdf.format(cal.getTime());

        Coupon c = new Coupon();
        // Associate to customer if logged in, otherwise general
        if(customerId>0) c.setCustomerID(customerId); else c.setCustomerID(null);
        c.setCouponTitle(code);
        c.setValidDate(today);
        c.setExpireDate(expire);
        c.setIsGeneral(customerId>0?0:1);
        c.setExchangePoints(null);

        switch(code){
            case "GIAM10": // 10% off, no cap
                c.setCouponValue(10);
                c.setMinPrice(0);
                c.setMaxDiscount(null);
                break;
            case "GIAM20K": // 20k VND off for orders >= 100k
                c.setCouponValue(20000);
                c.setMinPrice(100000);
                c.setMaxDiscount(null);
                break;
            case "GIAM50K": // 50k off, cap 50k, min 300k
                c.setCouponValue(50000);
                c.setMinPrice(300000);
                c.setMaxDiscount(50000);
                break;
            case "BEPNHATA": // 100k off, cap 100k, min 0
                c.setCouponValue(20);
                c.setMinPrice(200000);
                c.setMaxDiscount(100000);
                break;
            default:
                return null; // unrecognised code
        }
        return c;
    }
} 
