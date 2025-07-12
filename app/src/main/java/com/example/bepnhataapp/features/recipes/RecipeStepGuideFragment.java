package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.dao.InstructionRecipeDao;
import com.example.bepnhataapp.common.model.InstructionRecipe;
import java.util.List;
import android.util.Log;

public class RecipeStepGuideFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "recipe_id";
    private long recipeId;

    public static RecipeStepGuideFragment newInstance(long recipeId) {
        RecipeStepGuideFragment fragment = new RecipeStepGuideFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step_guide, container, false);
        if (getArguments() != null) recipeId = getArguments().getLong(ARG_RECIPE_ID);
        RecyclerView recyclerView = view.findViewById(R.id.rcStepGuide);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<InstructionRecipe> steps = new InstructionRecipeDao(getContext()).getByRecipe(recipeId);
        Log.d("RecipeStepGuideFragment", "recipeId=" + recipeId + ", steps=" + steps.size());
        RecipeStepGuideAdapter adapter = new RecipeStepGuideAdapter(steps);
        recyclerView.setAdapter(adapter);
        return view;
    }
} 
