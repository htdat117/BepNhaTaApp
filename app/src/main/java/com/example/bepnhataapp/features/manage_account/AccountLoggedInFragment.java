package com.example.bepnhataapp.features.manage_account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;

public class AccountLoggedInFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_logged_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUserName();

        // Liên kết các nút trạng thái đơn hàng với ManageOrderActivity
        View waitConfirm = view.findViewById(R.id.layout_wait_confirm);
        View waitPickup = view.findViewById(R.id.layout_wait_pickup);
        View outForDelivery = view.findViewById(R.id.layout_out_for_delivery);
        View reviewOrder = view.findViewById(R.id.layout_review_order);

        if (waitConfirm != null) {
            waitConfirm.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                intent.putExtra("tab_index", 0); // Chờ xác nhận
                startActivity(intent);
            });
        }
        if (waitPickup != null) {
            waitPickup.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                intent.putExtra("tab_index", 1); // Chờ lấy hàng
                startActivity(intent);
            });
        }
        if (outForDelivery != null) {
            outForDelivery.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                intent.putExtra("tab_index", 2); // Chờ giao hàng
                startActivity(intent);
            });
        }
        if (reviewOrder != null) {
            reviewOrder.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                intent.putExtra("tab_index", 3); // Đánh giá (hoặc Đã giao tuỳ theo thứ tự tab)
                startActivity(intent);
            });
        }
    }

    private void updateUserName() {
        TextView tvUserName = getView().findViewById(R.id.tvUserName);
        if (tvUserName == null) return;
        String phone = SessionManager.getPhone(requireContext());
        if (phone != null) {
            CustomerDao dao = new CustomerDao(requireContext());
            Customer c = dao.findByPhone(phone);
            if (c != null) {
                tvUserName.setText(c.getFullName());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserName();
    }
} 
