package com.example.bepnhataapp.features.ingredients;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepnhataapp.databinding.FragmentUsageGuideBinding;

public class UsageGuideFragment extends Fragment {

    private static final String ARG_STORAGE = "arg_storage";
    private static final String ARG_EXPIRE = "arg_expire";
    private static final String ARG_NOTE = "arg_note";

    private FragmentUsageGuideBinding binding;

    public static UsageGuideFragment newInstance(String storageGuide, String expiry, String note) {
        UsageGuideFragment fragment = new UsageGuideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORAGE, storageGuide);
        args.putString(ARG_EXPIRE, expiry);
        args.putString(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUsageGuideBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            String storage = getArguments().getString(ARG_STORAGE, "");
            String expire = getArguments().getString(ARG_EXPIRE, "");
            String note = getArguments().getString(ARG_NOTE, "");

            if (!TextUtils.isEmpty(storage)) {
                binding.tvStorageContent.setText(storage);
            }
            if (!TextUtils.isEmpty(expire)) {
                binding.tvExpiryContent.setText(expire);
            }
            if (!TextUtils.isEmpty(note)) {
                binding.tvNoteContent.setText(note);
            }
        }

        return binding.getRoot();
    }
} 