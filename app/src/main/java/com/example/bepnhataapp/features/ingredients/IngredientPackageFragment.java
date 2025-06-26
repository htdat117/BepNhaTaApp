package com.example.bepnhataapp.features.ingredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.databinding.FragmentIngredientPackageBinding;
import com.example.bepnhataapp.common.adapter.IngredientAdapter;
import com.example.bepnhataapp.common.models.Ingredient;

import java.util.List;

public class IngredientPackageFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";

    public static IngredientPackageFragment newInstance(long productId) {
        IngredientPackageFragment fragment = new IngredientPackageFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentIngredientPackageBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIngredientPackageBinding.inflate(inflater, container, false);

        long productId = getArguments() != null ? getArguments().getLong(ARG_PRODUCT_ID, -1) : -1;
        List<Ingredient> ingredientList = new java.util.ArrayList<>();

        if (productId != -1) {
            com.example.bepnhataapp.common.dao.ProductIngredientDao piDao = new com.example.bepnhataapp.common.dao.ProductIngredientDao(requireContext());
            java.util.List<com.example.bepnhataapp.common.model.ProductIngredient> piList = piDao.getIngredientsOfProduct(productId);
            com.example.bepnhataapp.common.dao.IngredientDao ingDao = new com.example.bepnhataapp.common.dao.IngredientDao(requireContext());
            for (com.example.bepnhataapp.common.model.ProductIngredient pi : piList) {
                com.example.bepnhataapp.common.model.Ingredient ingEntity = ingDao.getById(pi.getIngredientID());
                if (ingEntity != null) {
                    String qtyText = pi.getQuantity() + " " + (ingEntity.getUnit() != null ? ingEntity.getUnit() : "");
                    String link = ingEntity.getImageLink();
                    if(link!=null && !link.isEmpty()){
                        ingredientList.add(new Ingredient(link, ingEntity.getIngredientName(), qtyText));
                    } else {
                        byte[] imgData = ingEntity.getImage();
                        if (imgData != null && imgData.length > 0) {
                            ingredientList.add(new Ingredient(imgData, ingEntity.getIngredientName(), qtyText));
                        } else {
                            ingredientList.add(new Ingredient(R.drawable.food_placeholder, ingEntity.getIngredientName(), qtyText));
                        }
                    }
                }
            }
        }

        // If list is empty (productId invalid or no data), show placeholder sample data (optional)
        if (ingredientList.isEmpty()) {
            ingredientList.add(new Ingredient(R.drawable.food_placeholder, "Không có dữ liệu", ""));
        }

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredientList);
        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIngredients.setAdapter(ingredientAdapter);

        return binding.getRoot();
    }
} 