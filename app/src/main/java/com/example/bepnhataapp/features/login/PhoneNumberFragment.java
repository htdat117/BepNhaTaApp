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
import android.text.TextWatcher;
import android.text.Editable;
import java.util.regex.Pattern;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.model.Customer;

public class PhoneNumberFragment extends Fragment {

    public interface OnPhoneNumberSubmittedListener {
        void onPhoneNumberSubmitted(String phoneNumber);
    }

    private OnPhoneNumberSubmittedListener listener;
    private Button continueButton;
    private CustomerDao customerDao;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9}$");

    private boolean isValidPhoneNumber(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

    private static final String ARG_PREFILL_PHONE = "prefill_phone";

    public static PhoneNumberFragment newInstancePrefill(String phone) {
        PhoneNumberFragment f = new PhoneNumberFragment();
        Bundle b = new Bundle();
        b.putString(ARG_PREFILL_PHONE, phone);
        f.setArguments(b);
        return f;
    }

    private String prefillPhone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prefillPhone = getArguments().getString(ARG_PREFILL_PHONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_number, container, false);

        EditText editTextPhone = view.findViewById(R.id.editTextPhone);
        android.widget.ImageView clearBtn = view.findViewById(R.id.clearButton);
        clearBtn.setOnClickListener(v -> {
            editTextPhone.setText("");
        });
        if (prefillPhone != null && !prefillPhone.isEmpty()) {
            editTextPhone.setText(prefillPhone);
        }
        continueButton = view.findViewById(R.id.button_continue);
        continueButton.setEnabled(false); // disabled until valid

        // Initialize DAO
        customerDao = new CustomerDao(requireContext());

        // Real-time validation
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = s.toString().trim();
                if (phone.isEmpty()) {
                    editTextPhone.setError(null);
                    continueButton.setEnabled(false);
                    return;
                }
                if (!isValidPhoneNumber(phone)) {
                    editTextPhone.setError("Số điện thoại không hợp lệ");
                    continueButton.setEnabled(false);
                    return;
                }
                // Check existence in DB
                Customer existing = customerDao.findByPhone(phone);
                if (existing == null) {
                    editTextPhone.setError("Số điện thoại chưa được đăng ký");
                    continueButton.setEnabled(false);
                } else {
                    editTextPhone.setError(null);
                    continueButton.setEnabled(true);
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhone.getText().toString().trim();
                if (listener != null) {
                    listener.onPhoneNumberSubmitted(phoneNumber);
                }
            }
        });

        view.findViewById(R.id.tvRegister).setOnClickListener(v1 -> {
            startActivity(new android.content.Intent(getActivity(), RegistrationActivity.class));
        });

        android.view.View btnGuest = view.findViewById(R.id.buy_nologin);
        if(btnGuest!=null){
            btnGuest.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.home.HomeActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                if(getActivity()!=null) getActivity().finish();
            });
        }

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
