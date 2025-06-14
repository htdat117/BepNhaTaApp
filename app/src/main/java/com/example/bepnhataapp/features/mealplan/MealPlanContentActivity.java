package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.data.FakeMealPlanRepository;
import com.example.bepnhataapp.features.mealplan.adapters.RecommendedAdapter;
import com.example.bepnhataapp.features.mealplan.adapters.GridSpacingItemDecoration;
import java.time.LocalDate;

public class MealPlanContentActivity extends AppCompatActivity {

    private LocalDate currentDate;
    private TextView tvCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_content);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        currentDate = LocalDate.now();
        tvCurrentDate = findViewById(R.id.tvCurrentDate);

        // Navigation arrows
        findViewById(R.id.btnPrevDay).setOnClickListener(v -> {
            currentDate = currentDate.minusDays(1);
            updateDateLabel();
        });
        findViewById(R.id.btnNextDay).setOnClickListener(v -> {
            currentDate = currentDate.plusDays(1);
            updateDateLabel();
        });

        updateDateLabel();

        MealPlanViewModel viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @androidx.annotation.NonNull
            @Override
            public <T extends androidx.lifecycle.ViewModel> T create(@androidx.annotation.NonNull Class<T> modelClass) {
                //noinspection unchecked
                return (T) new MealPlanViewModel(new FakeMealPlanRepository());
            }
        }).get(MealPlanViewModel.class);

        viewModel.getState().observe(this, state -> {
            Fragment fragment = (state.week == null) ? new EmptyPlanFragment() : TimelineFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dayPlanContainer, fragment)
                    .commit();
            
            // Setup recommended section after fragment is loaded
            setupRecommendedSectionAfterFragmentLoad();
        });
    }
    
    private void setupRecommendedSectionAfterFragmentLoad() {
        // Wait for layout to be ready to ensure proper sizing
        final View contentContainer = findViewById(R.id.dayPlanContainer);
        contentContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Remove listener to avoid multiple calls
                contentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                
                // Now setup the recommended section
                setupRecommendedSection();
                
                // Force layout refresh
                View scrollView = findViewById(R.id.scrollView);
                if (scrollView != null) {
                    scrollView.requestLayout();
                }
            }
        });
    }
    
    private void setupRecommendedSection() {
        RecyclerView rvReco = findViewById(R.id.rvRecommended);
        java.util.List<Object> recoData = new java.util.ArrayList<>();
        
        // First category
        recoData.add("Nhu cầu dinh dưỡng");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Thực đơn cân bằng dinh dưỡng",7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bổ máu",7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Giúp làm việc trí não hiệu quả",7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Xây dựng cơ xương",7));

        // Second category
        recoData.add("Bữa tối nhanh chóng cho người bận rộn");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 20 phút (thao keo ngon)", 7 ));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 30 phút (không khóe hoang)", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 40 phút (chế biến đơn giản)", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bữa ăn 15 phút (siêu nhanh)", 7));

        // Third category
        recoData.add("Thực đơn ăn chay cho ngày rằm");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Hương Chay 3 Món - 7 Ngày Thanh Lọc", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Hương Chay 5 Món - 7 Ngày Thanh Lọc"));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Chay Thanh Đạm - 5 Món Đơn Giản"));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Chay Tinh Khiết - 10 Món Đặc Biệt"));

        // Fourth category
        recoData.add("Thực đơn cho người tập thể thao");
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Tăng cơ giảm mỡ - 7 ngày", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Dinh dưỡng cho runner", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Bổ sung protein - 10 món", 7));
        recoData.add(new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background, "Thực đơn tập gym", 7));

        RecommendedAdapter recoAdapter = new RecommendedAdapter(recoData);
        
        // Configure GridLayoutManager with proper spacing
        GridLayoutManager glm = new GridLayoutManager(this, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (recoAdapter.getItemViewType(position) == 0) ? 2 : 1;
            }
        });
        
        // Clear existing decorations to avoid duplicates
        for (int i = 0; i < rvReco.getItemDecorationCount(); i++) {
            rvReco.removeItemDecorationAt(i);
        }
        
        // Add spacing decoration
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        rvReco.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));
        
        rvReco.setLayoutManager(glm);
        rvReco.setAdapter(recoAdapter);
        rvReco.setNestedScrollingEnabled(false);
        
        // Force layout recalculation
        rvReco.post(() -> {
            rvReco.requestLayout();
        });
    }

    private void updateDateLabel() {
        if (tvCurrentDate == null) return;
        String text;
        LocalDate today = LocalDate.now();
        if (currentDate.isEqual(today)) {
            text = "Hôm nay, " + today.getDayOfMonth() + " tháng " + today.getMonthValue();
        } else {
            text = currentDate.getDayOfMonth() + " tháng " + currentDate.getMonthValue();
        }
        tvCurrentDate.setText(text);
    }
} 