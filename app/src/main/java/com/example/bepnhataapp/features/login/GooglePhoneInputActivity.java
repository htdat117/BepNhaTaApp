package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import java.util.regex.Pattern;

public class GooglePhoneInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        String email = getIntent().getStringExtra("google_email");
        String name  = getIntent().getStringExtra("google_name");

        Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");
        android.widget.EditText edtPhone = findViewById(R.id.editTextPhone);
        android.widget.ImageView clearBtn = findViewById(R.id.clearPhoneButton);
        android.widget.Button btnContinue = findViewById(R.id.button_continue);
        android.widget.CheckBox cbTerms = findViewById(R.id.checkboxTerms);

        clearBtn.setOnClickListener(v -> edtPhone.setText(""));
        btnContinue.setEnabled(false);

        CustomerDao dao = new CustomerDao(this);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int st,int c,int a){}
            @Override public void onTextChanged(CharSequence s,int st,int b,int c){}
            @Override public void afterTextChanged(Editable s){
                String phone = s.toString().trim();
                boolean ok = PHONE_PATTERN.matcher(phone).matches() && cbTerms.isChecked();
                btnContinue.setEnabled(ok);
                if(!PHONE_PATTERN.matcher(phone).matches()){
                    edtPhone.setError("Số điện thoại không hợp lệ");
                } else edtPhone.setError(null);
            }
        };
        edtPhone.addTextChangedListener(watcher);
        cbTerms.setOnCheckedChangeListener((b,isChecked)->watcher.afterTextChanged(edtPhone.getText()));

        btnContinue.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            if(!PHONE_PATTERN.matcher(phone).matches()){
                edtPhone.setError("Số điện thoại không hợp lệ");
                return;
            }
            if(dao.findByPhone(phone)!=null){
                edtPhone.setError("Số điện thoại đã tồn tại");
                Toast.makeText(this,"Số điện thoại đã được đăng ký",Toast.LENGTH_SHORT).show();
                return;
            }
            // chuyển sang OTP
            android.content.Intent intent = new android.content.Intent(this, AuthenticationActivity.class);
            intent.putExtra("flow","register_google");
            intent.putExtra("phone",phone);
            intent.putExtra("google_email",email);
            intent.putExtra("google_name",name);
            startActivity(intent);
            finish();
        });
    }
} 