package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class MealTimeAdapter extends RecyclerView.Adapter<MealTimeAdapter.MealTimeVH> {

    public static class RecipeItem {
        public final long recipeId;
        public final int imageRes;
        public final String imageUrl;
        public final String name;
        public final int kcal;

        public RecipeItem(long id, int resId, String n, int kc) { this(id, resId, null, n, kc); }
        public RecipeItem(long id, String url, String n, int kc) { this(id, 0, url, n, kc); }
        private RecipeItem(long id, int resId, String url, String n, int kc) {
            this.recipeId = id;
            this.imageRes = resId;
            this.imageUrl = url;
            this.name = n;
            this.kcal = kc;
        }
    }

    public static class MealTimeSection {
        public final String title;
        public final int totalCalories;
        public final List<RecipeItem> recipes;
        public final com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot;
        public MealTimeSection(String t, int total, List<RecipeItem> r, com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum s) {
            title = t; totalCalories = total; recipes = r; slot = s; }
    }

    public interface OnMealSectionActionListener {
        void onAddNew(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot);
        void onDeleteSection(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot);
        void onAddNote(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot);
        void onSetReminder(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot);
        void onBuyAll(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot);
        void onDeleteRecipe(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId);
        void onChangeRecipe(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId);
        void onChangeDay(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId);
        void onMoveMeal(com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot, long recipeId);
        void onBuyIngredients(long recipeId);
    }

    private final List<MealTimeSection> data;
    private final OnMealSectionActionListener listener;
    public MealTimeAdapter(List<MealTimeSection> d, OnMealSectionActionListener l) { data = d; listener = l; }

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
            android.widget.ImageView img = row.findViewById(R.id.imgRecipe);
            if(r.imageUrl!=null && !r.imageUrl.isEmpty()){
                Glide.with(img.getContext()).load(r.imageUrl).placeholder(r.imageRes==0? R.drawable.placeholder_banner_background : r.imageRes).into(img);
            } else {
                img.setImageResource(r.imageRes);
            }
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
                    int mid = item1.getItemId();
                    if(mid == com.example.bepnhataapp.R.id.menu_buy_ingredients){
                        if(listener!=null) listener.onBuyIngredients(r.recipeId);
                    } else if(mid == com.example.bepnhataapp.R.id.menu_change_recipe){
                        if(listener!=null) listener.onChangeRecipe(s.slot, r.recipeId);
                    } else if(mid == com.example.bepnhataapp.R.id.menu_change_day){
                        if(listener!=null) listener.onChangeDay(s.slot, r.recipeId);
                    } else if(mid == com.example.bepnhataapp.R.id.menu_move_meal){
                        if(listener!=null) listener.onMoveMeal(s.slot, r.recipeId);
                    } else if(mid == com.example.bepnhataapp.R.id.menu_delete_recipe){
                        if(listener!=null) listener.onDeleteRecipe(s.slot, r.recipeId);
                    } else {
                        android.widget.Toast.makeText(v1.getContext(), item1.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
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
                int id = item.getItemId();
                com.example.bepnhataapp.common.model.DayPlan.MealTimeEnum slot = s.slot;
                if(id == com.example.bepnhataapp.R.id.menu_add_new){
                    if(listener!=null) listener.onAddNew(slot);
                } else if(id == com.example.bepnhataapp.R.id.menu_delete_meal){
                    if(listener!=null) listener.onDeleteSection(slot);
                } else if(id == com.example.bepnhataapp.R.id.menu_add_note){
                    if(listener!=null) listener.onAddNote(slot);
                } else if(id == com.example.bepnhataapp.R.id.menu_set_reminder){
                    if(listener!=null) listener.onSetReminder(slot);
                } else if(id == com.example.bepnhataapp.R.id.menu_buy_all){
                    if(listener!=null) listener.onBuyAll(slot);
                } else {
                    android.widget.Toast.makeText(v.getContext(), item.getTitle(), android.widget.Toast.LENGTH_SHORT).show();
                }
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
