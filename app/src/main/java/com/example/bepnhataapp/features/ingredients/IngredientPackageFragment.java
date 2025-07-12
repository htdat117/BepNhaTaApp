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
import com.example.bepnhataapp.common.model.Ingredient;

import java.util.List;

public class IngredientPackageFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_SERVING_FACTOR = "serving";

    public static IngredientPackageFragment newInstance(long productId, int servingFactor) {
        IngredientPackageFragment fragment = new IngredientPackageFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PRODUCT_ID, productId);
        args.putInt(ARG_SERVING_FACTOR, servingFactor);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentIngredientPackageBinding binding;
    private int servingFactor = 1;
    private com.example.bepnhataapp.common.adapter.IngredientAdapter ingredientAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIngredientPackageBinding.inflate(inflater, container, false);

        long productId = getArguments() != null ? getArguments().getLong(ARG_PRODUCT_ID, -1) : -1;
        servingFactor = getArguments()!=null ? getArguments().getInt(ARG_SERVING_FACTOR,1) : 1;
        List<Ingredient> ingredientList = new java.util.ArrayList<>();

        if (productId != -1) {
            com.example.bepnhataapp.common.dao.ProductIngredientDao piDao = new com.example.bepnhataapp.common.dao.ProductIngredientDao(requireContext());
            java.util.List<com.example.bepnhataapp.common.model.ProductIngredient> piList = piDao.getIngredientsOfProduct(productId);
            com.example.bepnhataapp.common.dao.IngredientDao ingDao = new com.example.bepnhataapp.common.dao.IngredientDao(requireContext());
            for (com.example.bepnhataapp.common.model.ProductIngredient pi : piList) {
                Ingredient ingEntity = ingDao.getById(pi.getIngredientID());
                if (ingEntity != null) {
                    double qty = pi.getQuantity() * servingFactor;
                    String qtyStr = (qty == (int)qty ? String.valueOf((int)qty) : String.valueOf(qty));
                    String qtyText = qtyStr + " " + (ingEntity.getUnit() != null ? ingEntity.getUnit() : "");
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

        ingredientAdapter = new com.example.bepnhataapp.common.adapter.IngredientAdapter(getContext(), ingredientList, R.layout.item_ingredient_row);
        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIngredients.setAdapter(ingredientAdapter);
        // Header text
        android.widget.TextView tvHeader = binding.getRoot().findViewById(R.id.tvServingHeader);
        if(tvHeader!=null){
            tvHeader.setText("Nguyên liệu cho " + (servingFactor==1?2:4) + " người");
        }

        return binding.getRoot();
    }

    public void setServingFactor(int factor){
        this.servingFactor = factor;
        // rebuild list quantities
        if(getArguments()!=null) getArguments().putInt(ARG_SERVING_FACTOR,factor);
        // re-fetch and update list quickly
        long productId = getArguments()!=null ? getArguments().getLong(ARG_PRODUCT_ID,-1):-1;
        if(productId==-1) return;
        java.util.List<Ingredient> newList = new java.util.ArrayList<>();
        com.example.bepnhataapp.common.dao.ProductIngredientDao piDao = new com.example.bepnhataapp.common.dao.ProductIngredientDao(requireContext());
        java.util.List<com.example.bepnhataapp.common.model.ProductIngredient> piList = piDao.getIngredientsOfProduct(productId);
        com.example.bepnhataapp.common.dao.IngredientDao ingDao = new com.example.bepnhataapp.common.dao.IngredientDao(requireContext());
        for(com.example.bepnhataapp.common.model.ProductIngredient pi : piList){
            Ingredient ingEntity = ingDao.getById(pi.getIngredientID());
            if(ingEntity!=null){
                double qty = pi.getQuantity()*servingFactor;
                String qtyStr = (qty==(int)qty?String.valueOf((int)qty):String.valueOf(qty));
                String qtyText = qtyStr + " " + (ingEntity.getUnit()!=null? ingEntity.getUnit():"");
                if(ingEntity.getImageLink()!=null && !ingEntity.getImageLink().isEmpty()){
                    newList.add(new Ingredient(ingEntity.getImageLink(), ingEntity.getIngredientName(), qtyText));
                } else {
                    byte[] imgData = ingEntity.getImage();
                    if(imgData!=null && imgData.length>0){
                        newList.add(new Ingredient(imgData, ingEntity.getIngredientName(), qtyText));
                    } else {
                        newList.add(new Ingredient(R.drawable.food_placeholder, ingEntity.getIngredientName(), qtyText));
                    }
                }
            }
        }
        if(ingredientAdapter!=null) ingredientAdapter.updateData(newList);
        else {
            ingredientAdapter = new com.example.bepnhataapp.common.adapter.IngredientAdapter(getContext(), newList, R.layout.item_ingredient_row);
            if(binding!=null) binding.rvIngredients.setAdapter(ingredientAdapter);
        }

        // update header text
        if(getView()!=null){
            android.widget.TextView tvHeader = getView().findViewById(R.id.tvServingHeader);
            if(tvHeader!=null){
                tvHeader.setText("Nguyên liệu cho " + (servingFactor==1?2:4) + " người");
            }
        }
    }
} 
