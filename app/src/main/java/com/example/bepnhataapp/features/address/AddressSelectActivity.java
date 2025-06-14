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
import com.example.bepnhataapp.common.models.AddressItem;
import java.util.ArrayList;
import java.util.List;

public class AddressSelectActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        // header setup
        ImageView ivBack = findViewById(R.id.iv_logo);
        TextView tvTitle = findViewById(R.id.txtContent);
        TextView tvChange = findViewById(R.id.txtChange);
        if(tvTitle!=null) tvTitle.setText("Chọn địa chỉ giao hàng");
        if(tvChange!=null) tvChange.setVisibility(View.GONE);
        if(ivBack!=null) ivBack.setOnClickListener(v->finish());

        RecyclerView recyclerView = findViewById(R.id.recyclerAddress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AddressAdapter adapter = new AddressAdapter(getMockData());
        recyclerView.setAdapter(adapter);
    }

    private List<AddressItem> getMockData(){
        List<AddressItem> list = new ArrayList<>();
        list.add(new AddressItem("Đoàn Kiên","033 523 6269","312 Quang Trung, Phường 10, Gò Vấp",true));
        list.add(new AddressItem("Đoàn Kiên","033 523 6269","312 Quang Trung, Phường 10, Gò Vấp",false));
        list.add(new AddressItem("Đoàn Kiên","033 523 6269","312 Quang Trung, Phường 10, Gò Vấp",false));
        return list;
    }
} 