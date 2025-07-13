package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.utils.OtpService;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.KeyEvent;
import com.example.bepnhataapp.common.utils.SessionManager;
import android.os.CountDownTimer;

public class AuthenticationActivity extends AppCompatActivity {

    private String flow;
    private String phone;
    private android.widget.TextView tvCountdown;
    private android.widget.TextView tvResend;
    private CountDownTimer resendTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        flow = getIntent().getStringExtra("flow");
        phone = getIntent().getStringExtra("phone");

        tvCountdown = findViewById(R.id.tvCountdown);
        tvResend = findViewById(R.id.tvResend);

        // Send OTP immediately on screen open
        if (phone != null && !phone.isEmpty()) {
            OtpService.sendOtp(this, phone);
            startResendTimer();
        }

        // Setup OTP focus change
        setupOtpInputs();

        findViewById(R.id.closeButton).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.confirmButton).setOnClickListener(v -> {
            // Gather 6 boxes input
            String entered = "";
            entered += ((android.widget.EditText)findViewById(R.id.editTextOtp1)).getText().toString();
            entered += ((android.widget.EditText)findViewById(R.id.editTextOtp2)).getText().toString();
            entered += ((android.widget.EditText)findViewById(R.id.editTextOtp3)).getText().toString();
            entered += ((android.widget.EditText)findViewById(R.id.editTextOtp4)).getText().toString();
            entered += ((android.widget.EditText)findViewById(R.id.editTextOtp5)).getText().toString();
            entered += ((android.widget.EditText)findViewById(R.id.editTextOtp6)).getText().toString();

            if (phone == null || !OtpService.verifyOtp(this, phone, entered)) {
                Toast.makeText(this, "OTP sai hoặc hết hạn", Toast.LENGTH_SHORT).show();
                return;
            }
            OtpService.clearOtp(this, phone);
            android.content.Intent next;
            if ("reset".equals(flow)) {
                next = new android.content.Intent(this, NewPasswordActivity.class);
                next.putExtra("phone", phone);
            } else if ("register".equals(flow)) {
                next = new android.content.Intent(this, AccountInfoActivity.class);
                next.putExtra("phone", phone);
                // close this activity so it is not on back stack
                startActivity(next);
                finish();
                return;
            } else if ("register_google".equals(flow)) {
                // Tạo customer mới dựa trên thông tin Google + phone
                String gEmail = getIntent().getStringExtra("google_email");
                String gName  = getIntent().getStringExtra("google_name");

                com.example.bepnhataapp.common.dao.CustomerDao dao = new com.example.bepnhataapp.common.dao.CustomerDao(this);
                com.example.bepnhataapp.common.model.Customer c = new com.example.bepnhataapp.common.model.Customer();
                c.setFullName(gName!=null?gName:phone);
                c.setEmail(gEmail);
                c.setPhone(phone);
                c.setCustomerType("Bạc");
                c.setLoyaltyPoint(0);
                c.setCreatedAt(java.time.LocalDateTime.now().toString());
                c.setStatus("active");
                dao.insert(c);

                // Đăng nhập session bằng phone (ưu tiên phone)
                SessionManager.login(this, phone);

                android.content.Intent intentHome = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                intentHome.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                finish();
                return;
            } else if ("login".equals(flow)) {
                // Successful OTP login
                SessionManager.login(this, phone);
                next = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                next.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(next);
                finish();
                return;
            } else {
                // default just finish
                finish();
                return;
            }
            startActivity(next);
        });

        tvResend.setOnClickListener(v -> {
            if (resendTimer != null) resendTimer.cancel();
            // Disable button and restart timer
            OtpService.sendOtp(this, phone);
            startResendTimer();
        });
    }

    private void setupOtpInputs() {
        int[] ids = {R.id.editTextOtp1, R.id.editTextOtp2, R.id.editTextOtp3,
                R.id.editTextOtp4, R.id.editTextOtp5, R.id.editTextOtp6};

        android.widget.EditText[] boxes = new android.widget.EditText[ids.length];
        for (int i = 0; i < ids.length; i++) {
            boxes[i] = findViewById(ids[i]);
            final int index = i;
            boxes[i].addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                @Override public void onTextChanged(CharSequence s, int st, int before, int count) {}
                @Override public void afterTextChanged(android.text.Editable s) {
                    if (s.length() == 1 && index < boxes.length - 1) {
                        boxes[index + 1].requestFocus();
                    } else if (s.length() == 0 && index > 0) {
                        boxes[index - 1].requestFocus();
                    }
                }
            });

            // Handle backspace when box is already empty
            boxes[i].setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == android.view.KeyEvent.ACTION_DOWN &&
                        keyCode == android.view.KeyEvent.KEYCODE_DEL) {
                    if (boxes[index].getText().length() == 0 && index > 0) {
                        boxes[index - 1].requestFocus();
                        boxes[index - 1].setSelection(boxes[index - 1].getText().length());
                    }
                }
                return false;
            });
        }
    }

    private void startResendTimer() {
        // Disable resend
        tvResend.setEnabled(false);
        tvResend.setAlpha(0.5f);

        final long totalMs = 50_000; // 50 seconds
        resendTimer = new CountDownTimer(totalMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long sec = millisUntilFinished / 1000;
                String time = String.format(java.util.Locale.getDefault(), "00:%02d", sec);
                tvCountdown.setText(time);
            }

            @Override
            public void onFinish() {
                tvCountdown.setText("00:00");
                tvResend.setEnabled(true);
                tvResend.setAlpha(1f);
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (resendTimer != null) resendTimer.cancel();
    }
}
