package com.example.bepnhataapp.features.manage_account;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;

public class ManageAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Fragment fragment;
        if (isLoggedIn()) {
            fragment = new AccountLoggedInFragment();
        } else {
            fragment = new AccountNotLoggedInFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        // Thiết lập click cho các mục cần đăng nhập
        setupItemClicks();
    }

    private boolean isLoggedIn() {
        // TODO: Thay thế bằng logic kiểm tra đăng nhập thực tế
        return false;
    }

    @Override
    protected int getBottomNavigationContainerId() {
        // Trang này không có bottom navigation, trả về 0 hoặc layout id không tồn tại
        return 0;
    }

    // Thêm hàm thiết lập click cho các mục yêu cầu đăng nhập
    private void setupItemClicks() {
        if (isLoggedIn()) return; // Đã đăng nhập thì không cần popup

        int[] clickableIds = {
                R.id.layout_account_info,
                R.id.layout_shipping_address,
                R.id.layout_member_points,
                R.id.layout_my_voucher,
                R.id.layout_favorite_dishes,
                R.id.layout_logout
        };

        for (int id : clickableIds) {
            android.view.View v = findViewById(id);
            if (v != null) {
                v.setOnClickListener(view -> showLoginPromptDialog());
            }
        }
    }

    // Hiển thị popup yêu cầu đăng nhập
    private void showLoginPromptDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        android.view.View dialogView = getLayoutInflater().inflate(R.layout.dialog_login_prompt, null);
        builder.setView(dialogView);

        final android.app.AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialogView.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());
        dialogView.findViewById(R.id.btnOk).setOnClickListener(v -> {
            dialog.dismiss();
            // Mở màn hình đăng nhập
            android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.login.LoginActivity.class);
            startActivity(intent);
        });

        dialog.setOnShowListener(dlg -> {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85); // thu hẹp 85% màn hình
            dialog.getWindow().setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        });

        dialog.show();
    }
}