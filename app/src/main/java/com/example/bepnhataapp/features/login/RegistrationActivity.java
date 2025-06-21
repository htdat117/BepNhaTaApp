package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Pattern;
import com.example.bepnhataapp.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;

public class RegistrationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // DAO
        CustomerDao customerDao = new CustomerDao(this);

        Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");

        android.widget.EditText edtPhone = findViewById(R.id.editTextPhone);
        android.widget.ImageView clearPhone = findViewById(R.id.clearPhoneButton);
        clearPhone.setOnClickListener(v -> {
            edtPhone.setText("");
        });
        android.widget.CheckBox cb = findViewById(R.id.checkboxTerms);
        android.widget.Button btnContinue = findViewById(R.id.button_continue);
        android.widget.TextView tvHaveAccount = findViewById(R.id.tvHaveAccount);

        tvHaveAccount.setOnClickListener(v -> {
            android.content.Intent login = new android.content.Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
        });

        btnContinue.setEnabled(false);

        // Real-time validation
        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                if (!PHONE_PATTERN.matcher(phone).matches()) {
                    edtPhone.setError("Số điện thoại không hợp lệ");
                    btnContinue.setEnabled(false);
                    return;
                }
                edtPhone.setError(null);
                btnContinue.setEnabled(cb.isChecked());
            }
        });

        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String phone = edtPhone.getText().toString().trim();
            btnContinue.setEnabled(isChecked && PHONE_PATTERN.matcher(phone).matches());
        });

        btnContinue.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                edtPhone.setError("Số điện thoại không hợp lệ");
                return;
            }
            Customer existing = customerDao.findByPhone(phone);
            if (existing != null) {
                // Redirect to login with prefilled phone
                android.content.Intent loginIntent = new android.content.Intent(this, LoginActivity.class);
                loginIntent.putExtra("phone", phone);
                startActivity(loginIntent);
                finish();
                return;
            }
            if (!cb.isChecked()) {
                android.widget.Toast.makeText(this, "Vui lòng chấp nhận điều khoản để tiếp tục", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            android.content.Intent intent = new android.content.Intent(this, AuthenticationActivity.class);
            intent.putExtra("flow", "register");
            intent.putExtra("phone", phone);
            startActivity(intent);
        });

        findViewById(R.id.closeButton).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
} 