package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;

public class NewPasswordActivity extends AppCompatActivity {

    private String phone;
    private CustomerDao customerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        phone = getIntent().getStringExtra("phone");
        customerDao = new CustomerDao(this);

        android.view.View close = findViewById(R.id.closeButton);
        if (close != null) {
            close.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        EditText edtNewPass = findViewById(R.id.editTextNewPassword);
        EditText edtConfirm = findViewById(R.id.editTextConfirmPassword);
        android.widget.TextView tvNewPassError = findViewById(R.id.tvNewPassError);
        android.widget.TextView tvConfirmError = findViewById(R.id.tvConfirmPassError);

        android.widget.ImageView btnClearNew = findViewById(R.id.btnClearNewPass);
        android.widget.ImageView btnToggleNew = findViewById(R.id.btnToggleNewPass);
        android.widget.ImageView btnClearConfirm = findViewById(R.id.btnClearConfirmPass);
        android.widget.ImageView btnToggleConfirm = findViewById(R.id.btnToggleConfirmPass);

        if (btnClearNew != null) btnClearNew.setOnClickListener(v -> edtNewPass.setText(""));
        if (btnClearConfirm != null) btnClearConfirm.setOnClickListener(v -> edtConfirm.setText(""));

        if (btnToggleNew != null) {
            btnToggleNew.setOnClickListener(v -> togglePasswordVisibility(edtNewPass, btnToggleNew));
        }
        if (btnToggleConfirm != null) {
            btnToggleConfirm.setOnClickListener(v -> togglePasswordVisibility(edtConfirm, btnToggleConfirm));
        }

        findViewById(R.id.button_complete).setOnClickListener(v -> {
            String newPass = edtNewPass.getText().toString();
            String confirm = edtConfirm.getText().toString();

            // Reset errors
            tvNewPassError.setVisibility(android.view.View.GONE);
            tvConfirmError.setVisibility(android.view.View.GONE);

            boolean hasError = false;
            if (newPass.isEmpty()) {
                tvNewPassError.setText("Vui lòng nhập mật khẩu mới");
                tvNewPassError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else if (!isStrongPassword(newPass)) {
                tvNewPassError.setText("Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường và số");
                tvNewPassError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (confirm.isEmpty()) {
                tvConfirmError.setText("Vui lòng xác nhận mật khẩu");
                tvConfirmError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            } else if (!newPass.equals(confirm)) {
                tvConfirmError.setText("Mật khẩu xác nhận không khớp");
                tvConfirmError.setVisibility(android.view.View.VISIBLE);
                hasError = true;
            }

            if (hasError) return;

            if (phone == null) {
                Toast.makeText(this, "Thiếu số điện thoại", Toast.LENGTH_SHORT).show();
                return;
            }

            Customer c = customerDao.findByPhone(phone);
            if (c == null) {
                Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                return;
            }

            c.setPassword(newPass);
            int rows = customerDao.update(c);
            if (rows > 0) {
                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                // Chuyển về màn Login, prefill số điện thoại
                android.content.Intent intent = new android.content.Intent(this, LoginActivity.class);
                intent.putExtra("phone", phone);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Đảm bảo mật khẩu đủ mạnh: tối thiểu 8 ký tự, có chữ hoa, chữ thường và số.
     */
    private boolean isStrongPassword(String pass) {
        if (pass.length() < 8) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false;
        for(char c: pass.toCharArray()){
            if(Character.isUpperCase(c)) hasUpper = true;
            else if(Character.isLowerCase(c)) hasLower = true;
            else if(Character.isDigit(c)) hasDigit = true;
            if(hasUpper && hasLower && hasDigit) return true;
        }
        return false;
    }

    private void togglePasswordVisibility(EditText editText, android.widget.ImageView eye) {
        int inputType = editText.getInputType();
        boolean isPasswordVisible = (inputType & android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

        if (isPasswordVisible) {
            // turn to hidden
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
            eye.setImageResource(R.drawable.ic_eye); // closed eye icon
        } else {
            editText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            eye.setImageResource(R.drawable.ic_eye_open); // open eye icon (need drawable)
        }
        // move cursor to end
        editText.setSelection(editText.getText().length());
    }
}
