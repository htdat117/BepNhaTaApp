package com.example.bepnhataapp.features.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.home.RecipeAdapter;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.FavouriteRecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.FavouriteRecipe;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.models.Recipe;
import com.example.bepnhataapp.common.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRecipeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerViewFavorites);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        loadDataAsync(rv);
        return view;
    }

    private void loadDataAsync(RecyclerView rv) {
        new Thread(() -> {
            List<Recipe> list = new ArrayList<>();
            if (getContext() != null && SessionManager.isLoggedIn(getContext())) {
                String phone = SessionManager.getPhone(getContext());
                Customer c = new CustomerDao(getContext()).findByPhone(phone);
                if (c != null) {
                    long customerId = c.getCustomerID();
                    FavouriteRecipeDao favDao = new FavouriteRecipeDao(getContext());
                    List<FavouriteRecipe> favs = favDao.getByCustomerID(customerId);
                    RecipeDao rDao = new RecipeDao(getContext());
                    for (FavouriteRecipe fr : favs) {
                        RecipeEntity re = rDao.getById(fr.getRecipeID());
                        if (re != null) {
                            list.add(new Recipe(re.getRecipeID(), re.getRecipeName(), re.getCategory(), re.getImageThumb(), true));
                        }
                    }
                }
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> rv.setAdapter(new RecipeAdapter(list)));
            }
        }).start();
    }
} 