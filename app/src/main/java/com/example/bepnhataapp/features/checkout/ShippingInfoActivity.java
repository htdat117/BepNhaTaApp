package com.example.bepnhataapp.features.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.AddressDao;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Address;
import com.example.bepnhataapp.common.utils.SessionManager;

public class ShippingInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_info);

        // header setup
        ((android.widget.TextView) findViewById(R.id.txtContent)).setText("Thông tin giao hàng");
        findViewById(R.id.txtChange).setVisibility(View.GONE);
        View header = findViewById(R.id.header);
        if (header != null) {
            View btnBack = header.findViewById(R.id.btnBack);
            if (btnBack != null) {
                btnBack.setOnClickListener(v -> finish());
            }
        }

        attachClear(R.id.edtName);
        attachClear(R.id.edtPhone);
        attachClear(R.id.edtCity);
        attachClear(R.id.edtDistrict);
        attachClear(R.id.edtAddress);

        // check if editing existing address
        long addressId = getIntent().getLongExtra("address_id", -1);
        AddressDao dao = new AddressDao(this);
        Address editingAddr = null;
        android.widget.CheckBox cbDefault = findViewById(R.id.cbDefault);
        if (addressId > 0) {
            editingAddr = dao.get(addressId);
            if (editingAddr != null) {
                // update header
                ((android.widget.TextView) findViewById(R.id.txtContent)).setText("Chỉnh sửa địa chỉ");
                ((android.widget.Button) findViewById(R.id.btnSubmit)).setText("Lưu");

                // pre-fill fields
                ((EditText) findViewById(R.id.edtName)).setText(editingAddr.getReceiverName());
                ((EditText) findViewById(R.id.edtPhone)).setText(editingAddr.getPhone());
                ((EditText) findViewById(R.id.edtCity)).setText(editingAddr.getProvince());
                ((EditText) findViewById(R.id.edtDistrict)).setText(editingAddr.getDistrict());
                ((EditText) findViewById(R.id.edtAddress)).setText(editingAddr.getAddressLine());
                ((EditText) findViewById(R.id.edtNote)).setText(editingAddr.getNote()!=null?editingAddr.getNote():"");
                if(cbDefault!=null) cbDefault.setChecked(editingAddr.isDefault());
            }
        }

        if(!SessionManager.isLoggedIn(this) && cbDefault!=null) cbDefault.setVisibility(View.GONE);

        Address finalEditingAddr = editingAddr; // need effectively final for lambda

        findViewById(R.id.btnSubmit).setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.edtName)).getText().toString().trim();
            String phone = ((EditText) findViewById(R.id.edtPhone)).getText().toString().trim();
            String city = ((EditText) findViewById(R.id.edtCity)).getText().toString().trim();
            String district = ((EditText) findViewById(R.id.edtDistrict)).getText().toString().trim();
            String addressLine = ((EditText) findViewById(R.id.edtAddress)).getText().toString().trim();
            String noteStr = ((EditText) findViewById(R.id.edtNote)).getText().toString().trim();
            boolean isDefaultChecked = cbDefault!=null && cbDefault.isChecked();

            if (name.isEmpty() || phone.isEmpty() || city.isEmpty() || addressLine.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            String normalized = phone.replaceAll("\\s+", "");
            if (normalized.startsWith("+84")) normalized = "0" + normalized.substring(3);
            else if (normalized.startsWith("84") && normalized.length() > 2) normalized = "0" + normalized.substring(2);

            String vnRegex = "^0((3[2-9])|(5[689])|(7[06-9])|(8[1-5])|(9[0-9]))[0-9]{7}$";
            if (!normalized.matches(vnRegex)) {
                Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            phone = normalized;

            long customerId = 0;
            if (SessionManager.isLoggedIn(this)) {
                String p = SessionManager.getPhone(this);
                if (p != null) {
                    com.example.bepnhataapp.common.model.Customer c = new CustomerDao(this).findByPhone(p);
                    if (c != null) customerId = c.getCustomerID();
                }
            }

            Address addr;
            if (finalEditingAddr != null) {
                addr = finalEditingAddr;
            } else {
                addr = new Address();
                addr.setCustomerID(customerId);
                // default true for new address
                addr.setDefault(true);
            }

            addr.setReceiverName(name);
            addr.setPhone(phone);
            addr.setProvince(city);
            addr.setDistrict(district);
            addr.setAddressLine(addressLine);
            addr.setNote(noteStr);
            addr.setDefault(isDefaultChecked);

            if (isDefaultChecked){
                // clear existing default for this customer
                android.database.sqlite.SQLiteDatabase db = new com.example.bepnhataapp.common.databases.DBHelper(this).getWritableDatabase();
                db.execSQL("UPDATE "+ com.example.bepnhataapp.common.databases.DBHelper.TBL_ADDRESSES +" SET isDefault=0 WHERE customerID=?", new Object[]{customerId});
            }

            if (finalEditingAddr != null) {
                dao.update(addr);
                Toast.makeText(this, "Đã cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
            } else {
                long id = dao.insert(addr);
                addr.setAddressID(id);
                Toast.makeText(this, "Đã lưu địa chỉ mới", Toast.LENGTH_SHORT).show();
            }

            // trả về để CheckoutActivity hiển thị
            com.example.bepnhataapp.common.model.AddressItem ai = new com.example.bepnhataapp.common.model.AddressItem(
                    addr.getAddressID(),
                    addr.getReceiverName(),
                    addr.getPhone(),
                    addr.getAddressLine()+", "+addr.getDistrict()+", "+addr.getProvince(),
                    addr.isDefault());

            android.content.Intent res = new android.content.Intent();
            res.putExtra("selected_address", ai);
            res.putExtra("selected_note", noteStr);
            res.putExtra("is_default", addr.isDefault());
            setResult(RESULT_OK, res);
            finish();
        });

        // Delete button (visible only when editing existing address)
        android.widget.Button btnDelete = findViewById(R.id.btnDelete);
        if(finalEditingAddr==null || (finalEditingAddr!=null && finalEditingAddr.getCustomerID()==0)){
            if(btnDelete!=null) btnDelete.setVisibility(View.GONE);
            View spacer = findViewById(R.id.spacerDelete);
            if(spacer!=null) spacer.setVisibility(View.GONE);
            android.widget.Button btnSubmit = findViewById(R.id.btnSubmit);
            if(btnSubmit!=null){
                if(finalEditingAddr==null) btnSubmit.setText("Đồng ý");
                else btnSubmit.setText("Lưu");
            }
        }else{
            if(btnDelete!=null){
                btnDelete.setOnClickListener(v->{
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Xoá địa chỉ")
                            .setMessage("Bạn có chắc muốn xoá địa chỉ này?")
                            .setPositiveButton("Xoá", (d,w)->{
                                dao.delete(finalEditingAddr.getAddressID());
                                Toast.makeText(this,"Đã xoá địa chỉ",Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            })
                            .setNegativeButton("Huỷ",null)
                            .show();
                });
            }
        }
    }
    private void attachClear(int includeId){
        View inc = findViewById(includeId);
        if (inc == null) return;

        if(inc instanceof EditText){
            // this layout uses plain EditText, so nothing to attach
            return;
        }

        EditText et = inc.findViewById(R.id.etInput);
        ImageButton btn = inc.findViewById(R.id.btnClear);
        if (et != null && btn != null) {
            btn.setOnClickListener(v -> et.setText(""));
        }
    }
} 
