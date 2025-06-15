package com.example.bepnhataapp.features.policy;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.bepnhataapp.R;

public class PolicyFragment extends Fragment {
    public static final String ARG_POLICY_TYPE = "policy_type";
    public static final int TYPE_TERMS = 0;
    public static final int TYPE_PRIVACY = 1;
    public static final int TYPE_SHIPPING = 2;
    public static final int TYPE_RETURN = 3;

    private int policyType = 0;

    // Declare TextViews instead of Buttons
    private TextView btnDieuKhoan, btnBaoMat, btnGiaoHang, btnDoiTra;

    // Listener for communicating with the hosting Activity
    private OnPolicyTabSelectedListener mListener;

    public interface OnPolicyTabSelectedListener {
        void onPolicyTabSelected(String title);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnPolicyTabSelectedListener) {
            mListener = (OnPolicyTabSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPolicyTabSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_policy_content, container, false);
        if (getArguments() != null) {
            policyType = getArguments().getInt(ARG_POLICY_TYPE, 0);
        }

        // Initialize TextViews
        btnDieuKhoan = view.findViewById(R.id.btnDieuKhoan);
        btnBaoMat = view.findViewById(R.id.btnBaoMat);
        btnGiaoHang = view.findViewById(R.id.btnGiaoHang);
        btnDoiTra = view.findViewById(R.id.btnDoiTra);

        // Set OnClickListeners
        if (btnDieuKhoan != null) {
            btnDieuKhoan.setOnClickListener(v -> {
                policyType = TYPE_TERMS;
                showPolicyContent();
                updateButtonStates(); // Update button states after click
                if (mListener != null) mListener.onPolicyTabSelected("ĐIỀU KHOẢN SỬ DỤNG");
            });
        }
        if (btnBaoMat != null) {
            btnBaoMat.setOnClickListener(v -> {
                policyType = TYPE_PRIVACY;
                showPolicyContent();
                updateButtonStates(); // Update button states after click
                if (mListener != null) mListener.onPolicyTabSelected("CHÍNH SÁCH BẢO MẬT");
            });
        }
        if (btnGiaoHang != null) {
            btnGiaoHang.setOnClickListener(v -> {
                policyType = TYPE_SHIPPING;
                showPolicyContent();
                updateButtonStates(); // Update button states after click
                if (mListener != null) mListener.onPolicyTabSelected("CHÍNH SÁCH GIAO HÀNG");
            });
        }
        if (btnDoiTra != null) {
            btnDoiTra.setOnClickListener(v -> {
                policyType = TYPE_RETURN;
                showPolicyContent();
                updateButtonStates(); // Update button states after click
                if (mListener != null) mListener.onPolicyTabSelected("CHÍNH SÁCH ĐỔI TRẢ");
            });
        }

        showPolicyContent();
        updateButtonStates(); // Initial update when view is created
        // Set initial title based on current policyType
        if (mListener != null) {
            String initialTitle = "CHÍNH SÁCH"; // Default title
            switch (policyType) {
                case TYPE_TERMS:
                    initialTitle = "ĐIỀU KHOẢN SỬ DỤNG";
                    break;
                case TYPE_PRIVACY:
                    initialTitle = "CHÍNH SÁCH BẢO MẬT";
                    break;
                case TYPE_SHIPPING:
                    initialTitle = "CHÍNH SÁCH GIAO HÀNG";
                    break;
                case TYPE_RETURN:
                    initialTitle = "CHÍNH SÁCH ĐỔI TRẢ";
                    break;
            }
            mListener.onPolicyTabSelected(initialTitle);
        }
        return view;
    }

    private void showPolicyContent() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment contentFragment;
        switch (policyType) {
            case TYPE_PRIVACY:
                contentFragment = new PolicyPrivacyFragment();
                break;
            case TYPE_SHIPPING:
                contentFragment = new PolicyShippingFragment();
                break;
            case TYPE_RETURN:
                contentFragment = new PolicyReturnFragment();
                break;
            case TYPE_TERMS:
            default:
                contentFragment = new PolicyTermsFragment();
                break;
        }
        ft.replace(R.id.content_container, contentFragment);
        ft.commit();
    }

    private void updateButtonStates() {
        int radius = (int) getResources().getDimension(R.dimen.corner_radius_8dp);

        // Define drawables for selected and unselected states
        GradientDrawable selectedDrawable = new GradientDrawable();
        selectedDrawable.setShape(GradientDrawable.RECTANGLE);
        selectedDrawable.setColor(getResources().getColor(R.color.primary1, null));
        selectedDrawable.setCornerRadius(radius);

        GradientDrawable unselectedDrawable = new GradientDrawable();
        unselectedDrawable.setShape(GradientDrawable.RECTANGLE);
        unselectedDrawable.setColor(getResources().getColor(R.color.dark3, null));
        unselectedDrawable.setCornerRadius(radius);

        // Apply background and text color based on selected state
        // Điều khoản sử dụng
        if (btnDieuKhoan != null) {
            if (policyType == TYPE_TERMS) {
                btnDieuKhoan.setBackground(selectedDrawable);
                btnDieuKhoan.setTextColor(getResources().getColor(R.color.white, null));
            } else {
                btnDieuKhoan.setBackground(unselectedDrawable);
                btnDieuKhoan.setTextColor(getResources().getColor(R.color.dark1, null));
            }
        }

        // Chính sách bảo mật
        if (btnBaoMat != null) {
            if (policyType == TYPE_PRIVACY) {
                btnBaoMat.setBackground(selectedDrawable);
                btnBaoMat.setTextColor(getResources().getColor(R.color.white, null));
            } else {
                btnBaoMat.setBackground(unselectedDrawable);
                btnBaoMat.setTextColor(getResources().getColor(R.color.dark1, null));
            }
        }

        // Chính sách giao hàng
        if (btnGiaoHang != null) {
            if (policyType == TYPE_SHIPPING) {
                btnGiaoHang.setBackground(selectedDrawable);
                btnGiaoHang.setTextColor(getResources().getColor(R.color.white, null));
            } else {
                btnGiaoHang.setBackground(unselectedDrawable);
                btnGiaoHang.setTextColor(getResources().getColor(R.color.dark1, null));
            }
        }

        // Chính sách đổi trả
        if (btnDoiTra != null) {
            if (policyType == TYPE_RETURN) {
                btnDoiTra.setBackground(selectedDrawable);
                btnDoiTra.setTextColor(getResources().getColor(R.color.white, null));
            } else {
                btnDoiTra.setBackground(unselectedDrawable);
                btnDoiTra.setTextColor(getResources().getColor(R.color.dark1, null));
            }
        }
    }
} 