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
import com.example.bepnhataapp.common.models.Ingredient;
import com.example.bepnhataapp.common.adapter.IngredientAdapter;

import java.util.ArrayList;
import java.util.List;

public class IngredientPackageFragment extends Fragment {

    private FragmentIngredientPackageBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIngredientPackageBinding.inflate(inflater, container, false);

        // Sample ingredient data
        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(new Ingredient(R.drawable.food, "Cua biển tươi", "200 gram"));
        ingredientList.add(new Ingredient(R.drawable.food, "Xương heo", "200 gram"));
        ingredientList.add(new Ingredient(R.drawable.food, "Tôm tươi", "100 gram"));
        ingredientList.add(new Ingredient(R.drawable.food, "Chả cua", "100 gram"));
        ingredientList.add(new Ingredient(R.drawable.food, "Bánh canh", "500 gram"));
        ingredientList.add(new Ingredient(R.drawable.food, "Trứng cút", "4 quả"));
        ingredientList.add(new Ingredient(R.drawable.food, "Gói gia vị tạo hương", "1 gói"));
        ingredientList.add(new Ingredient(R.drawable.food, "Gói gia vị Bếp Nhà Ta", "1 gói"));
        ingredientList.add(new Ingredient(R.drawable.food, "Rau sống ăn kèm", "200 gram"));

        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredientList);
        binding.rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvIngredients.setAdapter(ingredientAdapter);

        return binding.getRoot();
    }
} 