package com.example.bepnhataapp.features.manage_pinfor;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.EmailOtpService;
import com.example.bepnhataapp.common.utils.OtpService;
import com.example.bepnhataapp.common.utils.SessionManager;

public class opt_phone extends AppCompatActivity {

    private String verifyType; // phone | email
    private String verifyTarget;
    private long customerId;
    private String newPhone, newEmail, newName, newBirthday, newGender;
    private EditText[] otpInputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_opt_phone);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Read extras
        customerId = getIntent().getLongExtra("customer_id", -1);
        verifyType = getIntent().getStringExtra("verify_type");
        verifyTarget = getIntent().getStringExtra("verify_target");
        newPhone = getIntent().getStringExtra("new_phone");
        newEmail = getIntent().getStringExtra("new_email");
        newName = getIntent().getStringExtra("new_name");
        newBirthday = getIntent().getStringExtra("new_birthday");
        newGender = getIntent().getStringExtra("new_gender");

        TextView tvNotice = findViewById(R.id.tv_otp_notice);
        TextView tvTarget = findViewById(R.id.tv_otp_email);
        tvNotice.setText("Mã xác thực đã được gửi đến ");
        tvTarget.setText(verifyTarget);

        // Input fields
        otpInputs = new EditText[6];
        otpInputs[0] = findViewById(R.id.otp_1);
        otpInputs[1] = findViewById(R.id.otp_2);
        otpInputs[2] = findViewById(R.id.otp_3);
        otpInputs[3] = findViewById(R.id.otp_4);
        otpInputs[4] = findViewById(R.id.otp_5);
        otpInputs[5] = findViewById(R.id.otp_6);

        // Auto move focus
        for (int i = 0; i < 6; i++) {
            final int idx = i;
            otpInputs[i].addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s,int st,int c,int a){}
                @Override public void onTextChanged(CharSequence s,int st,int b,int c){}
                @Override public void afterTextChanged(android.text.Editable s){
                    if (s.length()==1 && idx<5) otpInputs[idx+1].requestFocus();
                    if (allFilled(otpInputs)) verifyCode(collectCode(otpInputs));
                }
            });
        }

        TextView tvResend = findViewById(R.id.tv_resend);
        TextView tvTimer = findViewById(R.id.tv_timer);

        tvResend.setOnClickListener(v -> {
            if ("phone".equals(verifyType)) {
                OtpService.sendOtp(this, verifyTarget);
            } else {
                EmailOtpService.sendOtp(this, verifyTarget);
            }
            startCountdown(tvTimer); // restart timer
        });

        startCountdown(tvTimer);
    }

    private boolean allFilled(EditText[] arr){
        for(EditText e:arr){if(e.getText().length()!=1) return false;}return true;}
    private String collectCode(EditText[] arr){StringBuilder sb=new StringBuilder();for(EditText e:arr) sb.append(e.getText());return sb.toString();}

    private void verifyCode(String code){
        boolean ok;
        if ("phone".equals(verifyType)) {
            ok = OtpService.verifyOtp(this, verifyTarget, code);
            if (ok) OtpService.clearOtp(this, verifyTarget);
        } else {
            ok = EmailOtpService.verifyOtp(this, verifyTarget, code);
            if (ok) EmailOtpService.clear(this, verifyTarget);
        }
        if (!ok) {
            Toast.makeText(this, "Mã OTP không chính xác", Toast.LENGTH_SHORT).show();
            clearOtpInputs();
            return;
        }
        // update data
        CustomerDao dao = new CustomerDao(this);
        Customer c = dao.findById(customerId);
        if (c == null) {
            Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (newPhone != null) {
            c.setPhone(newPhone);
            SessionManager.login(this, newPhone);
        }
        if (newEmail != null) {
            c.setEmail(newEmail);
        }
        c.setFullName(newName);
        c.setBirthday(newBirthday);
        c.setGender(newGender);
        dao.update(c);
        Toast.makeText(this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void startCountdown(TextView tv) {
        final long[] millisLeft = {5 * 60 * 1000};
        tv.setText("05:00");
        android.os.Handler handler = new android.os.Handler();
        Runnable r = new Runnable() {
            @Override public void run() {
                millisLeft[0] -= 1000;
                if (millisLeft[0] < 0) return;
                int sec = (int) (millisLeft[0] / 1000);
                int m = sec / 60;
                int s = sec % 60;
                tv.setText(String.format("%02d:%02d", m, s));
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(r, 1000);
    }

    private void clearOtpInputs(){
        for(EditText e: otpInputs){e.setText("");}
        if(otpInputs.length>0) otpInputs[0].requestFocus();
    }
}
