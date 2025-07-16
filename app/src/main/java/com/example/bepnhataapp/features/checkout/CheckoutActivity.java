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
import com.example.bepnhataapp.common.model.CartItem;
import com.example.bepnhataapp.common.utils.CartHelper;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.common.utils.OrderHelper;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    /**
     * Demo flag: set to true if you want to test luồng "đã đăng nhập" (chọn địa chỉ sẵn có).
     * Giữ false mặc định để mở trang nhập thông tin giao hàng.
     */
    private static final boolean DEMO_LOGGED_IN = false;
    private RecyclerView recyclerView;
    private java.util.List<CartItem> orderItems;
    private TextView tvGrandTotal, tvBottomTotal, tvBottomSave;
    private Button btnPlaceOrder;
    private View cardShipping;
    private TextView tvShipName, tvShipPhone, tvShipLabel;
    private View layoutNamePhone;
    private com.example.bepnhataapp.common.model.AddressItem currentAddress;
    private String currentShipNote="";
    private int selectedPaymentIdx = -1; // 0 COD,1 VCB
    private static final int REQUEST_VOUCHER = 301;
    private com.example.bepnhataapp.common.model.Coupon selectedCoupon = null;
    private double voucherDiscount = 0;
    private double goodsTotalOld=0, shippingFee=0, discount=0, grandTotal=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // header controls
        ImageView ivBack = findViewById(R.id.btnBack); // back arrow in header layout_back_header
        TextView tvTitle = findViewById(R.id.txtContent);
        TextView tvChange = findViewById(R.id.txtChange);
        if (tvTitle != null) tvTitle.setText("Thanh toán");
        if (tvChange != null) tvChange.setVisibility(View.INVISIBLE); // keep space for centering
        if (ivBack != null) ivBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerOrderItems);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        tvBottomTotal = findViewById(R.id.tvBottomTotal);
        tvBottomSave = findViewById(R.id.tvBottomSave);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<CartItem> items;
        java.io.Serializable extra = getIntent().getSerializableExtra("selected_items");
        if(extra!=null && extra instanceof java.util.ArrayList){
            //noinspection unchecked
            items = (java.util.ArrayList<CartItem>) extra;
        }else{
            items = CartHelper.loadItems(this);
        }
        orderItems = items;
        OrderItemAdapter adapter = new OrderItemAdapter(items);
        recyclerView.setAdapter(adapter);

        // Calculate totals
        double totalGoods=0, save=0;
        for(CartItem ci: items){
            totalGoods+=ci.getTotal();
            save+=ci.getTotalSave();
        }
        goodsTotalOld = totalGoods + save;
        shippingFee = 20000;
        voucherDiscount = 0;
        discount = save + voucherDiscount;
        grandTotal = goodsTotalOld + shippingFee - discount;
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));

        TextView tvGoodsTotal = findViewById(R.id.tvGoodsTotal);
        TextView tvShippingFee = findViewById(R.id.tvShippingFee);
        TextView tvVoucherDiscount = findViewById(R.id.tvVoucherDiscount);
        TextView tvSaveDiscount = findViewById(R.id.tvSaveDiscount);

        if(tvGoodsTotal!=null) tvGoodsTotal.setText(nf.format(goodsTotalOld)+"đ");
        if(tvShippingFee!=null) tvShippingFee.setText(nf.format(shippingFee)+"đ");
        if(tvVoucherDiscount!=null) tvVoucherDiscount.setText("-"+nf.format(voucherDiscount)+"đ");
        if(tvSaveDiscount!=null) tvSaveDiscount.setText("-"+nf.format(save)+"đ");

        if(tvGrandTotal!=null) tvGrandTotal.setText(nf.format(grandTotal)+"đ");
        if(tvBottomTotal!=null) tvBottomTotal.setText(nf.format(grandTotal)+"đ");
        if(tvBottomSave!=null) tvBottomSave.setText("-"+nf.format(discount)+"đ");

        // Ẩn dòng Voucher ngay từ đầu nếu chưa có mã
        updateVoucherRowVisibility();

        cardShipping = findViewById(R.id.cardShipping);
        tvShipName = findViewById(R.id.tvShippingName);
        tvShipPhone = findViewById(R.id.tvShippingPhone);
        tvShipLabel = findViewById(R.id.tvShippingLabel);
        layoutNamePhone = findViewById(R.id.layoutNamePhone);

        // initial state
        if(SessionManager.isLoggedIn(this)){
            // load default address for current customer
            boolean addressAutoSelected = false;
            com.example.bepnhataapp.common.dao.AddressDao addressDao = new com.example.bepnhataapp.common.dao.AddressDao(this);
            com.example.bepnhataapp.common.model.Address def = addressDao.getDefault(getCurrentCustomerId());
            if(def == null){
                java.util.List<com.example.bepnhataapp.common.model.Address> list = addressDao.getByCustomer(getCurrentCustomerId());
                if(!list.isEmpty()){
                    def = list.get(0); // chọn địa chỉ đầu tiên khi không có mặc định
                    addressAutoSelected = true;
                }
            }
            if(def!=null){
                currentAddress = new com.example.bepnhataapp.common.model.AddressItem(def.getAddressID(),def.getReceiverName(),def.getPhone(),def.getAddressLine()+", "+def.getDistrict()+", "+def.getProvince(),def.isDefault());
                currentShipNote = def.getNote();
                if(addressAutoSelected || !def.isDefault()){
                    android.widget.Toast.makeText(this,"Đã chọn địa chỉ giao hàng",android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            // Guest checkout: try to load the most recent address (customerID = 0)
            com.example.bepnhataapp.common.dao.AddressDao dao = new com.example.bepnhataapp.common.dao.AddressDao(this);
            com.example.bepnhataapp.common.model.Address defGuest = dao.getDefault(0);
            boolean guestAuto=false;
            if(defGuest == null){
                java.util.List<com.example.bepnhataapp.common.model.Address> guestList = dao.getByCustomer(0);
                if(!guestList.isEmpty()) {defGuest = guestList.get(0); guestAuto=true;}
            }
            if(defGuest != null){
                currentAddress = new com.example.bepnhataapp.common.model.AddressItem(defGuest.getAddressID(), defGuest.getReceiverName(), defGuest.getPhone(), defGuest.getAddressLine()+", "+defGuest.getDistrict()+", "+defGuest.getProvince(), defGuest.isDefault());
                currentShipNote = defGuest.getNote();
                if(guestAuto || !defGuest.isDefault()){
                    android.widget.Toast.makeText(this,"Đã chọn địa chỉ giao hàng: "+defGuest.getAddressLine(),android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        }

        if(currentAddress==null){
            // show placeholder
            if(tvShipLabel!=null) tvShipLabel.setVisibility(View.GONE);
            if(layoutNamePhone!=null) layoutNamePhone.setVisibility(View.GONE);
            TextView tvAddr = findViewById(R.id.tvShippingAddress);
            if(tvAddr!=null) tvAddr.setText("Nhập địa chỉ giao hàng");
        }else{
            if(tvShipLabel!=null) tvShipLabel.setVisibility(View.VISIBLE);
            if(layoutNamePhone!=null) layoutNamePhone.setVisibility(View.VISIBLE);
            if(tvShipName!=null) tvShipName.setText(currentAddress.getName());
            if(tvShipPhone!=null) tvShipPhone.setText(currentAddress.getPhone());
            TextView tvAddr = findViewById(R.id.tvShippingAddress);
            if(tvAddr!=null) tvAddr.setText(currentAddress.getAddress());
        }

        if(cardShipping!=null){
            cardShipping.setOnClickListener(v->{
                if(SessionManager.isLoggedIn(CheckoutActivity.this)){
                    // đã đăng nhập: chọn địa chỉ từ danh sách
                    android.content.Intent intent = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.address.AddressSelectActivity.class);
                    if(currentAddress!=null){
                        intent.putExtra("selected_address_id", currentAddress.getId());
                    }
                    startActivityForResult(intent, 201);
                }else{
                    if(currentAddress!=null){
                        // đã có địa chỉ tạm, mở form chỉnh sửa
                        android.content.Intent it = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.checkout.ShippingInfoActivity.class);
                        it.putExtra("address_id", currentAddress.getId());
                        startActivityForResult(it, 201);
                    }else{
                        // chưa có -> hiển thị dialog chọn login hay mua nhanh
                        showLoginDialog();
                    }
                }
            });
        }

        TextView tvVoucherLink = findViewById(R.id.tvVoucher);
        if(tvVoucherLink!=null){
            tvVoucherLink.setOnClickListener(v->{
                android.content.Intent it=new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.voucher.VoucherSelectActivity.class);
                double orderSubtotal=0; for(CartItem ci:orderItems){ orderSubtotal+=ci.getTotal(); }
                it.putExtra("order_subtotal", orderSubtotal);
                if(selectedCoupon!=null){ it.putExtra("selected_coupon_id", selectedCoupon.getCouponID()); }
                startActivityForResult(it, REQUEST_VOUCHER);
            });
        }

        btnPlaceOrder.setOnClickListener(v -> {
            // Validate address
            boolean hasAddress;
            if(SessionManager.isLoggedIn(this)){
                // Đã đăng nhập: chỉ cần người dùng đã chọn địa chỉ giao hàng (currentAddress)
                // thay vì buộc phải là địa chỉ mặc định trong CSDL
                hasAddress = currentAddress != null;
            }else{
                hasAddress = currentAddress!=null;
            }
            if(!hasAddress){
                android.widget.Toast.makeText(this,"Vui lòng nhập thông tin giao hàng",android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            if(selectedPaymentIdx==-1){
                android.widget.Toast.makeText(this,"Vui lòng chọn phương thức thanh toán",android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            // gather purchased items
            java.util.ArrayList<CartItem> purchased = new java.util.ArrayList<>();
            for(CartItem ci: orderItems) purchased.add(ci);

            // note & email
            String noteStr = ((android.widget.EditText)findViewById(R.id.edtNote)).getText().toString().trim();

            if(selectedPaymentIdx==0){ // COD
                // Lưu đơn hàng xuống DB
                OrderHelper.saveOrder(
                        CheckoutActivity.this,
                        purchased,
                        "COD",
                        currentAddress!=null? currentAddress.getId():0,
                        shippingFee,
                        discount,
                        noteStr,
                        grandTotal
                );

                com.example.bepnhataapp.common.utils.CartHelper.removeProducts(CheckoutActivity.this, purchased);
                android.content.Intent it = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.checkout.PaymentSuccessActivity.class);
                startActivity(it);
                finish();
            } else {
                android.content.Intent it;
                if(selectedPaymentIdx==2 || selectedPaymentIdx==3){
                    it = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.checkout.WalletPaymentActivity.class);
                }else{
                    it = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.checkout.ConfirmPaymentActivity.class);
                }
                // pass data
                if(currentAddress!=null){
                    it.putExtra("name", currentAddress.getName());
                    it.putExtra("phone", currentAddress.getPhone());
                    it.putExtra("address", currentAddress.getAddress());
                    if(currentShipNote!=null && !currentShipNote.trim().isEmpty()) it.putExtra("email", currentShipNote);
                    if(!noteStr.isEmpty()) it.putExtra("note", noteStr);
                }
                it.putExtra("goods_total_old", goodsTotalOld);
                it.putExtra("shipping_fee", shippingFee);
                it.putExtra("discount", discount);
                // gửi chi tiết giảm giá riêng lẻ
                double prodSave = 0; for(CartItem ci: orderItems){ prodSave += ci.getTotalSave(); }
                it.putExtra("save_discount", prodSave);
                it.putExtra("voucher_discount", voucherDiscount);
                it.putExtra("grand_total", grandTotal);
                // selected items
                it.putExtra("selected_items", purchased);
                String paymentMethodStr;
                switch (selectedPaymentIdx){
                    case 1: paymentMethodStr="VCB"; break;
                    case 2: paymentMethodStr="Momo"; break;
                    case 3: paymentMethodStr="ZaloPay"; break;
                    default: paymentMethodStr="Other"; break;
                }
                it.putExtra("payment_method", paymentMethodStr);
                startActivity(it);
            }
        });

        androidx.recyclerview.widget.RecyclerView rvPayment = findViewById(R.id.recyclerPayment);
        if(rvPayment!=null){
            rvPayment.setLayoutManager(new LinearLayoutManager(this));
            java.util.List<PaymentAdapter.Method> methods = java.util.Arrays.asList(
                    new PaymentAdapter.Method(R.drawable.ic_truck,"COD"),
                    new PaymentAdapter.Method(R.drawable.ic_vcb,"VCB"),
                    new PaymentAdapter.Method(R.drawable.ic_momo,"Momo"),
                    new PaymentAdapter.Method(R.drawable.ic_zlpay,"ZaloPay")
            );
            PaymentAdapter payAdapter = new PaymentAdapter(methods, idx -> selectedPaymentIdx = idx);
            rvPayment.setAdapter(payAdapter);
            rvPayment.setNestedScrollingEnabled(false);
            rvPayment.setHasFixedSize(false);
            rvPayment.setItemAnimator(null);
            rvPayment.setFocusable(false);

            androidx.recyclerview.widget.DividerItemDecoration div = new androidx.recyclerview.widget.DividerItemDecoration(this, androidx.recyclerview.widget.DividerItemDecoration.VERTICAL);
            div.setDrawable(androidx.core.content.ContextCompat.getDrawable(this, R.drawable.divider_1dp));
            rvPayment.addItemDecoration(div);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable android.content.Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_VOUCHER && resultCode==RESULT_OK && data!=null){
            com.example.bepnhataapp.common.model.Coupon cp = (com.example.bepnhataapp.common.model.Coupon) data.getSerializableExtra("selected_coupon");
            if(cp!=null){
                selectedCoupon = cp;
                android.widget.Toast.makeText(this,"Đã áp dụng voucher thành công",android.widget.Toast.LENGTH_SHORT).show();
                // Tính tổng giá trị đơn sau khuyến mãi sản phẩm
                double orderSubtotal = 0;
                for(CartItem itm : orderItems){ orderSubtotal += itm.getTotal(); }

                // Kiểm tra điều kiện tối thiểu
                if(orderSubtotal < cp.getMinPrice()){
                    android.widget.Toast.makeText(this, "Đơn hàng chưa đạt giá trị tối thiểu để dùng voucher", android.widget.Toast.LENGTH_SHORT).show();
                    selectedCoupon = null; voucherDiscount = 0;
                }else{
                    double disc;
                    if(cp.getCouponValue() <= 100){ // phần trăm
                        disc = orderSubtotal * cp.getCouponValue() / 100;
                        if(cp.getMaxDiscount()!=null) disc = Math.min(disc, cp.getMaxDiscount());
                    }else{ // mã giảm giá cố định theo đồng
                        disc = cp.getCouponValue();
                        if(cp.getMaxDiscount()!=null) disc = Math.min(disc, cp.getMaxDiscount());
                    }
                    // không để giảm quá tổng tiền
                    disc = Math.min(disc, orderSubtotal);
                    voucherDiscount = disc;
                }
                // cập nhật text voucher code hiển thị cho link
                TextView tvCode = findViewById(R.id.tvVoucher);
                if(tvCode!=null && selectedCoupon!=null){
                    tvCode.setText(cp.getCouponTitle());
                }
                // Cập nhật totals
                recalcTotals();
            }
        }
        if(requestCode==201 && resultCode==RESULT_OK && data!=null){
            com.example.bepnhataapp.common.model.AddressItem addr = (com.example.bepnhataapp.common.model.AddressItem) data.getSerializableExtra("selected_address");
            if(addr!=null){
                currentAddress = addr;
                String tmpNote = data.getStringExtra("selected_note");
                if(tmpNote==null) tmpNote = data.getStringExtra("selected_email");
                currentShipNote = tmpNote;
                if(tvShipLabel!=null) tvShipLabel.setVisibility(View.VISIBLE);
                if(layoutNamePhone!=null) layoutNamePhone.setVisibility(View.VISIBLE);
                if(tvShipName!=null) tvShipName.setText(addr.getName());
                if(tvShipPhone!=null) tvShipPhone.setText(addr.getPhone());
                TextView tvAddr = findViewById(R.id.tvShippingAddress);
                if(tvAddr!=null){
                    tvAddr.setText(addr.getAddress());
                }
            }
        }
    }

    private List<CartItem> getSampleItems() {
        List<CartItem> list = new ArrayList<>();
        // Đã xoá các dòng fix cứng sản phẩm mẫu
        return list;
    }

    private void showLoginDialog(){
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_login_required);
        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

        android.widget.Button btnLogin = dialog.findViewById(R.id.btnLogin);
        android.widget.Button btnGuest = dialog.findViewById(R.id.btnGuest);

        if(btnLogin!=null){
            btnLogin.setOnClickListener(v->{
                dialog.dismiss();
                android.content.Intent it = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.login.LoginActivity.class);
                startActivity(it);
            });
        }

        if(btnGuest!=null){
            btnGuest.setOnClickListener(v->{
                dialog.dismiss();
                android.content.Intent it = new android.content.Intent(CheckoutActivity.this, com.example.bepnhataapp.features.checkout.ShippingInfoActivity.class);
                startActivityForResult(it, 201);
            });
        }

        dialog.show();
    }

    private long getCurrentCustomerId(){
        if(!SessionManager.isLoggedIn(this)) return 0;
        String phone = SessionManager.getPhone(this);
        if(phone==null) return 0;
        com.example.bepnhataapp.common.dao.CustomerDao dao=new com.example.bepnhataapp.common.dao.CustomerDao(this);
        com.example.bepnhataapp.common.model.Customer c=dao.findByPhone(phone);
        return c!=null?c.getCustomerID():0;
    }

    private void recalcTotals(){
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi","VN"));
        double save=0; // recalculate save from order items
        for(CartItem ci: orderItems){save+=ci.getTotalSave();}
        double newDiscount = save + voucherDiscount;
        double newGrandTotal = goodsTotalOld + shippingFee - newDiscount;
        // update fields
        discount = newDiscount; grandTotal = newGrandTotal;

        TextView tvVoucherDiscount = findViewById(R.id.tvVoucherDiscount);
        TextView tvSaveDiscount = findViewById(R.id.tvSaveDiscount);
        if(tvVoucherDiscount!=null) tvVoucherDiscount.setText("-"+nf.format(voucherDiscount)+"đ");
        if(tvSaveDiscount!=null) tvSaveDiscount.setText("-"+nf.format(save)+"đ");
        if(tvGrandTotal!=null) tvGrandTotal.setText(nf.format(newGrandTotal)+"đ");
        if(tvBottomTotal!=null) tvBottomTotal.setText(nf.format(newGrandTotal)+"đ");
        if(tvBottomSave!=null) tvBottomSave.setText("-"+nf.format(newDiscount)+"đ");

        updateVoucherRowVisibility();
    }

    private void updateVoucherRowVisibility(){
        TextView tvVoucherDiscount = findViewById(R.id.tvVoucherDiscount);
        if(tvVoucherDiscount==null) return;
        View row = (View) tvVoucherDiscount.getParent();
        if(row!=null){
            row.setVisibility(voucherDiscount==0? View.GONE : View.VISIBLE);
        }
    }
} 
