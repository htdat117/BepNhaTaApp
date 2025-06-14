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
import java.util.ArrayList;
import java.util.List;

public class VoucherSelectActivity extends AppCompatActivity {
    @Override protected void onCreate(@Nullable Bundle saved){super.onCreate(saved);setContentView(R.layout.activity_select_voucher);
        ImageView back=findViewById(R.id.iv_logo);TextView title=findViewById(R.id.txtContent);TextView change=findViewById(R.id.txtChange);
        if(title!=null)title.setText("Chọn Voucher");if(change!=null)change.setVisibility(View.GONE);if(back!=null)back.setOnClickListener(v->finish());

        RecyclerView rv=findViewById(R.id.recyclerVoucher);rv.setLayoutManager(new LinearLayoutManager(this));
        VoucherAdapter adapter=new VoucherAdapter(mock());rv.setAdapter(adapter);

        findViewById(R.id.btnAgree).setOnClickListener(v->{
            setResult(RESULT_OK);
            finish();
        });
    }
    private List<VoucherItem> mock(){List<VoucherItem> l=new ArrayList<>();for(int i=0;i<6;i++){l.add(new VoucherItem("GIAM20%","Giảm 20% đơn hàng chào mừng tháng 4, giảm tối đa 60K","30/04/2024"));}return l;}
} 