package com.example.bepnhataapp.features.favorite;

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
import com.example.bepnhataapp.features.recipes.RecipeAdapter;
import com.example.bepnhataapp.features.recipes.RecipeItem;
import com.example.bepnhataapp.common.dao.CustomerDao;
import com.example.bepnhataapp.common.dao.FavouriteRecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDao;
import com.example.bepnhataapp.common.dao.RecipeDetailDao;
import com.example.bepnhataapp.common.model.Customer;
import com.example.bepnhataapp.common.model.FavouriteRecipe;
import com.example.bepnhataapp.common.model.RecipeEntity;
import com.example.bepnhataapp.common.model.RecipeDetail;
import com.example.bepnhataapp.common.model.Recipe;
import com.example.bepnhataapp.common.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRecipeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        RecyclerView rv = view.findViewById(R.id.recyclerViewFavorites);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        loadDataAsync(rv);
        return view;
    }

    private void loadDataAsync(RecyclerView rv) {
        new Thread(() -> {
            java.util.List<RecipeItem> list = new ArrayList<>();
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
                            // Build RecipeItem similar to RecipeListFragment
                            String imgStr = re.getImageThumb()!=null? re.getImageThumb().trim() : "";
                            byte[] imgData = null;
                            try {
                                java.lang.reflect.Method m = re.getClass().getMethod("getImage");
                                imgData = (byte[]) m.invoke(re);
                            } catch(Exception ex) { }
                            int imageResId = R.drawable.placeholder_banner_background;
                            if(!imgStr.isEmpty()) {
                                int resId = getResources().getIdentifier(imgStr, "drawable", getContext().getPackageName());
                                if(resId!=0) imageResId = resId;
                            }
                            RecipeDetailDao detailDao = new RecipeDetailDao(getContext());
                            RecipeDetail det = detailDao.get(re.getRecipeID());
                            String timeStr = det!=null? det.getCookingTimeMinutes()+" phút" : "";
                            String benefitStr = det!=null? det.getBenefit():"";
                            String levelStr = det!=null? det.getLevel():"";
                            RecipeItem item = new RecipeItem(imageResId, imgStr, imgData, re.getRecipeName(), "", levelStr, timeStr, re.getCategory()!=null? re.getCategory():"");
                            item.setBenefit(benefitStr);
                            item.setLevel(levelStr);
                            item.setTime(timeStr);
                            item.setLikeCount(re.getLikeAmount());
                            item.setCommentCount(re.getCommentAmount());
                            list.add(item);
                        }
                    }
                }
            }
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> rv.setAdapter(new RecipeAdapter(list, new RecipeAdapter.OnRecipeActionListener() {
                    @Override
                    public void onView(RecipeItem recipe) {
                        // open detail
                        RecipeEntity found = new RecipeDao(getContext()).getAllRecipes().stream()
                                .filter(r -> r.getRecipeName().equals(recipe.getName())).findFirst().orElse(null);
                        if(found!=null){
                            android.content.Intent intent = new android.content.Intent(getContext(), com.example.bepnhataapp.features.recipes.RecipeDetailActivity.class);
                            intent.putExtra("recipeId", (long)found.getRecipeID());
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onDelete(RecipeItem recipe) {
                        // Optionally handle delete
                    }
                })));
            }
        }).start();
    }
} 
