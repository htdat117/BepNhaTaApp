package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Customer;

public class AccountInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        android.view.View close = findViewById(R.id.closeButton);
        if (close != null) {
            close.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        // UI references
        android.widget.EditText edtName = findViewById(R.id.editTextFullName);
        android.widget.EditText edtEmail = findViewById(R.id.editTextEmail);
        android.widget.EditText edtBirth = findViewById(R.id.editTextDateOfBirth);
        android.widget.EditText edtPassword = findViewById(R.id.editTextPassword);
        android.widget.EditText edtConfirm = findViewById(R.id.editTextConfirmPassword);
        android.widget.Button btnComplete = findViewById(R.id.button_complete);

        // Simple date picker for DOB
        android.widget.ImageView dateBtn = findViewById(R.id.datePickerButton);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        android.app.DatePickerDialog.OnDateSetListener listener = (view, y, m, d) -> {
            String dob = String.format(java.util.Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d);
            edtBirth.setText(dob);
        };
        dateBtn.setOnClickListener(v -> {
            new android.app.DatePickerDialog(this, listener,
                    cal.get(java.util.Calendar.YEAR),
                    cal.get(java.util.Calendar.MONTH),
                    cal.get(java.util.Calendar.DAY_OF_MONTH)).show();
        });

        btnComplete.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String dob = edtBirth.getText().toString().trim();
            String pass = edtPassword.getText().toString();
            String confirm = edtConfirm.getText().toString();

            if (name.isEmpty() || email.isEmpty() || dob.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                android.widget.Toast.makeText(this, "Vui lòng điền đủ thông tin", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                android.widget.Toast.makeText(this, "Email không hợp lệ", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass.equals(confirm)) {
                android.widget.Toast.makeText(this, "Mật khẩu xác nhận không khớp", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            com.example.bepnhataapp.common.dao.CustomerDao dao = new com.example.bepnhataapp.common.dao.CustomerDao(this);

            // Check email duplication
            java.util.List<Customer> existing = dao.getAll();
            for (Customer c0 : existing) {
                if (email.equalsIgnoreCase(c0.getEmail())) {
                    android.widget.Toast.makeText(this, "Email đã tồn tại", android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Customer c = new Customer();
            c.setFullName(name);
            c.setGender(""); // chưa chọn giới tính
            c.setBirthday(dob);
            c.setEmail(email);
            c.setPassword(pass);
            c.setPhone("");
            c.setAvatar(null);
            c.setCustomerType("Bạc");
            c.setLoyaltyPoint(0);
            c.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()));
            c.setStatus("active");

            long id = dao.insert(c);

            android.widget.Toast.makeText(this, "Đăng ký thành công!", android.widget.Toast.LENGTH_SHORT).show();

            android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}