package com.example.bepnhataapp.features.products;

import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.adapter.ReviewAdapter;
import com.example.bepnhataapp.common.model.Review;
import com.example.bepnhataapp.databinding.ActivityAllReviewsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.app.AlertDialog;
import android.widget.Toast;

public class AllReviewsActivity extends AppCompatActivity {

    private ActivityAllReviewsBinding binding;
    private long productId = -1;
    private List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter adapter;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivBack.setOnClickListener(v -> onBackPressed());

        // Get product id from intent
        if(getIntent()!=null){
            productId = getIntent().getLongExtra("productId", -1);
        }

        if(productId != -1){
            reviewList = new com.example.bepnhataapp.common.dao.ProductFeedbackDao(this).getReviewsByProductId(productId);
        }

        // If empty, add placeholder review
        if(reviewList.isEmpty()){
            java.util.List<Integer> sampleImgsRes = new java.util.ArrayList<>();
            sampleImgsRes.add(R.drawable.food);
            reviewList.add(new Review(R.drawable.profile_placeholder, "Chưa có đánh giá", 0f, "", "Hãy là người đầu tiên đánh giá sản phẩm này", sampleImgsRes));
        }

        adapter = new ReviewAdapter(this, reviewList);
        binding.rvAllReviews.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAllReviews.setAdapter(adapter);

        // Update summary UI
        float totalRating = 0f;
        int countRating = 0;
        for(Review r: reviewList){
            if(r.rating > 0){
                totalRating += r.rating;
                countRating++;
            }
        }
        float avgRating = countRating == 0 ? 0f : totalRating / countRating;
        binding.tvAverageRating.setText(String.format(java.util.Locale.getDefault(), "%.1f", avgRating));
        binding.ratingBarAverage.setRating(avgRating);
        binding.tvTotalReviews.setText("(" + countRating + ")");

        // Header title
        binding.tvHeader.setText("Tất cả đánh giá");

        // Sort row click listener
        binding.tvSortOption.setOnClickListener(v -> showSortDialog());
        // default sort: Gần đây nhất
        applySort("Gần đây nhất");
    }

    private void showSortDialog(){
        String[] options = {"Yêu thích nhất","Ít yêu thích nhất","Gần đây nhất","Lâu nhất"};
        new AlertDialog.Builder(this)
                .setTitle("Sắp xếp theo")
                .setItems(options, (dialog, which) -> {
                    applySort(options[which]);
                })
                .create()
                .show();
    }

    private void applySort(String sortLabel){
        if(reviewList==null||reviewList.size()<=1){
            binding.tvSortOption.setText(" "+sortLabel);
            return;
        }

        switch (sortLabel){
            case "Yêu thích nhất":
                Collections.sort(reviewList, (a,b) -> Float.compare(b.rating, a.rating));
                break;
            case "Ít yêu thích nhất":
                Collections.sort(reviewList, (a,b) -> Float.compare(a.rating, b.rating));
                break;
            case "Gần đây nhất":
                Collections.sort(reviewList, (a,b) -> compareDateDesc(a.timeAgo,b.timeAgo));
                break;
            case "Lâu nhất":
                Collections.sort(reviewList, (a,b) -> compareDateAsc(a.timeAgo,b.timeAgo));
                break;
        }
        adapter.notifyDataSetChanged();
        binding.tvSortOption.setText(" "+sortLabel);
    }

    private int compareDateDesc(String d1,String d2){
        java.util.Date date1 = parseDate(d1);
        java.util.Date date2 = parseDate(d2);
        return date2.compareTo(date1); // newer first
    }

    private int compareDateAsc(String d1,String d2){
        java.util.Date date1 = parseDate(d1);
        java.util.Date date2 = parseDate(d2);
        return date1.compareTo(date2); // older first
    }

    private java.util.Date parseDate(String str){
        try{
            if(str==null||str.isEmpty()) return new java.util.Date(0);
            return sdf.parse(str);
        }catch(ParseException e){
            return new java.util.Date(0);
        }
    }
} 
