package com.example.bepnhataapp.features.ingredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.bepnhataapp.databinding.FragmentUsageGuideBinding;

public class UsageGuideFragment extends Fragment {

    private FragmentUsageGuideBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUsageGuideBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
} 