package com.example.bepnhataapp.features.manage_pinfor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Locale;

public class change_infor extends AppCompatActivity {

    private EditText edtName, edtPhone, edtEmail, edtBirthday, edtGender;
    private TextView tvErrPhone, tvErrEmail;
    private Customer customer;
    private CustomerDao customerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_infor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Điều chỉnh status bar màu cam & icon trắng
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary1));
        WindowInsetsControllerCompat ic = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if(ic!=null) ic.setAppearanceLightStatusBars(false);

        // init
        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtEmail = findViewById(R.id.edt_email);
        edtBirthday = findViewById(R.id.edt_birthday);
        edtGender = findViewById(R.id.edt_gender);
        ImageButton btnBack = findViewById(R.id.btn_back);
        ImageButton btnClearName = findViewById(R.id.btn_clear_name);
        ImageButton btnClearPhone = findViewById(R.id.btn_clear_phone);
        ImageButton btnClearEmail = findViewById(R.id.btn_clear_email);
        Button btnSave = findViewById(R.id.btn_save);
        ImageButton btnPickBirthday = findViewById(R.id.btn_pick_birthday);
        ImageButton btnPickGender = findViewById(R.id.btn_pick_gender);
        tvErrPhone = findViewById(R.id.tv_error_phone);
        tvErrEmail = findViewById(R.id.tv_error_email);

        btnBack.setOnClickListener(v -> finish());

        customerDao = new CustomerDao(this);

        long id = getIntent().getLongExtra("customer_id", -1);
        if (id != -1) {
            customer = getCustomerById(id);
        } else {
            // fallback via phone session
            String phone = SessionManager.getPhone(this);
            customer = customerDao.findByPhone(phone);
        }

        if (customer == null) {
            Toast.makeText(this, "Không tìm thấy thông tin khách hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // populate fields
        edtName.setText(customer.getFullName());
        edtPhone.setText(customer.getPhone());
        edtEmail.setText(customer.getEmail());
        edtBirthday.setText(customer.getBirthday());
        edtGender.setText(customer.getGender());

        btnPickBirthday.setOnClickListener(v -> showDatePicker());
        btnPickGender.setOnClickListener(v -> showGenderDialog());

        btnSave.setOnClickListener(v -> saveInfo());

        btnClearName.setOnClickListener(v -> edtName.setText(""));
        btnClearPhone.setOnClickListener(v -> edtPhone.setText(""));
        btnClearEmail.setOnClickListener(v -> edtEmail.setText(""));

        tvErrPhone.setVisibility(android.view.View.GONE);
        tvErrEmail.setVisibility(android.view.View.GONE);
    }

    private Customer getCustomerById(long id) {
        java.util.List<Customer> list = customerDao.getAll();
        for (Customer c : list) {
            if (c.getCustomerID() == id) return c;
        }
        return null;
    }

    private void showDatePicker() {
        // Use MaterialDatePicker which lets user jump to year quickly
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày sinh")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(selection);
            String dateStr = String.format(Locale.getDefault(), "%02d/%02d/%04d",
                    c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR));
            edtBirthday.setText(dateStr);
        });

        picker.show(getSupportFragmentManager(), "date_picker");
    }

    private void showGenderDialog() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        new android.app.AlertDialog.Builder(this)
                .setTitle("Chọn giới tính")
                .setItems(genders, (dialog, which) -> edtGender.setText(genders[which]))
                .show();
    }

    private void saveInfo() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        tvErrPhone.setVisibility(android.view.View.GONE);
        tvErrEmail.setVisibility(android.view.View.GONE);

        if (!phone.matches("\\d{10,11}")) {
            tvErrPhone.setVisibility(android.view.View.VISIBLE);
            edtPhone.requestFocus();
            return;
        }

        String newEmail = edtEmail.getText().toString().trim();
        if (!newEmail.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            tvErrEmail.setVisibility(android.view.View.VISIBLE);
            edtEmail.requestFocus();
            return;
        }

        String newBirthday = edtBirthday.getText().toString().trim();
        String newGender = edtGender.getText().toString().trim();

        String oldPhone = customer.getPhone() == null ? "" : customer.getPhone().trim();
        String oldEmail = customer.getEmail() == null ? "" : customer.getEmail().trim();

        boolean phoneChanged = !phone.equalsIgnoreCase(oldPhone);
        boolean emailChanged = !newEmail.equalsIgnoreCase(oldEmail);

        if (!phoneChanged && !emailChanged) {
            // simple update no OTP
            customer.setFullName(name);
            customer.setBirthday(newBirthday);
            customer.setGender(newGender);
            int rows = customerDao.update(customer);
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Need OTP verification
        String verifyType;
        String verifyTarget;
        if (emailChanged && !phoneChanged) {
            verifyType = "phone"; // verify via existing phone
            verifyTarget = customer.getPhone();
            com.example.bepnhataapp.common.utils.OtpService.sendOtp(this, verifyTarget);
        } else {
            // phone changed (or both). verify via existing email
            verifyType = "email";
            verifyTarget = customer.getEmail();
            com.example.bepnhataapp.common.utils.EmailOtpService.sendOtp(this, verifyTarget);
        }

        android.content.Intent intent = new android.content.Intent(this, opt_phone.class);
        intent.putExtra("customer_id", customer.getCustomerID());
        intent.putExtra("verify_type", verifyType);
        intent.putExtra("verify_target", verifyTarget);
        intent.putExtra("new_phone", phoneChanged ? phone : null);
        intent.putExtra("new_email", emailChanged ? newEmail : null);
        intent.putExtra("new_name", name);
        intent.putExtra("new_birthday", newBirthday);
        intent.putExtra("new_gender", newGender);
        startActivity(intent);
    }
}
