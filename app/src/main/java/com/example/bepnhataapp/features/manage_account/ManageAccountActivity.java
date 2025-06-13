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
}