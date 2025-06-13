package com.example.bepnhataapp.features.mealplan;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.features.mealplan.data.FakeMealPlanRepository;
import com.example.bepnhataapp.features.mealplan.adapters.TimelineAdapter;
import com.example.bepnhataapp.features.mealplan.adapters.RecommendedAdapter;

public class MealPlanContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_content);

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

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
                    .replace(R.id.contentContainer, fragment)
                    .commit();
        });

        // Setup fake data for RecyclerViews
        RecyclerView rvTimeline = findViewById(R.id.rvTimeline);
        rvTimeline.setLayoutManager(new LinearLayoutManager(this));
        TimelineAdapter timelineAdapter = new TimelineAdapter(java.util.Arrays.asList(
                new TimelineAdapter.TimelineItem("Buổi sáng","Sườn nướng mật ong",826,R.drawable.placeholder_banner_background),
                new TimelineAdapter.TimelineItem("Buổi trưa","Sườn nướng mật ong",826,R.drawable.placeholder_banner_background),
                new TimelineAdapter.TimelineItem("Buổi tối","Sườn nướng mật ong",826,R.drawable.placeholder_banner_background)
        ));
        rvTimeline.setAdapter(timelineAdapter);

        RecyclerView rvReco = findViewById(R.id.rvRecommended);
        rvReco.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        RecommendedAdapter recoAdapter = new RecommendedAdapter(java.util.Arrays.asList(
                new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background,"Thực đơn cân bằng dinh dưỡng"),
                new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background,"Bổ máu"),
                new RecommendedAdapter.RecoItem(R.drawable.placeholder_banner_background,"Xây dựng khối cơ")
        ));
        rvReco.setAdapter(recoAdapter);
    }
} 