package com.example.bepnhataapp.features.policy;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
    private TextView btnDieuKhoan, btnBaoMat, btnGiaoHang, btnDoiTra;
    private OnPolicyTabSelectedListener mListener;
    private Fragment currentFragment;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            policyType = getArguments().getInt(ARG_POLICY_TYPE, 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_policy_content, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupClickListeners();
        if (savedInstanceState == null) {
            showPolicyContent();
            updateButtonStates();
            updateInitialTitle();
        }
    }

    private void initializeViews(View view) {
        btnDieuKhoan = view.findViewById(R.id.btnDieuKhoan);
        btnBaoMat = view.findViewById(R.id.btnBaoMat);
        btnGiaoHang = view.findViewById(R.id.btnGiaoHang);
        btnDoiTra = view.findViewById(R.id.btnDoiTra);
    }

    private void setupClickListeners() {
        if (btnDieuKhoan != null) {
            btnDieuKhoan.setOnClickListener(v -> handleTabClick(TYPE_TERMS, "ĐIỀU KHOẢN SỬ DỤNG"));
        }
        if (btnBaoMat != null) {
            btnBaoMat.setOnClickListener(v -> handleTabClick(TYPE_PRIVACY, "CHÍNH SÁCH BẢO MẬT"));
        }
        if (btnGiaoHang != null) {
            btnGiaoHang.setOnClickListener(v -> handleTabClick(TYPE_SHIPPING, "CHÍNH SÁCH GIAO HÀNG"));
        }
        if (btnDoiTra != null) {
            btnDoiTra.setOnClickListener(v -> handleTabClick(TYPE_RETURN, "CHÍNH SÁCH ĐỔI TRẢ"));
        }
    }

    private void handleTabClick(int type, String title) {
        if (policyType == type) return;
        
        policyType = type;
        showPolicyContent();
        updateButtonStates();
        if (mListener != null) {
            mListener.onPolicyTabSelected(title);
        }
    }

    private void showPolicyContent() {
        if (!isAdded()) return;
        
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        
        Fragment newFragment;
        switch (policyType) {
            case TYPE_PRIVACY:
                newFragment = new PolicyPrivacyFragment();
                break;
            case TYPE_SHIPPING:
                newFragment = new PolicyShippingFragment();
                break;
            case TYPE_RETURN:
                newFragment = new PolicyReturnFragment();
                break;
            case TYPE_TERMS:
            default:
                newFragment = new PolicyTermsFragment();
                break;
        }

        if (currentFragment != null) {
            ft.remove(currentFragment);
        }
        
        currentFragment = newFragment;
        ft.add(R.id.content_container, currentFragment);
        ft.commit();
    }

    private void updateButtonStates() {
        if (!isAdded()) return;

        int radius = (int) getResources().getDimension(R.dimen.corner_radius_8dp);
        Context context = requireContext();

        GradientDrawable selectedDrawable = new GradientDrawable();
        selectedDrawable.setShape(GradientDrawable.RECTANGLE);
        selectedDrawable.setColor(ContextCompat.getColor(context, R.color.primary1));
        selectedDrawable.setCornerRadius(radius);

        GradientDrawable unselectedDrawable = new GradientDrawable();
        unselectedDrawable.setShape(GradientDrawable.RECTANGLE);
        unselectedDrawable.setColor(ContextCompat.getColor(context, R.color.dark3));
        unselectedDrawable.setCornerRadius(radius);

        updateButtonState(btnDieuKhoan, TYPE_TERMS, selectedDrawable, unselectedDrawable);
        updateButtonState(btnBaoMat, TYPE_PRIVACY, selectedDrawable, unselectedDrawable);
        updateButtonState(btnGiaoHang, TYPE_SHIPPING, selectedDrawable, unselectedDrawable);
        updateButtonState(btnDoiTra, TYPE_RETURN, selectedDrawable, unselectedDrawable);
    }

    private void updateButtonState(TextView button, int type, GradientDrawable selectedDrawable, GradientDrawable unselectedDrawable) {
        if (button != null) {
            if (policyType == type) {
                button.setBackground(selectedDrawable);
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            } else {
                button.setBackground(unselectedDrawable);
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark1));
            }
        }
    }

    private void updateInitialTitle() {
        if (!isAdded() || mListener == null) return;

        String initialTitle = "CHÍNH SÁCH";
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        btnDieuKhoan = null;
        btnBaoMat = null;
        btnGiaoHang = null;
        btnDoiTra = null;
        currentFragment = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
} 
