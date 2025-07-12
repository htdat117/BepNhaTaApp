package com.example.bepnhataapp.features.manage_pinfor;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.bepnhataapp.R;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;

public class change_password extends AppCompatActivity {

    private EditText edtCurrent, edtNew, edtConfirm;
    private CustomerDao customerDao;
    private Customer currentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary1));
        WindowInsetsControllerCompat ic = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if(ic!=null) ic.setAppearanceLightStatusBars(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        edtCurrent = findViewById(R.id.edt_current_password);
        edtNew = findViewById(R.id.edt_new_password);
        edtConfirm = findViewById(R.id.edt_confirm_password);
        Button btnChange = findViewById(R.id.btn_change_password);

        ImageButton btnToggleCurrent = findViewById(R.id.btn_toggle_current);
        ImageButton btnToggleNew = findViewById(R.id.btn_toggle_new);
        ImageButton btnToggleConfirm = findViewById(R.id.btn_toggle_confirm);

        setupTogglePassword(btnToggleCurrent, edtCurrent);
        setupTogglePassword(btnToggleNew, edtNew);
        setupTogglePassword(btnToggleConfirm, edtConfirm);

        customerDao = new CustomerDao(this);
        String phone = SessionManager.getPhone(this);
        if (phone != null) {
            currentCustomer = customerDao.findByPhone(phone);
        }

        btnChange.setOnClickListener(v -> performChange());
    }

    private void setupTogglePassword(ImageButton btn, EditText edt) {
        btn.setOnClickListener(v -> {
            int type = edt.getInputType();
            boolean isVisible = (type & android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

            if (isVisible) {
                // Hide password
                edt.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btn.setImageResource(R.drawable.ic_eye);
            } else {
                // Show password
                edt.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btn.setImageResource(R.drawable.ic_eye_open);
            }
            edt.setSelection(edt.getText().length());
        });
    }

    private void performChange() {
        if (currentCustomer == null) {
            Toast.makeText(this, "Lỗi phiên đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        String cur = edtCurrent.getText().toString();
        String newPw = edtNew.getText().toString();
        String confirm = edtConfirm.getText().toString();

        if (TextUtils.isEmpty(cur) || TextUtils.isEmpty(newPw) || TextUtils.isEmpty(confirm)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!currentCustomer.getPassword().equals(cur)) {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPw.equals(confirm)) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPw.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        currentCustomer.setPassword(newPw);
        int rows = customerDao.update(currentCustomer);
        if (rows > 0) {
            Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }
}
