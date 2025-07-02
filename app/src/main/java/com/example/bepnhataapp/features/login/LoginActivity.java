package com.example.bepnhataapp.features.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bepnhataapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.utils.SessionManager;

public class LoginActivity extends AppCompatActivity implements PhoneNumberFragment.OnPhoneNumberSubmittedListener {

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.view.View close = findViewById(R.id.closeButton);
        if (close != null) {
            close.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }

        // Configure Google Sign-In to request email only
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google login button
        android.widget.ImageView btnGoogle = findViewById(R.id.btnGoogleLogin);
        if(btnGoogle!=null){
            btnGoogle.setOnClickListener(v -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
            });
        }

        if (savedInstanceState == null) {
            String prefill = getIntent().getStringExtra("phone");
            PhoneNumberFragment fragment;
            if (prefill != null && !prefill.isEmpty()) {
                fragment = PhoneNumberFragment.newInstancePrefill(prefill);
            } else {
                fragment = new PhoneNumberFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onPhoneNumberSubmitted(String phoneNumber) {
        // Navigate to password screen
        PasswordFragment passwordFragment = PasswordFragment.newInstance(phoneNumber);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, passwordFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if(account==null){
                android.widget.Toast.makeText(this, "Đăng nhập Google thất bại", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            String email = account.getEmail();
            String name = account.getDisplayName();

            // Save or create customer in local DB
            CustomerDao dao = new CustomerDao(this);
            Customer c = null;
            java.util.List<Customer> all = dao.getAll();
            for(Customer cc: all){
                if(email!=null && email.equalsIgnoreCase(cc.getEmail())){ c = cc; break; }
            }
            if(c==null){
                c = new Customer();
                c.setFullName(name!=null?name:email);
                c.setEmail(email);
                c.setPhone(email); // dùng email làm khoá đăng nhập trong session
                c.setCustomerType("Bạc");
                c.setLoyaltyPoint(0);
                c.setCreatedAt(java.time.LocalDateTime.now().toString());
                c.setStatus("active");
                dao.insert(c);
            }

            // Mark session as logged-in (use email)
            SessionManager.login(this, email);

            android.widget.Toast.makeText(this, "Đăng nhập Google thành công", android.widget.Toast.LENGTH_SHORT).show();

            // Navigate to ManageAccountActivity giống login thường
            Intent intent = new Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch(ApiException e){
            android.util.Log.e("LoginActivity", "Google sign in failed: "+e.getStatusCode());
            android.widget.Toast.makeText(this, "Đăng nhập Google thất bại", android.widget.Toast.LENGTH_SHORT).show();
        }
    }
}