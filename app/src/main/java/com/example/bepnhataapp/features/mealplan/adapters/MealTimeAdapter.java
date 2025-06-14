package com.example.bepnhataapp.features.mealplan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;

import java.util.List;

public class MealTimeAdapter extends RecyclerView.Adapter<MealTimeAdapter.MealTimeVH> {

    public static class RecipeItem {
        public final int imageRes;
        public final String name;
        public final int kcal;
        public RecipeItem(int img, String n, int kc) { imageRes = img; name = n; kcal = kc; }
    }

    public static class MealTimeSection {
        public final String title;
        public final int totalCalories;
        public final List<RecipeItem> recipes;
        public MealTimeSection(String t, int total, List<RecipeItem> r) { title = t; totalCalories = total; recipes = r; }
    }

    private final List<MealTimeSection> data;
    public MealTimeAdapter(List<MealTimeSection> d) { data = d; }

    @NonNull
    @Override
    public MealTimeVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mealtime_section, parent, false);
        return new MealTimeVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MealTimeVH holder, int position) {
        MealTimeSection s = data.get(position);
        holder.tvTitle.setText(s.title);
        holder.tvCals.setText(s.totalCalories + " Calories");

        // Populate recipe rows
        holder.container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
        for (RecipeItem r : s.recipes) {
            View row = inflater.inflate(R.layout.item_recipe, holder.container, false);
            ((android.widget.ImageView) row.findViewById(R.id.imgRecipe)).setImageResource(r.imageRes);
            ((TextView) row.findViewById(R.id.tvRecipeName)).setText(r.name);
            ((TextView) row.findViewById(R.id.tvRecipeKcal)).setText("~ " + r.kcal + " Kcal");

            android.widget.ImageButton optBtn = row.findViewById(R.id.btnRecipeOptions);
            optBtn.setOnClickListener(v1 -> {
                androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(v1.getContext(), v1);
                popup.getMenuInflater().inflate(com.example.bepnhataapp.R.menu.recipe_item_menu, popup.getMenu());

                try {
                    java.lang.reflect.Field f = popup.getClass().getDeclaredField("mPopup");
                    f.setAccessible(true);
                    Object helper = f.get(popup);
                    java.lang.reflect.Method setForceShowIcon = helper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class);
                    setForceShowIcon.invoke(helper, true);
                } catch (Exception ignored) {}

                popup.setOnMenuItemClickListener(item1 -> {
                    android.widget.Toast.makeText(v1.getContext(), item1.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
                    if (item1.getItemId() == com.example.bepnhataapp.R.id.menu_buy_ingredients) {
                        // Implement navigate to shopping list later
                    }
                    return true;
                });
                popup.show();
            });

            holder.container.addView(row);
        }

        holder.btnOptions.setOnClickListener(v -> {
            androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(v.getContext(), v);
            popup.getMenuInflater().inflate(com.example.bepnhataapp.R.menu.meal_section_menu, popup.getMenu());

            // force show icon
            try {
                java.lang.reflect.Field mField = popup.getClass().getDeclaredField("mPopup");
                mField.setAccessible(true);
                Object menuHelper = mField.get(popup);
                java.lang.reflect.Method setForceShowIcon = menuHelper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class);
                setForceShowIcon.invoke(menuHelper, true);
            } catch (Exception ignored) {}

            popup.setOnMenuItemClickListener(item -> {
                android.widget.Toast.makeText(v.getContext(), item.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
                return true;
            });

            popup.show();
        });
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class MealTimeVH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCals; LinearLayout container; android.widget.ImageButton btnOptions;
        MealTimeVH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMealTitle);
            tvCals = itemView.findViewById(R.id.tvMealCalories);
            container = itemView.findViewById(R.id.containerRecipes);
            btnOptions = itemView.findViewById(R.id.btnMealOptions);
        }
    }
} 