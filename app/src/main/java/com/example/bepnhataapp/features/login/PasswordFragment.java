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

import com.example.bepnhataapp.R;

public class PasswordFragment extends Fragment {

    private static final String ARG_PHONE_NUMBER = "phone_number";

    private String phoneNumber;

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

        Button loginButton = view.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In a real app, you would get the password from editTextPassword
                // and perform login logic with the phoneNumber
                Toast.makeText(getContext(), "Logging in for phone number: " + phoneNumber, Toast.LENGTH_SHORT).show();
            }
        });

        TextView forgotPasswordTextView = view.findViewById(R.id.forgotPassword);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                startActivity(intent);
            }
        });

        TextView loginByOtpTextView = view.findViewById(R.id.loginByOtp);
        loginByOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}