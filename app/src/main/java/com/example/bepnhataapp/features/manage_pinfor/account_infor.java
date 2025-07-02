package com.example.bepnhataapp.features.manage_pinfor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class account_infor extends AppCompatActivity {

    private ImageView imgAvatar;
    private TextView tvName, tvBirthday, tvPhone, tvEmail, tvGender, tvPassword;
    private Customer currentCustomer;
    private CustomerDao customerDao;
    private ActivityResultLauncher<String> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_infor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init views
        imgAvatar = findViewById(R.id.img_avatar);
        ImageButton btnCamera = findViewById(R.id.btn_camera);
        ImageButton btnBack = findViewById(R.id.btn_back);
        tvName = findViewById(R.id.tv_name);
        tvBirthday = findViewById(R.id.tv_birthday);
        tvPhone = findViewById(R.id.tv_phone);
        tvEmail = findViewById(R.id.tv_email);
        tvGender = findViewById(R.id.tv_gender);
        tvPassword = findViewById(R.id.tv_password);
        Button btnEdit = findViewById(R.id.btn_edit);
        Button btnChangePassword = findViewById(R.id.btn_change_password);

        btnBack.setOnClickListener(v -> finish());

        customerDao = new CustomerDao(this);
        loadCustomerInfo();

        // Register image picker launcher
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imgAvatar.setImageBitmap(bitmap);

                    // Save to DB
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] avatarBytes = baos.toByteArray();

                    if (currentCustomer != null) {
                        currentCustomer.setAvatar(avatarBytes);
                        customerDao.update(currentCustomer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnCamera.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                if (currentCustomer == null) return;
                Intent intent = new Intent(account_infor.this, change_infor.class);
                intent.putExtra("customer_id", currentCustomer.getCustomerID());
                startActivity(intent);
            });
        }

        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(account_infor.this, change_password.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCustomerInfo(); // refresh after potential change
    }

    private void loadCustomerInfo() {
        String phone = SessionManager.getPhone(this);
        if (phone == null) return;

        currentCustomer = customerDao.findByPhone(phone);
        if (currentCustomer == null) return;

        tvName.setText(currentCustomer.getFullName());
        tvBirthday.setText(currentCustomer.getBirthday());
        tvPhone.setText(currentCustomer.getPhone());
        tvEmail.setText(currentCustomer.getEmail());
        String gender = currentCustomer.getGender();
        if (gender == null || gender.trim().isEmpty()) gender = "Chưa cập nhật";
        tvGender.setText(gender);

        // Hiển thị mật khẩu dạng ***** theo độ dài
        String pwd = currentCustomer.getPassword();
        if (pwd != null) {
            char[] mask = new char[pwd.length()];
            java.util.Arrays.fill(mask, '*');
            tvPassword.setText(new String(mask));
        }

        byte[] avatar = currentCustomer.getAvatar();
        if (avatar != null && avatar.length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
            imgAvatar.setImageBitmap(bmp);
        }
    }
}