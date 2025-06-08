package com.example.bepnhataapp.features.policy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_policy, container, false);
        if (getArguments() != null) {
            policyType = getArguments().getInt(ARG_POLICY_TYPE, 0);
        }
        showPolicyContent();
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
} 