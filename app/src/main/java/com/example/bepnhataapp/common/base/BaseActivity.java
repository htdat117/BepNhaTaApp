package com.example.bepnhataapp.common.base;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.home.HomeActivity;
import com.example.bepnhataapp.features.products.ProductActivity;
import com.example.bepnhataapp.features.mealplan.MealPlanActivity;
import com.example.bepnhataapp.features.recipes.RecipesActivity;
import com.example.bepnhataapp.features.tools.ToolsActivity;
import com.example.bepnhataapp.features.manage_account.ManageAccountActivity;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    // Interface to communicate from BottomNavigationFragment to Activity
    public interface OnNavigationItemReselectedListener {
        void onNavigationItemReselected(int itemId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Thiết lập sự kiện click cho header (nếu header tồn tại trong layout)
        setupHeaderClicks();
        // Cập nhật UI header tuỳ theo trạng thái đăng nhập
        updateHeaderInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh header every time activity is resumed to reflect possible login state change
        updateHeaderInfo();
    }

    private void setupHeaderClicks() {
        // Tìm các icon trên header (nếu layout hiện tại có include header)
        View root = findViewById(android.R.id.content);
        if (root == null) return;
        ImageView ivCart = root.findViewById(R.id.iv_cart);
        ImageView ivMessage = root.findViewById(R.id.iv_message);
        ImageView ivNotification = root.findViewById(R.id.iv_notification);
        ImageView ivLogo = root.findViewById(R.id.iv_logo);
        TextView tvLogin = root.findViewById(R.id.tv_login);

        View.OnClickListener goToManageAccount = v -> {
            if (!(BaseActivity.this instanceof ManageAccountActivity)) {
                Intent intent = new Intent(BaseActivity.this, ManageAccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        if (ivLogo != null) ivLogo.setOnClickListener(goToManageAccount);
        if (tvLogin != null) tvLogin.setOnClickListener(goToManageAccount);

        if (ivCart != null) {
            ivCart.setOnClickListener(v -> {
                if (!BaseActivity.this.getClass().equals(com.example.bepnhataapp.features.cart.CartActivity.class)) {
                    Intent it = new Intent(BaseActivity.this, com.example.bepnhataapp.features.cart.CartActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(it);
                }
            });
        }
    }

    /**
     * Thay đổi hiển thị header dựa trên trạng thái đăng nhập.
     * Khi đã đăng nhập thành công: hiển thị avatar và tên người dùng thay cho logo và chữ "Đăng nhập".
     */
    private void updateHeaderInfo() {
        View root = findViewById(android.R.id.content);
        if (root == null) return;

        ImageView ivLogo = root.findViewById(R.id.iv_logo);
        TextView tvLogin = root.findViewById(R.id.tv_login);
        if (ivLogo == null && tvLogin == null) return; // header not present

        if (SessionManager.isLoggedIn(this)) {
            // Lấy thông tin người dùng từ DB
            String phone = SessionManager.getPhone(this);
            CustomerDao dao = new CustomerDao(this);
            Customer customer = phone != null ? dao.findByPhone(phone) : null;

            // Cập nhật tên
            if (tvLogin != null) {
                if (customer != null && customer.getFullName() != null && !customer.getFullName().isEmpty()) {
                    tvLogin.setText(customer.getFullName());
                } else if (phone != null) {
                    tvLogin.setText(phone);
                }
            }

            // Cập nhật avatar (thay thế cho logo)
            if (ivLogo != null) {
                // Đặt background hình tròn để tạo khung avatar
                ivLogo.setBackgroundResource(R.drawable.bg_avatar);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ivLogo.setClipToOutline(true); // cắt ảnh theo viền (oval)
                }

                boolean avatarSet = false;
                if (customer != null && customer.getAvatar() != null && customer.getAvatar().length > 0) {
                    try {
                        Bitmap bmp = BitmapFactory.decodeByteArray(customer.getAvatar(), 0, customer.getAvatar().length);
                        if (bmp != null) {
                            // Bảo đảm bitmap được cắt tròn (cho API <21 hoặc phòng hờ)
                            Bitmap circleBmp = getCircularBitmap(bmp);
                            ivLogo.setImageBitmap(circleBmp);
                            avatarSet = true;
                        }
                    } catch (Exception ignored) { }
                }
                if (!avatarSet) {
                    ivLogo.setImageResource(R.drawable.ic_avatar);
                }
            }
        } else {
            // Chưa đăng nhập – giữ logo & text Đăng nhập
            if (tvLogin != null) {
                tvLogin.setText("Đăng nhập");
            }
            if (ivLogo != null) {
                ivLogo.setImageResource(R.drawable.ic_logo);
                ivLogo.setBackground(null); // bỏ khung tròn khi là logo
            }
        }
    }

    // Utility: crop bitmap to circle (fallback for older devices)
    private Bitmap getCircularBitmap(Bitmap src) {
        int size = Math.min(src.getWidth(), src.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect = new Rect(0, 0, size, size);
        canvas.drawBitmap(src, null, rect, paint);
        return output;
    }

    // Method to be implemented by child activities to get the container ID for the bottom navigation fragment
    protected abstract int getBottomNavigationContainerId();

    // Method to be implemented by child activities to handle navigation
    protected void handleNavigation(int itemId) {
        Intent intent = null;
        Class<?> targetActivity = null;

        if (itemId == R.id.nav_home) {
            targetActivity = HomeActivity.class;
        } else if (itemId == R.id.nav_ingredients) {
            targetActivity = ProductActivity.class;
        } else if (itemId == R.id.nav_recipes) {
            targetActivity = com.example.bepnhataapp.features.recipes.RecipesActivity.class;
        } else if (itemId == R.id.nav_meal_plan) {
            // Determine whether user already has a saved meal plan
            Class<?> defaultMealPlanActivity = MealPlanActivity.class;
            Class<?> mealPlanContentActivity = com.example.bepnhataapp.features.mealplan.MealPlanContentActivity.class;

            if (SessionManager.isLoggedIn(this)) {
                String phone = SessionManager.getPhone(this);
                if (phone != null) {
                    try {
                        com.example.bepnhataapp.common.dao.CustomerDao cDao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                        com.example.bepnhataapp.common.model.Customer customer = cDao.findByPhone(phone);
                        if (customer != null) {
                            com.example.bepnhataapp.common.dao.MealPlanDao mpDao = new com.example.bepnhataapp.common.dao.MealPlanDao(this);
                            if (mpDao.hasMealPlanForCustomer(customer.getCustomerID())) {
                                targetActivity = mealPlanContentActivity;
                            } else {
                                targetActivity = defaultMealPlanActivity;
                            }
                        } else {
                            targetActivity = defaultMealPlanActivity;
                        }
                    } catch (Exception e) {
                        android.util.Log.e(TAG, "Error checking existing meal plan: " + e.getMessage());
                        targetActivity = defaultMealPlanActivity;
                    }
                } else {
                    targetActivity = defaultMealPlanActivity;
                }
            } else {
                // Not logged in – still allow user to see the intro/setup screen
                targetActivity = defaultMealPlanActivity;
            }
        } else if (itemId == R.id.nav_tools) {
            targetActivity = ToolsActivity.class;
        }

        if (targetActivity != null) {
            Log.d(TAG, "Current activity: " + this.getClass().getSimpleName());
            Log.d(TAG, "Target activity: " + targetActivity.getSimpleName());

            if (!this.getClass().equals(targetActivity)) {
                try {
                    intent = new Intent(this, targetActivity);
                    if (targetActivity.equals(HomeActivity.class)) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    } else {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    Log.d(TAG, "Successfully started activity: " + targetActivity.getSimpleName());
                } catch (Exception e) {
                    Log.e(TAG, "Error starting activity: " + e.getMessage());
                }
            } else {
                Log.d(TAG, "Already in target activity");
            }
        }
    }

    // Method to attach the BottomNavigationFragment
    protected void setupBottomNavigationFragment(int selectedItemId) {
        BottomNavigationFragment bottomNavFragment = new BottomNavigationFragment();
        Bundle args = new Bundle();
        args.putInt("selectedItemId", selectedItemId);
        bottomNavFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(getBottomNavigationContainerId(), bottomNavFragment)
                .commit();
    }
} 