package com.example.bepnhataapp.features.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.features.manage_account.ManageAccountActivity;
import com.example.bepnhataapp.common.utils.SessionManager;

public class PasswordFragment extends Fragment {

    private static final String ARG_PHONE_NUMBER = "phone_number";

    private String phoneNumber;
    private CustomerDao customerDao;

    public static PasswordFragment newInstance(String phoneNumber) {
        PasswordFragment fragment = new PasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString(ARG_PHONE_NUMBER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password, container, false);

        customerDao = new CustomerDao(requireContext());

        EditText edtPass = view.findViewById(R.id.editTextPassword);
        ImageView clearBtn = view.findViewById(R.id.clearPasswordButton);
        ImageView eyeBtn = view.findViewById(R.id.togglePasswordVisibilityButton);

        // Clear password text
        clearBtn.setOnClickListener(v -> edtPass.setText(""));

        // Toggle show/hide password
        final boolean[] pwdVisible = {false};
        eyeBtn.setOnClickListener(v -> {
            pwdVisible[0] = !pwdVisible[0];
            if (pwdVisible[0]) {
                edtPass.setTransformationMethod(null);
            } else {
                edtPass.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            }
            edtPass.setSelection(edtPass.getText().length());

            // Change icon resource if available – fallback keeps same icon
            int resId = getResources().getIdentifier(pwdVisible[0] ? "ic_eye_open" : "ic_eye", "drawable", requireContext().getPackageName());
            if(resId!=0) eyeBtn.setImageResource(resId);
        });

        // Show/hide clear button depending on text length
        edtPass.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s,int st,int c,int a){}
            @Override public void onTextChanged(CharSequence s,int st,int b,int c){}
            @Override public void afterTextChanged(android.text.Editable s){
                clearBtn.setVisibility(s.length()>0 ? View.VISIBLE : View.INVISIBLE);
            }
        });

        Button loginButton = view.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passInput = edtPass.getText().toString();
                if(passInput.isEmpty()){
                    Toast.makeText(getContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                Customer c = customerDao.findByPhoneAndPassword(phoneNumber, passInput);
                if(c==null){
                    Toast.makeText(getContext(), "Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    return;
                }
                SessionManager.login(requireContext(), phoneNumber);
                Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ManageAccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        TextView forgotPasswordTextView = view.findViewById(R.id.forgotPassword);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                intent.putExtra("flow", "reset");
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });

        TextView loginByOtpTextView = view.findViewById(R.id.loginByOtp);
        loginByOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                intent.putExtra("flow", "login");
                intent.putExtra("phone", phoneNumber);
                startActivity(intent);
            }
        });

        // Register link
        TextView registerLink = view.findViewById(R.id.tvRegister);
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegistrationActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
