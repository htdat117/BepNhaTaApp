package com.example.bepnhataapp.features.customeridea;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
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
        android.widget.HorizontalScrollView hsPreview = findViewById(R.id.hsPreview);
        android.widget.LinearLayout llPreview = findViewById(R.id.llPreview);
        android.widget.RadioGroup rgChannel = findViewById(R.id.rgChannel);
        android.widget.TextView tvChannelError = findViewById(R.id.tvChannelError);

        java.util.List<android.net.Uri> selectedImages = new java.util.ArrayList<>();

        // ========= Image picker (multiple) =========
        ActivityResultLauncher<String[]> pickImagesLauncher = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.OpenMultipleDocuments(), uris -> {
                    if (uris == null || uris.isEmpty()) return;
                    for (android.net.Uri uri : uris) {
                        if (!selectedImages.contains(uri)) {
                            selectedImages.add(uri);
                            addPreviewImage(uri, llPreview, selectedImages);
                        }
                    }
                    hsPreview.setVisibility(View.VISIBLE);
                });

        btnUpload.setOnClickListener(v -> pickImagesLauncher.launch(new String[]{"image/*"}));

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

            if (rgChannel.getCheckedRadioButtonId() == -1) {
                tvChannelError.setVisibility(View.VISIBLE);
                valid = false;
            } else {
                tvChannelError.setVisibility(View.GONE);
            }

            if (!valid) return;

            // Hiển thị thông báo gửi thành công và reset biểu mẫu
            android.widget.Toast.makeText(this, "Đã gửi thành công", android.widget.Toast.LENGTH_SHORT).show();

            // Reset tất cả các trường nhập và thông báo trợ giúp
            edtName.setText("");
            edtPhone.setText("");
            edtEmail.setText("");
            edtProblem.setText("");
            edtContent.setText("");
            selectedImages.clear();
            llPreview.removeAllViews();
            hsPreview.setVisibility(View.GONE);
            rgChannel.clearCheck();
            tvChannelError.setVisibility(View.GONE);

            tilName.setHelperText(null);
            tilPhone.setHelperText(null);
            tilEmail.setHelperText(null);
            tilProblem.setHelperText(null);
            tilContent.setHelperText(null);
        });

        // Setup clear buttons for edit texts
        setupClearButtonWithAction(edtName);
        setupClearButtonWithAction(edtPhone);
        setupClearButtonWithAction(edtEmail);
        setupClearButtonWithAction(edtProblem);
        setupClearButtonWithAction(edtContent);

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

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

    private void addPreviewImage(android.net.Uri uri, android.widget.LinearLayout container, java.util.List<android.net.Uri> selectedImages) {
        android.content.Context ctx = this;
        android.widget.FrameLayout frame = new android.widget.FrameLayout(ctx);
        android.widget.ImageView img = new android.widget.ImageView(ctx);
        int sizePx = (int) (180 * getResources().getDisplayMetrics().density);
        img.setLayoutParams(new android.widget.FrameLayout.LayoutParams(sizePx, sizePx));
        img.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
        img.setImageURI(uri);

        android.widget.ImageButton btnRemove = new android.widget.ImageButton(ctx);
        int btnSize = (int) (24 * getResources().getDisplayMetrics().density);
        android.widget.FrameLayout.LayoutParams lpBtn = new android.widget.FrameLayout.LayoutParams(btnSize, btnSize);
        int margin = (int) (16 * getResources().getDisplayMetrics().density);
        lpBtn.setMargins(0, margin, margin, 0);
        lpBtn.gravity = android.view.Gravity.END | android.view.Gravity.TOP;
        btnRemove.setLayoutParams(lpBtn);
        btnRemove.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        btnRemove.setBackgroundColor(android.graphics.Color.TRANSPARENT);
        btnRemove.setColorFilter(androidx.core.content.ContextCompat.getColor(ctx, R.color.primary1), android.graphics.PorterDuff.Mode.SRC_IN);

        btnRemove.setOnClickListener(v -> {
            selectedImages.remove(uri);
            container.removeView(frame);
            if (selectedImages.isEmpty()) {
                findViewById(R.id.hsPreview).setVisibility(View.GONE);
            }
        });

        frame.addView(img);
        frame.addView(btnRemove);

        android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(sizePx, sizePx);
        lp.setMargins(8, 0, 8, 0);
        frame.setLayoutParams(lp);

        container.addView(frame);
    }
}
