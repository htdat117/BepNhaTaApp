package com.example.bepnhataapp.features.manage_account;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.core.view.WindowCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.base.BaseActivity;
import com.example.bepnhataapp.features.point.PointActivity;
import com.example.bepnhataapp.features.voucher.VoucherActivity;
import com.example.bepnhataapp.common.utils.SessionManager;

public class ManageAccountActivity extends BaseActivity implements BaseActivity.OnNavigationItemReselectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Đặt màu status bar và icon trắng
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary1));
        WindowInsetsControllerCompat ic = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (ic != null) ic.setAppearanceLightStatusBars(false);

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

        // Nếu đã đăng nhập, gán sự kiện Đăng xuất
        if (isLoggedIn()) {
            android.view.View logoutView = findViewById(R.id.layout_logout);
            if (logoutView != null) {
                logoutView.setOnClickListener(v -> showLogoutConfirmDialog());
            }
        }

        // Sau khi setup item clicks dành cho login prompt
        android.view.View faqView = findViewById(R.id.layout_faq);
        if (faqView != null) {
            faqView.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.faq.FAQActivity.class);
                startActivity(intent);
            });
        }

        // Mở trang Ý kiến khách hàng
        android.view.View ideaView = findViewById(R.id.layout_customer_idea);
        if (ideaView != null) {
            ideaView.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.customeridea.CustomerIdeaActivity.class);
                startActivity(intent);
            });
        }

        // Mở trang Chính sách và Điều khoản
        android.view.View policyTermsView = findViewById(R.id.layout_policy_terms);
        if (policyTermsView != null) {
            policyTermsView.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.policy.PolicyActivity.class);
                startActivity(intent);
            });
        }

        // Xử lý click cho Điểm thành viên và Voucher của tôi
        android.view.View memberPointsView = findViewById(R.id.layout_member_points);
        if (memberPointsView != null) {
            memberPointsView.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, PointActivity.class);
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }

        android.view.View myVoucherView = findViewById(R.id.layout_my_voucher);
        if (myVoucherView != null) {
            myVoucherView.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, VoucherActivity.class);
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }

        // Xử lý click cho Thông tin tài khoản và Địa chỉ giao hàng (chỉ khi đã đăng nhập)
        if (isLoggedIn()) {
            android.view.View accountInfoView = findViewById(R.id.layout_account_info);
            if (accountInfoView != null) {
                accountInfoView.setOnClickListener(v -> {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.manage_pinfor.account_infor.class);
                    startActivity(intent);
                });
            }

            android.view.View shippingAddressView = findViewById(R.id.layout_shipping_address);
            if (shippingAddressView != null) {
                shippingAddressView.setOnClickListener(v -> {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.manage_pinfor.shipping_address.class);
                    startActivity(intent);
                });
            }
        }

        // Xử lý click cho các trạng thái đơn hàng
        android.view.View waitConfirmView = findViewById(R.id.layout_wait_confirm);
        if (waitConfirmView != null) {
            waitConfirmView.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                    intent.putExtra("tab_index", 0); // Chờ xác nhận
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }

        android.view.View waitPickupView = findViewById(R.id.layout_wait_pickup);
        if (waitPickupView != null) {
            waitPickupView.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                    intent.putExtra("tab_index", 1); // Chờ lấy hàng
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }

        android.view.View outForDeliveryView = findViewById(R.id.layout_out_for_delivery);
        if (outForDeliveryView != null) {
            outForDeliveryView.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                    intent.putExtra("tab_index", 2); // Chờ giao hàng
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }

        android.view.View reviewOrderView = findViewById(R.id.layout_review_order);
        if (reviewOrderView != null) {
            reviewOrderView.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                    intent.putExtra("tab_index", 3); // Đánh giá/Đã giao
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }

        // Xử lý click cho 'Xem lịch sử mua hàng' (chuyển sang tab Đã giao)
        android.widget.TextView viewHistory = findViewById(R.id.view_history_order);
        if (viewHistory != null) {
            viewHistory.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.manage_order.ManageOrderActivity.class);
                    intent.putExtra("tab_index", 3); // Đã giao
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }

        // Thiết lập Bottom Navigation
        setupBottomNavigationFragment(R.id.nav_home); // chọn mặc định Trang chủ

        android.view.View favoriteView = findViewById(R.id.layout_favorite_dishes);
        if (favoriteView != null) {
            favoriteView.setOnClickListener(v -> {
                if (isLoggedIn()) {
                    android.content.Intent intent = new android.content.Intent(ManageAccountActivity.this, com.example.bepnhataapp.features.favorite.FavoriteActivity.class);
                    startActivity(intent);
                } else {
                    showLoginPromptDialog();
                }
            });
        }
    }

    private boolean isLoggedIn() {
        return com.example.bepnhataapp.common.utils.SessionManager.isLoggedIn(this);
    }

    @Override
    protected int getBottomNavigationContainerId() {
        return R.id.bottom_navigation_container;
    }

    // Xử lý khi người dùng chọn lại item ở Bottom Navigation
    @Override
    public void onNavigationItemReselected(int itemId) {
        handleNavigation(itemId);
    }

    // Thêm hàm thiết lập click cho các mục yêu cầu đăng nhập
    private void setupItemClicks() {
        if (isLoggedIn()) return; // Đã đăng nhập thì không cần popup

        int[] clickableIds = {
                R.id.layout_account_info,
                R.id.layout_shipping_address,
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

    // Hiển thị hộp thoại xác nhận đăng xuất
    private void showLogoutConfirmDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    SessionManager.logout(this);
                    // Reload activity để cập nhật UI
                    recreate();
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
