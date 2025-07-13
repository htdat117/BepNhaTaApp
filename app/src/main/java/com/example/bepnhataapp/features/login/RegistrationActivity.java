package com.example.bepnhataapp.features.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Pattern;
import com.example.bepnhataapp.R;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.example.bepnhataapp.common.utils.SessionManager;
import android.content.Intent;
import android.util.Log;

public class RegistrationActivity extends AppCompatActivity {
    private static final int RC_GOOGLE_SIGN_IN = 1001;
    private GoogleSignInClient googleClient;
    private FirebaseAuth mAuth;

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
                // Hiển thị lỗi khi số điện thoại đã tồn tại, không chuyển sang đăng nhập
                edtPhone.setError("Số điện thoại đã tồn tại");
                android.widget.Toast.makeText(this, "Số điện thoại này đã được đăng ký", android.widget.Toast.LENGTH_SHORT).show();
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

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        android.widget.ImageView imgGoogle = findViewById(R.id.imgGoogleLoginReg);
        if(imgGoogle!=null){
            imgGoogle.setOnClickListener(v -> {
                Intent i = googleClient.getSignInIntent();
                startActivityForResult(i, RC_GOOGLE_SIGN_IN);
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleResult(task);
        }
    }

    private void handleGoogleResult(Task<GoogleSignInAccount> completed){
        try{
            GoogleSignInAccount acct = completed.getResult(ApiException.class);
            if(acct == null){
                android.widget.Toast.makeText(this,"Đăng nhập Google thất bại",android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            AuthCredential cred = GoogleAuthProvider.getCredential(acct.getIdToken(),null);
            mAuth.signInWithCredential(cred).addOnCompleteListener(this,t->{
                if(!t.isSuccessful()){
                    android.widget.Toast.makeText(this,"Đăng nhập Google thất bại",android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser fu = t.getResult().getUser();
                if(fu==null){
                    android.widget.Toast.makeText(this,"Đăng nhập Google thất bại",android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                String email = fu.getEmail();
                String name  = fu.getDisplayName();

                // Kiểm tra email đã tồn tại
                CustomerDao dao = new CustomerDao(this);
                Customer existing = null;
                for(Customer c: dao.getAll()){
                    if(email!=null && email.equalsIgnoreCase(c.getEmail())){ existing = c; break; }
                }
                if(existing!=null){
                    // Đã có tài khoản ➜ login
                    SessionManager.login(this, existing.getPhone()!=null&&!existing.getPhone().isEmpty()? existing.getPhone(): email);
                    android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    // Chưa có ➜ yêu cầu nhập SĐT
                    android.content.Intent phoneIntent = new android.content.Intent(this, GooglePhoneInputActivity.class);
                    phoneIntent.putExtra("google_email", email);
                    phoneIntent.putExtra("google_name", name);
                    startActivity(phoneIntent);
                    finish();
                }
            });
        }catch(ApiException e){
            android.util.Log.e("RegistrationActivity","Google sign-in failed",e);
            android.widget.Toast.makeText(this,"Đăng nhập Google thất bại",android.widget.Toast.LENGTH_SHORT).show();
        }
    }
} 
