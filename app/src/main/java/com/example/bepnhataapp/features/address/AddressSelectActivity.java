package com.example.bepnhataapp.features.address;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.AddressItem;
import java.util.ArrayList;
import java.util.List;

import com.example.bepnhataapp.common.dao.AddressDao;
import com.example.bepnhataapp.common.model.Address;
import com.example.bepnhataapp.common.utils.SessionManager;

public class AddressSelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        // header setup
        ImageView ivBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.txtContent);
        TextView tvChange = findViewById(R.id.txtChange);
        if(tvTitle!=null) tvTitle.setText("Chọn địa chỉ giao hàng");
        if(tvChange!=null) tvChange.setVisibility(View.GONE);
        if(ivBack!=null) ivBack.setOnClickListener(v->finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerAddress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<AddressItem> addrItems = loadAddresses();
        long preselectedId = getIntent().getLongExtra("selected_address_id", 0);
        AddressAdapter adapter = new AddressAdapter(addrItems, item -> {
            android.content.Intent it = new android.content.Intent(AddressSelectActivity.this, com.example.bepnhataapp.features.checkout.ShippingInfoActivity.class);
            it.putExtra("address_id", item.getId());
            startActivityForResult(it, 101);
        }, preselectedId);
        recyclerView.setAdapter(adapter);

        View layoutEmpty = findViewById(R.id.layoutEmpty);
        if(addrItems.isEmpty()){
            if(layoutEmpty!=null) layoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            findViewById(R.id.btnAccept).setVisibility(View.GONE);
        }else{
            if(layoutEmpty!=null) layoutEmpty.setVisibility(View.GONE);
        }

        // handle add new button in both cases
        View btnAdd = findViewById(R.id.btnAddAddress);
        View btnAddEmpty = findViewById(R.id.btnAddEmpty);
        View.OnClickListener addListener = v -> {
            android.content.Intent it = new android.content.Intent(AddressSelectActivity.this, com.example.bepnhataapp.features.checkout.ShippingInfoActivity.class);
            startActivityForResult(it, 100);
        };
        if(btnAdd!=null) btnAdd.setOnClickListener(addListener);
        if(btnAddEmpty!=null) btnAddEmpty.setOnClickListener(addListener);

        View btnAccept = findViewById(R.id.btnAccept);
        if(btnAccept!=null){
            btnAccept.setOnClickListener(v->{
                AddressItem sel = adapter.getSelectedItem();
                if(sel==null){
                    android.widget.Toast.makeText(this, "Vui lòng chọn địa chỉ", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                android.widget.Toast.makeText(this,"Đã chọn địa chỉ giao hàng",android.widget.Toast.LENGTH_SHORT).show();
                android.content.Intent res = new android.content.Intent();
                res.putExtra("selected_address", sel);
                // query email
                com.example.bepnhataapp.common.model.Address a = new com.example.bepnhataapp.common.dao.AddressDao(AddressSelectActivity.this).get(sel.getId());
                if(a!=null && a.getNote()!=null) res.putExtra("selected_email", a.getNote());
                setResult(RESULT_OK,res);
                finish();
            });
        }

        findViewById(R.id.btnUseCurrentLocation).setOnClickListener(v -> {
            android.app.Dialog dialog = new android.app.Dialog(this);
            dialog.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_location_permission);
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

            android.widget.Button btnAllow = dialog.findViewById(R.id.btnAllow);
            android.widget.Button btnSkip = dialog.findViewById(R.id.btnSkip);
            btnAllow.setOnClickListener(v2 -> {
                dialog.dismiss();
                android.widget.Toast.makeText(this, "Chọn địa chỉ thành công", android.widget.Toast.LENGTH_SHORT).show();
            });
            btnSkip.setOnClickListener(v2 -> dialog.dismiss());
            dialog.show();
        });
    }

    private List<AddressItem> loadAddresses(){
        List<AddressItem> list = new ArrayList<>();
        if(!SessionManager.isLoggedIn(this)) return list;

        String phone = SessionManager.getPhone(this);
        if(phone==null) return list;

        com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
        com.example.bepnhataapp.common.model.Customer customer = cDao.findByPhone(phone);
        if(customer==null){
            // trường hợp bất thường: chưa có bản ghi Customer, trả về danh sách rỗng
            return list;
        }
        long customerId = customer.getCustomerID();
        AddressDao dao = new AddressDao(this);
        List<Address> addresses = dao.getByCustomer(customerId);
        for(Address a: addresses){
            String fullAddr = a.getAddressLine()+", "+a.getDistrict()+", "+a.getProvince();
            list.add(new AddressItem(a.getAddressID(), a.getReceiverName(), a.getPhone(), fullAddr, a.isDefault()));
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable android.content.Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==100 || requestCode==101) && resultCode==RESULT_OK){
            recreate(); // simple way: recreate activity to reload list
        }
    }
} 
