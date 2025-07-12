package com.example.bepnhataapp.features.policy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bepnhataapp.R;

public class PolicyShippingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.policy_shipping, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up any references to views here
    }
} 
