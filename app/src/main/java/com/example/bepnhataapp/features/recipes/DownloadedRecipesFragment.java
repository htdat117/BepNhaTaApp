    package com.example.bepnhataapp.features.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bepnhataapp.R;

import java.util.ArrayList;
import java.util.List;

public class DownloadedRecipesFragment extends Fragment implements RecipeAdapter.OnRecipeActionListener {

    private static final String TAG = "DownloadedRecipesFragment";

    private RecyclerView recyclerViewDownloaded;
    private LinearLayout layoutNoData;
    private ImageView btnBack;
    private TextView tvHeaderTitle;

    private List<RecipeItem> downloadedRecipeList;
    private RecipeAdapter recipeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_downloaded_content, container, false);

        // Ánh xạ các View
        recyclerViewDownloaded = view.findViewById(R.id.recyclerViewDownloaded);
        layoutNoData = view.findViewById(R.id.layoutNoData);
        btnBack = view.findViewById(R.id.btnBack);
        tvHeaderTitle = view.findViewById(R.id.tvHeaderTitle);

        // Thiết lập RecyclerView
        downloadedRecipeList = new ArrayList<>();
        // Thêm dữ liệu mẫu
        addSampleRecipes();

        recipeAdapter = new RecipeAdapter(downloadedRecipeList, this);
        recyclerViewDownloaded.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewDownloaded.setAdapter(recipeAdapter);

        // Xử lý nút quay lại
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack(); // Quay lại Fragment trước đó
                }
            });
        }

        // Cập nhật trạng thái hiển thị
        updateDisplayState();

        return view;
    }

    private void addSampleRecipes() {
        // Dữ liệu mẫu như trong hình ảnh bạn cung cấp
        downloadedRecipeList.add(new RecipeItem(R.drawable.food_placeholder, "Sườn non kho tiêu", "700 cal", "Giàu đạm", "50 phút"));
        downloadedRecipeList.add(new RecipeItem(R.drawable.food_placeholder, "Sườn non kho tiêu", "700 cal", "Giàu đạm", "50 phút"));

        // Gán lượt thích và bình luận giả lập cho mẫu
        for (RecipeItem item : downloadedRecipeList) {
            item.setLikeCount(150);
            item.setCommentCount(110);
        }
    }

    private void updateDisplayState() {
        if (downloadedRecipeList.isEmpty()) {
            recyclerViewDownloaded.setVisibility(View.GONE);
            layoutNoData.setVisibility(View.VISIBLE);
        } else {
            recyclerViewDownloaded.setVisibility(View.VISIBLE);
            layoutNoData.setVisibility(View.GONE);
        }
    }

    @Override
    public void onView(RecipeItem recipe) {
        Toast.makeText(getContext(), "Xem công thức: " + recipe.getName(), Toast.LENGTH_SHORT).show();
        // Logic để hiển thị chi tiết công thức
    }

    @Override
    public void onDelete(RecipeItem recipe) {
        downloadedRecipeList.remove(recipe);
        recipeAdapter.notifyDataSetChanged();
        updateDisplayState();
        Toast.makeText(getContext(), "Đã xóa: " + recipe.getName(), Toast.LENGTH_SHORT).show();
    }
} 
