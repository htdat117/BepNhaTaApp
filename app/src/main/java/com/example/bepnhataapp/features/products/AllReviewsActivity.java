package com.example.bepnhataapp.features.products;

import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.ReviewAdapter;
import com.example.bepnhataapp.common.models.Review;
import com.example.bepnhataapp.databinding.ActivityAllReviewsBinding;

import java.util.ArrayList;
import java.util.List;

public class AllReviewsActivity extends AppCompatActivity {

    private ActivityAllReviewsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        // Demo reviews data
        List<Integer> sampleImgs = new ArrayList<>();
        sampleImgs.add(R.drawable.food);
        sampleImgs.add(R.drawable.food);
        sampleImgs.add(R.drawable.food);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review(R.drawable.profile_placeholder, "Đức Mạnh", 5f, "2 giờ trước", "Nguyên liệu được đóng gói rất chỉnh chu. Món ăn rất ngon! Cả gia đình tôi đều thích!", sampleImgs));
        reviewList.add(new Review(R.drawable.profile_placeholder, "Kiên Đoàn", 5f, "1 ngày trước", "Tôi đã nấu thử và rất thành công món bánh canh cua. Nước dùng đậm đà, sợi bánh mềm dai, cua thịt ngọt thơm rất hợp khẩu vị gia đình tôi. Cháu nhà tôi ăn 3 tô vẫn thòm thèm.", sampleImgs));
        reviewList.add(new Review(R.drawable.profile_placeholder, "Đỗ Danh", 5f, "5 ngày trước", "Nguyên liệu rất tươi ngon và được đóng gói cẩn thận. Bánh canh thật sự rất tuyệt vời, cả gia đình đều thích. Bánh canh cua thơm ngon, nước dùng đậm đà, sợi bánh dai mềm và cua thì ngọt thịt, quá hợp khẩu vị nhà tôi!", sampleImgs));

        ReviewAdapter adapter = new ReviewAdapter(this, reviewList);
        binding.rvAllReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllReviews.setAdapter(adapter);
    }
} 