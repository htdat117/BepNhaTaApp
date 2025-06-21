package com.example.bepnhataapp.features.customeridea;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.bepnhataapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class CustomerIdeaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_idea);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary1));
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if (controller != null) controller.setAppearanceLightStatusBars(false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ========= View bindings =========
        EditText edtName = findViewById(R.id.edtName);
        EditText edtPhone = findViewById(R.id.edtPhone);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtProblem = findViewById(R.id.edtProblem);
        EditText edtContent = findViewById(R.id.edtContent);
        android.widget.Button btnUpload = findViewById(R.id.btnUpload);
        android.widget.Button btnSend = findViewById(R.id.btnSend);
        com.google.android.material.textfield.TextInputLayout tilName = findViewById(R.id.tilName);
        com.google.android.material.textfield.TextInputLayout tilPhone = findViewById(R.id.tilPhone);
        com.google.android.material.textfield.TextInputLayout tilEmail = findViewById(R.id.tilEmail);
        com.google.android.material.textfield.TextInputLayout tilProblem = findViewById(R.id.tilProblem);
        com.google.android.material.textfield.TextInputLayout tilContent = findViewById(R.id.tilContent);

        // Keep a reference to selected image
        final android.net.Uri[] selectedImage = {null};

        // ========= Image picker =========
        ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), uri -> {
                    if (uri != null) {
                        selectedImage[0] = uri;
                        android.widget.Toast.makeText(this, "Đã chọn ảnh", android.widget.Toast.LENGTH_SHORT).show();
                    }
                });

        btnUpload.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // ========= Validation helper =========
        final java.util.regex.Pattern phonePattern = java.util.regex.Pattern.compile("^(\\+84|0)\\d{9}$");

        // Hiển thị lỗi ngay khi mất focus bằng helperText để không làm đổi màu nền
        edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edtName.getText().toString().trim().isEmpty()) {
                    tilName.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    tilName.setHelperText("Vui lòng nhập họ tên");
                } else {
                    tilName.setHelperText(null);
                }
            }
        });
        edtPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String phone = edtPhone.getText().toString().trim();
                if (phone.isEmpty()) {
                    tilPhone.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    tilPhone.setHelperText("Vui lòng nhập số điện thoại");
                } else if (!phonePattern.matcher(phone).matches()) {
                    tilPhone.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    tilPhone.setHelperText("Số điện thoại không hợp lệ");
                } else {
                    tilPhone.setHelperText(null);
                }
            }
        });
        edtEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String email = edtEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    tilEmail.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    tilEmail.setHelperText("Vui lòng nhập email");
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.toLowerCase(java.util.Locale.ROOT).endsWith("@gmail.com")) {
                    tilEmail.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    tilEmail.setHelperText("Email Gmail không hợp lệ");
                } else {
                    tilEmail.setHelperText(null);
                }
            }
        });
        edtProblem.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edtProblem.getText().toString().trim().isEmpty()) {
                    tilProblem.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    tilProblem.setHelperText("Vui lòng nhập vấn đề");
                } else {
                    tilProblem.setHelperText(null);
                }
            }
        });
        edtContent.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edtContent.getText().toString().trim().isEmpty()) {
                    tilContent.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
                    tilContent.setHelperText("Vui lòng nhập nội dung");
                } else {
                    tilContent.setHelperText(null);
                }
            }
        });

        btnSend.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String problem = edtProblem.getText().toString().trim();
            String content = edtContent.getText().toString().trim();

            boolean valid = true;
            if (name.isEmpty()) { tilName.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED)); tilName.setHelperText("Vui lòng nhập họ tên"); valid = false; } else { tilName.setHelperText(null); }
            if (phone.isEmpty()) { tilPhone.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED)); tilPhone.setHelperText("Vui lòng nhập số điện thoại"); valid = false; }
            else if (!phonePattern.matcher(phone).matches()) { tilPhone.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED)); tilPhone.setHelperText("Số điện thoại không hợp lệ"); valid = false; } else { tilPhone.setHelperText(null); }
            if (email.isEmpty()) { tilEmail.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED)); tilEmail.setHelperText("Vui lòng nhập email"); valid = false; }
            else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.toLowerCase(java.util.Locale.ROOT).endsWith("@gmail.com")) { tilEmail.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED)); tilEmail.setHelperText("Email Gmail không hợp lệ"); valid = false; } else { tilEmail.setHelperText(null); }
            if (problem.isEmpty()) { tilProblem.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED)); tilProblem.setHelperText("Vui lòng nhập vấn đề"); valid = false; } else { tilProblem.setHelperText(null); }
            if (content.isEmpty()) { tilContent.setHelperTextColor(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED)); tilContent.setHelperText("Vui lòng nhập nội dung"); valid = false; } else { tilContent.setHelperText(null); }

            if (!valid) return;

            // Gửi email tới chủ app
            android.content.Intent emailIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"hungtn22411@st.uel.edu.vn"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ý kiến khách hàng - " + name);
            StringBuilder sb = new StringBuilder();
            sb.append("Họ tên: ").append(name).append('\n');
            sb.append("Số điện thoại: ").append(phone).append('\n');
            sb.append("Email: ").append(email).append('\n');
            sb.append("Vấn đề: ").append(problem).append('\n');
            sb.append("Nội dung: \n").append(content).append('\n');
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());

            if (selectedImage[0] != null) {
                emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, selectedImage[0]);
                emailIntent.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            try {
                startActivity(android.content.Intent.createChooser(emailIntent, "Chọn ứng dụng email"));
            } catch (android.content.ActivityNotFoundException ex) {
                android.widget.Toast.makeText(this, "Không tìm thấy ứng dụng email trên thiết bị", android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        // Setup clear buttons for edit texts
        setupClearButtonWithAction(edtName);
        setupClearButtonWithAction(edtPhone);
        setupClearButtonWithAction(edtEmail);
        setupClearButtonWithAction(edtProblem);
        setupClearButtonWithAction(edtContent);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Ẩn icon cảnh báo mặc định của TextInputLayout (chỉ hiển thị mỗi dòng chữ đỏ bên dưới)
        tilName.setErrorIconDrawable(null);
        tilPhone.setErrorIconDrawable(null);
        tilEmail.setErrorIconDrawable(null);
        tilProblem.setErrorIconDrawable(null);
        tilContent.setErrorIconDrawable(null);
    }

    private void setupClearButtonWithAction(EditText editText) {
        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Drawable drawableEnd = editText.getCompoundDrawables()[2];
                if (drawableEnd != null) {
                    int drawableWidth = drawableEnd.getBounds().width();
                    int right = editText.getRight();
                    int left = right - drawableWidth - editText.getPaddingRight();
                    if (event.getRawX() >= left) {
                        editText.setText("");
                        return true;
                    }
                }
            }
            return false;
        });
    }
}