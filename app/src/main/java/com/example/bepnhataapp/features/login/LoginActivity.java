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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements PhoneNumberFragment.OnPhoneNumberSubmittedListener {

    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

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

        /*
         * Configure Google Sign-In. In addition to the e-mail we now also request an
         * ID-token which is required by Google when the application is configured
         * with an OAuth-2 client on Firebase / Google Cloud.  The generated
         * `default_web_client_id` constant is placed automatically in
         *   res/values/strings.xml
         * Make sure you have registered the SHA-1 fingerprint of your keystore in
         * the Firebase console, then download the latest google-services.json and
         * replace the existing one in the project.
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // Google login button
        android.widget.ImageView btnGoogle = findViewById(R.id.btnGoogleLogin);
        if(btnGoogle!=null){
            btnGoogle.setOnClickListener(v -> {
                // Kiểm tra Google Play Services & kết nối mạng trước khi mở Sign-In
                if(!com.example.bepnhataapp.common.utils.GooglePlayServicesHelper.isGooglePlayServicesAvailable(this)){
                    android.widget.Toast.makeText(this, "Thiếu Google Play Services hoặc chưa cập nhật", android.widget.Toast.LENGTH_LONG).show();
                    return;
                }
                android.net.ConnectivityManager cm = (android.net.ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                android.net.NetworkInfo ni = cm!=null? cm.getActiveNetworkInfo():null;
                if(ni==null || !ni.isConnected()){
                    android.widget.Toast.makeText(this, "Thiết bị không có kết nối Internet", android.widget.Toast.LENGTH_LONG).show();
                    return;
                }

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

            String idToken = account.getIdToken();
            if(idToken == null){
                android.widget.Toast.makeText(this, "Không lấy được ID Token", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if(firebaseUser==null){
                        android.widget.Toast.makeText(this, "Đăng nhập Google thất bại", android.widget.Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String email = firebaseUser.getEmail();
                    String name = firebaseUser.getDisplayName();

                    // Save or create customer in local DB
                    CustomerDao dao = new CustomerDao(this);
                    Customer c = null;
                    java.util.List<Customer> all = dao.getAll();
                    for(Customer cc: all){
                        if(email!=null && email.equalsIgnoreCase(cc.getEmail())){ c = cc; break; }
                    }
                    if(c==null){
                        // Chưa có tài khoản → yêu cầu xác thực số điện thoại trước khi tạo
                        Intent phoneIntent = new Intent(this, GooglePhoneInputActivity.class);
                        phoneIntent.putExtra("google_email", email);
                        phoneIntent.putExtra("google_name", name);
                        startActivity(phoneIntent);
                        finish();
                        return;
                    }

                    // Đã có tài khoản → login bình thường
                    SessionManager.login(this, c.getPhone()!=null && !c.getPhone().isEmpty()? c.getPhone() : email);

                    android.widget.Toast.makeText(this, "Đăng nhập Google thành công", android.widget.Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, com.example.bepnhataapp.features.manage_account.ManageAccountActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    android.util.Log.e("LoginActivity", "Firebase auth failed", task.getException());
                    android.widget.Toast.makeText(this, "Đăng nhập Google thất bại", android.widget.Toast.LENGTH_SHORT).show();
                }
            });

        } catch(ApiException e){
            int statusCode = e.getStatusCode();
            android.util.Log.e("LoginActivity", "Google sign in failed with status: " + statusCode, e);
            android.widget.Toast.makeText(this, "Đăng nhập Google thất bại (mã lỗi: " + statusCode + ")", android.widget.Toast.LENGTH_LONG).show();
        }
    }
}
