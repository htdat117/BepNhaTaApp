package com.example.bepnhataapp.features.login;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bepnhataapp.R;

public class PhoneNumberFragment extends Fragment {

    public interface OnPhoneNumberSubmittedListener {
        void onPhoneNumberSubmitted(String phoneNumber);
    }

    private OnPhoneNumberSubmittedListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);

        EditText editTextPhone = view.findViewById(R.id.editTextPhone);
        Button continueButton = view.findViewById(R.id.button_continue);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhone.getText().toString();
                if (listener != null) {
                    listener.onPhoneNumberSubmitted(phoneNumber);
                }
            }
        });

        view.findViewById(R.id.tvRegister).setOnClickListener(v1 -> {
            startActivity(new android.content.Intent(getActivity(), RegistrationActivity.class));
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPhoneNumberSubmittedListener) {
            listener = (OnPhoneNumberSubmittedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPhoneNumberSubmittedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}