package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.Customer;
import android.util.Patterns;
import java.util.regex.Pattern;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import com.google.android.material.datepicker.MaterialDatePicker;

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
        String phoneRegistered = getIntent().getStringExtra("phone");

        // Material DatePicker similar to edit info page
        View.OnClickListener openDate = v -> {
            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Chọn ngày sinh")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            picker.addOnPositiveButtonClickListener(selection -> {
                java.util.Calendar c = java.util.Calendar.getInstance();
                c.setTimeInMillis(selection);
                String dateStr = String.format(java.util.Locale.getDefault(), "%02d/%02d/%04d",
                        c.get(java.util.Calendar.DAY_OF_MONTH), c.get(java.util.Calendar.MONTH) + 1, c.get(java.util.Calendar.YEAR));
                edtBirth.setText(dateStr);
            });
            picker.show(getSupportFragmentManager(), "dob_picker");
        };
        edtBirth.setOnClickListener(openDate);
        edtBirth.setOnFocusChangeListener((v, hasFocus) -> { if(hasFocus) openDate.onClick(v);} );

        android.widget.EditText edtPassword = findViewById(R.id.editTextPassword);
        android.widget.EditText edtConfirm = findViewById(R.id.editTextConfirmPassword);
        android.widget.Button btnComplete = findViewById(R.id.button_complete);

        // Clear buttons & eye icons
        android.widget.ImageView clearNameBtn = findViewById(R.id.clearFullNameButton);
        android.widget.ImageView clearEmailBtn = findViewById(R.id.clearEmailButton);
        android.widget.ImageView clearPasswordBtn = findViewById(R.id.clearPasswordButton);
        android.widget.ImageView clearConfirmBtn = findViewById(R.id.clearConfirmPasswordButton);

        android.widget.ImageView eyePassBtn = findViewById(R.id.togglePasswordVisibilityButton);
        android.widget.ImageView eyeConfirmBtn = findViewById(R.id.toggleConfirmPasswordVisibilityButton);

        // (buildClearWatcher helper is now a private method of the Activity)

        clearNameBtn.setOnClickListener(v -> edtName.setText(""));
        clearEmailBtn.setOnClickListener(v -> edtEmail.setText(""));
        clearPasswordBtn.setOnClickListener(v -> edtPassword.setText(""));
        clearConfirmBtn.setOnClickListener(v -> edtConfirm.setText(""));

        // attach watchers to manage visibility initial state too
        edtName.addTextChangedListener(buildClearWatcher(edtName, clearNameBtn));
        edtEmail.addTextChangedListener(buildClearWatcher(edtEmail, clearEmailBtn));
        edtPassword.addTextChangedListener(buildClearWatcher(edtPassword, clearPasswordBtn));
        edtConfirm.addTextChangedListener(buildClearWatcher(edtConfirm, clearConfirmBtn));

        // Password visibility toggles
        final boolean[] pwdVisible = {false};
        eyePassBtn.setOnClickListener(v -> {
            pwdVisible[0] = !pwdVisible[0];
            if (pwdVisible[0]) {
                edtPassword.setTransformationMethod(null);
            } else {
                edtPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            }
            edtPassword.setSelection(edtPassword.getText().length());
            // change icon if have eye_off resource
            int res = getResources().getIdentifier(pwdVisible[0] ? "ic_eye_off" : "ic_eye", "drawable", getPackageName());
            if(res!=0) eyePassBtn.setImageResource(res);
        });

        final boolean[] confirmVisible = {false};
        eyeConfirmBtn.setOnClickListener(v -> {
            confirmVisible[0] = !confirmVisible[0];
            if(confirmVisible[0]) {
                edtConfirm.setTransformationMethod(null);
            } else {
                edtConfirm.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            }
            edtConfirm.setSelection(edtConfirm.getText().length());
            int res = getResources().getIdentifier(confirmVisible[0] ? "ic_eye_off" : "ic_eye", "drawable", getPackageName());
            if(res!=0) eyeConfirmBtn.setImageResource(res);
        });

        // Validation patterns
        final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$"); // >=8, letters+digit

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int st,int c,int a){}
            @Override public void onTextChanged(CharSequence s,int st,int b,int c){}
            @Override public void afterTextChanged(Editable s){
                String email = edtEmail.getText().toString().trim();
                String pass = edtPassword.getText().toString();
                String confirm = edtConfirm.getText().toString();
                boolean ok = !edtName.getText().toString().trim().isEmpty()
                        && !edtBirth.getText().toString().trim().isEmpty()
                        && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        && PASSWORD_PATTERN.matcher(pass).matches()
                        && pass.equals(confirm);
                btnComplete.setEnabled(ok);
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtEmail.setError("Email không hợp lệ");
                } else { edtEmail.setError(null);}   
                if(!PASSWORD_PATTERN.matcher(pass).matches()){
                    edtPassword.setError("Mật khẩu ≥8 ký tự, gồm chữ & số");
                } else { edtPassword.setError(null);} 
                if(!confirm.equals(pass)){
                    edtConfirm.setError("Mật khẩu xác nhận không khớp");
                } else { edtConfirm.setError(null);}  
            }
        };

        // disable button initially
        btnComplete.setEnabled(false);

        edtName.addTextChangedListener(watcher);
        edtEmail.addTextChangedListener(watcher);
        edtBirth.addTextChangedListener(watcher);
        edtPassword.addTextChangedListener(watcher);
        edtConfirm.addTextChangedListener(watcher);

        btnComplete.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String dob = edtBirth.getText().toString().trim();
            String pass = edtPassword.getText().toString();
            String confirm = edtConfirm.getText().toString();

            if (!PASSWORD_PATTERN.matcher(pass).matches()) {
                android.widget.Toast.makeText(this, "Mật khẩu phải ≥8 ký tự, gồm chữ & số", android.widget.Toast.LENGTH_SHORT).show();
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
            c.setPhone( phoneRegistered != null ? phoneRegistered : "" );
            c.setAvatar(null);
            c.setCustomerType("Bạc");
            c.setLoyaltyPoint(0);
            c.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()));
            c.setStatus("active");

            long id = dao.insert(c);

            android.widget.Toast.makeText(this, "Đăng ký thành công!", android.widget.Toast.LENGTH_SHORT).show();
            // Tự động đăng nhập sau khi đăng ký
            com.example.bepnhataapp.common.utils.SessionManager.login(this, phoneRegistered != null ? phoneRegistered : "");

            android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    /**
     * Tạo TextWatcher để tự động hiển thị/ẩn nút xoá (clear) khi EditText có/không có nội dung.
     */
    private android.text.TextWatcher buildClearWatcher(final android.widget.EditText et,
                                                       final android.widget.ImageView btn) {
        return new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int start,int count,int after) {}
            @Override public void onTextChanged(CharSequence s,int start,int before,int count) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                btn.setVisibility(s.length() > 0 ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
            }
        };
    }
}
