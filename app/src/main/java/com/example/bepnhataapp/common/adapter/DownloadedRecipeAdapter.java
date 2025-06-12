package com.example.bepnhataapp.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.models.DownloadedRecipe;
import java.util.List;

public class DownloadedRecipeAdapter extends RecyclerView.Adapter<DownloadedRecipeAdapter.ViewHolder> {
    private List<DownloadedRecipe> list;
    private OnRecipeActionListener listener;

    public interface OnRecipeActionListener {
        void onView(DownloadedRecipe recipe);
        void onDelete(DownloadedRecipe recipe);
    }

    public DownloadedRecipeAdapter(List<DownloadedRecipe> list, OnRecipeActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downloaded_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DownloadedRecipe recipe = list.get(position);
        holder.imgRecipe.setImageResource(recipe.getImageResId());
        holder.tvTitle.setText(recipe.getTitle());
        holder.tvCal.setText(recipe.getCal());
        holder.tvType.setText(recipe.getType());
        holder.tvTime.setText(recipe.getTime());

        holder.btnView.setOnClickListener(v -> listener.onView(recipe));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(recipe));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgRecipe;
        TextView tvTitle, tvCal, tvType, tvTime;
        Button btnView, btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.imageViewRecipe);
            tvTitle = itemView.findViewById(R.id.textViewRecipeName);
            tvCal = itemView.findViewById(R.id.textViewCalories);
            tvType = itemView.findViewById(R.id.textViewProtein);
            tvTime = itemView.findViewById(R.id.textViewTime);
            btnView = itemView.findViewById(R.id.btnViewRecipe);
            btnDelete = itemView.findViewById(R.id.btnDeleteRecipe);
        }
    }
} 