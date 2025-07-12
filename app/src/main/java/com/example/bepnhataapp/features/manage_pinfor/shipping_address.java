package com.example.bepnhataapp.features.manage_pinfor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.AddressDao;
import com.example.bepnhataapp.common.model.Address;
import com.example.bepnhataapp.common.model.AddressItem;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.features.address.AddressAdapter;

import java.util.ArrayList;
import java.util.List;

public class shipping_address extends AppCompatActivity {

    private static final int REQ_ADD_ADDRESS = 100;
    private static final int REQ_EDIT_ADDRESS = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shipping_address);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) btnBack.setOnClickListener(v -> finish());

        // Setup RecyclerView
        RecyclerView rv = findViewById(R.id.rv_addresses);
        rv.setLayoutManager(new LinearLayoutManager(this));
        List<AddressItem> items = loadAddresses();
        AddressAdapter adapter = new AddressAdapter(items, item -> {
            android.content.Intent it = new android.content.Intent(shipping_address.this, com.example.bepnhataapp.features.checkout.ShippingInfoActivity.class);
            it.putExtra("address_id", item.getId());
            startActivityForResult(it, REQ_EDIT_ADDRESS);
        });
        rv.setAdapter(adapter);

        // Add new address button
        Button btnAdd = findViewById(R.id.btn_add_address);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                android.content.Intent it = new android.content.Intent(shipping_address.this, com.example.bepnhataapp.features.checkout.ShippingInfoActivity.class);
                startActivityForResult(it, REQ_ADD_ADDRESS);
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQ_ADD_ADDRESS || requestCode == REQ_EDIT_ADDRESS) && resultCode == RESULT_OK) {
            // Simply reload the activity to refresh list
            recreate();
        }
    }

    private List<AddressItem> loadAddresses() {
        List<AddressItem> list = new ArrayList<>();

        if (!SessionManager.isLoggedIn(this)) return list;

        String phone = SessionManager.getPhone(this);
        if (phone == null) return list;

        com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
        com.example.bepnhataapp.common.model.Customer customer = cDao.findByPhone(phone);
        if (customer == null) return list;

        long customerId = customer.getCustomerID();
        AddressDao dao = new AddressDao(this);
        List<Address> addresses = dao.getByCustomer(customerId);
        for (Address a : addresses) {
            String fullAddr = a.getAddressLine() + ", " + a.getDistrict() + ", " + a.getProvince();
            list.add(new AddressItem(a.getAddressID(), a.getReceiverName(), a.getPhone(), fullAddr, a.isDefault()));
        }
        return list;
    }
}
