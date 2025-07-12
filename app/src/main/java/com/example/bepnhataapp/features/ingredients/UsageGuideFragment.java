package com.example.bepnhataapp.features.ingredients;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.databinding.FragmentUsageGuideBinding;

public class UsageGuideFragment extends Fragment {

    private static final String ARG_STORAGE = "arg_storage";
    private static final String ARG_EXPIRE = "arg_expire";
    private static final String ARG_NOTE = "arg_note";
    private static final String ARG_IMAGE_URL = "arg_image_url";
    private static final String ARG_IMAGE_BLOB = "arg_image_blob";
    private static final String ARG_IMAGE_RES = "arg_image_res";

    private FragmentUsageGuideBinding binding;

    public static UsageGuideFragment newInstance(String storageGuide, String expiry, String note, String imageUrl, byte[] imageBlob, int imageResId) {
        UsageGuideFragment fragment = new UsageGuideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STORAGE, storageGuide);
        args.putString(ARG_EXPIRE, expiry);
        args.putString(ARG_NOTE, note);
        args.putString(ARG_IMAGE_URL, imageUrl);
        args.putByteArray(ARG_IMAGE_BLOB, imageBlob);
        args.putInt(ARG_IMAGE_RES, imageResId);
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
            String imageUrl = getArguments().getString(ARG_IMAGE_URL, null);
            byte[] imageBlob = getArguments().getByteArray(ARG_IMAGE_BLOB);
            int imageResId = getArguments().getInt(ARG_IMAGE_RES, 0);

            if (!TextUtils.isEmpty(storage)) {
                binding.tvStorageContent.setText(storage.replace("/n", "\n").replace("\\n", "\n"));
            }
            if (!TextUtils.isEmpty(expire)) {
                binding.tvExpiryContent.setText(expire.replace("/n", "\n").replace("\\n", "\n"));
            }
            if (!TextUtils.isEmpty(note)) {
                binding.tvNoteContent.setText(note.replace("/n", "\n").replace("\\n", "\n"));
            }
            // Hiển thị ảnh minh họa
            if (binding.ivGuideImage != null) {
                Log.d("DEBUG_IMAGE", "imageUrl nhận vào: " + imageUrl);
                if (imageUrl != null && (imageUrl.startsWith("http://") || imageUrl.startsWith("https://"))) {
                    Glide.with(this).load(imageUrl).placeholder(R.drawable.food_placeholder).into(binding.ivGuideImage);
                } else if (imageBlob != null && imageBlob.length > 0) {
                    Glide.with(this).load(imageBlob).placeholder(R.drawable.food_placeholder).into(binding.ivGuideImage);
                } else if (imageResId != 0) {
                    binding.ivGuideImage.setImageResource(imageResId);
                } else {
                    binding.ivGuideImage.setImageResource(R.drawable.food_placeholder);
                }
            }
        }

        return binding.getRoot();
    }
} 
